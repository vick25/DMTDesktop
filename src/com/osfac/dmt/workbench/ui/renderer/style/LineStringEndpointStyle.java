package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.swing.Icon;

public abstract class LineStringEndpointStyle extends LineStringStyle implements ChoosableStyle {

    private boolean start;
    protected String name;
    protected Icon icon;

    public LineStringEndpointStyle(String name, Icon icon, boolean start) {
        super(name, icon);
        this.name = name;
        this.icon = icon;
        this.start = start;
    }

    protected void paintLineString(LineString lineString, Viewport viewport,
            Graphics2D graphics) throws Exception {
        if (lineString.isEmpty()) {
            return;
        }

        paint(start ? lineString.getCoordinateN(0)
                : lineString.getCoordinateN(lineString.getNumPoints() - 1),
                start ? lineString.getCoordinateN(1)
                : lineString.getCoordinateN(lineString.getNumPoints() - 2),
                viewport, graphics);
    }

    private void paint(Coordinate terminal, Coordinate next, Viewport viewport,
            Graphics2D graphics) throws Exception {
        paint(viewport.toViewPoint(new Point2D.Double(terminal.x, terminal.y)),
                viewport.toViewPoint(new Point2D.Double(next.x, next.y)), viewport,
                graphics);
    }

    protected abstract void paint(Point2D terminal, Point2D next,
            Viewport viewport, Graphics2D graphics) throws Exception;

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }
}
