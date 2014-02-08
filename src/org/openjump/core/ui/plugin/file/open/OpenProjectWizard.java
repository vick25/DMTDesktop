package org.openjump.core.ui.plugin.file.open;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSource;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.util.java2xml.XML2Java;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.plugin.PlugInManager;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.osfac.dmt.workbench.ui.plugin.WorkbenchContextReference;
import com.osfac.dmt.workbench.ui.wizard.WizardDialog;
import com.vividsolutions.jts.geom.Envelope;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.openjump.core.model.TaskEvent;
import org.openjump.core.model.TaskListener;
import org.openjump.core.ui.images.IconLoader;
import org.openjump.core.ui.plugin.file.FindFile;
import org.openjump.core.ui.plugin.file.OpenRecentPlugIn;
import org.openjump.core.ui.swing.wizard.AbstractWizardGroup;

public class OpenProjectWizard extends AbstractWizardGroup {

    /**
     * The key for the wizard.
     */
    public static final String KEY = OpenProjectWizard.class.getName();
    public static final String FILE_CHOOSER_DIRECTORY_KEY =
            OpenProjectWizard.class.getName() + " - FILE CHOOSER DIRECTORY";
    /**
     * The workbench context.
     */
    private WorkbenchContext workbenchContext;
    private SelectProjectFilesPanel selectProjectPanel;
    private Task sourceTask;
    private Task newTask;
    private File[] files;
    private Envelope savedTaskEnvelope = null;

    /**
     * Construct a new OpenFileWizard.
     *
     * @param workbenchContext The workbench context.
     */
    public OpenProjectWizard(final WorkbenchContext workbenchContext) {
        super(I18N.get(KEY), IconLoader.icon("folder_layout_add.png"),
                SelectProjectFilesPanel.KEY);
        this.workbenchContext = workbenchContext;
        initPanels(workbenchContext);
    }

    public OpenProjectWizard(final WorkbenchContext workbenchContext,
            final File[] files) {
        this.workbenchContext = workbenchContext;
        this.files = files;
        initPanels(workbenchContext);
    }

    private void initPanels(final WorkbenchContext workbenchContext) {
        selectProjectPanel = new SelectProjectFilesPanel(workbenchContext);
        addPanel(selectProjectPanel);
    }

    public void initialize(WorkbenchContext workbenchContext, WizardDialog dialog) {
        selectProjectPanel.setDialog(dialog);
    }

    /**
     * Load the files selected in the wizard.
     *
     * @param monitor The task monitor.
     */
    public void run(WizardDialog dialog, TaskMonitor monitor) {
        if (files == null) {
            //Blackboard blackboard = PersistentBlackboardPlugIn.get(workbenchContext);
            //String dir = (String)blackboard.get(FILE_CHOOSER_DIRECTORY_KEY);
            //if (dir != null) selectProjectPanel.setCurrentDirectory(new File(dir));
            File[] selectedFiles = selectProjectPanel.getSelectedFiles();
            open(selectedFiles, monitor);
        } else {
            open(files, monitor);
        }
    }

