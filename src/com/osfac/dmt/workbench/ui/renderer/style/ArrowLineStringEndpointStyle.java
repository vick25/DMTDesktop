package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class ArrowLineStringEndpointStyle extends LineStringEndpointStyle {

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
    public ArrowLineStringEndpointStyle(String name, boolean start, String iconFile,
            double finAngle, double finLength, boolean filled) {
        super(name, IconLoader.icon(iconFile), start);
        this.finAngle = finAngle;
        this.finLength = finLength;
        this.filled = filled;
    }

    protected void paint(Point2D terminal, Point2D next, Viewport viewport,
            Graphics2D graphics) throws NoninvertibleTransformException {
        if (terminal.equals(next)) {
            return;
        }

        graphics.setColor(lineColorWithAlpha);
        graphics.setStroke(stroke);

        GeneralPath arrowhead = arrowhead(terminal, next, finLength, finAngle);

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
    private GeneralPath arrowhead(Point2D shaftTip, Point2D shaftTail,
            double finLength, double finAngle) {
        GeneralPath arrowhead = new GeneralPath();
        Point2D finTip1 = fin(shaftTip, shaftTail, finLength, finAngle);
        Point2D finTip2 = fin(shaftTip, shaftTail, finLength, -finAngle);
        arrowhead.moveTo((float) finTip1.getX(), (float) finTip1.getY());
        arrowhead.lineTo((float) shaftTip.getX(), (float) shaftTip.getY());
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

    public abstract static class Feathers extends ArrowLineStringEndpointStyle {

        private final static int SPACING = 5;
        private final static int FEATHERS = 2;

        public Feathers(String name, boolean start, String iconFile) {
            super(name, start, iconFile, MEDIUM_ANGLE, MEDIUM_LENGTH, false);
        }

        protected void paint(Point2D terminal, Point2D next, Viewport viewport,
                Graphics2D graphics) throws NoninvertibleTransformException {
            for (int i = 0; i < FEATHERS; i++) {
                Point2D unit = GUIUtil.multiply(GUIUtil.subtract(next, terminal),
                        1d / next.distance(terminal));
                Point2D pseudoTerminal = GUIUtil.add(terminal,
                        GUIUtil.multiply(unit, (finLength + (i * SPACING))));
                super.paint(pseudoTerminal, terminal, viewport, graphics);
            }
        }

        public void initialize(Layer layer) {
            super.initialize(layer);

            //Force line width to 1; anything thicker looks ugly (feathers will run
            //into each other). [Bob Boseko]
            stroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND);
        }
    }

    public static class FeathersStart extends Feathers {

        public FeathersStart() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.start-feathers"), true, "FeathersStart.gif");
        }
    }

    public static class FeathersEnd extends Feathers {

        public FeathersEnd() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.end-feathers"), false, "FeathersEnd.gif");
        }
    }

    public static class OpenStart extends ArrowLineStringEndpointStyle {

        public OpenStart() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.start-arrow-open"), true, "ArrowStartOpen.gif", MEDIUM_ANGLE,
                    MEDIUM_LENGTH, false);
        }
    }

    public static class OpenEnd extends ArrowLineStringEndpointStyle {

        public OpenEnd() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.end-arrow-open"), false, "ArrowEndOpen.gif", MEDIUM_ANGLE,
                    MEDIUM_LENGTH, false);
        }
    }

    public static class SolidStart extends ArrowLineStringEndpointStyle {

        public SolidStart() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.start-arrow-solid"), true, "ArrowStartSolid.gif",
                    MEDIUM_ANGLE, MEDIUM_LENGTH, true);
        }
    }

    public static class SolidEnd extends ArrowLineStringEndpointStyle {

        public SolidEnd() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.end-arrow-solid"), false, "ArrowEndSolid.gif", MEDIUM_ANGLE,
                    MEDIUM_LENGTH, true);
        }
    }

    public static class NarrowSolidStart extends ArrowLineStringEndpointStyle {

        public NarrowSolidStart() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.start-arrow-solid-narrow"), true,
                    "ArrowStartSolidNarrow.gif", SMALL_ANGLE, LARGE_LENGTH, true);
        }
    }

    public static class NarrowSolidEnd extends ArrowLineStringEndpointStyle {

        public NarrowSolidEnd() {
            super(I18N.get("ui.renderer.style.ArrowLineStringEndpointStyle.end-arrow-solid-narrow"), false, "ArrowEndSolidNarrow.gif",
                    SMALL_ANGLE, LARGE_LENGTH, true);
        }
    }
}
