package com.osfac.dmt.qa.diff;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.vividsolutions.jts.geom.Geometry;
import java.util.*;

public class MatchCollection {

    private FeatureCollection inputFC;
    private Collection matchFeatures = new ArrayList();
    private Collection matchGeometries = new ArrayList();

    public MatchCollection(FeatureCollection fc, boolean splitIntoComponents) {
        this.inputFC = fc;
        init(fc, splitIntoComponents);
    }

    private void init(FeatureCollection fc, boolean splitIntoComponents) {
        for (Iterator i = fc.iterator(); i.hasNext();) {
            Feature feat = (Feature) i.next();
            MatchFeature matchFeat = new MatchFeature(feat);
            matchFeatures.add(matchFeat);
            Geometry geom = feat.getGeometry();
            Collection list = MatchGeometry.splitGeometry(geom, splitIntoComponents);
            for (Iterator j = list.iterator(); j.hasNext();) {
                Geometry g = (Geometry) j.next();
                MatchGeometry matchGeom = new MatchGeometry(matchFeat, g);
                matchGeometries.add(matchGeom);
            }
        }
    }

    public Iterator geometryIterator() {
        return matchGeometries.iterator();
    }

    /**
     * An iterator over all MatchFeatures in the collection.
     */
    public Iterator iterator() {
        return matchFeatures.iterator();
    }

    public int size() {
        return matchFeatures.size();
    }

    public int geometrySize() {
        return matchGeometries.size();
    }

    /**
     * Updates the match flag for features based on the matches
     */
    public void computeFeatureMatches() {
        // set all feature matches to true
        for (Iterator i = matchFeatures.iterator(); i.hasNext();) {
            MatchFeature mf = (MatchFeature) i.next();
            mf.setMatched(true);
        }
        // clear feature matches if any feature geometry is unmatched
        for (Iterator j = matchGeometries.iterator(); j.hasNext();) {
            MatchGeometry mg = (MatchGeometry) j.next();
            if (!mg.isMatched()) {
                mg.getFeature().setMatched(false);
            }
        }
    }

    /**
     * Ensures that if a feature is unmatched, any features matched to its
     * geometries are also unmatched
     */
    public void propagateUnmatchedFeatures() {
        for (Iterator j = matchGeometries.iterator(); j.hasNext();) {
            MatchGeometry mg = (MatchGeometry) j.next();
            if (!mg.getFeature().isMatched()) {
                MatchGeometry mgOpposite = mg.getMatch();
                if (mgOpposite != null) {
                    mgOpposite.getFeature().setMatched(false);
                }
            }
        }
    }

    public FeatureCollection getUnmatchedFeatures() {
        FeatureCollection noMatch = new FeatureDataset(inputFC.getFeatureSchema());
        for (Iterator i = matchFeatures.iterator(); i.hasNext();) {
            MatchFeature mf = (MatchFeature) i.next();
            if (!mf.isMatched()) {
                noMatch.add(mf.getFeature());
            }
        }
        return noMatch;
    }
}
