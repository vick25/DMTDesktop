package com.osfac.dmt.workbench.ui.toolbox;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

public abstract class ToolboxPlugIn extends AbstractPlugIn {

    /**
     * @return the toolbox for this plug-in class.
     */
    public ToolboxDialog getToolbox(WorkbenchContext context) {
        if (toolbox == null) {
            toolbox = new ToolboxDialog(context);
            toolbox.setTitle(getName());
            initializeToolbox(toolbox);
            toolbox.finishAddingComponents();
        }
        return toolbox;
    }
    private ToolboxDialog toolbox = null;

    protected abstract void initializeToolbox(ToolboxDialog toolbox);

    /**
     * Toolbox subclasses can override this method to implement their own behaviour when the plug-in
     * is called. Remember to call super.execute to make the toolbox visible.
     */
    @Override
    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        getToolbox(context.getWorkbenchContext()).setVisible(!getToolbox(context.getWorkbenchContext()).isVisible());
        return true;
    }

    /**
     * Creates a menu item with a checkbox beside it that appears when the toolbox is visible.
     *
     * @param icon null to leave unspecified
     */
    public void createMainMenuItem(String[] menuPath, Icon icon, final WorkbenchContext context)
            throws Exception {
        new FeatureInstaller(context)
                .addMainMenuItemWithJava14Fix(this, menuPath, new StringBuilder(getName()).append("...").toString(),
                        true, icon, new EnableCheck() {
                    @Override
                    public String check(JComponent component) {
                        ((JCheckBoxMenuItem) component).setSelected(getToolbox(context).isVisible());
                        return null;
                    }
                });
    }
}
