package com.cadplan.jump;

import java.awt.*;
import javax.swing.*;

public class Furniture extends JComponent {

    protected Rectangle location = new Rectangle(0, 0, 0, 0);
    protected boolean show = false;
    protected Color color = Color.BLACK;
    protected Color bcolor = null;
    protected boolean setBackColor = false;
    public static Stroke boundsStroke = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{1.0f, 3.0f}, 0.0f);
    public static Stroke normalStroke = new BasicStroke();
    public static Color boundsColor = Color.BLUE;
    protected int layerNumber = 0;
    protected boolean validItem = true;

    public boolean inside(int x, int y, double scale) {
        boolean inside = false;
        if (show) {
            inside = true;
            if (x < location.x * scale) {
                inside = false;
            }
            if (x > (location.x + location.width) * scale) {
                inside = false;
            }
            if (y < location.y * scale) {
                inside = false;
            }
            if (y > (location.y + location.height) * scale) {
                inside = false;
            }
            //JOptionPane.showMessageDialog(null,"x="+x+" y="+y+"  scale="+scale+"  location:"+location+"  inside="+inside);
        }
        return inside;
    }
}
