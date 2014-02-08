package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.CloneableInternalFrame;
import com.osfac.dmt.workbench.ui.LayerViewPanelProxy;
import javax.swing.JInternalFrame;

public class CloneWindowPlugIn extends AbstractPlugIn {

    public CloneWindowPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);

        JInternalFrame frame = ((CloneableInternalFrame) context.getActiveInternalFrame()).internalFrameClone();
        context.getWorkbenchFrame().addInternalFrame(frame);
        // [mmichaud] now, the user is asked if he really wants to close the window
        // in the case where other internal windows depends upon this one
        frame.setClosable(true);
        if (frame instanceof LayerViewPanelProxy) {
            //Need to update image buffer; otherwise, user will just see a white panel.
            //[Bob Boseko]
            ((LayerViewPanelProxy) frame).getLayerViewPanel().repaint();
        }
        return true;
    }
}
