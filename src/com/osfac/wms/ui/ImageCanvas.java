package com.osfac.wms.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 * A simple canvas which draws a given image in the upper left corner of itself.
 *
 * @author Chris Hodgson chodgson@refractions.net
 */
public class ImageCanvas extends Canvas {

    Image img;

    /**
     * Creates a new instance of ImageCanvas.
     */
    public ImageCanvas() {
        super();
    }

    /**
     * Sets the Image to be displayed on the canvas.
     *
     * @param img the Image to be displayed
     */
    public void setImage(Image img) {
        this.img = img;
    }

    /**
     * Paints its Image (if specified) in the upper-left hand corner of itself.
     *
     * @param g the Graphics object on which to paint.
     */
    @Override
    public void paint(Graphics g) {
        if (img != null) {
            g.drawImage(img, 0, 0, this);
        }
    }
}
