package org.openjump.core.ui.plugin.datastore;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSource;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.datastore.ConnectionDescriptor;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn;
import com.osfac.dmt.workbench.ui.plugin.datastore.AddDatastoreLayerPanel;
import com.osfac.dmt.workbench.ui.plugin.datastore.DataStoreDataSource;
import com.osfac.dmt.workbench.ui.wizard.WizardDialog;
import java.awt.Color;
import java.util.Collection;
import javax.swing.SwingUtilities;
import org.openjump.core.ui.plugin.file.open.ChooseProjectPanel;
import org.openjump.core.ui.swing.wizard.AbstractWizardGroup;

public class AddDataStoreLayerWizard extends AbstractWizardGroup {
  private static final String KEY = AddDataStoreLayerWizard.class.getName();

  private AddDataStoreLayerWizardPanel dataStoreWizardPanel;

  private WorkbenchContext workbenchContext;

  private ChooseProjectPanel chooseProjectPanel;

  public AddDataStoreLayerWizard(WorkbenchContext workbenchContext) {
    super(I18N.get(KEY), IconLoader.icon("database_add.png"),
      AddDataStoreLayerWizardPanel.class.getName());
    this.workbenchContext = workbenchContext;
    dataStoreWizardPanel = new AddDataStoreLayerWizardPanel(workbenchContext);
    addPanel(dataStoreWizardPanel);
    chooseProjectPanel = new ChooseProjectPanel(workbenchContext,
      dataStoreWizardPanel.getID());
    addPanel(chooseProjectPanel);
  }

  public String getFirstId() {
    String firstId = super.getFirstId();
    if (!chooseProjectPanel.hasActiveTaskFrame()
      && chooseProjectPanel.hasTaskFrames()) {
      chooseProjectPanel.setNextID(firstId);
      return chooseProjectPanel.getID();
    } else {
      return firstId;
    }
  }
  
  public void run(WizardDialog dialog, TaskMonitor monitor) throws Exception {
    chooseProjectPanel.activateSelectedProject();
    try {
      AddDatastoreLayerPanel dataStorePanel = dataStoreWizardPanel.getDataStorePanel();
      if (dataStorePanel.validateInput() == null) {
        final Layer layer = createLayer(dataStorePanel, monitor);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            Collection<Category> selectedCategories = workbenchContext.getLayerNamePanel()
              .getSelectedCategories();
            LayerManager layerManager = workbenchContext.getLayerManager();
            String categoryName = StandardCategoryNames.WORKING;
            if (!selectedCategories.isEmpty()) {
              categoryName = selectedCategories.iterator().next().getName();
            }
            try {
                workbenchContext.getLayerViewPanel().getViewport().update();
            } catch(Exception e) {
                //throw NoninvertibleTransformationException;
            }
            layerManager.addLayerable(categoryName, layer);
          }
        });
        workbenchContext.getLayerViewPanel().getViewport().update();
      }
      else throw new Exception(dataStorePanel.validateInput());
    } catch (Exception e) {
      monitor.report(e);
      throw e;
    }
  }

  private Layer createLayer(final AddDatastoreLayerPanel panel,
    TaskMonitor monitor) throws Exception {

    String datasetName = panel.getDatasetName();
    LayerManager layerManager = workbenchContext.getLayerManager();
    Color fillColor = layerManager.generateLayerFillColor();
    FeatureCollection featureCollection = AddNewLayerPlugIn.createBlankFeatureCollection();
    Layer layer = new Layer(datasetName, fillColor, featureCollection,
      layerManager);

    String geometryAttributeName = panel.getGeometryAttributeName();
    String whereClause = panel.getWhereClause();
    int limit = panel.getMaxFeatures();
    ConnectionDescriptor connectionDescriptor = panel.getConnectionDescriptor();
    boolean caching = panel.isCaching();
    DataStoreDataSource ds = new DataStoreDataSource(datasetName,
      geometryAttributeName, whereClause, limit, connectionDescriptor, caching,
      workbenchContext);

    DataSourceQuery dsq = new DataSourceQuery(ds, null, datasetName);

    layer.setDataSourceQuery(dsq);

    CoordinateSystemRegistry crsRegistry = CoordinateSystemRegistry.instance(workbenchContext.getBlackboard());
    try {
        layerManager.setFiringEvents(false); // added by michaudm on 2009-04-05
        load(layer, crsRegistry, monitor);
        layerManager.setFiringEvents(true); // added by michaudm on 2009-04-05
    }
    finally {layerManager.setFiringEvents(true);}
    return layer;
  }

  public static void load(Layer layer, CoordinateSystemRegistry registry,
    TaskMonitor monitor) throws Exception {
    layer.setFeatureCollection(executeQuery(layer.getDataSourceQuery()
      .getQuery(), layer.getDataSourceQuery().getDataSource(), registry,
      monitor));
    layer.setFeatureCollectionModified(false);
  }

  private static FeatureCollection executeQuery(String query,
    DataSource dataSource, CoordinateSystemRegistry registry,
    TaskMonitor monitor) throws Exception {
    Connection connection = dataSource.getConnection();
    try {
      return dataSource.installCoordinateSystem(connection.executeQuery(query,
        monitor), registry);
    } finally {
      connection.close();
    }
  }

}
