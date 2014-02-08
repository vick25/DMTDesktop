package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;

public class SaveProjectPlugIn extends AbstractSaveProjectPlugIn {

    public static final ImageIcon ICON = IconLoader.icon("layout.png");
    private SaveProjectAsPlugIn saveProjectAsPlugIn;

    public SaveProjectPlugIn(SaveProjectAsPlugIn saveProjectAsPlugIn) {
        this.saveProjectAsPlugIn = saveProjectAsPlugIn;
    }

    public String getName() {
        return I18N.get("ui.plugin.SaveProjectPlugIn.save-project");
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);

        if (context.getTask().getProjectFile() == null) {
            return saveProjectAsPlugIn.execute(context);
        }

        save(context.getTask(), context.getTask().getProjectFile(),
                context.getWorkbenchFrame());

        return true;
    }
}
