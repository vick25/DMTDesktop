package com.osfac.dmt.workbench.ui.renderer;

import java.awt.Graphics2D;

public interface Renderer {

    public abstract void clearImageCache();

    public abstract boolean isRendering();

    /**
     * @return contentID which identifies this Renderer by what it draws
     */
    public abstract Object getContentID();

    public abstract void copyTo(Graphics2D graphics);

    /**
     * @return null if no rendering work needs to be done
     */
    public abstract Runnable createRunnable();

    public abstract void cancel();

    public static interface Factory {

        public Renderer create();
    }

    //[sstein: 20.01.2006] from Ole for RenderingManager changes
    // for not hardwired renderers and to including pirol image layers
    /**
     * @deprecated Replaced by {@link RendererFactory}
     */
    public static interface ContentDependendFactory {

        public Renderer create(Object contentID);
    }
}