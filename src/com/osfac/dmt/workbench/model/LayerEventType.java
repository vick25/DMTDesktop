package com.osfac.dmt.workbench.model;

/**
 * Whether a Layer was added, removed, or modified.
 */
public final class LayerEventType {

    public final static LayerEventType ADDED = new LayerEventType("ADDED");
    public final static LayerEventType REMOVED = new LayerEventType("REMOVED");
    /**
     * Metadata includes the name of the layer.
     */
    public final static LayerEventType METADATA_CHANGED = new LayerEventType(
            "METADATA_CHANGED");
    /**
     * The data changed or a style changed.
     */
    public final static LayerEventType APPEARANCE_CHANGED = new LayerEventType(
            "APPEARANCE_CHANGED");
    public final static LayerEventType VISIBILITY_CHANGED = new LayerEventType(
            "VISIBILITY_CHANGED");
    private String name;

    private LayerEventType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
