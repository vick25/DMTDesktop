package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.SwingUtilities;

/**
 * Filters out middle- and right-clicks.
 */
public class LeftClickFilter implements CursorTool {

    private CursorTool wrappee;

    public LeftClickFilter(CursorTool wrappee) {
        this.wrappee = wrappee;
    }

    public CursorTool getWrappee() {
        return wrappee;
    }

    public Icon getIcon() {
        return wrappee.getIcon();
    }

    public String getName() {
        return wrappee.getName();
    }

    public Cursor getCursor() {
        return wrappee.getCursor();
    }

    public void activate(LayerViewPanel panel) {
        wrappee.activate(panel);
    }

    public void deactivate() {
        wrappee.deactivate();
    }

    public void mouseClicked(MouseEvent e) {
        if (isOnlyLeftMouseButton(e) || isRightMouseButtonUsed()) {
            wrappee.mouseClicked(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isOnlyLeftMouseButton(e) || isRightMouseButtonUsed()) {
            wrappee.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isOnlyLeftMouseButton(e) || isRightMouseButtonUsed()) {
            wrappee.mouseReleased(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
        wrappee.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        wrappee.mouseExited(e);
    }

    public void mouseDragged(MouseEvent e) {
        if (isOnlyLeftMouseButton(e) || isRightMouseButtonUsed()) {
            wrappee.mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        wrappee.mouseMoved(e);
    }

    public boolean isRightMouseButtonUsed() {
        boolean rightMouseButtonUsed = false;
        if (wrappee instanceof AbstractCursorTool) {
            rightMouseButtonUsed = ((AbstractCursorTool) wrappee).isRightMouseButtonUsed();
        }
        return rightMouseButtonUsed;
    }

    public boolean isGestureInProgress() {
        return wrappee.isGestureInProgress();
    }

    public void cancelGesture() {
        wrappee.cancelGesture();
    }

    private boolean isOnlyLeftMouseButton(MouseEvent e) {
        //A future CursorTool may check whether *both* buttons are pressed (to
        //indicate that the interaction should be cancelled). [Bob Boseko]
        return SwingUtilities.isLeftMouseButton(e)
                && !SwingUtilities.isRightMouseButton(e);
    }
}
