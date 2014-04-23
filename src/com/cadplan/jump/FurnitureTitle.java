package com.cadplan.jump;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class FurnitureTitle extends Furniture {

    public String text;
    public Font font;

    public FurnitureTitle(String text, Font font, Rectangle location, boolean show) {
        this.text = text;
        this.font = font;
        this.location = location;
        this.show = show;
        layerNumber = 20;

    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLocation(Rectangle location) {
        this.location = location;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void paint(Graphics g, double scale) {
        Graphics2D g2 = (Graphics2D) g;
        int x = location.x;
        int y = location.y;
        g.setColor(color);
        if (scale > 0.0) {
            g2.scale(scale, scale);
        }
        g2.setFont(font);
        //g2.drawString(text,x,y+location.height);
        if (text == null || text.length() == 0) {
            return;
        }
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle2D bounds = layout.getBounds();
        location.width = (int) bounds.getWidth();
        location.height = (int) bounds.getHeight();

        g.setFont(font);
        //layout.draw(g2, (float)location.x, (float)location.y + (float) location.height);
        g.drawString(text, location.x, location.y + location.height);

        if (scale > 0.0) {
            g2.setColor(boundsColor);
            g2.setStroke(boundsStroke);
            g2.drawRect(location.x, location.y, location.width, location.height);
            g2.setStroke(normalStroke);
            g2.scale(1.0 / scale, 1.0 / scale);
        }
    }
}
