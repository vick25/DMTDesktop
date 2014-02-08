package com.osfac.dmt.workbench.plugin;

import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.driver.DriverManager;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.ui.ErrorHandler;
import com.osfac.dmt.workbench.ui.HTMLFrame;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.geom.Envelope;
import javax.swing.JInternalFrame;
import org.openjump.core.rasterimage.RasterImageLayer;

/**
 * Passed to PlugIns to enable them to access the rest of the JUMP Workbench.
 *
 * @see PlugIn
 */
public class PlugInContext implements LayerManagerProxy {

    private Task task;
    private LayerNamePanel layerNamePanel;
    private LayerViewPanel layerViewPanel;
    private WorkbenchContext workbenchContext;
    private EnableCheckFactory checkFactory;
    private FeatureInstaller featureInstaller;
    private LayerManagerProxy layerManagerProxy;

    public PlugInContext(
            WorkbenchContext workbenchContext,
            Task task,
            LayerManagerProxy layerManagerProxy,
            LayerNamePanel layerNamePanel,
            LayerViewPanel layerViewPanel) {
        this.workbenchContext = workbenchContext;
        this.task = task;
        this.layerManagerProxy = layerManagerProxy;
        this.layerNamePanel = layerNamePanel;
        this.layerViewPanel = layerViewPanel;
        checkFactory = new EnableCheckFactory(workbenchContext);
        featureInstaller = new FeatureInstaller(workbenchContext);
    }

    public DriverManager getDriverManager() {
        return workbenchContext.getDriverManager();
    }

    public ErrorHandler getErrorHandler() {
        return workbenchContext.getErrorHandler();
    }

    public WorkbenchContext getWorkbenchContext() {
        return workbenchContext;
    }

    /**
     * @return the ith layer clicked on the layer-list panel, or null if the
     * user hasn't clicked an ith layer
     */
    public Layer getSelectedLayer(int i) {
        Layer[] selectedLayers = getSelectedLayers();

        if (selectedLayers.length > i) {
            return selectedLayers[i];
        }

        return null;
    }

    /**
     * @return the ith selected layer, or if there is none, the ith layer
     */
    public Layer getCandidateLayer(int i) {
        Layer lyr = getSelectedLayer(i);

        if (lyr != null) {
            return lyr;
        }

        return getLayerManager().getLayer(i);
    }

    //<<TODO:DESIGN>> Return List instead of array [Bob Boseko]
    public Layer[] getSelectedLayers() {
        return getLayerNamePanel().getSelectedLayers();
    }

    public Envelope getSelectedLayerEnvelope() {
        return getSelectedLayer(0).getFeatureCollectionWrapper().getEnvelope();
    }

    public Task getTask() {
        return task;
    }

    public LayerNamePanel getLayerNamePanel() {
        return layerNamePanel;
    }

    public LayerManager getLayerManager() {
        return layerManagerProxy.getLayerManager();
    }

    public LayerViewPanel getLayerViewPanel() {
        return layerViewPanel;
    }

    public WorkbenchFrame getWorkbenchFrame() {
        return workbenchContext.getWorkbench().getFrame();
    }

    public Layer addLayer(
            String categoryName,
            String layerName,
            FeatureCollection featureCollection) {
        return getLayerManager().addLayer(categoryName, layerName, featureCollection);
    }

    public void addLayerSextanteRaster(
            String categoryName,
            RasterImageLayer raster) {
        getLayerManager().addLayerable(categoryName, raster);
    }

    public HTMLFrame getOutputFrame() {
        return workbenchContext.getWorkbench().getFrame().getOutputFrame();
    }

    public JInternalFrame getActiveInternalFrame() {
        return workbenchContext.getWorkbench().getFrame().getActiveInternalFrame();
    }

    public EnableCheckFactory getCheckFactory() {
        return checkFactory;
    }

    public FeatureInstaller getFeatureInstaller() {
        return featureInstaller;
    }
}
