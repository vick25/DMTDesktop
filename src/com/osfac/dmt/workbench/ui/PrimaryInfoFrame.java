package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.LayerManagerProxy;

/**
 * A TaskFrame can have several InfoFrames, but one PrimaryInfoFrame. This is
 * the InfoFrame used by the InfoTool. Other InfoFrames appear when the user
 * clicks the View Geometry Text or View Layer Attributes menus, for example.
 * This class simply marks an InfoFrame as being primary. WorkbenchFrame
 * positions InfoFrames differently depending on whether or not they are
 * primary.
 */
public class PrimaryInfoFrame extends InfoFrame {

    public PrimaryInfoFrame(WorkbenchContext workbenchContext, LayerManagerProxy layerManagerProxy, TaskFrame taskFrame) {
        super(workbenchContext, layerManagerProxy, taskFrame);
    }
}
