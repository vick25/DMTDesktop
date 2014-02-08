package com.osfac.dmt.workbench.ui.zoom;

import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxPlugIn;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.NoninvertibleTransformException;

public class ZoomBarPlugIn extends ToolboxPlugIn {

    private static final int WIDTH = 300;

    protected void initializeToolbox(ToolboxDialog toolbox) {
        try {
            final ZoomBar zoomBar = new ZoomBar(true, false, toolbox.getContext().getWorkbench().getFrame());
            toolbox.getCenterPanel().add(zoomBar, BorderLayout.CENTER);
            zoomBar.setPreferredSize(new Dimension(WIDTH, (int) zoomBar.getPreferredSize().getHeight()));
            toolbox.addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    try {
                        zoomBar.updateComponents();
                    } catch (NoninvertibleTransformException x) {
                        x.printStackTrace();
                    }
                }
            });
            toolbox.setInitialLocation(new GUIUtil.Location(20, false, 20, true));
        } catch (NoninvertibleTransformException x) {
            toolbox.getContext().getWorkbench().getFrame().handleThrowable(x);
        }
    }
}
