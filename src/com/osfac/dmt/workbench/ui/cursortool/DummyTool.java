package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

public class DummyTool implements CursorTool {

    public DummyTool() {
    }

    public Cursor getCursor() {
        return Cursor.getDefaultCursor();
    }

    public void activate(LayerViewPanel layerViewPanel) {
    }

    public void deactivate() {
    }

    public boolean isRightMouseButtonUsed() {
        return false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public Icon getIcon() {
        return null;
    }

    public boolean isGestureInProgress() {
        return false;
    }

    public void cancelGesture() {
    }

    public String getName() {
        return I18N.get("ui.cursortool.DummyTool.dummy-cursor-tool");
    }
}
