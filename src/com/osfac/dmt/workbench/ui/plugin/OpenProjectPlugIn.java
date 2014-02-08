package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSource;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.util.java2xml.XML2Java;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedBasePlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * @deprecated replaced by org.openjump.core.ui.plugin.file.OpenProjectPlugIn
 */
public class OpenProjectPlugIn extends ThreadedBasePlugIn {

    private final static String RECENT_MENU_LIST_KEY =
            OpenProjectPlugIn.class.getName() + " - RECENT_MENU_LIST";  //LDB: probably shouldn't internationalize keys
    public final static int RECENT_MENU_LIST_MAX = 8;
    private JFileChooser fileChooser;
    private Task newTask;
    private Task sourceTask;

    public OpenProjectPlugIn() {
    }

    public String getName() {
        return I18N.get("ui.plugin.OpenProjectPlugIn.open-project");
    }

    public void initialize(PlugInContext context) throws Exception {
    }

    private void initFileChooser() {
        if (fileChooser != null) {
            return;
        }

        //Don't initialize fileChooser in field declaration -- seems too early
        // because
        //we sometimes get a WindowsFileChooserUI NullPointerException [Jon
        // Aquino 12/10/2003]
        fileChooser = GUIUtil.createJFileChooserWithExistenceChecking();
        fileChooser.setDialogTitle(I18N.get("ui.plugin.OpenProjectPlugIn.open-project"));
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        GUIUtil.removeChoosableFileFilters(fileChooser);
        fileChooser
                .addChoosableFileFilter(SaveProjectAsPlugIn.JUMP_PROJECT_FILE_FILTER);
        fileChooser.addChoosableFileFilter(GUIUtil.ALL_FILES_FILTER);
        fileChooser.setFileFilter(SaveProjectAsPlugIn.JUMP_PROJECT_FILE_FILTER);
    }

    public boolean execute(PlugInContext context) throws Exception {
        initFileChooser();
        reportNothingToUndoYet(context);

        if (JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(context
                .getWorkbenchFrame())) {
            return false;
        }
        open(fileChooser.getSelectedFile(), context.getWorkbenchFrame());
        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        loadLayers(context, sourceTask.getLayerManager(), newTask.getLayerManager(),
                CoordinateSystemRegistry.instance(context.getWorkbenchContext()
                .getBlackboard()), monitor);
    }

    public void open(File file, WorkbenchFrame workbenchFrame)
            throws Exception {
        FileReader reader = new FileReader(file);

        try {
            sourceTask = (Task) new XML2Java(workbenchFrame.getContext()
                    .getWorkbench().getPlugInManager().getClassLoader()).read(
                    reader, Task.class);
            //I can't remember why I'm creating a new Task instead of using
            //sourceTask. There must be a good reason. [Bob Boseko]
            // Probably to reverse the order of the layerables. See comments.
            // Probably also to set the LayerManager coordinate system.
            // [Bob Boseko 2005-03-16]
            initializeDataSources(sourceTask, workbenchFrame.getContext());
            newTask = new Task();
            newTask.setName(GUIUtil.nameWithoutExtension(file));
            newTask.setProjectFile(file);
            newTask.setProperties(sourceTask.getProperties());
            workbenchFrame.addTaskFrame(newTask);
        } finally {
            reader.close();
            putRecentList(workbenchFrame.getContext(), file.getAbsolutePath());
            //ArrayList recentList = getRecentList(blackboard); //retrieve list of recent tasks
        }
    }

    public static ArrayList getRecentList(WorkbenchContext context) {
        Blackboard blackboard = PersistentBlackboardPlugIn.get(context);
        if (blackboard.get(RECENT_MENU_LIST_KEY) == null) {
            blackboard.put(RECENT_MENU_LIST_KEY, new ArrayList(RECENT_MENU_LIST_MAX));
        }
        ArrayList list = (ArrayList) blackboard.get(RECENT_MENU_LIST_KEY);
        return list;
    }

