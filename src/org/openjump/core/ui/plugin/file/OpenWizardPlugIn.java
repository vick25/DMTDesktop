package org.openjump.core.ui.plugin.file;

import com.osfac.dmt.I18N;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.registry.Registry;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.WorkbenchToolBar;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import java.util.List;
import javax.swing.Icon;
import org.openjump.core.ui.images.IconLoader;
import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;
import org.openjump.core.ui.plugin.file.open.OpenProjectWizard;
import org.openjump.core.ui.swing.wizard.WizardGroup;
import org.openjump.core.ui.swing.wizard.WizardGroupDialog;

public class OpenWizardPlugIn extends AbstractThreadedUiPlugIn {

    private static final String KEY = OpenWizardPlugIn.class.getName();
    private WizardGroupDialog dialog;
    private WizardGroup lastWizard;

    public OpenWizardPlugIn() {
        super(I18N.get(KEY), IconLoader.icon("folder_add_small.png"));
    }

    public static void addWizard(final WorkbenchContext workbenchContext,
            final WizardGroup wizard) {
        Registry registry = workbenchContext.getRegistry();
        registry.createEntry(KEY, wizard);
    }

    @Override
    public void initialize(PlugInContext context) throws Exception {
        super.initialize(context);
        DMTWorkbench workbench = workbenchContext.getWorkbench();
        WorkbenchFrame frame = workbench.getFrame();

        Icon icon = getIcon();
        String name = getName();

        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
        featureInstaller.addMainMenuItem(new String[]{MenuNames.FILE}, this, 1);

        // Add tool-bar Icon
        WorkbenchToolBar toolBar = frame.getToolBar();
        toolBar.addPlugIn(1, this, IconLoader.icon("folder_add.png"), enableCheck, workbenchContext);

        // Add layer pop-up menu
        featureInstaller.addPopupMenuItem(frame.getCategoryPopupMenu(), this, name
                + "{pos:3}", false, icon, enableCheck);
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        Registry registry = workbenchContext.getRegistry();

        WorkbenchFrame workbenchFrame = context.getWorkbenchFrame();
        String name = getName();
        if (dialog == null) {
            dialog = new WizardGroupDialog(workbenchContext, workbenchFrame, name);

            List<WizardGroup> wizards = registry.getEntries(KEY);
            lastWizard = wizards.get(0);
            for (WizardGroup wizard : wizards) {
                dialog.addWizard(wizard);
                if (wizard instanceof OpenProjectWizard) {
                    lastWizard = wizard;
                }
            }
        }
        dialog.setSelectedWizard(lastWizard);
        dialog.pack();
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        lastWizard = dialog.getSelectedWizard();
        return dialog.wasFinishPressed();
    }

    @Override
    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        WizardGroup wizard = dialog.getSelectedWizard();
        wizard.run(dialog, monitor);
    }
}
