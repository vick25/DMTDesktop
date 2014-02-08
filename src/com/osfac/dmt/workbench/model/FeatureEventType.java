package com.osfac.dmt.workbench.model;

/**
 * Whether a Feature was added, removed, or modified.
 */
public class FeatureEventType {

    public final static FeatureEventType ADDED = new FeatureEventType("ADDED");
    public final static FeatureEventType DELETED = new FeatureEventType(
            "DELETED");
    public final static FeatureEventType GEOMETRY_MODIFIED = new FeatureEventType(
            "GEOMETRY MODIFIED");
    public final static FeatureEventType ATTRIBUTES_MODIFIED = new FeatureEventType(
            "ATTRIBUTES MODIFIED");
    private String name;

    public String toString() {
        return name;
    }

    private FeatureEventType(String name) {
        this.name = name;
    }
}
