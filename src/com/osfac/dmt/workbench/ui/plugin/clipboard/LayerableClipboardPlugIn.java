package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jts.util.Assert;
import java.util.Iterator;
import org.openjump.core.rasterimage.RasterImageLayer;

public abstract class LayerableClipboardPlugIn extends AbstractPlugIn {

    public LayerableClipboardPlugIn() {
    }

    protected Layerable cloneLayerable(Layerable layerable) {
        if (layerable instanceof Layer) {
            return cloneLayer((Layer) layerable);
        }

        if (layerable instanceof WMSLayer) {
            try {
                return (Layerable) ((WMSLayer) layerable).clone();
            } catch (CloneNotSupportedException e) {
                Assert.shouldNeverReachHere();
            }
        }

        if (layerable instanceof RasterImageLayer) {
            try {
                return (Layerable) ((RasterImageLayer) layerable).clone();
            } catch (CloneNotSupportedException e) {
                Assert.shouldNeverReachHere();
            }
        }

        Assert.shouldNeverReachHere();

        return null;
    }

    protected Layer cloneLayer(Layer layer) {
        LayerManager dummyLayerManager = new LayerManager();
        dummyLayerManager.setFiringEvents(false);

        Layer clone = new Layer();
        clone.setLayerManager(dummyLayerManager);

        //If this is the fence layer, #setName will call #applyStyles, which requires
        //that the clone have a BasicStyle. So set the styles before setting the
        //name. [Bob Boseko]
        clone.setStyles(layer.cloneStyles());
        clone.setName(layer.getName());
        clone.setFeatureCollection(cloneFeatureCollection(
                layer.getFeatureCollectionWrapper()));

        return clone;
    }

    private FeatureCollection cloneFeatureCollection(
            FeatureCollection featureCollection) {
        FeatureDataset d = new FeatureDataset(featureCollection.getFeatureSchema());

        for (Iterator i = featureCollection.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            d.add((Feature) f.clone());
        }

        return d;
    }
}
