package com.osfac.dmt.workbench.ui.plugin.datastore;

import com.osfac.dmt.datastore.AdhocQuery;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.io.FeatureInputStream;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSource;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.datastore.ConnectionDescriptor;
import com.osfac.dmt.workbench.datastore.ConnectionManager;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.plugin.WorkbenchContextReference;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements the DataSource interface in order to persist a query issued from
 * RunDatastoreQueryPlugIn.
 */
public class DataStoreQueryDataSource extends DataSource implements WorkbenchContextReference {

    public static final String DATASET_NAME_KEY = "Dataset Name";
    public static final String SQL_QUERY_KEY = "SQL Query";
    public static final String CONNECTION_DESCRIPTOR_KEY = "Connection Descriptor";
    private WorkbenchContext context;

    public DataStoreQueryDataSource() {
        // Called by Java2XML [Bob Boseko 2005-03-16]
    }

    public DataStoreQueryDataSource(String datasetName,
            String query,
            ConnectionDescriptor connectionDescriptor,
            WorkbenchContext context) {
        setProperties(CollectionUtil.createMap(new Object[]{
                    DATASET_NAME_KEY, datasetName,
                    SQL_QUERY_KEY, query,
                    CONNECTION_DESCRIPTOR_KEY, connectionDescriptor}));
        setWorkbenchContext(context);
    }

    public boolean isWritable() {
        return false;
    }

    public Connection getConnection() {
        return new Connection() {
            public FeatureCollection executeQuery(String query,
                    Collection exceptions, TaskMonitor monitor) {
                try {
                    return createFeatureCollection();
                } catch (Exception e) {
                    exceptions.add(e);
                    return null;
                }
            }

            public FeatureCollection executeQuery(String query,
                    TaskMonitor monitor) throws Exception {
                Collection exceptions = new ArrayList();
                FeatureCollection featureCollection = executeQuery(query,
                        exceptions, monitor);
                if (!exceptions.isEmpty()) {
                    throw (Exception) exceptions.iterator().next();
                }
                return featureCollection;
            }

            public void executeUpdate(String query,
                    FeatureCollection featureCollection, TaskMonitor monitor)
                    throws Exception {
                throw new UnsupportedOperationException();
            }

            public void close() {
                // Do nothing, because DataStore connections are always
                // open (managed by the ConnectionManager). [Bob Boseko
                // 2005-03-16]
            }
        };
    }

    private FeatureCollection createFeatureCollection() {
        FeatureInputStream featureInputStream = null;
        FeatureDataset featureDataset = null;

        ConnectionDescriptor connectionDescriptor =
                (ConnectionDescriptor) getProperties().get(CONNECTION_DESCRIPTOR_KEY);
        String query = (String) getProperties().get(SQL_QUERY_KEY);
        String driver = connectionDescriptor.getDataStoreDriverClassName();
        if (driver.contains("Postgis") && query.matches("(?s).*\\$\\{[^\\{\\}]*\\}.*")) {
            query = expandQuery(query, context.createPlugInContext());
        }

        try {
            featureInputStream = ConnectionManager.instance(context)
                    .getOpenConnection(connectionDescriptor).execute(new AdhocQuery(query));
            featureDataset = new FeatureDataset(featureInputStream.getFeatureSchema());
            int i = 0;
            while (featureInputStream.hasNext()) {
                featureDataset.add(featureInputStream.next());
            }
            return featureDataset;
        } catch (Exception e) {
            context.getWorkbench().getFrame().handleThrowable(e);
        } finally {
            if (featureInputStream != null) {
                try {
                    featureInputStream.close();
                } catch (Exception e) {
                }
            }
        }
        return featureDataset;
    }

    public void setWorkbenchContext(WorkbenchContext context) {
        this.context = context;
        try {
            // This method is called by OpenProjectPlugIn in the
            // GUI thread, so now is a good time to prompt for
            // a password if necessary. [Bob Boseko 2005-03-16]
            new PasswordPrompter().getOpenConnection(ConnectionManager
                    .instance(context),
                    (ConnectionDescriptor) getProperties().get(
                    CONNECTION_DESCRIPTOR_KEY), context.getWorkbench()
                    .getFrame());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String expandQuery(String query, PlugInContext context) {
        GeometryFactory gf = new GeometryFactory();
        Geometry viewG = gf.toGeometry(context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates());
        Geometry fenceG = context.getLayerViewPanel().getFence();
        if (viewG != null) {
            query = query.replaceAll("\\$\\{view\\}", "\\${view:-1}");
            query = query.replaceAll("\\$\\{view(?::(-?[0-9]+))\\}", "ST_GeomFromText('" + viewG.toText() + "',$1)");
        }
        if (fenceG != null) {
            query = query.replaceAll("\\$\\{fence\\}", "\\${fence:-1}");
            query = query.replaceAll("\\$\\{fence(?::(-?[0-9]+))\\}", "ST_GeomFromText('" + fenceG.toText() + "',$1)");
        } else {
            query = query.replaceAll("\\$\\{fence\\}", "\\${fence:-1}");
            query = query.replaceAll("\\$\\{fence(?::(-?[0-9]+))\\}", "ST_GeomFromText('POLYGON EMPTY',$1)");
        }
        return query;
    }
}
