package com.cadplan.jump;

import com.osfac.dmt.workbench.ui.renderer.style.VertexStyle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class WKTVertexPanel extends JPanel {

    VertexStyle symbol;

    public WKTVertexPanel(String imageName) {
        symbol = new WKTVertexStyle();
        ((WKTVertexStyle) symbol).setName(imageName);
        double scale = ((WKTVertexStyle) symbol).getScale();
        int size = ((WKTVertexStyle) symbol).wktShape.extent;
        ((WKTVertexStyle) symbol).setSize((int) ((double) size / scale));
    }

    @Override
    public void paint(Graphics g) {
        ((WKTVertexStyle) symbol).render((Graphics2D) g);
    }
}
