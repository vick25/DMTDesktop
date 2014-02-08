package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.swing.Icon;

public abstract class LineStringSegmentStyle extends LineStringStyle implements ChoosableStyle {

    protected String name;
    protected Icon icon;

    public LineStringSegmentStyle(String name, Icon icon) {
        super(name, icon);
        this.name = name;
        this.icon = icon;
    }

    protected void paintLineString(LineString lineString, Viewport viewport,
            Graphics2D graphics) throws Exception {
        for (int i = 0; i < lineString.getNumPoints() - 1; i++) {
            paint(lineString.getCoordinateN(i),
                    lineString.getCoordinateN(i + 1),
                    viewport, graphics);
        }
    }

    protected void paint(Coordinate p0, Coordinate p1, Viewport viewport,
            Graphics2D graphics) throws Exception {
        paint(viewport.toViewPoint(new Point2D.Double(p0.x, p0.y)),
                viewport.toViewPoint(new Point2D.Double(p1.x, p1.y)), viewport,
                graphics);
    }

    protected abstract void paint(Point2D p0, Point2D p1,
            Viewport viewport, Graphics2D graphics) throws Exception;

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }
}
