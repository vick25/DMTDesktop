package com.osfac.dmt.workbench.model;

import com.vividsolutions.jts.util.Assert;

/**
 * An addition, removal, or modification of a Layer.
 *
 * @see Layer
 * @see LayerEventType
 */
public class LayerEvent {

    private Layerable layerable;
    private LayerEventType type;
    private Category category;
    private int layerableIndex;

    public LayerEvent(Layerable layerable, LayerEventType type,
            Category category, int layerIndex) {
        Assert.isTrue(category != null);
        Assert.isTrue(layerable != null);
        Assert.isTrue(type != null);
        this.layerable = layerable;
        this.type = type;
        this.category = category;
        this.layerableIndex = layerIndex;
    }

    public LayerEventType getType() {
        return type;
    }

    public Layerable getLayerable() {
        return layerable;
    }

    public Category getCategory() {
        return category;
    }

    public int getLayerableIndex() {
        return layerableIndex;
    }
}
