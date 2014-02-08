package com.osfac.dmt.datastore.postgis;

import com.osfac.dmt.datastore.AdhocQuery;
import com.osfac.dmt.datastore.DataStoreConnection;
import com.osfac.dmt.datastore.DataStoreException;
import com.osfac.dmt.datastore.DataStoreMetadata;
import com.osfac.dmt.datastore.FilterQuery;
import com.osfac.dmt.datastore.Query;
import com.osfac.dmt.datastore.SpatialReferenceSystemID;
import com.osfac.dmt.io.FeatureInputStream;
import java.sql.*;

public class PostgisDSConnection implements DataStoreConnection {

    private PostgisDSMetadata dbMetadata;
    private Connection connection;

    public PostgisDSConnection(Connection conn) {
        connection = conn;
        dbMetadata = new PostgisDSMetadata(this);
    }

    public Connection getConnection() {
        return connection;
    }

    public DataStoreMetadata getMetadata() {
        return dbMetadata;
    }

    public FeatureInputStream execute(Query query) {
        if (query instanceof FilterQuery) {
            try {
                return executeFilterQuery((FilterQuery) query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (query instanceof AdhocQuery) {
            return executeAdhocQuery((AdhocQuery) query);
        }
        throw new IllegalArgumentException("Unsupported Query type");
    }

    /**
     * Executes a filter query.
     *
     * The SRID is optional for queries - it will be determined automatically
     * from the table metadata if not supplied.
     *
     * @param query the query to execute
     * @return the results of the query
     * @throws SQLException
     */
    public FeatureInputStream executeFilterQuery(FilterQuery query) throws SQLException {
        SpatialReferenceSystemID srid = dbMetadata.getSRID(query.getDatasetName(), query.getGeometryAttributeName());
        String[] colNames = dbMetadata.getColumnNames(query.getDatasetName());

        PostgisSQLBuilder builder = new PostgisSQLBuilder(srid, colNames);
        String queryString = builder.getSQL(query);

        PostgisFeatureInputStream ifs = new PostgisFeatureInputStream(connection, queryString);
        return ifs;
    }

    public FeatureInputStream executeAdhocQuery(AdhocQuery query) {
        String queryString = query.getQuery();
        PostgisFeatureInputStream ifs = new PostgisFeatureInputStream(connection, queryString);
        return ifs;
    }

    public void close()
            throws DataStoreException {
        try {
            connection.close();
        } catch (Exception ex) {
            throw new DataStoreException(ex);
        }
    }

    public boolean isClosed() throws DataStoreException {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new DataStoreException(e);
        }
    }
}