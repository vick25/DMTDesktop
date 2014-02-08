package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.renderer.Renderer;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.JInternalFrame;

public abstract class InstallRendererPlugIn extends AbstractPlugIn {

    private Object contentID;
    private boolean aboveLayerables;

    public InstallRendererPlugIn(Object contentID, boolean aboveLayerables) {
        this.contentID = contentID;
        this.aboveLayerables = aboveLayerables;
    }

    public void initialize(PlugInContext context) throws Exception {
        JInternalFrame[] frames = context.getWorkbenchFrame().getInternalFrames();

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] instanceof TaskFrame) {
                ensureHasRenderer((TaskFrame) frames[i]);
            }
        }

        context.getWorkbenchFrame().getDesktopPane().addContainerListener(new ContainerAdapter() {
            public void componentAdded(ContainerEvent e) {
                if (!(e.getChild() instanceof TaskFrame)) {
                    return;
                }

                TaskFrame taskFrame = (TaskFrame) e.getChild();
                ensureHasRenderer(taskFrame);
            }
        });
    }

    private void ensureHasRenderer(final TaskFrame taskFrame) {
        if (aboveLayerables) {
            taskFrame.getLayerViewPanel().getRenderingManager().putAboveLayerables(
                    contentID,
                    createFactory(taskFrame));
        } else {
            taskFrame.getLayerViewPanel().getRenderingManager().putBelowLayerables(
                    contentID,
                    createFactory(taskFrame));
        }
    }

    protected abstract Renderer.Factory createFactory(TaskFrame frame);
}
