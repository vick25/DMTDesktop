package com.osfac.dmt.feature;

import com.vividsolutions.jts.geom.Envelope;
import java.io.Serializable;
import java.util.*;

/**
 * Default implementation of FeatureCollection.
 */
public class FeatureDataset implements FeatureCollection, Serializable {

    private static final long serialVersionUID = 5573446944516446540L;
    private FeatureSchema featureSchema;
    //<<TODO>> Possibly use hashtable to do spatial indexing [Bob Boseko]
    private ArrayList features;
    private Envelope envelope = null;

    /**
     * Creates a FeatureDataset, initialized with a group of Features.
     *
     * @param newFeatures an initial group of features to add to this
     * FeatureDataset
     * @param featureSchema the types of the attributes of the features in this
     * collection
     */
    public FeatureDataset(Collection newFeatures, FeatureSchema featureSchema) {
        features = new ArrayList(newFeatures);
        this.featureSchema = featureSchema;
    }

    /**
     * Creates a FeatureDataset.
     *
     * @param featureSchema the types of the attributes of the features in this
     * collection
     */
    public FeatureDataset(FeatureSchema featureSchema) {
        this(new ArrayList(), featureSchema);
    }

    /**
     * Returns the Feature at the given index (zero-based).
     */
    public Feature getFeature(int index) {
        return (Feature) features.get(index);
    }

    public FeatureSchema getFeatureSchema() {
        return featureSchema;
    }

    /**
     * Because the envelope is cached, the envelope may be incorrect if you
     * later change a Feature's geometry using Feature#setGeometry.
     */
    public Envelope getEnvelope() {
        if (envelope == null) {
            envelope = new Envelope();

            for (Iterator i = features.iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                envelope.expandToInclude(feature.getGeometry()
                        .getEnvelopeInternal());
            }
        }

        return envelope;
    }

    public List getFeatures() {
        return Collections.unmodifiableList(features);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return a List containing the features whose envelopes intersect the
     * given envelope
     */
    //<<TODO:DESIGN>> Perhaps return value should be a Set, not a List, because order
    //doesn't matter. [Bob Boseko]
    public List query(Envelope envelope) {
        if (!envelope.intersects(getEnvelope())) {
            return new ArrayList();
        }

        //<<TODO:NAMING>> Rename this method to getFeatures(Envelope), to parallel
        //getFeatures() [Bob Boseko]
        ArrayList queryResult = new ArrayList();

        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();

            if (feature.getGeometry().getEnvelopeInternal().intersects(envelope)) {
                queryResult.add(feature);
            }
        }

        return queryResult;
    }

    public void add(Feature feature) {
        features.add(feature);
        if (envelope != null) {
            envelope.expandToInclude(feature.getGeometry().getEnvelopeInternal());
        }
    }

    /**
     * Returns whether or not this Feature is in this collection
     *
     * @return true if this feature is in this collection, as determined using
     * Feature#equals
     */
    public boolean contains(Feature feature) {
        return features.contains(feature);
    }

    /**
     * Removes the features which intersect the given envelope
     */
    public Collection remove(Envelope env) {
        Collection features = query(env);
        removeAll(features);

        return features;
    }

    public void remove(Feature feature) {
        features.remove(feature);
        invalidateEnvelope();
    }

    /**
     * Removes all features from this collection.
     */
    public void clear() {
        invalidateEnvelope();
        features.clear();
    }

    public int size() {
        return features.size();
    }

    public Iterator iterator() {
        return features.iterator();
    }

    /**
     * Clears the cached envelope of this FeatureDataset's Features. Call this
     * method when a Feature's Geometry is modified.
     */
    public void invalidateEnvelope() {
        envelope = null;
    }

    public void addAll(Collection features) {
        this.features.addAll(features);
        if (envelope != null) {
            for (Iterator i = features.iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                envelope.expandToInclude(feature.getGeometry().getEnvelopeInternal());
            }
        }
    }

    /*public void removeAll(Collection features) {
     System.out.println("debut removeAll:"+System.currentTimeMillis());
     this.features.removeAll(features);
     invalidateEnvelope();
     System.out.println("  fin removeAll:"+System.currentTimeMillis());
     }*/
    // [michaudm 2009-05-16] creating a map on the fly improves dramatically
    // the removeAll performance if c is large
    // note that the semantic is slightly changed as the FID is used to identify 
    // features to remove rather than object Equality
    public void removeAll(Collection c) {
        java.util.Map<Integer, Feature> map = new java.util.HashMap<Integer, Feature>();
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            map.put(f.getID(), f);
        }
        for (Iterator i = c.iterator(); i.hasNext();) {
            map.remove(((Feature) i.next()).getID());
        }
        features = new ArrayList();
        features.addAll(map.values());
        invalidateEnvelope();
    }
}
