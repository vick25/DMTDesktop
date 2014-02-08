package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.Assert;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.Icon;

public abstract class LineStringStyle implements Style {

    protected boolean enabled = true;
    protected Stroke stroke;

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            Assert.shouldNeverReachHere();
            return null;
        }
    }
    protected Color lineColorWithAlpha;
    protected Color fillColorWithAlpha;

    public LineStringStyle(String name, Icon icon) {
    }

    protected void paintGeometry(Geometry geometry, Graphics2D graphics,
            Viewport viewport) throws Exception {
        if (geometry instanceof MultiPoint) {
            return;
        }

// MD - removed, to allow MultiPolygons to be styled as well
//        if (geometry instanceof MultiPolygon) {
//            return;
//        }

        if (geometry instanceof GeometryCollection) {
            paintGeometryCollection((GeometryCollection) geometry, graphics,
                    viewport);
            return;
        }
        if (geometry instanceof Polygon) {
            paintPolygon((Polygon) geometry, graphics, viewport);
            return;
        }
        if (!(geometry instanceof LineString)) {
            return;
        }
        LineString lineString = (LineString) geometry;
        if (lineString.getNumPoints() < 2) {
            return;
        }
        paintLineString(lineString, viewport, graphics);
    }

    /**
     * @param lineString has 2 or more points
     */
    protected abstract void paintLineString(LineString lineString,
            Viewport viewport, Graphics2D graphics) throws Exception;

    private void paintGeometryCollection(GeometryCollection gc,
            Graphics2D graphics, Viewport viewport) throws Exception {
        for (int i = 0; i < gc.getNumGeometries(); i++) {
            paintGeometry(gc.getGeometryN(i), graphics, viewport);
        }
    }

    private void paintPolygon(Polygon polygon, Graphics2D graphics,
            Viewport viewport) throws Exception {
        paintGeometry(polygon.getExteriorRing(), graphics, viewport);

        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            paintGeometry(polygon.getInteriorRingN(i), graphics, viewport);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void initialize(Layer layer) {
        stroke = new BasicStroke(layer.getBasicStyle().getLineWidth(),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        lineColorWithAlpha = GUIUtil.alphaColor(layer.getBasicStyle()
                .getLineColor(),
                layer.getBasicStyle().getAlpha());
        fillColorWithAlpha = GUIUtil.alphaColor(layer.getBasicStyle()
                .getFillColor(),
                layer.getBasicStyle().getAlpha());
    }

    public void paint(Feature f, Graphics2D g, Viewport viewport) throws Exception {
        paintGeometry(f.getGeometry(), g, viewport);
    }
}
