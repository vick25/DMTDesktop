package com.osfac.dmt.workbench;

import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.driver.DriverManager;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.registry.Registry;
import com.osfac.dmt.workbench.ui.ErrorHandler;
import com.osfac.dmt.workbench.ui.FeatureTextWriterRegistry;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelProxy;

/**
 * Convenience methods for accessing the various elements in the Workbench
 * structure. Some getters return null -- subclasses may choose to override them
 * or leave them unimplemented, depending on their needs.
 */
public abstract class WorkbenchContext implements LayerViewPanelProxy, LayerNamePanelProxy, LayerManagerProxy {

    public DriverManager getDriverManager() {
        return null;
    }

    public DMTWorkbench getWorkbench() {
        return null;
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public Blackboard getBlackboard() {
        return null;
    }

    public LayerNamePanel getLayerNamePanel() {
        return null;
    }

    public LayerViewPanel getLayerViewPanel() {
        return null;
    }

    //Sometimes you can have a layer manager but no layer view panel
    //e.g. when the attribute window is at the forefront. [Bob Boseko]
    public LayerManager getLayerManager() {
        return null;
    }

    public Task getTask() {
        return null;
    }

    /**
     * Creates a snapshot of the system for use by plug-ins.
     */
    public PlugInContext createPlugInContext() {
        return new PlugInContext(this, getTask(), this, getLayerNamePanel(), getLayerViewPanel());
    }

    public FeatureTextWriterRegistry getFeatureTextWriterRegistry() {
        return featureTextWriterRegistry;
    }
    private Registry registry = new Registry();
    private FeatureTextWriterRegistry featureTextWriterRegistry = new FeatureTextWriterRegistry();

    public Registry getRegistry() {
        return registry;
    }
}