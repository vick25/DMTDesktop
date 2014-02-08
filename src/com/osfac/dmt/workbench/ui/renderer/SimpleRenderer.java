package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Graphics2D;

/**
 * Advantage over ImageCachingRenderer: no cached image (typically 1 MB each).
 * Disadvantage: must redraw image each time (slower). Classic tradeoff between
 * space and time.
 */
public abstract class SimpleRenderer implements Renderer {

    protected volatile boolean cancelled = false;

    public SimpleRenderer(Object contentID, LayerViewPanel panel) {
        this.contentID = contentID;
        this.panel = panel;
    }
    private Object contentID;
    protected LayerViewPanel panel;

    protected abstract void paint(Graphics2D g) throws Exception;

    public void clearImageCache() {
    }

    public boolean isRendering() {
        return false;
    }

    public Object getContentID() {
        return contentID;
    }

    public void copyTo(Graphics2D graphics) {
        try {
            cancelled = false;
            paint(graphics);
        } catch (Throwable t) {
            panel.getContext().warnUser(WorkbenchFrame.toMessage(t));
            t.printStackTrace(System.err);
        }
    }

    public Runnable createRunnable() {
        return null;
    }

    public void cancel() {
        cancelled = true;
    }
}