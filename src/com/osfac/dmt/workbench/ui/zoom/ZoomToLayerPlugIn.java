package com.osfac.dmt.workbench.ui.zoom;

import com.vividsolutions.jts.geom.Envelope;
import com.osfac.dmt.geom.EnvelopeUtil;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

public class ZoomToLayerPlugIn extends AbstractPlugIn {

    public ZoomToLayerPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getViewport().zoom(EnvelopeUtil.bufferByFraction(
                envelopeOfSelectedLayers(context), 0.03));
        return true;
    }

    private Envelope envelopeOfSelectedLayers(PlugInContext context) {
        Envelope envelope = new Envelope();
        for (Iterator i = Arrays.asList(context.getLayerNamePanel()
                .getSelectedLayers()).iterator();
                i.hasNext();) {
            Layer layer = (Layer) i.next();
            envelope.expandToInclude(layer.getFeatureCollectionWrapper().getEnvelope());
        }
        return envelope;
    }

    public MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                1)).add(new EnableCheck() {
            public String check(JComponent component) {
                ((JMenuItem) component).setText(getName()
                        + StringUtil.s(
                        workbenchContext.getLayerNamePanel()
                        .getSelectedLayers().length));

                return null;
            }
        });
    }
    public static final ImageIcon ICON = IconLoader.icon("zoom.gif");
}
