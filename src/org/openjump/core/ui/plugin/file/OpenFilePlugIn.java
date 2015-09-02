package org.openjump.core.ui.plugin.file;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import java.io.File;
import org.openjump.core.ui.enablecheck.BooleanPropertyEnableCheck;
import org.openjump.core.ui.images.IconLoader;
import org.openjump.core.ui.plugin.AbstractWizardPlugin;
import org.openjump.core.ui.plugin.file.open.OpenFileWizard;

public class OpenFilePlugIn extends AbstractWizardPlugin {

    private static final String KEY = OpenFilePlugIn.class.getName();
    private static final String FILE_DOES_NOT_EXIST = I18N.get(new StringBuilder(KEY).
            append(".file-does-not-exist").toString());

    /**
     * Construct the main Open File plug-in.
     */
    public OpenFilePlugIn() {
        super(IconLoader.icon("folder_page.png"));
    }

    /**
     * Construct an Open File for the recent menu to load an individual file.
     *
     * @param workbenchContext The workbench context.
     * @param file The file to load.
     */
    public OpenFilePlugIn(final WorkbenchContext workbenchContext, final File file) {
        super(file.getName(), file.getAbsolutePath());
        setWorkbenchContext(workbenchContext);
        File[] files = new File[]{
            file
        };
        this.enableCheck = new BooleanPropertyEnableCheck(file, "exists", true,
                new StringBuilder(FILE_DOES_NOT_EXIST).append(": ").append(file.getAbsolutePath()).toString());
        OpenFileWizard openFileWizard = new OpenFileWizard(workbenchContext, files);
        setWizard(openFileWizard);
    }

    public OpenFilePlugIn(WorkbenchContext workbenchContext, File[] files) {
        setWorkbenchContext(workbenchContext);
        OpenFileWizard openFileWizard = new OpenFileWizard(workbenchContext, files);
        setWizard(openFileWizard);
    }

    /**
     * Initialize the main instance of this plug-in, should not be called for the Recent menu open
     * file plug-ins.
     *
     * @param context The plug-in context.
     * @exception Exception If there was an error initializing the plug-in.
     */
    @Override
    public void initialize(final PlugInContext context) throws Exception {
        super.initialize(context);
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);

        // Add File Menu
        featureInstaller.addMainMenuItem(new String[]{MenuNames.FILE}, this, 2);
        // Register the Open File Wizard
        OpenFileWizard openFileWizard = new OpenFileWizard(workbenchContext);
        setWizard(openFileWizard);
        OpenWizardPlugIn.addWizard(workbenchContext, openFileWizard);
    }
}
