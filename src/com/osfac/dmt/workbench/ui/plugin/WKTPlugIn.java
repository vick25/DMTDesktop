package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.WKTReader;
import com.osfac.dmt.workbench.WorkbenchException;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.EnterWKTDialog;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.Iterator;

/**
 * Base class for PlugIns that ask the user to enter Well-Known Text.
 */
public abstract class WKTPlugIn extends AbstractPlugIn {

    protected Layer layer;

    public WKTPlugIn() {
    }

    private static void validate(FeatureCollection c, PlugInContext context) throws WorkbenchException {
        for (Iterator i = c.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            IsValidOp op = new IsValidOp(f.getGeometry());
            if (!op.isValid()) {
                if (context
                        .getWorkbenchContext()
                        .getWorkbench()
                        .getBlackboard()
                        .get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                    throw new WorkbenchException(op.getValidationError().getMessage());
                }
                context.getWorkbenchFrame().warnUser(op.getValidationError().getMessage());
            }
        }
    }

    protected abstract Layer layer(PlugInContext context);

    public boolean execute(PlugInContext context) throws Exception {
        layer = layer(context);
        EnterWKTDialog d = createDialog(context);
        d.setVisible(true);
        return d.wasOKPressed();
    }

    protected abstract void apply(FeatureCollection c, PlugInContext context) throws WorkbenchException;

    protected EnterWKTDialog createDialog(final PlugInContext context) {
        final EnterWKTDialog d =
                new EnterWKTDialog(context.getWorkbenchFrame(), I18N.get("ui.plugin.WKTPlugIn.enter-well-known-text"), true);
        d.setPreferredSize(new Dimension(500, 400));
        d.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (d.wasOKPressed()) {
                        apply(d.getText(), context);
                    }
                    d.setVisible(false);
                } catch (Throwable t) {
                    context.getErrorHandler().handleThrowable(t);
                }
            }
        });
        GUIUtil.centreOnWindow(d);
        return d;
    }

    protected void apply(String wkt, PlugInContext context) throws Exception {
        try (StringReader stringReader = new StringReader(wkt)) {
            WKTReader wktReader = new WKTReader();
            FeatureCollection c = wktReader.read(stringReader);
            validate(c, context);
            apply(c, context);
        }
    }
}
