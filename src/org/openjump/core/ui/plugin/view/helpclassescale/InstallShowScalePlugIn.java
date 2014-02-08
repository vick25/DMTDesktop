package org.openjump.core.ui.plugin.view.helpclassescale;

import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.plugin.InstallRendererPlugIn;
import com.osfac.dmt.workbench.ui.renderer.Renderer;

/**
 * Ensures that all TaskFrames get a scale bar.
 *
 * @author sstein
 */
public class InstallShowScalePlugIn extends InstallRendererPlugIn {

    public InstallShowScalePlugIn() {
        super(ShowScaleRenderer.CONTENT_ID, true);
    }

    protected Renderer.Factory createFactory(final TaskFrame frame) {
        return new Renderer.Factory() {
            public Renderer create() {
                return new ShowScaleRenderer(frame.getLayerViewPanel());
            }
        };
    }
}