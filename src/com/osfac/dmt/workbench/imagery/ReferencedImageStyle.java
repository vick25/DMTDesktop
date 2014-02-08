package com.osfac.dmt.workbench.imagery;

import com.osfac.dmt.DMTException;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.renderer.style.Style;

/**
 * A JUMP style that will paint images
 */
public class ReferencedImageStyle implements Style {

    boolean enabled = true;

    public ReferencedImageStyle() {
    }
    private ImageryLayerDataset imageryLayerDataset = new ImageryLayerDataset();

    public void paint(Feature f, java.awt.Graphics2D g, Viewport viewport)
            throws Exception {
        if (imageryLayerDataset.referencedImage(f) == null) {
            return;
        }
        try {
            imageryLayerDataset.referencedImage(f).paint(f, g, viewport);
        } catch (DMTException e) {
            f.setAttribute(ImageryLayerDataset.ATTR_ERROR, e.getMessage());
            viewport.getPanel().getContext().setStatusMessage("Error reading image.");
        }
    }

    public void initialize(Layer l) {
    }

    public Object clone() {
        return new ReferencedImageStyle();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ImageryLayerDataset getImageryLayerDataset() {
        return imageryLayerDataset;
    }
}