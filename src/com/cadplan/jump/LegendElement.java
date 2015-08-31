package com.cadplan.jump;

import com.osfac.dmt.workbench.ui.renderer.style.VertexStyle;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Collection;

public class LegendElement {

    public boolean include = false;
    public String name;
    public Color lineColor;
    public Color fillColor;
    public Stroke lineStroke;
    public Paint fillPattern;
    public Collection themeStyles;
    public Collection keyValues;
    public VertexStyle vertexStyle;
    public boolean showFill = true;
    public boolean showLine = true;

    public LegendElement(boolean include, String name, Color lineColor, Stroke lineStroke,
            Color fillColor, Paint fillPattern, Collection themeStyles, Collection keyValues, VertexStyle vertexStyle,
            boolean showLine, boolean showFill) {
        this.include = include;
        this.name = name;
        this.lineColor = lineColor;
        this.lineStroke = lineStroke;
        this.fillColor = fillColor;
        this.fillPattern = fillPattern;
        this.themeStyles = themeStyles;
        this.keyValues = keyValues;
        this.vertexStyle = vertexStyle;
        this.showLine = showLine;
        this.showFill = showFill;
    }

    @Override
    public String toString() {
        return name + "[" + lineColor + lineStroke + ":" + fillColor + ":" + fillPattern + "]<" + include + ">";
    }
}
