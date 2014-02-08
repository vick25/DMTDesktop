package com.osfac.dmt.workbench.model;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureCollectionWrapper;
import com.vividsolutions.jts.geom.Envelope;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 * Notifies listeners when features are added to or removed from a 
 * FeatureCollection.
 * <p> 
 * Prefer #addAll and #removeAll to #add and #remove, so that fewer events
 * will be fired.</p>
 */
public class ObservableFeatureCollection extends FeatureCollectionWrapper {
    private ArrayList listeners = new ArrayList();

    public ObservableFeatureCollection(FeatureCollection fc) {
        super(fc);
    }


    public void add(Listener listener) {
        listeners.add(listener);
    }

    public void add(Feature feature) {
        super.add(feature);
        fireFeaturesAdded(Arrays.asList(new Feature[] { feature }));
    }

    public void remove(Feature feature) {
        super.remove(feature);
        fireFeaturesRemoved(Arrays.asList(new Feature[] { feature }));
    }

    private void fireFeaturesAdded(Collection features) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener listener = (Listener) i.next();
            listener.featuresAdded(features);
        }
    }

    private void fireFeaturesRemoved(Collection features) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener listener = (Listener) i.next();
            listener.featuresRemoved(features);
        }
    }

    public void addAll(Collection features) {
        super.addAll(features);
        fireFeaturesAdded(features);
    }

    public void removeAll(Collection features) {
        super.removeAll(features);
        fireFeaturesRemoved(features);
    }

    public Collection remove(Envelope env) {
        Collection features = super.remove(env);
        fireFeaturesRemoved(features);

        return features;
    }

    /**
     * Listens for features being added to or removed from a 
     * FeatureCollection.
     */
    public static interface Listener {
        public void featuresAdded(Collection features);

        public void featuresRemoved(Collection features);
    }

}
