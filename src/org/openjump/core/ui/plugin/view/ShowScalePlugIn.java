package org.openjump.core.ui.plugin.view;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import org.openjump.core.ui.plugin.view.helpclassescale.InstallShowScalePlugIn;
import org.openjump.core.ui.plugin.view.helpclassescale.ShowScaleRenderer;

/**
 * - initializes renderplugin - plugin calculates the actual scale and draws the
 * text (and a white rectangle around) in the map window all things are done in
 * ShowScaleRenderer
 * 
* @author sstein
 */
public class ShowScalePlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        InstallShowScalePlugIn myInstallScalePlugIn = new InstallShowScalePlugIn();
        reportNothingToUndoYet(context);
        ShowScaleRenderer.setEnabled(!ShowScaleRenderer.isEnabled(
                context.getLayerViewPanel()), context.getLayerViewPanel());
        context.getLayerViewPanel().getRenderingManager().render(ShowScaleRenderer.CONTENT_ID);
        return true;
    }
}
