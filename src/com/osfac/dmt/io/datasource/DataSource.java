package com.osfac.dmt.io.datasource;

import com.osfac.dmt.coordsys.CoordinateSystem;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * A file, database, web service, or other source of data. To be savable to a
 * project file, a DataSource must not be an anonymous class (because the class
 * name is recorded in the project file) and it must have a parameterless
 * constructor (so it can be reconstructed by simply being instantiated and
 * having #setProperties called).
 */
public abstract class DataSource {

    private static Logger LOG = Logger.getLogger(DataSource.class);
    private HashMap properties;

    /**
     * Sets properties required to open a DataSource, such as username,
     * password, filename, coordinate system, etc. Called by
     * DataSourceQueryChoosers.
     */
    public void setProperties(Map properties) {
        this.properties = new HashMap(properties);
    }

    public Map getProperties() {
        //This method needs to be public because it is called by Java2XML [Bob Boseko 11/13/2003]

        //I was returning a Collections.unmodifiableMap before, but
        //Java2XML couldn't open it after saving it (can't instantiate
        //java.util.Collections$UnmodifiableMap). [Bob Boseko]
        return properties;
    }

    /**
     * Creates a new Connection to this DataSource.
     */
    public abstract Connection getConnection();
    /**
     * Filename property, used for file-based DataSources
     */
    public static final String FILE_KEY = "File";
    /**
     * Coordinate-system property, used for files and other DataSources that
     * have a single CoordinateSystem
     */
    public static final String COORDINATE_SYSTEM_KEY = "Coordinate System";

    public boolean isReadable() {
        return true;
    }

    public boolean isWritable() {
        return true;
    }

    public FeatureCollection installCoordinateSystem(FeatureCollection queryResult,
            CoordinateSystemRegistry registry) {
        if (queryResult == null) {
            return queryResult;
        }
        String coordinateSystemName = null;
        try {
            coordinateSystemName = (String) getProperties().get(COORDINATE_SYSTEM_KEY);
        } catch (NullPointerException e) {
            return queryResult;
        }
        if (coordinateSystemName == null) {
            return queryResult;
        }
        CoordinateSystem coordinateSystem = registry.get(coordinateSystemName);
        if (coordinateSystem == null) {
            return queryResult;
        }
        queryResult.getFeatureSchema().setCoordinateSystem(coordinateSystem);
        return queryResult;
    }
}
