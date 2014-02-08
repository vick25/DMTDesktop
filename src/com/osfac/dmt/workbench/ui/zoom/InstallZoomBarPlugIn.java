package com.osfac.dmt.workbench.ui.zoom;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import java.awt.Dimension;

public class InstallZoomBarPlugIn extends AbstractPlugIn {

    public void initialize(PlugInContext context) throws Exception {
        ZoomBar zoomBar = new ZoomBar(false, true, context.getWorkbenchFrame());
        zoomBar.setMaximumSize(new Dimension(200, 10000));
        context.getWorkbenchFrame().getToolBar().add(zoomBar);
    }
}