    private void open(File[] files, TaskMonitor monitor) {
        for (File file : files) {
            open(file, monitor);
        }
        // [mmichaud 2011-11-08] persist last used directory in workbench-state.xml
        if (files != null && files.length > 0) {
            File file = files[0];
            try {
                Blackboard blackboard = PersistentBlackboardPlugIn.get(workbenchContext);
                blackboard.put(FILE_CHOOSER_DIRECTORY_KEY, file.getAbsoluteFile().getParent());
            } catch (Exception ex) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                        + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
            }
        }
    }

    public void open(File file, TaskMonitor monitor) {
        try {
            try (FileReader reader = new FileReader(file)) {
                DMTWorkbench workbench = workbenchContext.getWorkbench();
                WorkbenchFrame workbenchFrame = workbench.getFrame();
                PlugInManager plugInManager = workbench.getPlugInManager();
                ClassLoader pluginClassLoader = plugInManager.getClassLoader();
                sourceTask = (Task) new XML2Java(pluginClassLoader).read(reader,
                        Task.class);
                initializeDataSources(sourceTask, workbenchFrame.getContext());
                newTask = new Task();
                newTask.setName(GUIUtil.nameWithoutExtension(file));
                newTask.setProjectFile(file);
                newTask.setProperties(sourceTask.getProperties());

                newTask.setTaskWindowLocation(sourceTask.getTaskWindowLocation());
                newTask.setTaskWindowSize(sourceTask.getTaskWindowSize());
                newTask.setMaximized(sourceTask.getMaximized());
                newTask.setSavedViewEnvelope(sourceTask.getSavedViewEnvelope());

                TaskFrame frame = workbenchFrame.addTaskFrame(newTask);
                Dimension size = newTask.getTaskWindowSize();
                if (size != null) {
                    frame.setSize(size);
                }
                Point location = newTask.getTaskWindowLocation();
                if ((location != null)
                        && (location.x < workbenchFrame.getSize().width)
                        && (location.y < workbenchFrame.getSize().height)) {
                    frame.setLocation(location);
                }
                if (newTask.getMaximized()) {
                    frame.setMaximum(true);
                }
                savedTaskEnvelope = newTask.getSavedViewEnvelope();
                LayerManager sourceLayerManager = sourceTask.getLayerManager();
                LayerManager newLayerManager = newTask.getLayerManager();
                CoordinateSystemRegistry crsRegistry = CoordinateSystemRegistry.instance(workbenchContext.getBlackboard());
                workbenchContext.getLayerViewPanel().setDeferLayerEvents(true);
                loadLayers(sourceLayerManager, newLayerManager, crsRegistry, monitor);
                workbenchContext.getLayerViewPanel().setDeferLayerEvents(false);
                OpenRecentPlugIn.get(workbenchContext).addRecentProject(file);
            }
        } catch (Exception ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
            monitor.report(ex);
        }
    }

    private void initializeDataSources(Task task, WorkbenchContext context) {
        LayerManager layerManager = task.getLayerManager();
        List<Layer> layers = layerManager.getLayers();
        for (Layer layer : layers) {
            DataSourceQuery dataSourceQuery = layer.getDataSourceQuery();
            DataSource dataSource = dataSourceQuery.getDataSource();
            if (dataSource instanceof WorkbenchContextReference) {
                WorkbenchContextReference workbenchRef = (WorkbenchContextReference) dataSource;
                workbenchRef.setWorkbenchContext(context);
            }
        }
    }

    private void loadLayers(LayerManager sourceLayerManager,
            LayerManager newLayerManager, CoordinateSystemRegistry registry,
            TaskMonitor monitor) throws Exception {
        DMTWorkbench workbench = workbenchContext.getWorkbench();
        WorkbenchFrame workbenchFrame = workbench.getFrame();
        FindFile findFile = new FindFile(workbenchFrame);
        boolean displayDialog = true;

        try {
            List<Category> categories = sourceLayerManager.getCategories();
            for (Category sourceLayerCategory : categories) {
                newLayerManager.addCategory(sourceLayerCategory.getName());

                // LayerManager#addLayerable adds layerables to the top. So reverse
                // the order.
                ArrayList<Layerable> layerables = new ArrayList<>(
                        sourceLayerCategory.getLayerables());
                Collections.reverse(layerables);

                for (Layerable layerable : layerables) {
                    if (monitor != null) {
                        monitor.report(I18N.get("ui.plugin.OpenProjectPlugIn.loading") + " "
                                + layerable.getName());
                    }
                    layerable.setLayerManager(newLayerManager);

                    if (layerable instanceof Layer) {
                        Layer layer = (Layer) layerable;
                        try {
                            load(layer, registry, monitor);
                        } catch (FileNotFoundException ex) {
                            if (displayDialog) {
                                displayDialog = false;

                                int response = JOptionPane.showConfirmDialog(
                                        workbenchFrame,
                                        I18N.get("ui.plugin.OpenProjectPlugIn.At-least-one-file-in-the-task-could-not-be-found")
                                        + "\n"
                                        + I18N.get("ui.plugin.OpenProjectPlugIn.Do-you-want-to-locate-it-and-continue-loading-the-task"),
                                        "JUMP", JOptionPane.YES_NO_OPTION);

                                if (response != JOptionPane.YES_OPTION) {
                                    break;
                                }
                            }

                            DataSourceQuery dataSourceQuery = layer.getDataSourceQuery();
                            DataSource dataSource = dataSourceQuery.getDataSource();
                            Map properties = dataSource.getProperties();
                            String fname = properties.get("File").toString();
                            String filename = findFile.getFileName(fname);
                            if (filename.length() > 0) {
                                // set the new source for this layer
                                properties.put(DataSource.FILE_KEY, filename);
                                dataSource.setProperties(properties);
                                load(layer, registry, monitor);
                            } else {
                                break;
                            }
                        }
                    }

                    newLayerManager.addLayerable(sourceLayerCategory.getName(), layerable);
                }
            }
            // fire TaskListener's
            Object[] listeners = workbenchFrame.getTaskListeners().toArray();
            for (int i = 0; i < listeners.length; i++) {
                TaskListener l = (TaskListener) listeners[i];
                l.taskLoaded(new TaskEvent(this, newLayerManager.getTask()));
            }
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        if (savedTaskEnvelope == null) {
                            workbenchContext.getLayerViewPanel().getViewport().zoomToFullExtent();
                        } else {
                            workbenchContext.getLayerViewPanel().getViewport().zoom(savedTaskEnvelope);
                        }
                    } catch (Exception ex) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                                + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
                    }
                }
            });
        }
    }

    public static void load(Layer layer, CoordinateSystemRegistry registry,
            TaskMonitor monitor) throws Exception {
        DataSourceQuery dataSourceQuery = layer.getDataSourceQuery();
        String query = dataSourceQuery.getQuery();
        DataSource dataSource = dataSourceQuery.getDataSource();
        FeatureCollection features = executeQuery(query, dataSource, registry,
                monitor);
        layer.setFeatureCollection(features);
        layer.setFeatureCollectionModified(false);
    }

    private static FeatureCollection executeQuery(String query,
            DataSource dataSource, CoordinateSystemRegistry registry,
            TaskMonitor monitor) throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            FeatureCollection features = connection.executeQuery(query, monitor);
            return dataSource.installCoordinateSystem(features, registry);
        } finally {
            connection.close();
        }
    }
}
