package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.dmt.workbench.plugin.PlugInContext;
import org.openjump.core.ui.plugin.AbstractWizardPlugin;
import org.openjump.core.ui.plugin.wms.AddWmsLayerWizard;

public class AddWMSQueryPlugIn extends AbstractWizardPlugin {

    public void initialize(PlugInContext context) throws Exception {
        super.initialize(context);
        AddWmsLayerWizard wizard = new AddWmsLayerWizard(context.getWorkbenchContext());
        setWizard(wizard);
    }
}
