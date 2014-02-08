package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;

public class WmsLayerRendererFactory implements RendererFactory<WMSLayer> {

    public Renderer create(WMSLayer layer, LayerViewPanel panel, int maxFeatures) {
        return new WMSLayerRenderer(layer, panel);
    }
}
