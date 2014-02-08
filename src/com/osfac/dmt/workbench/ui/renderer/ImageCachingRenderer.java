package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Graphics2D;
import javax.swing.SwingUtilities;

public abstract class ImageCachingRenderer implements Renderer {

    protected volatile boolean cancelled = false;
    private Object contentID;
    protected volatile ThreadSafeImage image = null;
    protected LayerViewPanel panel;
    protected volatile boolean rendering = false;

    public ImageCachingRenderer(Object contentID, LayerViewPanel panel) {
        this.contentID = contentID;
        this.panel = panel;
    }

    public void clearImageCache() {
        image = null;
    }

    public boolean isRendering() {
        return rendering;
    }

    public Object getContentID() {
        return contentID;
    }

    protected ThreadSafeImage getImage() {
        return image;
    }

    public void copyTo(Graphics2D graphics) {
        //Some subclasses override #getImage [Bob Boseko]
        if (getImage() == null) {
            return;
        }
        getImage().copyTo(graphics, null);
    }

    public Runnable createRunnable() {
        if (image != null) {
            return null;
        }
        //Rendering starts as soon as the #createRunnable request is made,
        //to get the animated clock icons going. [Bob Boseko]
        rendering = true;
        cancelled = false;
        return new Runnable() {
            public void run() {
                try {
                    if (cancelled) {
                        //This short-circuit exit made a big speed increase
                        // (21 March 2003). [Bob Boseko]
                        return;
                    }
                    image = new ThreadSafeImage(panel);
                    try {
                        renderHook(image);
                    } catch (Throwable t) {
                        panel.getContext()
                                .warnUser(WorkbenchFrame.toMessage(t));
                        t.printStackTrace(System.err);
                        return;
                    }
                    //Don't wait for the RenderingManager's 1-second
                    // repaint-timer. [Bob Boseko]
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            panel.superRepaint();
                        }
                    });
                } finally {
                    rendering = false;
                }
            }
        };
    }

    protected abstract void renderHook(ThreadSafeImage image) throws Exception;

    public void cancel() {
        cancelled = true;
    }
}
