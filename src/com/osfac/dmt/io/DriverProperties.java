package com.osfac.dmt.io;

import java.util.Properties;

/**
 * Object to store a bunch of key/value pairs used by the input/output
 * drivers/classes. <br>
 *
 * dp = new DriverProperties() <br> dp.set('DefaultValue','c:\me.shp') <br> <br>
 * is the same as:<br> <br> dp = new DriverProperties('c:\me.shp')<br> <br>
 * NOTE: dp.get('DefaultValue') is available via the parent class <br> Typically
 * one uses 'DefaultValue' or 'InputFile' or 'OutputFile'
 */
public class DriverProperties extends Properties {

    /**
     * Creates new DataProperties
     */
    public DriverProperties() {
    }

    /**
     * constructor that will autoset the key 'DefaultValue'
     *
     * @param defaultValue value portion for the the key 'DefaultValue'
     *
     */
    public DriverProperties(String defaultValue) {
        this.set("DefaultValue", defaultValue);
    }

    /**
     * Sets a key/value pair in the object. <br> It returns the object so you
     * can cascade sets: <br> dp.set ('a','value1') <br> .set('b','value2') <br>
     * ...
     *
     * @param key key name
     * @param value key's value
     */
    public DriverProperties set(String key, String value) {
        setProperty(key, value);
        return this;
    }
}
