package com.osfac.dmt.workbench.ui.zoom;

import com.vividsolutions.jts.geom.Envelope;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import javax.swing.Icon;

/**
 * Pans the image in the current task window. Image handling is designed to
 * minimize flickering and latency.
 *
 * @author Bob Boseko
 * @version 1.1
 */
public class PanTool extends AbstractZoomTool {
    // MD - incorporates fco lavin's fix for eliminating flicker

    private boolean dragging = false;
//  private Image origImage;
//  private Image auxImage = null;

    public PanTool() {
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("Hand.gif").getImage());
    }

    public Icon getIcon() {
        //return IconLoaderFamFam.icon("BigHand_small.png");
        return IconLoader.icon("BigHand.gif");
    }

    public void mouseDragged(MouseEvent e) {
        try {
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
            return;
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

        double xDisplacement = getModelDestination().x - getModelSource().x;
        double yDisplacement = getModelDestination().y - getModelSource().y;
        Envelope oldEnvelope = getPanel().getViewport()
                .getEnvelopeInModelCoordinates();
        getPanel().getViewport().zoom(new Envelope(oldEnvelope.getMinX()
                - xDisplacement, oldEnvelope.getMaxX() - xDisplacement,
                oldEnvelope.getMinY() - yDisplacement,
                oldEnvelope.getMaxY() - yDisplacement));
    }

    private void drawImage(Point p) throws NoninvertibleTransformException {
        double dx = p.getX() - getViewSource().getX();
        double dy = p.getY() - getViewSource().getY();

        auxImage = createImageIfNeeded(auxImage);
        auxImage.getGraphics().setColor(Color.WHITE);
        auxImage.getGraphics().fillRect(0, 0, auxImage.getWidth(getPanel()), auxImage.getHeight(getPanel()));
        auxImage.getGraphics().drawImage(origImage, (int) dx, (int) dy, getPanel());
        getPanel().getGraphics().drawImage(auxImage, 0, 0, getPanel());
    }
}