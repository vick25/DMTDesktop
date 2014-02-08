package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.datasource.FileDataSourceQueryChooser.FileChooserPanel;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.io.File;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/**
 * Prompts the user to pick a dataset to save.
 *
 * @see DataSourceQueryChooserDialog
 */
public class SaveDatasetAsPlugIn extends AbstractSaveDatasetAsPlugIn {

    protected Collection showDialog(WorkbenchContext context) {
        GUIUtil.centreOnWindow(getDialog());

        // initialize the FileChooser with the layer name [mmichaud 2007-08-25]
        FileChooserPanel fcp = (FileChooserPanel) context.getBlackboard().get(SaveFileDataSourceQueryChooser.FILE_CHOOSER_PANEL_KEY);
        if (fcp != null) {
            JFileChooser jfc = fcp.getChooser();
            jfc.setSelectedFile(new File(jfc.getCurrentDirectory(),
                    context.getLayerNamePanel().getSelectedLayers()[0].getName()));
        }

        getDialog().setVisible(true);
        return getDialog().wasOKPressed() ? getDialog().getCurrentChooser().getDataSourceQueries() : null;
    }

    protected void setSelectedFormat(String format) {
        getDialog().setSelectedFormat(format);
    }

    protected String getSelectedFormat() {
        return getDialog().getSelectedFormat();
    }

    private DataSourceQueryChooserDialog getDialog() {
        String KEY = getClass().getName() + " - DIALOG";
        if (null == getContext().getWorkbench().getBlackboard().get(KEY)) {
            getContext().getWorkbench().getBlackboard().put(
                    KEY,
                    new DataSourceQueryChooserDialog(
                    DataSourceQueryChooserManager
                    .get(
                    getContext().getWorkbench()
                    .getBlackboard())
                    .getSaveDataSourceQueryChoosers(),
                    getContext().getWorkbench().getFrame(), getName(),
                    true));
        }
        DataSourceQueryChooserDialog dialog = (DataSourceQueryChooserDialog) getContext().getWorkbench().getBlackboard().get(KEY);
        dialog.setDialogTask(DataSourceQueryChooserDialog.SAVEDIALOG);
        return dialog;
    }
    public static final ImageIcon ICON = IconLoader.icon("disk.png");
}
