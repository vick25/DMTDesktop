package com.osfac.dmt.workbench.ui.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Gets an icon from this class' package.
 */
public class IconLoader {

    public static ImageIcon icon(String filename) {
        return new ImageIcon(IconLoader.class.getResource(filename));
    }

    public static BufferedImage image(String filename) {
        try {
            return ImageIO.read(IconLoader.class.getResource(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static Image image(String filename) {
//        return IconLoader.icon(filename).getImage();
//    }
}
