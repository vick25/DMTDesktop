package com.osfac.dmt.workbench.ui.zoom;

import com.vividsolutions.jts.util.Assert;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class ZoomNextPlugIn extends AbstractPlugIn {

    public ZoomNextPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);

        Viewport viewport = context.getLayerViewPanel().getViewport();
        Assert.isTrue(viewport.getZoomHistory().hasNext());
        viewport.getZoomHistory().setAdding(false);

        try {
            viewport.zoom(viewport.getZoomHistory().next());
        } finally {
            viewport.getZoomHistory().setAdding(true);
        }

        return true;
    }

    public MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(new EnableCheck() {
            public String check(JComponent component) {
                LayerViewPanel layerViewPanel = workbenchContext.getLayerViewPanel();
                return (layerViewPanel == null || //[UT] 20.10.2005 not quite the error mesg
                        !layerViewPanel.getViewport()
                        .getZoomHistory().hasNext())
                        ? I18N.get("ui.zoom.ZoomNextPlugIn.already-at-end") : null;
            }
        });
    }

    public ImageIcon getIcon() {
        //return IconLoaderFamFam.icon("application_side_expand.png");
        return IconLoader.icon("Right.gif");
    }
}
