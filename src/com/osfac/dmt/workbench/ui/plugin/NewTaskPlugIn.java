package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;

public class NewTaskPlugIn extends AbstractPlugIn {

    public NewTaskPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getWorkbenchFrame().addTaskFrame();
        return true;
    }
    //[sstein 26.08.2006] added for toolbar

    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        return new MultiEnableCheck();
    }

    //[sstein 26.08.2006] added for toolbar
    public static ImageIcon getIcon() {
        return IconLoader.icon("layout_add.png");
    }

    //garuta 02.12.2011 added for file menu
    public static ImageIcon getIcon2() {
        return IconLoader.icon("layout_add_small.png");
    }
}
