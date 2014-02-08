package com.osfac.dmt.workbench.ui.zoom;

import com.osfac.dmt.util.MathUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import javax.swing.Icon;
import javax.swing.SwingUtilities;

public class ZoomTool extends AbstractZoomTool {

    /**
     * If the selection box has side length less than this value, the input will
     * be considered to be a click rather than a box.
     */
    public static final String ZOOM_TOOL_NAME = "Zoom In/Out";

    public ZoomTool() {
        setColor(Color.black);
    }

    public Icon getIcon() {
        return IconLoader.icon("Magnify.gif");
        //return IconLoaderFamFam.icon("zoom.png");
    }

    public String getName() {
        return ZOOM_TOOL_NAME;
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("MagnifyCursor.gif").getImage());
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();

        double minX = Math.min(getViewSource().getX(), getViewDestination().getX());
        double maxX = Math.max(getViewSource().getX(), getViewDestination().getX());
        double minY = Math.min(getViewSource().getY(), getViewDestination().getY());
        double maxY = Math.max(getViewSource().getY(), getViewDestination().getY());

        double widthOfNewViewAsPerceivedByOldView = maxX - minX;
        double heightOfNewViewAsPerceivedByOldView = maxY - minY;

        if ((widthOfNewViewAsPerceivedByOldView == 0)
                && (heightOfNewViewAsPerceivedByOldView == 0)) {
            //Handled by #mouseClicked
            return;
        }

        if ((widthOfNewViewAsPerceivedByOldView < BOX_TOLERANCE)
                && (heightOfNewViewAsPerceivedByOldView < BOX_TOLERANCE)) {
            zoomAt(new Point2D.Double(MathUtil.avg(minX, maxX),
                    MathUtil.avg(minY, maxY)), ZOOM_IN_FACTOR);

            return;
        }

        Point2D centreOfNewViewAsPerceivedByOldView = new Point2D.Double(minX
                + (widthOfNewViewAsPerceivedByOldView / 2),
                minY + (heightOfNewViewAsPerceivedByOldView / 2));
        getPanel().getViewport().zoom(centreOfNewViewAsPerceivedByOldView,
                widthOfNewViewAsPerceivedByOldView,
                heightOfNewViewAsPerceivedByOldView);

    }

    public void mouseClicked(MouseEvent e) {
        try {
            double zoomFactor = SwingUtilities.isRightMouseButton(e)
                    ? (1 / ZOOM_IN_FACTOR) : ZOOM_IN_FACTOR;
            zoomAt(e.getPoint(), zoomFactor);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    public void mousePressed(MouseEvent e) {
        try {
            if (SwingUtilities.isLeftMouseButton(e)) {
                super.mousePressed(e);
            }
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    private void zoomAt(Point2D p, double zoomFactor)
            throws NoninvertibleTransformException {
        //getPanel().getViewport().zoomToViewPoint(p, zoomFactor);
        zoomAt(p, zoomFactor, getAnimatingZoom());
    }

    public boolean isRightMouseButtonUsed() {
        return true;
    }

    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }

    public void activate(LayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
    }

    public void deactivate() {
        super.deactivate();
    }

    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
    }

    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }
}
