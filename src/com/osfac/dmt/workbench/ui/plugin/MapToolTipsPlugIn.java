package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

public class MapToolTipsPlugIn extends AbstractPlugIn {

    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
                checkFactory
                .createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component).setSelected(
                        workbenchContext.getLayerViewPanel().getToolTipWriter().isEnabled());
                return null;
            }
        });
    }

    public String getName() {
        //Can't use auto-naming, which produces "Map Tool Tips"; and Unix/Windows
        //CVS issues will occur if I rename MapToolTipsPlugIn to MapTooltipsPlugIn. [Bob Boseko]
        return I18N.get("ui.plugin.MapToolTipsPlugIn.map-tooltips");
    }

    public boolean execute(PlugInContext context) throws Exception {
        context.getLayerViewPanel().getToolTipWriter().setEnabled(
                !context.getLayerViewPanel().getToolTipWriter().isEnabled());
        return true;
    }
}
