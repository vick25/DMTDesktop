package org.openjump.core.ui.plugin.layer;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;

/**
 * <code>ChangeLayerableName</code> changes the name of a layer.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class ChangeLayerableNamePlugIn extends AbstractPlugIn {

    //private EnableCheck enableCheck;

    @Override
    public void initialize(PlugInContext context) throws Exception {
	    WorkbenchContext workbenchContext = context.getWorkbenchContext();
	    EnableCheck enableCheck = createEnableCheck(workbenchContext);
	    
	    // Install in main menu
	    FeatureInstaller installer = new FeatureInstaller(workbenchContext);
	    installer.addMainMenuItemWithJava14Fix(this,
	        new String[] { MenuNames.LAYER }, getName() + "...", false, null, enableCheck);
		
		// Install in layerName popup menu
	    JPopupMenu popupMenu = workbenchContext.getWorkbench().getFrame()
	        .getLayerNamePopupMenu();
	    installer.addPopupMenuItem(popupMenu, this, getName() + "{pos:5}",
		    false, null, enableCheck);
		
		// INstall in WMSLayerName popup menu
	    popupMenu = workbenchContext.getWorkbench().getFrame()
		    .getWMSLayerNamePopupMenu();
	    installer.addPopupMenuItem(popupMenu, this, getName() + "{pos:6}",
		    false, null, enableCheck);
    }

    @Override
    public String getName() {
	return I18N
		.get("org.openjump.core.ui.plugin.layer.ChangeLayerableName.Rename");
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
	    reportNothingToUndoYet(context);
	    final Layerable layer = (Layerable) context.getLayerNamePanel()
		    .selectedNodes(Layerable.class).iterator().next();
	    final String oldName = layer.getName();
	    final String newName =
	        (String)JOptionPane.showInputDialog(context.getWorkbenchFrame(),
			    I18N.get("org.openjump.core.ui.plugin.layer.ChangeLayerableName.Rename"),
			    getName(), JOptionPane.PLAIN_MESSAGE, null, null, oldName);
	    if(newName != null) {
	        execute(new UndoableCommand(getName()) {
		        @Override
		        public void execute() {
		            layer.setName(newName);
		        }
		        @Override
		        public void unexecute() {
		            layer.setName(oldName);
		        }
	        }, context);
	    }
	    return true;
    }

    /**
     * @param workbenchContext
     * @return an enable check
     */
    public EnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
	    //if (enableCheck != null) return enableCheck;
	    EnableCheckFactory enableCheckFactory = new EnableCheckFactory(workbenchContext);
	    MultiEnableCheck enableCheck = new MultiEnableCheck();
	    enableCheck.add(enableCheckFactory.createWindowWithLayerManagerMustBeActiveCheck());
	    enableCheck.add(enableCheckFactory.createExactlyNLayerablesMustBeSelectedCheck(1, Layerable.class));
	    
	    return enableCheck;
    }

}
