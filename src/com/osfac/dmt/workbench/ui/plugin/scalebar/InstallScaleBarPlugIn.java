package com.osfac.dmt.workbench.ui.plugin.scalebar;

import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.plugin.InstallRendererPlugIn;
import com.osfac.dmt.workbench.ui.renderer.Renderer;

/**
 * Ensures that all TaskFrames get a scale bar.
 */
public class InstallScaleBarPlugIn extends InstallRendererPlugIn {

    public InstallScaleBarPlugIn() {
        super(ScaleBarRenderer.CONTENT_ID, true);
    }

    protected Renderer.Factory createFactory(final TaskFrame frame) {
        return new Renderer.Factory() {
            public Renderer create() {
                return new ScaleBarRenderer(frame.getLayerViewPanel());
            }
        };
    }
}