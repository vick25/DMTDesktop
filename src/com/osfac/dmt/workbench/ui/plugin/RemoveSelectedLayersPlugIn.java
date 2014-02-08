package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;

public class RemoveSelectedLayersPlugIn extends AbstractPlugIn {

    public RemoveSelectedLayersPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        // Changed on 2007-05-21 to use the new LayerManager()[Michael Michaud]
        remove(context, (Layerable[]) (context.getLayerNamePanel()).selectedNodes(
                Layerable.class).toArray(new Layerable[]{}));
        System.gc();
        return true;
    }

    public void remove(PlugInContext context, Layerable[] selectedLayers) {
        for (int i = 0; i < selectedLayers.length; i++) {
            // SIGLE start [obedel]
            // dispose layer immediately
            //selectedLayers[i].getLayerManager().remove(selectedLayers[i]);
            // ... becomes ...
            // Changed again by [Michael Michaud] on 2007-05-21 to solve the memory leak
            // thanks to the new LayerManager.dispose(PlugInContext, Layerable) method
            selectedLayers[i].getLayerManager().dispose(context.getWorkbenchFrame(), selectedLayers[i]);
            // SIGLE end
        }
        //Don't call LayerManager#setFiringEvents and
        //LayerManager#fireModelChanged, so that each
        //removed node is individually removed from the tree. [Bob Boseko]
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayerablesMustBeSelectedCheck(
                1, Layerable.class));
    }
    public static final ImageIcon ICON = IconLoader.icon("cross.png");
}
