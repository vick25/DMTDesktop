package com.osfac.dmt.workbench.model;

import java.util.Collection;
import java.util.Collections;
import com.vividsolutions.jts.util.Assert;

/**
 * An addition, removal, or modification of a Feature.
 *
 * @see com.osfac.dmt.feature.Feature Feature
 * @see FeatureEventType
 */
public class FeatureEvent {

    private Layer layer;
    private FeatureEventType type;
    private Collection features;
    private Collection oldFeatureClones;
    //[UT] 25.08.2005 added 
    private Collection oldFeatureAttClones;

    /**
     * @param oldFeatureClones for GEOMETRY_MODIFIED events, clones of the
     * Features before they were modified; null for other events
     */
    public FeatureEvent(
            Collection features,
            FeatureEventType type,
            Layer layer,
            Collection oldFeatureClones) {
        Assert.isTrue(layer != null);
        Assert.isTrue(type != null);

        Assert.isTrue(
                (type == FeatureEventType.GEOMETRY_MODIFIED && oldFeatureClones != null)
                || (type != FeatureEventType.GEOMETRY_MODIFIED && oldFeatureClones == null)
                || (type == FeatureEventType.ATTRIBUTES_MODIFIED && oldFeatureAttClones == null));

        this.layer = layer;
        this.type = type;
        this.features = features;

        //      [UT] 25.08.2005 uncommented and...
        //      this.oldFeatureClones = oldFeatureClones;
        //[UT] 25.08.2005 did like this
        if (this.type == FeatureEventType.GEOMETRY_MODIFIED) {
            this.oldFeatureClones = oldFeatureClones;
        } else if (this.type == FeatureEventType.ATTRIBUTES_MODIFIED) {
            this.oldFeatureAttClones = oldFeatureClones;
        }
    }

    public Layer getLayer() {
        return layer;
    }

    public FeatureEventType getType() {
        return type;
    }

    public Collection getFeatures() {
        return Collections.unmodifiableCollection(features);
    }

    public Collection getOldFeatureClones() {
        return Collections.unmodifiableCollection(oldFeatureClones);
    }

    //[UT] 25.08.2005 added 
    public Collection getOldFeatureAttClones() {
        return Collections.unmodifiableCollection(oldFeatureAttClones);
    }
}
