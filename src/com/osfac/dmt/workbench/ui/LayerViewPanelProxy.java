package com.osfac.dmt.workbench.ui;

/**
 * An object that knows how to get the current LayerViewPanel (if more than one
 * exists, as in the case of an MDI application.
 */
public interface LayerViewPanelProxy {

    /**
     * @return null if the LayerViewPanelProxy currently has no associated
     * LayerViewPanel
     */
    public LayerViewPanel getLayerViewPanel();
}
