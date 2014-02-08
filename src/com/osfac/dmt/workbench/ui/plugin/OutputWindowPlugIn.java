package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;

public class OutputWindowPlugIn extends AbstractPlugIn {

    public OutputWindowPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getWorkbenchFrame().getOutputFrame().surface();
        return true;
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("Frame.gif");
    }
}
