package com.osfac.dmt.workbench.model;

/**
 * Whether a Category was added, removed, or modified.
 */
public final class CategoryEventType {

    public final static CategoryEventType ADDED = new CategoryEventType();
    public final static CategoryEventType REMOVED = new CategoryEventType();
    /**
     * Metadata includes the name of the category.
     */
    public final static CategoryEventType METADATA_CHANGED = new CategoryEventType();

    private CategoryEventType() {
    }
}
