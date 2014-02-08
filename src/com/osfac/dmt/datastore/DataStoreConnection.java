package com.osfac.dmt.datastore;

import com.osfac.dmt.io.FeatureInputStream;

/**
 * A connection to a datastore which can execute {@link Query}s.
 */
public interface DataStoreConnection {

    DataStoreMetadata getMetadata();

    FeatureInputStream execute(Query query);

    void close() throws DataStoreException;

    boolean isClosed() throws DataStoreException;
}