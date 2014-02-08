package com.osfac.dmt.feature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A representation of an object in the world, including its location, geometry,
 * and other attributes. A Feature has spatial attributes (polygons, points,
 * etc.) and non-spatial attributes (strings, dates, and numbers). <p> In the
 * current Workbench model, each feature has one spatial attribute (Geometry)
 * and zero or more non-spatial attributes.
 */
public interface Feature extends Cloneable, Comparable {

    /**
     * A low-level accessor that is not normally used.
     *
     * @param attributes may have a different length than the current
     * attributes.
     */
    public abstract void setAttributes(Object[] attributes);

    /**
     * A low-level accessor that is not normally used.
     */
    public abstract void setSchema(FeatureSchema schema);

    /**
     * Returns a number that uniquely identifies this feature. This number is
     * not persistent. (Implementors can obtain an ID from FeatureUtil#nextID).
     *
     * @return n, where this feature is the nth Feature created by this
     * application
     */
    public abstract int getID();

    /**
     * Sets the specified attribute.
     *
     * @param attributeIndex the array index at which to put the new attribute
     * @param newAttribute the new attribute
     */
    public abstract void setAttribute(int attributeIndex, Object newAttribute);

    /**
     * Sets the specified attribute.
     *
     * @param attributeName the name of the attribute to set
     * @param newAttribute the new attribute
     */
    public abstract void setAttribute(
            String attributeName,
            Object newAttribute);

    /**
     * Convenience method for setting the spatial attribute. JUMP Workbench
     * PlugIns and CursorTools should not use this method directly, but should
     * use an EditTransaction, so that the proper events are fired.
     *
     * @param geometry the new spatial attribute
     */
    public abstract void setGeometry(Geometry geometry);

    /**
     * Returns the specified attribute.
     *
     * @param i the index of the attribute to get
     * @return the attribute
     */
    public abstract Object getAttribute(int i);

    /**
     * Returns the specified attribute.
     *
     * @param name the name of the attribute to get
     * @return the attribute
     */
    public abstract Object getAttribute(String name);

    /**
     * Returns the result of calling #toString on the attribute at the given
     * (zero-based) index, or "" if it is null. Note that this method may be
     * called even if the attribute is not of type AttributeType.STRING.
     */
    public abstract String getString(int attributeIndex);

    /**
     * Returns a integer attribute.
     *
     * @param attributeIndex the index of the attribute to retrieve
     * @return the integer attribute with the given name
     */
    public abstract int getInteger(int attributeIndex);

    /**
     * Returns a double attribute.
     *
     * @param attributeIndex the index of the attribute to retrieve
     * @return the double attribute with the given name
     */
    public abstract double getDouble(int attributeIndex);

    /**
     * Returns the result of calling #toString on the attribute with the given
     * (case-sensitive) name, or "" if it is null. Note that this method may be
     * called even if the attribute is not of type AttributeType.STRING.
     */
    public abstract String getString(String attributeName);

    /**
     * Convenience method for returning the spatial attribute.
     *
     * @return the feature's spatial attribute
     */
    public abstract Geometry getGeometry();

    /**
     * Returns the feature's metadata
     *
     * @return the metadata describing the names and types of the attributes
     */
    public abstract FeatureSchema getSchema();

    /**
     * Clones this Feature. The geometry will also be cloned.
     *
     * @return a new Feature with the same attributes as this Feature
     */
    public abstract Object clone();

    /**
     * Clones this Feature.
     *
     * @param deep whether or not to clone the geometry
     * @return a new Feature with the same attributes as this Feature
     */
    public abstract Feature clone(boolean deep);

    /**
     * A low-level accessor that is not normally used.
     */
    public abstract Object[] getAttributes();
}
