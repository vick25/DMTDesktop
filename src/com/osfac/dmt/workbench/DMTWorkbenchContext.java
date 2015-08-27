package com.osfac.dmt.workbench;

import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.driver.DriverManager;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.ui.ErrorHandler;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelProxy;
import com.osfac.dmt.workbench.ui.TaskFrameProxy;
import javax.swing.JInternalFrame;

/**
 * Implementation of {@link WorkbenchContext} for the {@link
 * JUMPWorkbench}.
 */
public class DMTWorkbenchContext extends WorkbenchContext {

    private DMTWorkbench workbench;

    public DMTWorkbenchContext(DMTWorkbench workbench) {
        this.workbench = workbench;
    }

    @Override
    public DMTWorkbench getWorkbench() {
        return workbench;
    }

    @Override
    public Blackboard getBlackboard() {
        return workbench.getBlackboard();
    }

    @Override
    public DriverManager getDriverManager() {
        return workbench.getDriverManager();
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return workbench.getFrame();
    }

    @Override
    public Task getTask() {
        if (!(activeInternalFrame() instanceof TaskFrameProxy)) {
            return null;
        }

        return ((TaskFrameProxy) activeInternalFrame()).getTaskFrame().getTask();
    }

    @Override
    public LayerNamePanel getLayerNamePanel() {
        if (!(activeInternalFrame() instanceof LayerNamePanelProxy)) {
            return null;
        }

        return ((LayerNamePanelProxy) activeInternalFrame()).getLayerNamePanel();
    }

    @Override
    public LayerManager getLayerManager() {
        if (!(activeInternalFrame() instanceof LayerManagerProxy)) {
            //WarpingPanel assumes that this method returns null if the active frame is not
            //a LayerManagerProxy. [Bob Boseko]
            return null;
        }

        return ((LayerManagerProxy) activeInternalFrame()).getLayerManager();
    }

    @Override
    public LayerViewPanel getLayerViewPanel() {
        if (!(activeInternalFrame() instanceof LayerViewPanelProxy)) {
            return null;
        }

        return ((LayerViewPanelProxy) activeInternalFrame()).getLayerViewPanel();
    }

    private JInternalFrame activeInternalFrame() {
        return workbench.getFrame().getActiveInternalFrame();
    }
}
