package com.osfac.dmt.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * String-to-Object map that anyone can use. For example, the Options dialog has
 * a single instance, and it's stored on the Workbench Blackboard.
 */
public class Blackboard implements Cloneable, Serializable {

    private static final long serialVersionUID = 6504993615735124204L;
    private HashMap properties = new HashMap();

    /**
     * Used by Java2XML
     */
    public HashMap getProperties() {
        return properties;
    }

    /**
     * Used by Java2XML
     */
    public void setProperties(HashMap properties) {
        this.properties = properties;
    }

    public Blackboard put(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public Blackboard put(String key, boolean value) {
        put(key, new Boolean(value));
        return this;
    }

    public Blackboard putAll(Map properties) {
        this.properties.putAll(properties);
        return this;
    }

    public boolean get(String key, boolean defaultValue) {
        if (get(key) == null) {
            put(key, defaultValue);
        }

        return getBoolean(key);
    }

    public boolean getBoolean(String key) {
        return ((Boolean) get(key)).booleanValue();
    }

    public Blackboard put(String key, int value) {
        put(key, new Integer(value));
        return this;
    }

    public Blackboard put(String key, double value) {
        put(key, new Double(value));
        return this;
    }

    public double get(String key, double defaultValue) {
        if (get(key) == null) {
            put(key, defaultValue);
        }

        return getDouble(key);
    }

    public int get(String key, int defaultValue) {
        if (get(key) == null) {
            put(key, defaultValue);
        }

        return getInt(key);
    }

    public int getInt(String key) {
        return ((Integer) get(key)).intValue();
    }

    public double getDouble(String key) {
        return ((Double) get(key)).doubleValue();
    }

    public Object get(String key, Object defaultValue) {
        if (get(key) == null) {
            put(key, defaultValue);
        }

        return get(key);
    }

    public Object clone() {
        return new Blackboard().putAll(properties);
    }
}
