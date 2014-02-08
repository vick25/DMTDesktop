package com.osfac.dmt.workbench.model;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureCollectionWrapper;

/**
 * Restricts the number of Features in a FeatureCollection to 1.
 */
public class OneItemFeatureCollection extends FeatureCollectionWrapper {

    public OneItemFeatureCollection(FeatureCollection fc) {
        super(fc);
    }

    public void add(Feature feature) {
        clear();
        getWrappee().add(feature);
    }
}
