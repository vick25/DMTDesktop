package org.openjump.core.ui.plugin.view;

import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.plugin.InstallRendererPlugIn;
import com.osfac.dmt.workbench.ui.renderer.Renderer;

public class NorthArrowInstallRenderer extends InstallRendererPlugIn {

    public NorthArrowInstallRenderer() {
        super(NorthArrowRenderer.CONTENT_ID, true);
    }

    protected Renderer.Factory createFactory(final TaskFrame frame) {
        return new Renderer.Factory() {
            public Renderer create() {
                return new NorthArrowRenderer(frame.getLayerViewPanel());
            }
        };
    }
}