    public static void putRecentList(WorkbenchContext context, String path) {
        Blackboard blackboard = PersistentBlackboardPlugIn.get(context);
        if (blackboard.get(RECENT_MENU_LIST_KEY) == null) {
            blackboard.put(RECENT_MENU_LIST_KEY, new ArrayList(RECENT_MENU_LIST_MAX));
        }
        ArrayList list = (ArrayList) blackboard.get(RECENT_MENU_LIST_KEY);
        if (list.contains(path)) {
            return;
        }
        if (list.size() >= RECENT_MENU_LIST_MAX) {
            list.remove(list.size() - 1);  //remove last element
        }
        list.add(0, path);  //push rest of list down
    }

    private void initializeDataSources(Task task, WorkbenchContext context) {
        for (Iterator i = task.getLayerManager().getLayers().iterator(); i
                .hasNext();) {
            Layer layer = (Layer) i.next();
            if (layer.getDataSourceQuery().getDataSource() instanceof WorkbenchContextReference) {
                ((WorkbenchContextReference) layer.getDataSourceQuery()
                        .getDataSource()).setWorkbenchContext(context);
            }
        }
    }

    private void loadLayers(PlugInContext context, LayerManager sourceLayerManager,
            LayerManager newLayerManager, CoordinateSystemRegistry registry,
            TaskMonitor monitor) throws Exception {
        FindFile findFile = new FindFile(context);
        boolean displayDialog = true;

        for (Iterator i = sourceLayerManager.getCategories().iterator(); i
                .hasNext();) {
            Category sourceLayerCategory = (Category) i.next();
            // Explicitly add categories. Can't rely on
            // LayerManager#addLayerable to add the categories, because a
            // category might not have any layers. [Bob Boseko]
            newLayerManager.addCategory(sourceLayerCategory.getName());

            // LayerManager#addLayerable adds layerables to the top. So reverse
            // the order. [Bob Boseko]
            ArrayList layerables = new ArrayList(sourceLayerCategory
                    .getLayerables());
            Collections.reverse(layerables);

            for (Iterator j = layerables.iterator(); j.hasNext();) {
                Layerable layerable = (Layerable) j.next();
                if (monitor != null) {
                    monitor.report(I18N.get("ui.plugin.OpenProjectPlugIn.loading") + " " + layerable.getName());
                }
                layerable.setLayerManager(newLayerManager);

                if (layerable instanceof Layer) {
                    Layer layer = (Layer) layerable;
                    try {
                        load(layer, registry, monitor);
                    } catch (FileNotFoundException ex) {
                        if (displayDialog) {
                            displayDialog = false;

                            int response = JOptionPane.showConfirmDialog(context.getWorkbenchFrame(),
                                    I18N.get("ui.plugin.OpenProjectPlugIn.At-least-one-file-in-the-task-could-not-be-found") + "\n"
                                    + I18N.get("ui.plugin.OpenProjectPlugIn.Do-you-want-to-locate-it-and-continue-loading-the-task"),
                                    "JUMP", JOptionPane.YES_NO_OPTION);

                            if (response != JOptionPane.YES_OPTION) {
                                break;
                            }
                        }

                        String fname = layer.getDataSourceQuery().getDataSource().getProperties().get("File").toString();
                        String filename = findFile.getFileName(fname);
                        if (filename.length() > 0) {
                            //set the new source for this layer
                            Map properties = layer.getDataSourceQuery().getDataSource().getProperties();
                            properties.put(DataSource.FILE_KEY, filename);
                            layer.getDataSourceQuery().getDataSource().setProperties(properties);
                            load(layer, registry, monitor);
                        } else {
                            break;
                        }
                    }
                }

                newLayerManager.addLayerable(sourceLayerCategory.getName(),
                        layerable);
            }
        }
    }

    public static void load(Layer layer, CoordinateSystemRegistry registry, TaskMonitor monitor) throws Exception {
        layer.setFeatureCollection(executeQuery(layer
                .getDataSourceQuery().getQuery(), layer
                .getDataSourceQuery().getDataSource(), registry,
                monitor));
        layer.setFeatureCollectionModified(false);
    }

