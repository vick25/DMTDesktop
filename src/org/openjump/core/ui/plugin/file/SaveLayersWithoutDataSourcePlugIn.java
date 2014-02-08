package org.openjump.core.ui.plugin.file;

import com.osfac.dmt.I18N;
import com.osfac.dmt.io.DriverProperties;
import com.osfac.dmt.io.datasource.DataSource;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.DummyTaskMonitor;
import com.osfac.dmt.util.FileUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import org.openjump.core.ui.images.IconLoader;

/**
 * Subclass this to implement a 'Save Project' plugin.
 */
public class SaveLayersWithoutDataSourcePlugIn extends AbstractPlugIn {

    private static final String KEY = SaveLayersWithoutDataSourcePlugIn.class.getName();
    //public static final String SAVE_LAYERS_WITHOUT_DATASOURCE = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.save-layers-without-datasource");
    public static final String LAYERS_WITHOUT_DATASOURCE = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.layers-without-datasource-management");
    public static final String DONOTSAVE = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.do-not-save");
    public static final String SAVEASJML = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.save-as-jml");
    public static final String SAVEASSHP = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.save-as-shp");
    public static final String FILECHOOSER = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.directory-chooser");
    public static final String WARN_USER = I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.every-layer-has-a-datasource");
    private JFileChooser fileChooser;

    public SaveLayersWithoutDataSourcePlugIn() {
    }

    public String getName() {
        return I18N.get(KEY);
    }

    public void initialize(PlugInContext context) throws Exception {
        WorkbenchContext workbenchContext = context.getWorkbenchContext();

        EnableCheck enableCheck = createEnableCheck(workbenchContext);
        FeatureInstaller installer = new FeatureInstaller(workbenchContext);
        installer.addMainMenuItem(
                this, new String[]{MenuNames.FILE},
                new JMenuItem(getName(), IconLoader.icon("disk_multiple.png")),
                createEnableCheck(context.getWorkbenchContext()), 9);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle(FILECHOOSER);
    }

    public boolean execute(PlugInContext context) throws Exception {
        Collection<Layer> layersWithoutDataSource = layersWithoutDataSource(context.getTask());
        if (layersWithoutDataSource.size() == 0) {
            context.getWorkbenchFrame().warnUser(WARN_USER);
            return false;
        } else {
            int ret = fileChooser.showSaveDialog(context.getWorkbenchFrame());
            if (ret == JFileChooser.APPROVE_OPTION) {
                return execute(context, layersWithoutDataSource, fileChooser.getSelectedFile());
            }
        }
        return false;
    }

    public boolean execute(PlugInContext context, Collection<Layer> collection, File dir) throws Exception {
        MultiInputDialog dialog = new MultiInputDialog(
                context.getWorkbenchFrame(),
                LAYERS_WITHOUT_DATASOURCE,
                true);

        String tooltip = "<html>"
                + java.util.Arrays.toString(collection.toArray(new Object[0])).replaceAll(",", "<br>")
                + "</html>";

        dialog.addSubTitle(I18N.getMessage("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.layers-without-datasource",
                new Object[]{new Integer(collection.size())}))
                .setToolTipText(tooltip);
        dialog.addLabel(I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.hover-the-label-to-see-the-list"))
                .setToolTipText(tooltip);
        dialog.addRadioButton(DONOTSAVE, "ACTION", true, "");
        dialog.addRadioButton(SAVEASJML, "ACTION", false, "");
        dialog.addRadioButton(SAVEASSHP, "ACTION", false, "");

        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (dialog.wasOKPressed()) {
            if (dialog.getBoolean(DONOTSAVE)) {
                return false;
            } else {
                //File dir = FileUtil.removeExtensionIfAny(task);
                dir.mkdir();
                for (Layer layer : collection) {
                    String ext = null;
                    DataSource dataSource = null;
                    if (dialog.getBoolean(SAVEASJML)) {
                        ext = "dmt";
                        dataSource = new com.osfac.dmt.io.datasource.StandardReaderWriterFileDataSource.JML();
                    } else if (dialog.getBoolean(SAVEASSHP)) {
                        ext = "shp";
                        dataSource = new com.osfac.dmt.io.datasource.StandardReaderWriterFileDataSource.Shapefile();
                    }
                    saveLayer(layer, dir, dataSource, ext);
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private void saveLayer(Layer layer, File dir, DataSource dataSource, String ext) throws Exception {
        String name = FileUtil.getFileNameFromLayerName(layer.getName());
        // remove extension if any (ex. for layer image.png, will remove png
        int dotPos = name.indexOf(".");
        if (dotPos > 0) {
            name = name.substring(0, dotPos);
        }
        File fileName = FileUtil.addExtensionIfNone(new File(name), ext);
        String path = new File(dir, fileName.getName()).getAbsolutePath();

        DriverProperties dp = new DriverProperties();
        dp.set("File", path);
        dataSource.setProperties(dp);

        DataSourceQuery dsq = new DataSourceQuery(dataSource, path, path);
        layer.setDataSourceQuery(dsq).setFeatureCollectionModified(false);

        dataSource.getConnection().executeUpdate("", layer.getFeatureCollectionWrapper(), new DummyTaskMonitor());
    }

    private Collection<Layer> layersWithoutDataSource(Task task) {
        ArrayList<Layer> layersWithoutDataSource = new ArrayList<Layer>();
        for (Iterator i = task.getLayerManager().getLayers().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (!layer.hasReadableDataSource()) {
                layersWithoutDataSource.add(layer);
            }
        }
        return layersWithoutDataSource;
    }

    /**
     * @param workbenchContext
     * @return an enable check
     */
    public EnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        final WorkbenchContext wc = workbenchContext;
        EnableCheckFactory enableCheckFactory = new EnableCheckFactory(workbenchContext);
        MultiEnableCheck enableCheck = new MultiEnableCheck();
        enableCheck.add(enableCheckFactory.createWindowWithLayerManagerMustBeActiveCheck());
        enableCheck.add(new EnableCheck() {
            public String check(javax.swing.JComponent component) {
                return layersWithoutDataSource(wc.getTask()).size() > 0 ? null
                        : I18N.get("org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn.a-layer-without-datasource-must-exist");
            }
        });
        return enableCheck;
    }
}
