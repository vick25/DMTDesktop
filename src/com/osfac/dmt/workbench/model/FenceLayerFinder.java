package com.osfac.dmt.workbench.model;

import com.vividsolutions.jts.geom.Geometry;
import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureSchema;
import java.awt.Color;

/**
 * Retrieves the layer containing the single Fence polygon (if any) and sets its
 * styles.
 */
public class FenceLayerFinder extends SystemLayerFinder {

    public static final String LAYER_NAME = I18N.get("model.FenceLayerFinder.fence");

    public FenceLayerFinder(LayerManagerProxy layerManagerProxy) {
        super(LAYER_NAME, layerManagerProxy);
    }

    public Geometry getFence() {
        if (getLayer() == null) {
            return null;
        }

        if (getLayer().getFeatureCollectionWrapper().isEmpty()) {
            return null;
        }

        return ((Feature) getLayer().getFeatureCollectionWrapper().iterator().next()).getGeometry();
    }

    protected void applyStyles(Layer layer) {
        layer.getBasicStyle().setLineColor(Color.blue);
        layer.getBasicStyle().setRenderingLine(true);
        layer.getBasicStyle().setRenderingFill(false);
        layer.setDrawingLast(true);
    }

    private Feature toFeature(Geometry fence, FeatureSchema schema) {
        Feature feature = new BasicFeature(schema);
        feature.setGeometry(fence);

        return feature;
    }

    public void setFence(Geometry fence) {
        if (getLayer() == null) {
            createLayer();
        }

        if (fence != null) {
            getLayer().getFeatureCollectionWrapper().clear();
            getLayer().getFeatureCollectionWrapper().add(toFeature(fence,
                    getLayer().getFeatureCollectionWrapper().getFeatureSchema()));
        }

    }
}
