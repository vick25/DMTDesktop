package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;

public interface CursorTool extends MouseListener, MouseMotionListener {

    public Cursor getCursor();

    /**
     * @return null to use a default icon
     */
    public Icon getIcon();

    public void activate(LayerViewPanel layerViewPanel);

    public void deactivate();

    /**
     * @return true if this CursorTool uses the right mouse button; false to
     * allow the panel to show a popup-menu on right-clicks
     */
    public boolean isRightMouseButtonUsed();

    public boolean isGestureInProgress();

    /**
     * Notifies the CursorTool that a party is requesting that the gesture
     * currently in progress be aborted.
     */
    public void cancelGesture();

    /**
     * Returns a very brief description of this CursorTool.
     *
     * @return the name of this CursorTool
     */
    public String getName();
}
