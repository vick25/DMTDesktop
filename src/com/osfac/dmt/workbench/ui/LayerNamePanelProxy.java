package com.osfac.dmt.workbench.ui;

/**
 * Defines a proxy for a {@link LayerNamePanel}.
 */
public interface LayerNamePanelProxy {

    /**
     * @return null if the LayerViewPanelProxy currently has no associated
     * LayerViewPanel
     */
    public LayerNamePanel getLayerNamePanel();
}
