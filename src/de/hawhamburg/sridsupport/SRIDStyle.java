package de.hawhamburg.sridsupport;

import java.awt.Graphics2D;
import java.util.Iterator;

import com.vividsolutions.jts.util.Assert;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.renderer.style.Style;
/**
 * Ensures that all geometries have a given SRID. Because it is a Style, it will
 * be saved to the task file.
 */
public class SRIDStyle implements Style {
    private int srid = -1;
    public void paint(Feature f, Graphics2D g, Viewport viewport)
            throws Exception {
    }
    private boolean initialized = false;
    public void initialize(Layer layer) {
        if (initialized) {
            return;
        }
        updateSRIDs(layer);
        layer.getLayerManager().addLayerListener(new LayerListener() {
            public void featuresChanged(FeatureEvent e) {
                for (Iterator i = e.getFeatures().iterator(); i.hasNext();) {
                    Feature feature = (Feature) i.next();
                    feature.getGeometry().setSRID(srid);
                }
            }
            public void layerChanged(LayerEvent e) {
            }
            public void categoryChanged(CategoryEvent e) {
            }
        });
        initialized = true;
    }
    public void updateSRIDs(Layer layer) {
        for (Iterator i = layer.getFeatureCollectionWrapper().iterator(); i
                .hasNext();) {
            Feature feature = (Feature) i.next();
            feature.getGeometry().setSRID(srid);
        }
    }
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            Assert.shouldNeverReachHere();
            return null;
        }
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    private boolean enabled = true;
    public boolean isEnabled() {
        return enabled;
    }
    public int getSRID() {
        return srid;
    }
    public void setSRID(int srid) {
        this.srid = srid;
    }

}
