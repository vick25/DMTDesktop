package org.openjump.core.ui.plugin.layer;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPopupMenu;

public class ToggleVisiblityPlugIn extends AbstractPlugIn {

    private final static String toggleVisibility = I18N.get("org.openjump.core.ui.plugin.layer.ToggleVisiblityPlugIn.Toggle-Visibility");
    private final static String errorSeeOutputWindow = I18N.get("org.openjump.core.ui.plugin.layer.ToggleVisiblityPlugIn.Error-See-Output-Window");
    private final static String layerName = I18N.get("org.openjump.core.ui.plugin.mousemenu.SaveDatasetsPlugIn.Layer-Name");

    public void initialize(PlugInContext context) throws Exception {
        WorkbenchContext workbenchContext = context.getWorkbenchContext();
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
        JPopupMenu layerNamePopupMenu = workbenchContext.getWorkbench()
                .getFrame()
                .getLayerNamePopupMenu();
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                this, toggleVisibility,
                false, null,
                ToggleVisiblityPlugIn.createEnableCheck(workbenchContext));

        JPopupMenu wmsLayerNamePopupMenu = workbenchContext.getWorkbench()
                .getFrame()
                .getWMSLayerNamePopupMenu();
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
                this, toggleVisibility,
                false, null,
                ToggleVisiblityPlugIn.createEnableCheck2(workbenchContext));

    }

    public boolean execute(PlugInContext context) throws Exception {
        try {
            Collection layerCollection = (Collection) context.getWorkbenchContext().getLayerNamePanel().selectedNodes(Layerable.class);
            boolean firingEvents = context.getLayerManager().isFiringEvents();
            context.getLayerManager().setFiringEvents(false);
            try {
                for (Iterator j = layerCollection.iterator(); j.hasNext();) {
                    Layerable layer = (Layerable) j.next();
//	            	monitor.report(layerName+": " + layer.getName());
                    layer.setVisible(!layer.isVisible());
                }
            } finally {
                context.getLayerManager().setFiringEvents(firingEvents);
                context.getLayerViewPanel().repaint();
                context.getWorkbenchFrame().repaint();
            }
        } catch (Exception e) {
            context.getWorkbenchFrame().warnUser(errorSeeOutputWindow);
            context.getWorkbenchFrame().getOutputFrame().createNewDocument();
            context.getWorkbenchFrame().getOutputFrame().addText("ToggleVisiblityPlugIn Exception:" + e.toString());
            return false;
        }
        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    public static MultiEnableCheck createEnableCheck2(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayerablesMustBeSelectedCheck(1, Layerable.class));
    }
}
