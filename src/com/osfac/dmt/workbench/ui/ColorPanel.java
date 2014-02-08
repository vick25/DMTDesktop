package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.ui.renderer.style.BasicStyle;
import java.awt.*;
import javax.swing.JPanel;

/**
 * Displays a colour.
 */
public class ColorPanel extends JPanel {

    private Color fillColor = Color.red;
    private Color lineColor = Color.green;
    private int margin = 0;

    public ColorPanel() {
        setBackground(Color.white);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        //Before I simply set the ColorPanel's background colour. But I found this
        //caused weird paint effects e.g. a second copy of the panel appearing
        //at the top left corner of the rendered image. [Bob Boseko].
        //<<TODO:DESIGN>> Use the GraphicsState class [Bob Boseko]
        Color originalColor = g.getColor();
        g.setColor(getBackground());
        ((Graphics2D) g).setStroke(fillStroke);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(fillColor);
        if (fillPattern != null) {
            ((Graphics2D) g).setPaint(fillPattern);
        }
        g.fillRect(margin, margin, getWidth() - 1 - margin - margin,
                getHeight() - 1 - margin - margin);

        if (lineColor != null) {
            g.setColor(lineColor);
            ((Graphics2D) g).setStroke(lineStroke);

            //-1 to ensure the rectangle doesn't extend past the panel [Bob Boseko]
            g.drawRect(margin, margin, getWidth() - 1 - margin - margin,
                    getHeight() - 1 - margin - margin);
        }

        //<<TODO:DESIGN>> Put the next line in a finally block [Bob Boseko]
        g.setColor(originalColor);
    }

    /**
     * Workaround for bug 4238829 in the Java bug database
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        validate();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * @param lineColor the new line colour, or null to not draw the line
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getLineColor() {
        return lineColor;
    }
    private BasicStroke fillStroke = new BasicStroke(1);
    private Paint fillPattern = null;
    private int lineWidth = 1;
    private BasicStroke lineStroke = new BasicStroke(lineWidth);

    public void setLineWidth(int lineWidth) {
        lineStroke = new BasicStroke(lineWidth);
        this.lineWidth = lineWidth;
    }

    public void setStyle(BasicStyle style) {
        //if (style.isRenderingLinePattern())
        setLineStroke(style.getLineStroke());
        if (style.isRenderingFillPattern()) {
            fillPattern = style.getFillPattern();
        } else {
            fillPattern = null;
        }
    }

    public void setLineStroke(BasicStroke stroke) {
        float width = Math.min(3, stroke.getLineWidth());
        lineStroke = new BasicStroke(width,
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,
                stroke.getDashArray(), 0);
    }

    public int getLineWidth() {
        return lineWidth;
    }
}
