package com.cadplan.jump;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.renderer.style.VertexStyle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;

public class RendererData {

    public Layer layer;
    public Feature feature;
    public Stroke lineStroke;
    public Color lineColor;
    public Color fillColor;
    public Color baseLineColor;
    public Color baseFillColor;
    public Paint fillPattern;
    public double lineWidth;
    public double fontHeight;
    public double fontAngle;
    public VertexStyle vertexStyle;
    public Point2D.Double firstVertex;
    public Point2D.Double lastVertex;
    public Point2D.Double thisVertex;
    public Point2D.Double previousVertex;
    public int vertexSize;
    public double lableSize;
    public double labelHeight;
    public Font labelFont;
    public Color labelColor;
    public double labelAngle;
    public double lineAngle;
    public double startAngle;
    public double endAngle;
    public String labelAlignment;
    public String labelName;
    public String labelValue;
    public java.util.List styleList;
    public Coordinate[] cline;
    public Geometry polygon;
    public boolean showVertex;
    public double x;
    public double y;
    public float px;
    public float py;
    public int index;

    public RendererData() {
    }
}
