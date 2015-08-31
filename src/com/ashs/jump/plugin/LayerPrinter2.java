package com.ashs.jump.plugin;

import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelContext;
import com.osfac.dmt.workbench.ui.renderer.RenderingManager;
import com.osfac.dmt.workbench.ui.renderer.ThreadQueue;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Renders layers as an Image, which can then be saved to a file or printed.
 */
public class LayerPrinter2 {

    private boolean debug = false;

    /**
     * @param layers earlier layers will be rendered above later layers
     */
    public BufferedImage print(Collection layers, Envelope envelope, int extentInPixels) throws Exception {
        Assert.isTrue(!layers.isEmpty());

        final Throwable[] throwable = new Throwable[]{null};
        LayerViewPanel panel
                = new LayerViewPanel(((Layer) layers.iterator().next()).getLayerManager(),
                        new LayerViewPanelContext() {
                            @Override
                            public void setStatusMessage(String message) {
                            }

                            @Override
                            public void warnUser(String warning) {
                            }

                            @Override
                            public void handleThrowable(Throwable t) {
                                throwable[0] = t;
                            }
                        });
        int extentInPixelsX;
        int extentInPixelsY;
        double width = envelope.getWidth();
        double height = envelope.getHeight();

        if (width > height) {
            extentInPixelsX = extentInPixels;
            extentInPixelsY = (int) Math.round(height / width * extentInPixels);
        } else {
            extentInPixelsY = extentInPixels;
            extentInPixelsX = (int) Math.round(width / height * extentInPixels);
        }

//        extentInPixelsX = 2*extentInPixelsX;
//        extentInPixelsY = 2*extentInPixelsY;
        if (debug) {
            System.out.println("LayerPrinter2: extents: " + extentInPixelsX + "," + extentInPixelsY);
        }
        panel.setSize(extentInPixelsX, extentInPixelsY);
        panel.getViewport().zoom(envelope);

        BufferedImage bufferedImage = new BufferedImage(panel.getWidth(),
                panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint( // LDB Added
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(graphics, extentInPixels);

        RenderingManager renderingManager = panel.getRenderingManager();
        renderingManager.renderAll();
        ThreadQueue runningThreads = renderingManager.getDefaultRendererThreadQueue();
        while (runningThreads.getRunningThreads() > 0) {
            Thread.sleep(200);
        }
        renderingManager.copyTo(graphics);

        if (throwable[0] != null) {
            throw throwable[0] instanceof Exception ? (Exception) throwable[0]
                    : new Exception(throwable[0].getMessage());
        }
        panel.dispose();
        return bufferedImage;
    }

    private void paintBackground(Graphics2D graphics, int extent) {
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, extent, extent);
    }
}