    private static FeatureCollection executeQuery(String query, DataSource dataSource,
            CoordinateSystemRegistry registry, TaskMonitor monitor)
            throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            return dataSource.installCoordinateSystem(connection.executeQuery(
                    query, monitor), registry);
        } finally {
            connection.close();
        }
    }

    public class FindFile {

        private Vector prefixList = new Vector(5, 5);
        private JFileChooser fileChooser;
        private PlugInContext context;

        public FindFile(PlugInContext context) {
            this.context = context;
            fileChooser = new JFileChooser();
            fileChooser = GUIUtil.createJFileChooserWithExistenceChecking();
            fileChooser.setDialogTitle("Choose current location of: ");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
        }

        public FindFile(PlugInContext context, JFileChooser fileChooser) {
            this(context);
            this.fileChooser = fileChooser;
        }

        public String getFileName(String filenamepath) throws Exception {
            //strip off file name
            File oldFile = new File(filenamepath);
            String oldPath = oldFile.getPath();
            //see if something in the prefixList matches all or part of oldPath  
            for (Iterator i = prefixList.iterator(); i.hasNext();) {
                PathPrefixes prefix = (PathPrefixes) i.next();
                if (oldPath.toLowerCase().indexOf(prefix.getOldPrefix().toLowerCase()) > -1) //found match
                {
                    //replace matching portion with new prefix
                    String newFileNamePath = filenamepath.substring(prefix.getOldPrefix().length());
                    newFileNamePath = prefix.getNewPrefix() + newFileNamePath;
                    File newFile = new File(newFileNamePath);
                    if (newFile.exists()) {
                        return newFileNamePath;
                    }
                    //else continue to look through list
                }
            }

            //at this point didn't find a match
            //ask user to find file
            fileChooser.setDialogTitle("Choose current location of: " + filenamepath);
            GUIUtil.removeChoosableFileFilters(fileChooser);
            fileChooser.addChoosableFileFilter(GUIUtil.ALL_FILES_FILTER);
            String ext = "";
            int k = filenamepath.lastIndexOf('.');

            if ((k > 0) && (k < (filenamepath.length() - 1))) {
                ext = filenamepath.substring(k + 1);
                FileFilter fileFilter = GUIUtil.createFileFilter(ext.toUpperCase() + " Files", new String[]{ext.toLowerCase()});
                fileChooser.addChoosableFileFilter(fileFilter);
                fileChooser.setFileFilter(fileFilter);
            }

            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(context.getWorkbenchFrame())) {
                String newParent = fileChooser.getSelectedFile().getParent() + File.separator;
                String oldParent = new File(filenamepath).getParent() + File.separator;

                //find where they differ
                int i = newParent.length();
                int j = oldParent.length();
                while (newParent.substring(i).equalsIgnoreCase(oldParent.substring(j))) {
                    i--;
                    j--;
                }
                while (newParent.charAt(i) != File.separatorChar) {
                    i++;
                }
                while (oldParent.charAt(j) != File.separatorChar) {
                    j++;
                }

                String newPrefix = newParent.substring(0, ++i);
                String oldPrefix = oldParent.substring(0, ++j);

                PathPrefixes pathPrefix = new PathPrefixes(oldPrefix, newPrefix);
                prefixList.add(pathPrefix);
                return fileChooser.getSelectedFile().getPath();
            }
            return ""; //user canceled find file
        }
    }

    public class PathPrefixes {

        private String oldPrefix = "";
        private String newPrefix = "";

        public PathPrefixes(String oldPrefix, String newPrefix) {
            this.oldPrefix = oldPrefix;
            this.newPrefix = newPrefix;
        }

        public String getOldPrefix() {
            return oldPrefix;
        }

        public String getNewPrefix() {
            return newPrefix;
        }
    }
}