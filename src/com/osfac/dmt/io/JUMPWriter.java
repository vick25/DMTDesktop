package com.osfac.dmt.io;

import com.osfac.dmt.feature.FeatureCollection;

/**
 * Interface for JUMPWriter classes. Note: This is the old I/O API. Developers
 * writing new I/O classes are encouraged to use the new API
 * (com.osfac.dmt.io.datasource).
 */
public interface JUMPWriter {

    /**
     * Write the specified file (dp property 'OutputFile' or 'DefaultValue')
     * with any other specific parameters.
     */
    void write(FeatureCollection featureCollection, DriverProperties dp)
            throws IllegalParametersException, Exception;
}
