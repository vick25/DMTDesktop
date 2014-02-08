package org.openjump.core.ui.plugin.file;

import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelContext;
import com.osfac.dmt.workbench.ui.renderer.RenderingManager;
import com.osfac.dmt.workbench.ui.renderer.ThreadQueue;
import com.vividsolutions.jts.geom.Envelope;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Renders layers as an Image, which can then be saved to a file or printed.
 */
public class LayerPrinter2 {

    private RenderingManager renderingManager;
    private Graphics2D graphics;
    private LayerViewPanel panel = null;

    public LayerViewPanel getLayerViewPanel() {
        return panel;
    }

    /**
     * @param layers earlier layers will be rendered above later layers
     */
    public BufferedImage print(Collection layers, Envelope envelope, int extentInPixels)
            throws Exception {

        final Throwable[] throwable = new Throwable[]{null};
        panel =
                new LayerViewPanel(
                (!layers.isEmpty()) ? ((Layer) layers.iterator().next()).getLayerManager()
                : new LayerManager(),
                new LayerViewPanelContext() {
                    public void setStatusMessage(String message) {
                    }

                    public void warnUser(String warning) {
                    }

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

        panel.setSize(extentInPixelsX, extentInPixelsY);
        panel.getViewport().zoom(envelope);

        BufferedImage bufferedImage = new BufferedImage(panel.getWidth(),
                panel.getHeight(), BufferedImage.TYPE_INT_RGB); //formerly TYPE_INT_ARGB);
        graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(graphics, extentInPixelsX, extentInPixelsY);

        renderingManager = panel.getRenderingManager();
//				old method
        renderingManager.renderAll();
        ThreadQueue runningThreads = renderingManager.getDefaultRendererThreadQueue();
        while (runningThreads.getRunningThreads() > 0) {
            Thread.sleep(200);
        }
        renderingManager.copyTo(graphics);
//				new method of rendering requires RenderingManager changes
//				panel.getRenderingManager().setRenderingMode(
//						RenderingManager.EXECUTE_ON_EVENT_THREAD); //block the GUI until done
//				renderingManager.renderAll();
//				renderingManager.copyTo(graphics);
//				panel.getRenderingManager().setRenderingMode(RenderingManager.INTERACTIVE);

        if (throwable[0] != null) {
            throw throwable[0] instanceof Exception ? (Exception) throwable[0]
                    : new Exception(throwable[0].getMessage());
        }
        panel.dispose();
        return bufferedImage;
    }

    private void paintBackground(Graphics2D graphics, int extentX, int extentY) {
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, extentX, extentY);
    }
}
