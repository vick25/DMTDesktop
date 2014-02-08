package com.osfac.dmt.qa.diff;

import com.osfac.dmt.feature.Feature;

public class MatchFeature {

    private Feature feature;
    private boolean isMatched;

    public MatchFeature(Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setMatched(boolean isMatched) {
        this.isMatched = isMatched;
    }

    public boolean isMatched() {
        return isMatched;
    }
}
