package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import java.io.File;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * UI for picking a file-based dataset to save. Does not automatically append an
 * extension if user does not specify one because, unlike Windows, on Unix it is
 * common for files not to have extensions.
 */
public class SaveFileDataSourceQueryChooser extends FileDataSourceQueryChooser {

    private static final String FILE_CHOOSER_DIRECTORY_KEY = SaveFileDataSourceQueryChooser.class.getName()
            + " - FILE CHOOSER DIRECTORY";
    public static final String FILE_CHOOSER_PANEL_KEY = SaveFileDataSourceQueryChooser.class.getName()
            + " - SAVE FILE CHOOSER PANEL";
    private static final Pattern fileNameRegex = Pattern.compile(
            // Start of the path
            "^"
            + // Protocole (ex. file:) or machine name (\\machine) or parent directory (..) or current directory (.)
            "([a-zA-Z]:|\\\\\\\\[^/\\\\\\?%\\*:\\|\"<>]+|\\.\\.|\\.)?"
            + // directory names (/directory or \directory) name does not include /\?%*:|"<>
            "([/\\\\]([^/\\\\\\?%\\*:\\|\"<>]+|\\.\\.|\\.))*"
            + // file name (does not include /\?%*:|"<>)
            "([^/\\\\\\?%\\*:\\|\"<>\\.]+)"
            + // file name extension
            "(\\.[^/\\\\\\?%\\*:\\|\"<>\\.]+)?"
            + "$");
    private WorkbenchContext context;

    /**
     * @param extensions e.g. txt
     */
    public SaveFileDataSourceQueryChooser(Class dataSourceClass,
            String description, String[] extensions, WorkbenchContext context) {
        super(dataSourceClass, description, extensions);
        this.context = context;
    }

    protected FileChooserPanel getFileChooserPanel() {
        //Moved this local String to a static public String to be able to access to it
        //from SaveDatasetAsPlugIn [mmichaud 2007-08-25]
        //final String FILE_CHOOSER_PANEL_KEY = SaveFileDataSourceQueryChooser.class.getName() +
        //    " - SAVE FILE CHOOSER PANEL";

        //SaveFileDataSourceQueryChoosers share the same JFileChooser so that the user's
        //work is not lost when he switches data-source types. The JFileChooser options
        //are set once because setting them freezes the GUI for a few seconds. [Bob Boseko]
        if (blackboard().get(FILE_CHOOSER_PANEL_KEY) == null) {
            final JFileChooser fileChooser = GUIUtil.createJFileChooserWithOverwritePrompting();
            // enforce the type to have mac java show the file name input field
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setControlButtonsAreShown(false);
            blackboard().put(FILE_CHOOSER_PANEL_KEY,
                    new FileChooserPanel(fileChooser, blackboard()));

            if (PersistentBlackboardPlugIn.get(context).get(FILE_CHOOSER_DIRECTORY_KEY) != null) {
                fileChooser.setCurrentDirectory(new File(
                        (String) PersistentBlackboardPlugIn.get(context).get(FILE_CHOOSER_DIRECTORY_KEY)));
            }
            fileChooser.addAncestorListener(new AncestorListener() {
                public void ancestorAdded(AncestorEvent event) {
                    if (event.getAncestor() instanceof DataSourceQueryChooserDialog) {
                        fileChooser.rescanCurrentDirectory();
                    }
                }

                public void ancestorMoved(AncestorEvent event) {
                }

                public void ancestorRemoved(AncestorEvent event) {
                }
            });
        }

        return (FileChooserPanel) blackboard().get(FILE_CHOOSER_PANEL_KEY);
    }

    private Blackboard blackboard() {
        return context.getBlackboard();
    }

    public Collection getDataSourceQueries() {
        //User has pressed OK, so persist the directory. [Bob Boseko]
        PersistentBlackboardPlugIn.get(context).put(FILE_CHOOSER_DIRECTORY_KEY,
                getFileChooserPanel().getChooser().getCurrentDirectory().toString());

        return super.getDataSourceQueries();
    }

    public boolean isInputValid() {
        boolean isInputValid = super.isInputValid();

        //String path = getFileChooserPanel().getChooser().getSelectedFile().getPath();
        //System.out.println("whould be writing to this location: " + path);
        if (!fileNameRegex.matcher(getFileChooserPanel().getChooser().getSelectedFile().getPath()).matches()) {
            context.getWorkbench()
                    .getFrame()
                    .warnUser(I18N.get("com.osfac.dmt.workbench.datasource.SaveFileDataSourceQueryChooser.Invalid-file-name"));
            isInputValid = false;
        } else {
            context.getWorkbench().getFrame().setStatusMessage("");
        }
        return isInputValid;
    }
}
