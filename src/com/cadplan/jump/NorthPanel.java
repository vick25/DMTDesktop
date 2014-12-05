package com.cadplan.jump;

import java.awt.*;
import javax.swing.*;

public class NorthPanel extends JPanel {

    FurnitureNorth north;

    public NorthPanel(FurnitureNorth north) {
        this.north = north;
    }

    public void paint(Graphics g) {
        north.sf = 1.0;
        int xsize = getSize().width;
        int ysize = getSize().height;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, xsize, ysize);

        north.drawNorth(g, 10, 30, 1);
        north.drawNorth(g, 90, 30, 2);
        north.drawNorth(g, 160, 30, 3);
        north.drawNorth(g, 200, 30, 4);
        north.drawNorth(g, 290, 30, 5);
        north.drawNorth(g, 360, 30, 6);
    }
}
