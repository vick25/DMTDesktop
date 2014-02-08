package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

public class WMSLayerRenderer extends ImageCachingRenderer {

    public WMSLayerRenderer(WMSLayer layer, LayerViewPanel panel) {
        super(layer, panel);
    }

    public ThreadSafeImage getImage() {
        if (!getLayer().isVisible()) {
            return null;
        }

        return super.getImage();
    }

    public Runnable createRunnable() {
        if (!LayerRenderer.render(getLayer(), panel)) {
            return null;
        }
        return super.createRunnable();
    }

    public void copyTo(Graphics2D graphics) {
        if (!LayerRenderer.render(getLayer(), panel)) {
            return;
        }
        super.copyTo(graphics);
    }

    private WMSLayer getLayer() {
        return (WMSLayer) getContentID();
    }

    protected void renderHook(ThreadSafeImage image) throws Exception {
        if (!getLayer().isVisible()) {
            return;
        }

        //Create the image outside the synchronized call to #draw, because it
        // takes
        //a few seconds, and we don't want to block repaints. [Bob Boseko]
        final Image sourceImage = getLayer().createImage(panel);

        //Drawing can take a long time. If the renderer is cancelled during
        // this
        //time, don't draw when the request returns. [Bob Boseko]
        if (cancelled) {
            return;
        }

        image.draw(new ThreadSafeImage.Drawer() {
            public void draw(Graphics2D g) throws Exception {
                //Not sure what the best rule is; SRC_OVER seems to work. [Jon
                // Aquino]
                g.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, getLayer().getAlpha() / 255f));
                g.drawImage(sourceImage, 0, 0, null);
            }
        });
    }
}