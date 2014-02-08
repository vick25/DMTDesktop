package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.InfoFrame;
import com.osfac.dmt.workbench.ui.SelectionManagerProxy;
import com.osfac.dmt.workbench.ui.TaskFrameProxy;
import java.util.Iterator;

public class FeatureInfoPlugIn extends AbstractPlugIn {

    public FeatureInfoPlugIn() {
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
                .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        //Don't pass in TaskFrame as LayerManagerProxy, because the TaskFrame may
        //be closed and thus the LayerManagerProxy may return null. [Bob Boseko]
        InfoFrame infoFrame =
                new InfoFrame(
                context.getWorkbenchContext(),
                (LayerManagerProxy) context.getActiveInternalFrame(),
                ((TaskFrameProxy) context.getActiveInternalFrame()).getTaskFrame());
        infoFrame.setSize(500, 300);

        for (Iterator i = context.getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (((SelectionManagerProxy) context.getActiveInternalFrame())
                    .getSelectionManager()
                    .getFeaturesWithSelectedItems(layer)
                    .isEmpty()) {
                continue;
            }

            infoFrame.getModel().add(
                    layer,
                    ((SelectionManagerProxy) context.getActiveInternalFrame()).getSelectionManager().getFeaturesWithSelectedItems(
                    layer));
        }

        infoFrame.setSelectedTab(infoFrame.getGeometryTab());
        context.getWorkbenchFrame().addInternalFrame(infoFrame);

        return true;
    }
}
