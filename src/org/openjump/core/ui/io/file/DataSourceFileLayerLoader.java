package org.openjump.core.ui.io.file;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSource;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.LangUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.ui.HTMLFrame;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjump.core.ui.util.ExceptionUtil;
import org.openjump.core.ui.util.TaskUtil;
import org.openjump.util.UriUtil;

/**
 * The DataSourceFileLayerLoader is an implementation of {@link FileLayerLoader}
 * that wraps an existing file based {@link DataSource} class.
 *
 * @author Paul Austin
 */
public class DataSourceFileLayerLoader extends AbstractFileLayerLoader {

    /**
     * The {@link DataSource} class.
     */
    private Class dataSourceClass;
    /**
     * The workbench context.
     */
    private WorkbenchContext workbenchContext;

    /**
     * Construct a new DataSourceFileLayerLoader.
     *
     * @param workbenchContext The workbench context.
     * @param dataSourceClass The {@link DataSource} class.
     * @param description The file format name.
     * @param extensions The list of supported extensions.
     */
    public DataSourceFileLayerLoader(WorkbenchContext workbenchContext,
            Class dataSourceClass, String description, List<String> extensions) {
        super(description, extensions);
        this.workbenchContext = workbenchContext;
        this.dataSourceClass = dataSourceClass;
    }

    /**
     * Open the file specified by the URI with the map of option values.
     *
     * @param monitor The TaskMonitor.
     * @param uri The URI to the file to load.
     * @param options The map of options.
     * @return True if the file could be loaded false otherwise.
     */
    public boolean open(TaskMonitor monitor, URI uri, Map<String, Object> options) {
        DataSource dataSource = (DataSource) LangUtil.newInstance(dataSourceClass);
        Map<String, Object> properties = toProperties(uri, options);
        dataSource.setProperties(properties);
        String name = UriUtil.getFileNameWithoutExtension(uri);
        DataSourceQuery dataSourceQuery = new DataSourceQuery(dataSource, null,
                name);
        ArrayList exceptions = new ArrayList();
        String layerName = dataSourceQuery.toString();
        monitor.report("Loading " + layerName + "...");

        Connection connection = dataSourceQuery.getDataSource().getConnection();
        try {
            FeatureCollection dataset = dataSourceQuery.getDataSource()
                    .installCoordinateSystem(
                    connection.executeQuery(dataSourceQuery.getQuery(), exceptions,
                    monitor),
                    CoordinateSystemRegistry.instance(workbenchContext.getBlackboard()));
            if (dataset != null) {
                LayerManager layerManager = workbenchContext.getLayerManager();
                Layer layer = new Layer(layerName,
                        layerManager.generateLayerFillColor(), dataset, layerManager);
                Category category = TaskUtil.getSelectedCategoryName(workbenchContext);
                layerManager.addLayerable(category.getName(), layer);
                layer.setName(layerName);

//        category.add(0, layer);

                layer.setDataSourceQuery(dataSourceQuery);
                layer.setFeatureCollectionModified(false);
            }
        } finally {
            connection.close();
        }
        if (!exceptions.isEmpty()) {
            WorkbenchFrame workbenchFrame = workbenchContext.getWorkbench()
                    .getFrame();
            HTMLFrame outputFrame = workbenchFrame.getOutputFrame();
            outputFrame.createNewDocument();
            ExceptionUtil.reportExceptions(exceptions, dataSourceQuery,
                    workbenchFrame, outputFrame);
            workbenchFrame.warnUser(I18N.get("datasource.LoadDatasetPlugIn.problems-were-encountered"));
            return false;
        }

        return true;
    }

    /**
     * Convert the URI and map of options for the data source. If the URI is a
     * ZIP uri the File option will be set to the ZIP file name and the
     * CompressedFile set to the entry in the ZIP file.
     *
     * @param uri The URI to the file.
     * @param options The selected options.
     * @return The options.
     */
    protected Map<String, Object> toProperties(URI uri,
            Map<String, Object> options) {
        Map<String, Object> properties = new HashMap<>();
        File file;
        if (uri.getScheme().equals("zip")) {
            file = UriUtil.getZipFile(uri);
            String compressedFile = UriUtil.getZipEntryName(uri);
            properties.put("CompressedFile", compressedFile);
        } else {
            file = new File(uri);
        }
        String filePath = file.getAbsolutePath();
        properties.put(DataSource.FILE_KEY, filePath);
        properties.putAll(options);
        return properties;
    }
}
