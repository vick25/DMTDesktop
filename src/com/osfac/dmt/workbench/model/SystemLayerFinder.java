package com.osfac.dmt.workbench.model;

import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import java.awt.Color;

/**
 * A "system-maintained layer" has a fixed set of styles and is identified by
 * name. For example, the vector layer has blue features with arrowheads and is
 * named "Warping Vectors". A SystemLayerFinder class will find a particular
 * system-maintained layer, and can create it if necessary.
 */
public abstract class SystemLayerFinder {

    private String layerName;
    private LayerManagerProxy layerManagerProxy;

    public SystemLayerFinder(String layerName,
            LayerManagerProxy layerManagerProxy) {
        this.layerManagerProxy = layerManagerProxy;
        this.layerName = layerName;
    }

    public String getLayerName() {
        return layerName;
    }

    public static class NonSavePromptingLayer extends Layer {

        public NonSavePromptingLayer() {
            super();
        }

        public NonSavePromptingLayer(String name, Color fillColor,
                FeatureCollection featureCollection, LayerManager layerManager) {
            super(name, fillColor, featureCollection, layerManager);
        }

        public boolean isFeatureCollectionModified() {
            //Prevent save prompt. [Bob Boseko]
            return false;
        }
    }

    public Layer createLayer() {
        FeatureSchema schema = new FeatureSchema();
        schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);

        FeatureDataset dataset = new FeatureDataset(schema);
        Layer layer = new NonSavePromptingLayer(layerName, Color.blue, dataset,
                layerManagerProxy.getLayerManager());
        boolean firingEvents = layerManagerProxy.getLayerManager()
                .isFiringEvents();
        //Can't fire events because this Layerable hasn't been added to the
        //LayerManager yet. [Bob Boseko]
        layerManagerProxy.getLayerManager().setFiringEvents(false);

        try {
            applyStyles(layer);
        } finally {
            layerManagerProxy.getLayerManager().setFiringEvents(firingEvents);
        }

        layerManagerProxy.getLayerManager().addLayer(
                StandardCategoryNames.SYSTEM, layer);

        return layer;
    }

    /**
     * @return the layer, or null if there is no layer
     */
    public Layer getLayer() {
        // Don't automatically create the layer. For example, #getLayer may
        // be called by an EnableCheck; we wouldn't want the layer to get
        // created in this case. [Bob Boseko]
        Layer layer = null;
        // it is possible that the getLayerManager() can return a null value,
        // we must avoid a NPE here
        LayerManager layerManager = layerManagerProxy.getLayerManager();
        if (layerManager != null) {
            layer = layerManager.getLayer(layerName);

        }
        return layer;
    }

    protected abstract void applyStyles(Layer layer);
}