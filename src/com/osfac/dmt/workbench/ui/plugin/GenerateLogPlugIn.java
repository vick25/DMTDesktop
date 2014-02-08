package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.Date;
import javax.swing.ImageIcon;

public class GenerateLogPlugIn extends AbstractPlugIn {

    public GenerateLogPlugIn() {
    }

    public String getName() {
        return I18N.get("ui.plugin.GenerateLogPlugIn.log");
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("application_view_list.png");
    }

    public boolean execute(PlugInContext context) throws java.lang.Exception {
        reportNothingToUndoYet(context);
        context.getOutputFrame().createNewDocument();
        context.getOutputFrame().addHeader(1, I18N.get("ui.plugin.GenerateLogPlugIn.jump-workbench-log"));
        context.getOutputFrame().addHeader(2, I18N.get("ui.plugin.GenerateLogPlugIn.generated") + " " + new Date());
        context.getOutputFrame().addText(context.getWorkbenchFrame().getLog());
        context.getOutputFrame().surface();
        return true;
    }
}
