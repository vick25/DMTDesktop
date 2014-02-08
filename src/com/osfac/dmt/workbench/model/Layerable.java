package com.osfac.dmt.workbench.model;

import com.osfac.dmt.util.Blackboard;

/**
 * A "sheet" of spatial data, overlaid on other "sheets".
 */
public interface Layerable {

    public void setName(String name);

    public String getName();

    public void setVisible(boolean visible);

    public boolean isVisible();

    public LayerManager getLayerManager();

    /**
     * Called by Java2XML
     */
    public void setLayerManager(LayerManager layerManager);

    public Blackboard getBlackboard();

    /**
     * @return the larger units/pixel value
     */
    public Double getMinScale();

    public Layerable setMinScale(Double minScale);

    /**
     * @return the smaller units/pixel value
     */
    public Double getMaxScale();

    public Layerable setMaxScale(Double maxScale);

    public boolean isScaleDependentRenderingEnabled();

    public Layerable setScaleDependentRenderingEnabled(boolean scaleDependentRenderingEnabled);
}
