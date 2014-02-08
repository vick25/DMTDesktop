package org.openjump.core.ui.plugin.view;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

public class NorthArrowPlugIn extends AbstractPlugIn {

    private static final String NORTH_ARROW = I18N.get("org.openjump.core.ui.plugin.view.NorthArrowPlugIn.North-Arrow");

    public void initialize(PlugInContext context) throws Exception {
        FeatureInstaller featureInstaller = context.getFeatureInstaller();
        final WorkbenchContext workbenchContext = context.getWorkbenchContext();
        NorthArrowInstallRenderer northArrowInstallRenderer = new NorthArrowInstallRenderer();
        northArrowInstallRenderer.initialize(new PlugInContext(workbenchContext,
                null, null, null, null));
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        featureInstaller.addMainMenuItem(this,
                new String[]{MenuNames.VIEW},
                getName(),
                true,
                null,
                new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component)
                        .setSelected(NorthArrowRenderer.isEnabled(workbenchContext
                        .getLayerViewPanel()));
                return null;
            }
        }));
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        NorthArrowRenderer.setEnabled(!NorthArrowRenderer.isEnabled(
                context.getLayerViewPanel()), context.getLayerViewPanel());
        context.getLayerViewPanel().getRenderingManager().render(NorthArrowRenderer.CONTENT_ID);

        return true;
    }

    public String getName() {
        return NORTH_ARROW;
    }
}
