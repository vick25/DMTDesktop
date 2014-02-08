package com.osfac.dmt.datastore;

import com.osfac.dmt.parameter.ParameterList;
import com.osfac.dmt.parameter.ParameterListSchema;

/**
 * A driver for a given type of datastore
 */
public interface DataStoreDriver {

    public static final Object REGISTRY_CLASSIFICATION = DataStoreDriver.class
            .getName();

    String getName();

    ParameterListSchema getParameterListSchema();

    DataStoreConnection createConnection(ParameterList params) throws Exception;

    /**
     * @return a description of the driver
     */
    public String toString();

    boolean isAdHocQuerySupported();
}