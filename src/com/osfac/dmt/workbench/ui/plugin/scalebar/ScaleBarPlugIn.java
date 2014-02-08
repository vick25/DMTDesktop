package com.osfac.dmt.workbench.ui.plugin.scalebar;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;

public class ScaleBarPlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        ScaleBarRenderer.setEnabled(!ScaleBarRenderer.isEnabled(
                context.getLayerViewPanel()), context.getLayerViewPanel());
        context.getLayerViewPanel().getRenderingManager().render(ScaleBarRenderer.CONTENT_ID);
        return true;
    }
}
