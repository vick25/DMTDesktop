package com.osfac.dmt.io;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureSchema;

/**
 * A stream of features from an external source, which may throw exceptions
 * during processing.
 */
public interface FeatureInputStream {

    public FeatureSchema getFeatureSchema();

    public Feature next() throws Exception;

    public boolean hasNext() throws Exception;

    public void close() throws Exception;
}