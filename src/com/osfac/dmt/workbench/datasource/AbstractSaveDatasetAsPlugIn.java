package com.osfac.dmt.workbench.datasource;

import com.vividsolutions.jts.util.Assert;
import com.osfac.dmt.I18N;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;

public abstract class AbstractSaveDatasetAsPlugIn
        extends AbstractLoadSaveDatasetPlugIn {

    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        Assert.isTrue(getDataSourceQueries().size() == 1);

        DataSourceQuery dataSourceQuery = (DataSourceQuery) getDataSourceQueries().iterator().next();
        Assert.isTrue(dataSourceQuery.getDataSource().isWritable());
        monitor.report(I18N.get("datasource.SaveDatasetAsPlugIn.saving") + " "
                + dataSourceQuery.toString() + "...");

        Connection connection = dataSourceQuery.getDataSource().getConnection();
        try {
            connection
                    .executeUpdate(dataSourceQuery.getQuery(), context
                    .getSelectedLayer(0).getFeatureCollectionWrapper(),
                    monitor);
        } finally {
            connection.close();
        }
        context.getSelectedLayer(0).setDataSourceQuery(dataSourceQuery)
                .setFeatureCollectionModified(false);
    }

    public static MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(
                workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }
}
