package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class ExportImagePlugIn extends AbstractPlugIn {

    protected static boolean java14OrNewer() {
        String version = System.getProperty("java.version");
        if (version.indexOf("1.0") == 0) {
            return false;
        }
        if (version.indexOf("1.1") == 0) {
            return false;
        }
        if (version.indexOf("1.2") == 0) {
            return false;
        }
        if (version.indexOf("1.3") == 0) {
            return false;
        }
        //Allow 1.4, 1.5, 1.6, 2.0, etc. [Bob Boseko]
        return true;
    }

    protected BufferedImage image(LayerViewPanel layerViewPanel) {
        //Don't use TYPE_INT_ARGB, which makes JPEGs pinkish (presumably because
        //JPEGs don't support transparency [Bob Boseko 11/6/2003]
        BufferedImage image = new BufferedImage(layerViewPanel.getWidth(),
                layerViewPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        layerViewPanel.paintComponent((Graphics2D) image.getGraphics());
        return image;
    }
}