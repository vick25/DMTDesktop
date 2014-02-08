package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public abstract class CircleLineStringEndpointStyle extends LineStringEndpointStyle {

    private final static int DIAMETER = 8;
    private Stroke circleStroke = new BasicStroke(2);

    private CircleLineStringEndpointStyle(String name, boolean start, String iconFile) {
        super(name, IconLoader.icon(iconFile), start);
    }

    protected void paint(Point2D terminal, Point2D next, Viewport viewport,
            Graphics2D graphics) throws NoninvertibleTransformException {
        graphics.setColor(lineColorWithAlpha);
        graphics.setStroke(circleStroke);
        graphics.draw(toShape(terminal));
    }

    private Shape toShape(Point2D viewPoint) {
        return new Ellipse2D.Double(viewPoint.getX() - (DIAMETER / 2d),
                viewPoint.getY() - (DIAMETER / 2d), DIAMETER, DIAMETER);
    }

    public static class Start extends CircleLineStringEndpointStyle {

        public Start() {
            super(I18N.get("ui.renderer.style.CircleLineStringEndpointStyle.Start-Circle"), true, "CircleStart.gif");
        }
    }

    public static class End extends CircleLineStringEndpointStyle {

        public End() {
            super(I18N.get("ui.renderer.style.CircleLineStringEndpointStyle.End-Circle"), false, "CircleEnd.gif");
        }
    }
}
