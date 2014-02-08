package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;

public class ClearSelectionPlugIn extends AbstractPlugIn {

    public ClearSelectionPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getSelectionManager().clear();
        return true;
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));
    }

    public static ImageIcon getIcon() {
        return IconLoader.icon("deselect.gif");
        //return IconLoaderFamFam.icon("cross.png");
    }
}
