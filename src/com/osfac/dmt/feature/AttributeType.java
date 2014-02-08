package com.osfac.dmt.feature;

import com.vividsolutions.jts.geom.Geometry;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Whether an attribute on a feature is a string, geometry, integer, etc.
 *
 * @see com.osfac.dmt.feature.FeatureSchema
 */
public class AttributeType implements Serializable {

    private static final long serialVersionUID = -8443945292593638566L;
    private static HashMap nameToAttributeTypeMap = new HashMap();

    /**
     * Returns all instances of AttributeType.
     */
    public static Collection allTypes() {
        return nameToAttributeTypeMap.values();
    }
    /**
     * For strings
     */
    public final static AttributeType STRING = new AttributeType("STRING", String.class);
    /**
     * For spatial data
     */
    public final static AttributeType GEOMETRY = new AttributeType("GEOMETRY", Geometry.class);
    /**
     * For long values (64-bit)
     */
    public final static AttributeType INTEGER = new AttributeType("INTEGER", Integer.class);

    /**
     * @see <a
     * href="http://www.javaworld.com/javaworld/javatips/jw-javatip122.html">www.javaworld.com</a>
     */
    private Object readResolve() {
        return nameToAttributeTypeMap.get(name);
    }
    /**
     * For dates
     *
     * @see java.util.Date
     */
    public final static AttributeType DATE = new AttributeType("DATE", Date.class);
    //<<TODO:IMPROVE>> If it's a long, perhaps we should name it LONG instead
    //of INTEGER. [Bob Boseko] 
    //Why was it necessary to use long values? [Bob Boseko]
    /**
     * For double-precision values (64-bit)
     */
    public final static AttributeType DOUBLE = new AttributeType("DOUBLE", Double.class);
    /**
     * Experimental
     */
    public final static AttributeType OBJECT = new AttributeType("OBJECT", Object.class);
    private String name;

    private AttributeType(String name, Class javaClass) {
        this.name = name;
        this.javaClass = javaClass;
        nameToAttributeTypeMap.put(name, this);
    }

    public String toString() {
        return name;
    }

    /**
     * Converts a type name to an AttributeType.
     *
     * @param name the name of the AttributeType to retrieve
     * @return the corresponding AttributeType
     * @throws InvalidAttributeTypeException if the type name is unrecognized
     */
    public final static AttributeType toAttributeType(String name) {
        AttributeType type = (AttributeType) nameToAttributeTypeMap.get(name);

        if (type == null) {
            throw new IllegalArgumentException();
        }

        return type;
    }

    /**
     * Returns the Java class of attributes that are said to be of this
     * AttributeType.
     */
    public Class toJavaClass() {
        return javaClass;
    }
    private Class javaClass;

    /**
     * Returns the AttributeType of attributes that are instances of the given
     * class, or null if it is not associated with any AttributeType.
     */
    public static AttributeType toAttributeType(Class javaClass) {
        for (Iterator i = allTypes().iterator(); i.hasNext();) {
            AttributeType type = (AttributeType) i.next();
            if (type.toJavaClass() == javaClass) {
                return type;
            }
        }
        return null;
    }
}
