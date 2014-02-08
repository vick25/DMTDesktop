package com.osfac.dmt.workbench.ui.plugin.datastore;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.DummyTaskMonitor;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn;
import com.osfac.dmt.workbench.ui.plugin.OpenProjectPlugIn;
import javax.swing.ImageIcon;

public class AddDatastoreLayerPlugIn extends AbstractAddDatastoreLayerPlugIn {

    public static final ImageIcon ICON = IconLoader.icon("database_add.png");

    public boolean execute(final PlugInContext context) throws Exception {
        ((AddDatastoreLayerPanel) panel(context)).setCaching(true);
        return super.execute(context);
    }

    public String getName() {
        return I18N.get("jump.workbench.ui.plugin.datastore.AddDatastoreLayerPlugIn.Add-Datastore-Layer");
    }

    private Layer createLayer(
            final AddDatastoreLayerPanel panel,
            final PlugInContext context) throws Exception {

        Layer layer = new Layer(
                panel.getDatasetName(),
                context.getLayerManager().generateLayerFillColor(),
                AddNewLayerPlugIn.createBlankFeatureCollection(),
                context.getLayerManager());

        DataStoreDataSource ds = new DataStoreDataSource(
                panel.getDatasetName(),
                panel.getGeometryAttributeName(),
                panel.getWhereClause(),
                panel.getMaxFeatures(),
                panel.getConnectionDescriptor(),
                panel.isCaching(),
                context.getWorkbenchContext());

        DataSourceQuery dsq = new DataSourceQuery(ds, null, panel.getDatasetName());

        layer.setDataSourceQuery(dsq);

        context.getLayerManager().setFiringEvents(false); // added by michaudm on 2009-04-23
        OpenProjectPlugIn.load(layer,
                CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()),
                new DummyTaskMonitor());
        context.getLayerManager().setFiringEvents(true); // added by michaudm on 2009-04-23
        return layer;
    }

    protected ConnectionPanel createPanel(PlugInContext context) {
        return new AddDatastoreLayerPanel(context.getWorkbenchContext());
    }

    protected Layerable createLayerable(ConnectionPanel panel,
            TaskMonitor monitor, PlugInContext context) throws Exception {
        monitor.report(I18N.get("jump.workbench.ui.plugin.datastore.AddDatastoreLayerPlugIn.Creating-layer"));
        return createLayer((AddDatastoreLayerPanel) panel, context);
    }
}