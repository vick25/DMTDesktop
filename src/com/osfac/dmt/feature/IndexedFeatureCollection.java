package com.osfac.dmt.feature;

import com.osfac.dmt.I18N;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import java.util.*;

/**
 * An IndexedFeatureCollection creates a new collection which is backed by a
 * FeatureCollection, but which is indexed for query purposes. In this
 * implementation, Features cannot be added or removed (an Exception is thrown)
 * and Features' Geometries should not be modified (otherwise they will be out
 * of sync with the spatial index).
 */
public class IndexedFeatureCollection extends FeatureCollectionWrapper {

    private SpatialIndex spatialIndex;

    /**
     * Constructs an IndexedFeatureCollection wrapping the given
     * FeatureCollection and using the default spatial index.
     */
    public IndexedFeatureCollection(FeatureCollection fc) {
        //Based on tests on Victoria ICI data, 10 is an optimum node-capacity for
        //fast queries. [Bob Boseko]
        this(fc, new STRtree(10));
    }

    /**
     * Constructs an IndexedFeatureCollection wrapping the given
     * FeatureCollection and using the given empty spatial index.
     */
    public IndexedFeatureCollection(FeatureCollection fc,
            SpatialIndex spatialIndex) {
        super(fc);
        this.spatialIndex = spatialIndex;
        createIndex();
    }

    public void add(Feature feature) {
        throw new UnsupportedOperationException(I18N.get("feature.IndexedFeatureCollection.index-cannot-be-modified"));
    }

    public void remove(Feature feature) {
        throw new UnsupportedOperationException(I18N.get("feature.IndexedFeatureCollection.index-cannot-be-modified"));
    }

    public List query(Envelope env) {
        //System.out.println("FC size = " + base.size());
        //System.out.println("Quadtree size = " + quadtreeIndex.size());
        // index query returns list of *potential* overlaps (e.g. it is a primary filter)
        List candidate = spatialIndex.query(env);

        // filter out only Features where envelope actually intersects
        List result = new ArrayList();

        for (Iterator i = candidate.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Geometry g = f.getGeometry();

            if (env.intersects(g.getEnvelopeInternal())) {
                result.add(f);
            }
        }

        return result;
    }

    private void createIndex() {
        int count = 0; // debugging

        for (Iterator i = iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            spatialIndex.insert(f.getGeometry().getEnvelopeInternal(), f);
            count++;
        }
    }

    public void addAll(Collection features) {
        throw new UnsupportedOperationException(I18N.get("feature.IndexedFeatureCollection.index-cannot-be-modified"));
    }

    public Collection remove(Envelope env) {
        throw new UnsupportedOperationException(I18N.get("feature.IndexedFeatureCollection.index-cannot-be-modified"));
    }

    public void removeAll(Collection features) {
        throw new UnsupportedOperationException(I18N.get("feature.IndexedFeatureCollection.index-cannot-be-modified"));
    }
}
