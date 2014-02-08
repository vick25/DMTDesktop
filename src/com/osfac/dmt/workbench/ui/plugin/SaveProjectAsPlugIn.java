package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.util.FileUtil;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class SaveProjectAsPlugIn extends AbstractSaveProjectPlugIn {

    public static final ImageIcon ICON = IconLoader.icon("layout_save.png");
    public static final String FILE_CHOOSER_DIRECTORY_KEY =
            SaveProjectAsPlugIn.class.getName() + " - FILE CHOOSER DIRECTORY";
    public static final FileFilter JUMP_PROJECT_FILE_FILTER =
            GUIUtil.createFileFilter(I18N.get("ui.plugin.SaveProjectAsPlugIn.jump-project-files"),
            new String[]{"dmt", "jcs"});
    private JFileChooser fileChooser;

    public void initialize(PlugInContext context) throws Exception {
        //Don't initialize fileChooser at field declaration; otherwise get
        // intermittent
        //exceptions:
        //java.lang.NullPointerException
        //        at javax.swing.ImageIcon.<init>(ImageIcon.java:161)
        //        at javax.swing.ImageIcon.<init>(ImageIcon.java:147)
        //        at
        // com.sun.java.swing.plaf.windows.WindowsFileChooserUI$ShortCutPanel.<init>(WindowsFileChooserUI.java:603)
        //        at
        // com.sun.java.swing.plaf.windows.WindowsFileChooserUI.installComponents(WindowsFileChooserUI.java:361)
        //        at
        // javax.swing.plaf.basic.BasicFileChooserUI.installUI(BasicFileChooserUI.java:130)
        //        at
        // com.sun.java.swing.plaf.windows.WindowsFileChooserUI.installUI(WindowsFileChooserUI.java:176)
        //        at javax.swing.JComponent.setUI(JComponent.java:449)
        //        at javax.swing.JFileChooser.updateUI(JFileChooser.java:1701)
        //        at javax.swing.JFileChooser.setup(JFileChooser.java:345)
        //        at javax.swing.JFileChooser.<init>(JFileChooser.java:320)
        //[Bob Boseko 2004-01-12]
        fileChooser = GUIUtil.createJFileChooserWithOverwritePrompting("dmt");
        fileChooser.setDialogTitle(I18N.get("ui.plugin.SaveProjectAsPlugIn.save-project"));
        GUIUtil.removeChoosableFileFilters(fileChooser);
        fileChooser.addChoosableFileFilter(JUMP_PROJECT_FILE_FILTER);
        fileChooser.addChoosableFileFilter(GUIUtil.ALL_FILES_FILTER);
        fileChooser.setFileFilter(JUMP_PROJECT_FILE_FILTER);
        Blackboard blackboard = PersistentBlackboardPlugIn.get(context.getWorkbenchContext());
        String dir = (String) blackboard.get(FILE_CHOOSER_DIRECTORY_KEY);
        if (dir != null) {
            fileChooser.setCurrentDirectory(new File(dir));
        }
    }

    public String getName() {
        return I18N.get("ui.plugin.SaveProjectAsPlugIn.save-project-as");
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        if (context.getTask().getProjectFile() != null) {
            fileChooser.setSelectedFile(context.getTask().getProjectFile());
        }
        if (JFileChooser.APPROVE_OPTION != fileChooser.showSaveDialog(context
                .getWorkbenchFrame())) {
            return false;
        }
        File file = fileChooser.getSelectedFile();

        java.util.Collection collection = ignoredLayers(context.getTask());
        if (collection.size() > 0) {
            // Starting with OpenJUMP 1.4.1beta (2011-04-20), the plugin uses
            // org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn
            // to give the user the possibility to save unsaved layers to HD
            // before saving the project
            new org.openjump.core.ui.plugin.file.SaveLayersWithoutDataSourcePlugIn()
                    .execute(context, collection, FileUtil.removeExtensionIfAny(file));
        }

        file = FileUtil.addExtensionIfNone(file, "dmt");
        save(context.getTask(), file, context.getWorkbenchFrame());
        // Session-based persistence
        context.getWorkbenchContext()
                .getBlackboard()
                .put(FILE_CHOOSER_DIRECTORY_KEY, file.getAbsoluteFile().getParent());
        // File-based persistence
        PersistentBlackboardPlugIn.get(context.getWorkbenchContext())
                .put(FILE_CHOOSER_DIRECTORY_KEY,
                file.getAbsoluteFile().getParent());
        return true;
    }
}