package com.osfac.dmt.workbench.model;

import java.awt.Color;

/**
 * Convenience functions for setting and working with Layer Styles. The
 * convention for methods which change a layer style is that the layerChanged
 * event is <i>not</i> fired. This allows further modification of the style by
 * the caller.
 */
public class LayerStyleUtil {

    public LayerStyleUtil() {
    }

    /**
     * Sets the style for a linear layer.
     *
     * @param vertexSize 0 if vertices are not to be shown
     */
    public static void setLinearStyle(Layer lyr, Color lineColor,
            int lineWidth, int vertexSize) {
        lyr.getBasicStyle().setLineColor(lineColor);
        lyr.getBasicStyle().setRenderingFill(false);
        lyr.getBasicStyle().setAlpha(255);
        lyr.getBasicStyle().setLineWidth(lineWidth);
        lyr.setSynchronizingLineColor(false);
        lyr.getVertexStyle().setSize(vertexSize);
        lyr.getVertexStyle().setEnabled(vertexSize > 0);
    }
}
