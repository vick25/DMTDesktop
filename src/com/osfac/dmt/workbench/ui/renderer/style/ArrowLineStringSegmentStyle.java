package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class ArrowLineStringSegmentStyle extends LineStringSegmentStyle {

    private final static double SMALL_ANGLE = 10;
    private final static double MEDIUM_ANGLE = 30;
    private final static double MEDIUM_LENGTH = 10;
    private final static double LARGE_LENGTH = 15;
    private boolean filled;
    private double finAngle;
    protected double finLength;

    /**
     * @param finAngle degrees
     * @param finLength pixels
     */
    public ArrowLineStringSegmentStyle(String name, String iconFile,
            double finAngle, double finLength, boolean filled) {
        super(name, IconLoader.icon(iconFile));
        this.finAngle = finAngle;
        this.finLength = finLength;
        this.filled = filled;
    }

    protected void paint(Point2D p0, Point2D p1, Viewport viewport,
            Graphics2D graphics) throws NoninvertibleTransformException {
        if (p0.equals(p1)) {
            return;
        }

        graphics.setColor(lineColorWithAlpha);
        graphics.setStroke(stroke);

        GeneralPath arrowhead = arrowhead(p0, p1, finLength, finAngle);

        if (filled) {
            arrowhead.closePath();
            graphics.fill(arrowhead);
        }

        //#fill isn't affected by line width, but #draw is. Therefore, draw even
        //if we've already filled. [Bob Boseko]
        graphics.draw(arrowhead);
    }

    /**
     * @param tail the tail of the whole arrow; just used to determine angle
     * @param finLength required distance from the tip to each fin's tip
     */
    private GeneralPath arrowhead(Point2D p0, Point2D p1,
            double finLength, double finAngle) {
        Point2D mid = new Point2D.Float((float) ((p0.getX() + p1.getX()) / 2),
                (float) ((p0.getY() + p1.getY()) / 2));
        GeneralPath arrowhead = new GeneralPath();
        Point2D finTip1 = fin(mid, p0, finLength, finAngle);
        Point2D finTip2 = fin(mid, p0, finLength, -finAngle);
        arrowhead.moveTo((float) finTip1.getX(), (float) finTip1.getY());
        arrowhead.lineTo((float) mid.getX(), (float) mid.getY());
        arrowhead.lineTo((float) finTip2.getX(), (float) finTip2.getY());

        return arrowhead;
    }

    private Point2D fin(Point2D shaftTip, Point2D shaftTail, double length,
            double angle) {
        double shaftLength = shaftTip.distance(shaftTail);
        Point2D finTail = shaftTip;
        Point2D finTip = GUIUtil.add(GUIUtil.multiply(GUIUtil.subtract(
                shaftTail, shaftTip), length / shaftLength), finTail);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate((angle * Math.PI) / 180, finTail.getX(),
                finTail.getY());

        return affineTransform.transform(finTip, null);
    }

    public static class Open extends ArrowLineStringSegmentStyle {

        public Open() {
            super(I18N.get("ui.renderer.style.ArrowLineStringSegmentStyle.Segment-Mid-Arrow-Open"), "ArrowMidOpen.gif", MEDIUM_ANGLE,
                    MEDIUM_LENGTH, false);
        }
    }

    public static class Solid extends ArrowLineStringSegmentStyle {

        public Solid() {
            super(I18N.get("ui.renderer.style.ArrowLineStringSegmentStyle.Segment-Mid-Arrow-Solid"), "ArrowMidSolid.gif",
                    MEDIUM_ANGLE, MEDIUM_LENGTH, true);
        }
    }

    public static class NarrowSolid extends ArrowLineStringSegmentStyle {

        public NarrowSolid() {
            super(I18N.get("ui.renderer.style.ArrowLineStringSegmentStyle.Segment-Mid-Arrow-Solid-Narrow"),
                    "ArrowMidSolidNarrow.gif", SMALL_ANGLE, LARGE_LENGTH, true);
        }
    }
}
