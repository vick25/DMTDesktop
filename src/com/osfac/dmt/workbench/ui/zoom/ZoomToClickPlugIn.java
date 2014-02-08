package com.osfac.dmt.workbench.ui.zoom;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;

// Replaced on 2011-10-27 by 
// org.openjump.core.ui.plugin.mousemenu.ZoomInPlugIn and
// org.openjump.core.ui.plugin.mousemenu.ZoomOutPlugIn
public class ZoomToClickPlugIn extends AbstractPlugIn {

    private double zoomFactor;

    public ZoomToClickPlugIn(double newZoomFactor) {
        zoomFactor = newZoomFactor;
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getViewport().zoomToViewPoint(context.getLayerViewPanel()
                .getLastClickedPoint(),
                zoomFactor);

        return true;
    }
}
