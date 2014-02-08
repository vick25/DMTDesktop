package org.openjump.core.ui.plugin.tools;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.renderer.RenderingManager;
import com.osfac.dmt.workbench.ui.zoom.AbstractZoomTool;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 * Zooms the image in the current task window. Uses raster scaling operations.
 *
 * @author Larry Becker
 * @version 1.01
 */
public class ZoomRealtimeTool extends AbstractZoomTool {

    private static final double ZOOM_FACTOR = 5d;
    private static final double ZOOM_OUT_LIMIT = 0.1d;
    private boolean dragging = false;
    private boolean rightMouse = false;
    private static final String sName = I18N.get("org.openjump.core.ui.plugin.tools.ZoomRealtimeTool.Zoom-Realtime");

    public ZoomRealtimeTool() {
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("MagnifyCursor2.gif").getImage());
    }

    public Icon getIcon() {
        return IconLoader.icon("Magnify3.gif");
    }

    public boolean isRightMouseButtonUsed() {
        return true;
    }

    public String getName() {
        return sName;
    }

    public void mouseDragged(MouseEvent e) {
        try {
            rightMouse = SwingUtilities.isRightMouseButton(e);
            if (!dragging) {
                dragging = true;
                getPanel().getRenderingManager().setPaintingEnabled(false);
                cacheImage();
            }

            drawImage(e.getPoint());
            super.mouseDragged(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (!dragging) {
            try {
                getPanel().getViewport().zoomToViewPoint(e.getPoint(), 1.0d);
            } catch (NoninvertibleTransformException ex) {
                return;
            }

        }

        getPanel().getRenderingManager().setPaintingEnabled(true);
        dragging = false;
        super.mouseReleased(e);
    }

    protected Shape getShape(Point2D source, Point2D destination) {
        return null;
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();
        RenderingManager renderManager = getPanel().getRenderingManager();
        renderManager.setPaintingEnabled(false);
// LDB: substitute the following code for the ..Queue().add below if available
//	getPanel().getRenderingManager().setRenderingMode(new Runnable() {
//		public void run() {
//			RenderingManager renderManager = getPanel().getRenderingManager();
//			renderManager.setPaintingEnabled(true);
//			renderManager.repaintPanel();  		
//		}
//	},RenderingManager.INTERACTIVE);

        getPanel().getViewport().zoomToViewPoint(zoomTo, scale);

        renderManager.getDefaultRendererThreadQueue().add(
                new Runnable() {
                    public void run() {
                        RenderingManager renderManager = getPanel().getRenderingManager();
                        renderManager.setPaintingEnabled(true);
                        renderManager.repaintPanel();
                    }
                });
    }

    private void drawImage(Point p) throws NoninvertibleTransformException {
        double xdrag = p.getX() - getViewSource().getX();
        double ydrag = p.getY() - getViewSource().getY();
        double scaleFactor;
        if (rightMouse) {
            scaleFactor = ZOOM_FACTOR * xdrag; //to reverse do:(getViewSource().getX() - p.getX());
        } else {
            scaleFactor = ZOOM_FACTOR * ydrag;
        }
        double w = origImage.getWidth(getPanel());
        double h = origImage.getHeight(getPanel());
        scale = (h + scaleFactor) / h; //normalize
        scale = (scale < ZOOM_OUT_LIMIT) ? ZOOM_OUT_LIMIT : scale;
        double w2 = w * scale;
        double h2 = h * scale;
        double dx = (w - w2) / 2;
        double dy = (h - h2) / 2;
        double xoff = 0;
        double yoff = 0;
        if (rightMouse) {
            yoff = ydrag / scale;
        } else {
            xoff = xdrag / scale;
        }
        zoomTo.x = dx + w2 / 2 - xoff;
        zoomTo.y = dy + h2 / 2 - yoff;
        if (rightMouse) {
            dy += ydrag;
        } else {
            dx += xdrag;
        }

        drawImage((int) dx, (int) dy, scale);
    }
}