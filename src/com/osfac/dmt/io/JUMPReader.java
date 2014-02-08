package com.osfac.dmt.io;

import com.osfac.dmt.feature.FeatureCollection;

/**
 * Interface for JUMPReader classes. Note: This is the old I/O API. Developers
 * writing new I/O classes are encouraged to use the new API
 * (com.osfac.dmt.io.datasource).
 */
public interface JUMPReader {

    /**
     * Read the specified file using the filename given by the "File" property
     * and any other parameters.
     */
    FeatureCollection read(DriverProperties dp) throws Exception;
}
