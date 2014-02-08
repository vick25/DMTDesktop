package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import java.util.Collection;

public interface LayerNamePanel extends LayerManagerProxy {

    public Collection getSelectedCategories();

    public Collection selectedNodes(Class c);

    public Layer[] getSelectedLayers();

    /**
     * @return e.g. the first selected editable layer, otherwise the first
     * editable layer, otherwise null
     */
    public Layer chooseEditableLayer();

    public void addListener(LayerNamePanelListener listener);

    public void removeListener(LayerNamePanelListener listener);

    /**
     * The parent window is closing.
     */
    public void dispose();
}
