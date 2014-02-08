package com.osfac.dmt.workbench.ui.zoom;

import com.osfac.dmt.geom.EnvelopeUtil;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;

public class ZoomToFencePlugIn extends AbstractPlugIn {

    public ZoomToFencePlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getViewport().zoom(EnvelopeUtil.bufferByFraction(
                context.getLayerViewPanel().getFence().getEnvelopeInternal(),
                0.03));

        return true;
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("ZoomFence.gif");
        //return IconLoaderFamFam.icon("shape_square_go.png");
    }
}
