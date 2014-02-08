package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

/**
 * Enables the behaviour of a CursorTool instance to be overridden.
 */
public abstract class DelegatingTool implements CursorTool {

    private CursorTool delegate = new DummyTool();
    private boolean active = false;
    private LayerViewPanel layerViewPanel;

    public CursorTool getDelegate() {
        return delegate;
    }

    public DelegatingTool(CursorTool cursorTool) {
        setDelegate(cursorTool);
    }

    public void setDelegate(CursorTool delegate) {
        if (this.delegate == delegate) {
            //Don't activate/deactivate. [Bob Boseko]
            return;
        }

//        if (active) {
//            this.delegate.deactivate();
//        }

        this.delegate = delegate;

        if (active) {
            this.delegate.activate(layerViewPanel);
        }
    }

    public String getName() {
        return delegate.getName();
    }

    public Icon getIcon() {
        return delegate.getIcon();
    }

    public boolean isGestureInProgress() {
        return delegate.isGestureInProgress();
    }

    public void cancelGesture() {
        delegate.cancelGesture();
    }

    public void mousePressed(MouseEvent e) {
        delegate.mousePressed(e);
    }

    public void mouseClicked(MouseEvent e) {
        delegate.mouseClicked(e);
    }

    public void activate(LayerViewPanel layerViewPanel) {
        this.layerViewPanel = layerViewPanel;
        delegate.activate(layerViewPanel);
        active = true;
    }

    public Cursor getCursor() {
        return delegate.getCursor();
    }

    public void deactivate() {
        delegate.deactivate();
        active = false;
    }

    public void mouseReleased(MouseEvent e) {
        delegate.mouseReleased(e);
    }

    public void mouseEntered(MouseEvent e) {
        delegate.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        delegate.mouseExited(e);
    }

    public void mouseDragged(MouseEvent e) {
        delegate.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        delegate.mouseMoved(e);
    }

    public boolean isRightMouseButtonUsed() {
        return delegate.isRightMouseButtonUsed();
    }
}
