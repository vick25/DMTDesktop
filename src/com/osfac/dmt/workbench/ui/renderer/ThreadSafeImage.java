package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;

/**
 * Reading and writing can be done on separate threads.
 */
public class ThreadSafeImage implements Cloneable {

    private Image image = null;
    private Graphics2D graphics = null;
    private LayerViewPanel panel;
    private GraphicsState dummyGraphicsState = new GraphicsState() {
        public void restore(Graphics2D g) {
        }
    };

    public ThreadSafeImage(LayerViewPanel panel) {
        this.panel = panel;
    }

    private Image getImage() {
        if (image == null) {
            image = panel.createBlankPanelImage();
        }

        return image;
    }

    private Graphics2D getGraphics() {
        if (graphics == null) {
            graphics = (Graphics2D) getImage().getGraphics();
            //Not sure if we need antialiasing here. Oh well, doesn't hurt. [Bob Boseko 11/21/2003]
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        return graphics;
    }

    public synchronized void copyTo(Graphics2D destination,
            ImageObserver imageObserver) {
        if (getImage() == null) {
            //Nothing we can do. [Bob Boseko]
            return;
        }

        destination.drawImage(getImage(), 0, 0, imageObserver);
    }

    /**
     * @return false if we cannot generate an off-screen panel from the panel
     * because of various conditions during initialization
     */
    private boolean isPanelReady() {
        if (panel.getSize().equals(new Dimension(0, 0))) {
            return false;
        }

        if (getImage() == null) {
            return false;
        }

        return true;
    }

    public synchronized void draw(Drawer drawer) throws Exception {
        if (!isPanelReady()) {
            return;
        }
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawer.draw(g);
    }

    public synchronized GraphicsState getGraphicsState() {
        if (!isPanelReady()) {
            return dummyGraphicsState;
        }

        return new GraphicsState(getGraphics());
    }

    public synchronized void setGraphicsState(GraphicsState gs) {
        if (!isPanelReady()) {
            return;
        }

        gs.restore(getGraphics());
    }

    /**
     * If the panel is not ready, returns null.
     */
    public Object clone() {
        ThreadSafeImage clone = new ThreadSafeImage(panel);

        if (!clone.isPanelReady()) {
            return null;
        }

        copyTo(clone.getGraphics(), null);

        return clone;
    }

    public interface Drawer {

        public void draw(Graphics2D g) throws Exception;
    }
}
