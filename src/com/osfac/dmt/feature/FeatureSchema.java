package com.osfac.dmt.feature;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystem;
import com.vividsolutions.jts.util.Assert;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Metadata for a FeatureCollection: attribute names and types.
 *
 * @see FeatureCollection
 */
public class FeatureSchema implements Cloneable, Serializable {

    private static final long serialVersionUID = -8627306219650589202L;
    //<<TODO:QUESTION>> Is this an efficient implementation? Must cast the Integer to
    //an int. [Bob Boseko]
    private CoordinateSystem coordinateSystem = CoordinateSystem.UNSPECIFIED;
    private HashMap attributeNameToIndexMap = new HashMap();
    private int geometryIndex = -1;
    private int attributeCount = 0;
    private ArrayList attributeNames = new ArrayList();
    private ArrayList attributeTypes = new ArrayList();
    private ArrayList<Boolean> attributeReadOnly = new ArrayList<>();

    //todo Deep-copy! [Bob Boseko]
    //deep copy done 25. Juli 2005 [sstein] 
    public Object clone() {
        try {
            FeatureSchema fs = new FeatureSchema();
            for (int i = 0; i < this.attributeCount; i++) {
                AttributeType at = (AttributeType) this.attributeTypes.get(i);
                String aname = (String) this.attributeNames.get(i);
                fs.addAttribute(aname, at);
                fs.setAttributeReadOnly(i, isAttributeReadOnly(i));
            }
            fs.setCoordinateSystem(this.coordinateSystem);
            return fs;
        } catch (Exception ex) {
            Assert.shouldNeverReachHere();
            return null;
        }
    }

    /**
     * Returns the zero-based index of the attribute with the given name
     * (case-sensitive)
     *
     * @throws IllegalArgumentException if attributeName is unrecognized
     */
    public int getAttributeIndex(String attributeName) {
        //<<TODO:RECONSIDER>> Attribute names are currently case sensitive.
        //I wonder whether or not this is desirable. [Bob Boseko]
        Integer index = (Integer) attributeNameToIndexMap.get(attributeName);
        if (index == null) {
            throw new IllegalArgumentException(
                    I18N.get("feature.FeatureSchema.unrecognized-attribute-name") + " " + attributeName);
        }
        return index.intValue();
    }

    /**
     * Returns whether this FeatureSchema has an attribute with this name
     *
     * @param attributeName the name to look up
     * @return whether this FeatureSchema has an attribute with this name
     */
    public boolean hasAttribute(String attributeName) {
        return attributeNameToIndexMap.get(attributeName) != null;
    }

    /**
     * Returns the attribute index of the Geometry, or -1 if there is no
     * Geometry attribute
     */
    public int getGeometryIndex() {
        return geometryIndex;
    }

    /**
     * Returns the (case-sensitive) name of the attribute at the given
     * zero-based index.
     */
    public String getAttributeName(int attributeIndex) {
        return (String) attributeNames.get(attributeIndex);
    }

    /**
     * Returns whether the attribute at the given zero-based index is a string,
     * integer, double, etc.
     */
    public AttributeType getAttributeType(int attributeIndex) {
        return (AttributeType) attributeTypes.get(attributeIndex);
    }

    /**
     * Returns whether the attribute with the given name (case-sensitive) is a
     * string, integer, double, etc.
     */
    public AttributeType getAttributeType(String attributeName) {
        return getAttributeType(getAttributeIndex(attributeName));
    }

    /**
     * Returns the total number of spatial and non-spatial attributes in this
     * FeatureSchema. There are 0 or 1 spatial attributes and 0 or more
     * non-spatial attributes.
     */
    public int getAttributeCount() {
        return attributeCount;
    }

    /**
     * Adds an attribute with the given case-sensitive name.
     *
     * @throws AssertionFailedException if a second Geometry is being added
     */
    public void addAttribute(String attributeName, AttributeType attributeType) {
        if (AttributeType.GEOMETRY == attributeType) {
            Assert.isTrue(geometryIndex == -1);
            geometryIndex = attributeCount;
        }
        attributeNames.add(attributeName);
        attributeNameToIndexMap.put(attributeName, new Integer(attributeCount));
        attributeTypes.add(attributeType);
        // default to current implementation - all attributes are editable 
        // (not readonly)
        attributeReadOnly.add(false);
        attributeCount++;
    }

    /**
     * Returns whether the two FeatureSchemas have the same attribute names with
     * the same types and in the same order.
     */
    public boolean equals(Object other) {
        return this.equals(other, false);
    }

    /**
     * Returns whether the two FeatureSchemas have the same attribute names with
     * the same types and (optionally) in the same order.
     */
    public boolean equals(Object other, boolean orderMatters) {
        if (!(other instanceof FeatureSchema)) {
            return false;
        }
        FeatureSchema otherFeatureSchema = (FeatureSchema) other;
        if (attributeNames.size() != otherFeatureSchema.attributeNames.size()) {
            return false;
        }
        for (int i = 0; i < attributeNames.size(); i++) {
            String attributeName = (String) attributeNames.get(i);
            if (!otherFeatureSchema.attributeNames.contains(attributeName)) {
                return false;
            }
            if (orderMatters
                    && !otherFeatureSchema.attributeNames.get(i).equals(attributeName)) {
                return false;
            }
            if (getAttributeType(attributeName)
                    != otherFeatureSchema.getAttributeType(attributeName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the CoordinateSystem associated with this FeatureSchema, but does
     * not perform any reprojection.
     *
     * @return this FeatureSchema
     */
    public FeatureSchema setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
        return this;
    }

    /**
     * @see #setCoordinateSystem(CoordinateSystem)
     */
    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Returns the "readonly" status of the attribute specified by the
     * attributeIndex.<br> <br> A return result of <tt>TRUE</tt> means the a
     * user will not be able to edit the attribute in the layer's attribute
     * table, even though the layer's "editable" flag has been set to
     * <tt>TRUE</tt>
     *
     * @param attributeIndex The index of the attribute in question.
     * @return <tt>TRUE</tt> if the specified attribute has been previously set
     * as readonly.
     * @see #setAttributeReadOnly(int, boolean)
     */
    public boolean isAttributeReadOnly(int attributeIndex) {
        return attributeReadOnly.get(attributeIndex);
    }

    /**
     * Sets the "readonly" status of the attribute specified by the
     * attributeIndex. <br> <br> Some schemas (like those that represent
     * database tables) can have attributes that should not be modified (like
     * primary keys). Setting such an attribute as readonly means a user will
     * not be able to edit the attribute in the layer's attribute table, even
     * though the layer's "editable" flag has been set to <tt>TRUE</tt>
     *
     * @param attributeIndex The index of the attribute to set
     * @param isReadOnly A flag that indicates whether the specified attribute
     * should be considered "readonly".
     * @see #isAttributeReadOnly(int)
     */
    public void setAttributeReadOnly(int attributeIndex, boolean isReadOnly) {
        attributeReadOnly.set(attributeIndex, isReadOnly);
    }
}
