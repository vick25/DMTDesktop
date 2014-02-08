package org.openjump.core.ui.plugin.mousemenu;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.osfac.dmt.workbench.ui.zoom.ZoomToClickPlugIn;
import com.osfac.dmt.workbench.WorkbenchContext;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import org.openjump.core.ui.images.IconLoader;

public class ZoomOutPlugIn extends AbstractPlugIn {
    
    public static final ImageIcon ICON = IconLoader.icon("zoom_out.png");
    
    public void initialize(PlugInContext context) throws Exception { 
    
        WorkbenchContext workbenchContext = context.getWorkbenchContext();
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
        JPopupMenu popupMenu = LayerViewPanel.popupMenu();
  
        featureInstaller.addPopupMenuItem(popupMenu, this,
            new String[] {I18N.get("ui.MenuNames.ZOOM")}, 
            getName(),
            false, 
            GUIUtil.toSmallIcon(ICON),
            ZoomInPlugIn.createEnableCheck(workbenchContext));
    }
    
    public ZoomOutPlugIn(){
    }
    
    public boolean execute(final PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        new ZoomToClickPlugIn(0.5).execute(context);
        return true;
    }
    
    public String getName(){
        return I18N.get("org.openjump.core.ui.plugin.mousemenu.ZoomOutPlugIn"); 
    }
    
     
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
            checkFactory.createWindowWithSelectionManagerMustBeActiveCheck()
        );
    }
    
}
