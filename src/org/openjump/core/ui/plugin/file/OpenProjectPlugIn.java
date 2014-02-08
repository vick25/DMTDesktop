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
import org.openjump.core.ui.plugin.file.open.OpenProjectWizard;

public class OpenProjectPlugIn extends AbstractWizardPlugin {

    private static final String KEY = OpenProjectPlugIn.class.getName();
    private static final String FILE_DOES_NOT_EXIST = I18N.get(KEY + ".file-does-not-exist");
    private File[] files;
    private OpenProjectWizard wizard;

    public OpenProjectPlugIn() {
        super(IconLoader.icon("folder_layout_add.png"));
    }

    public OpenProjectPlugIn(WorkbenchContext workbenchContext, File file) {
        super(file.getName(), file.getAbsolutePath());
        this.workbenchContext = workbenchContext;
        this.files = new File[]{
            file
        };
        this.enableCheck = new BooleanPropertyEnableCheck(file, "exists", true,
                FILE_DOES_NOT_EXIST + ": " + file.getAbsolutePath());
    }

    public OpenProjectPlugIn(WorkbenchContext workbenchContext, File[] files) {
        this.workbenchContext = workbenchContext;
        this.files = files;
    }

    public void initialize(PlugInContext context) throws Exception {
        super.initialize(context);
        FeatureInstaller featureInstaller = context.getFeatureInstaller();

        // Add File Menu
        featureInstaller.addMainMenuItem(new String[]{
                    MenuNames.FILE
                }, this, 3);

        wizard = new OpenProjectWizard(workbenchContext);
        setWizard(wizard);
        OpenWizardPlugIn.addWizard(workbenchContext, wizard);
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        if (wizard == null) {
            wizard = new OpenProjectWizard(workbenchContext, files);
            setWizard(wizard);
        }
        // TODO Auto-generated method stub
        return super.execute(context);
    }
}
