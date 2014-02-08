package com.osfac.dmt.workbench.model;

/**
 * Notified when a Category, Layer, or Feature is added, removed, or modified.
 *
 * @see FeatureEventType
 * @see LayerEventType
 * @see CategoryEventType
 */
public interface LayerListener {

    /**
     * Fired when a feature is added to or removed from a layer, or when a
     * feature is edited (using an EditTransaction). Warning: this event is
     * fired often.
     */
    public void featuresChanged(FeatureEvent e);

    public void layerChanged(LayerEvent e);

    public void categoryChanged(CategoryEvent e);
}
