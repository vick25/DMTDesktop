package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

//[sstein] : 14.08.2005 added variable maxFeatures with getters and setters
public class LayerRenderer extends FeatureCollectionRenderer {

    private Layer layer;
    private LayerViewPanel panel;
    public static final String ALWAYS_USE_IMAGE_CACHING_KEY = LayerRenderer.class
            .getName()
            + " - ALWAYS USE IMAGE CACHING";

    public LayerRenderer(final Layer layer, LayerViewPanel panel) {
        //Use layer as the contentID [Bob Boseko]
        super(layer, panel, new ImageCachingFeatureCollectionRenderer(layer,
                panel) {
            protected ThreadSafeImage getImage() {
                if (!layer.isVisible()) {
                    return null;
                }

                return super.getImage();
            }

            public Runnable createRunnable() {
                if (!layer.isVisible()) {
                    //If the cached image is null, leave it alone. [Jon
                    // Aquino]
                    return null;
                }

                return super.createRunnable();
            }
        });
        this.layer = layer;
        this.panel = panel;
    }

    public Runnable createRunnable() {
        if (!render(layer, panel)) {
            return null;
        }
        return super.createRunnable();
    }

    public void copyTo(Graphics2D graphics) {
        if (!render(layer, panel)) {
            return;
        }
        super.copyTo(graphics);
    }

    public static boolean render(Layerable layerable, LayerViewPanel panel) {
        if (!layerable.isVisible()) {
            return false;
        }
        if (!layerable.getLayerManager().getLayerables(Layerable.class).contains(layerable)) {
            // Get here after deleting a layer. [Bob Boseko 2005-03-29]
            return false;
        }
        return withinVisibleScaleRange(layerable, panel);
    }

    public static boolean withinVisibleScaleRange(Layerable layerable,
            LayerViewPanel panel) {
        // When working with scale, the max is less than the min.
        // [Bob Boseko 2005-03-01]
        Assert.isTrue(layerable.getMaxScale() == null
                || layerable.getMinScale() == null
                || layerable.getMaxScale().doubleValue() <= layerable
                .getMinScale().doubleValue());
        if (!layerable.isScaleDependentRenderingEnabled()) {
            return true;
        }
        if (layerable.getMaxScale() != null
                && scale(panel) < layerable.getMaxScale().doubleValue()) {
            return false;
        }
        if (layerable.getMinScale() != null
                && scale(panel) > layerable.getMinScale().doubleValue()) {
            return false;
        }
        return true;
    }

    /**
     * @return the inverse of the viewport's scale; it is inverted so that it
     * increases as the user zooms out, as is usually expected
     */
    private static double scale(LayerViewPanel panel) {
        return 1d / panel.getViewport().getScale();
    }

    protected Collection styles() {
        //new ArrayList to avoid ConcurrentModificationExceptions. [Bob Boseko]
        ArrayList styles = new ArrayList(layer.getStyles());
        styles.remove(layer.getVertexStyle());
        styles.remove(layer.getLabelStyle());

        //Move to last. [Bob Boseko]
        styles.add(layer.getVertexStyle());
        styles.add(layer.getLabelStyle());

        return styles;
    }

    protected boolean useImageCaching(Map layerToFeaturesMap) {
        if (layer.getBlackboard().get(ALWAYS_USE_IMAGE_CACHING_KEY, false)) {
            return true;
        }
        return super.useImageCaching(layerToFeaturesMap);
    }

    protected Map layerToFeaturesMap() {
        Envelope viewportEnvelope = panel.getViewport()
                .getEnvelopeInModelCoordinates();

        return Collections.singletonMap(layer, layer
                .getFeatureCollectionWrapper().query(viewportEnvelope));
    }

    /**
     * @return Returns the number of maxFeatures to render as vector graphic.
     */
    public int getMaxFeatures() {
        return super.getMaxFeatures();
    }

    /**
     * @param maxFeatures The maximum number of Features to render as vector
     * graphic.<p> Use this method before using method render(Object contentID)
     * or render(Object contentID, boolean clearImageCache)
     */
    public void setMaxFeatures(int maxFeatures) {
        super.setMaxFeatures(maxFeatures);
    }
}