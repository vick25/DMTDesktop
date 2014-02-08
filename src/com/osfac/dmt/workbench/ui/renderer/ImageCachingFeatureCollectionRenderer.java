package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.renderer.style.Style;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @see SimpleFeatureCollectionRenderer
 * @see FeatureCollectionRenderer
 */
public class ImageCachingFeatureCollectionRenderer extends ImageCachingRenderer {

    public ImageCachingFeatureCollectionRenderer(Object contentID, LayerViewPanel panel) {
        super(contentID, panel);
    }
    private Collection styles = new ArrayList();
    private Map layerToFeaturesMap = new HashMap();

    protected void setLayerToFeaturesMap(Map layerToFeaturesMap) {
        this.layerToFeaturesMap = layerToFeaturesMap;
    }

    protected void setStyles(Collection styles) {
        this.styles = styles;
    }

    protected void renderHook(ThreadSafeImage image, Collection features,
            Layer layer, final Style style) throws Exception {
        if (!layer.isVisible()) {
            return;
        }
        if (style == null || !style.isEnabled()) {
            return;
        }
        style.initialize(layer);
        //new ArrayList to avoid ConcurrentModificationException. [Bob Boseko]

        // Revert to the original list, as the features may be a LazyList
        // containing a huge number of features from a database. Monitor the
        // frequency of ConcurrentModificationException errors. [Bob Boseko
        // 2005-03-02]
        for (Iterator i = features.iterator(); i.hasNext();) {
            final Feature feature = (Feature) i.next();
            if (cancelled) {
                break;
            }
            if (feature.getGeometry() == null || feature.getGeometry().isEmpty()) {
                continue;
            }
            //Because image.draw is synchronized, it might be faster to do
            //several paints inside #draw. [Bob Boseko]
            image.draw(new ThreadSafeImage.Drawer() {
                public void draw(Graphics2D g) throws Exception {
                    style.paint(feature, g, panel.getViewport());
                }
            });
        }
    }

    protected void renderHook(ThreadSafeImage image) throws Exception {
        for (Iterator i = styles.iterator(); i.hasNext();) {
            Style style = (Style) i.next();
            for (Iterator j = layerToFeaturesMap.keySet().iterator(); j
                    .hasNext();) {
                Layer layer = (Layer) j.next();
                Collection features = (Collection) layerToFeaturesMap
                        .get(layer);
                renderHook(image, features, layer, style);
            }
        }
    }
}