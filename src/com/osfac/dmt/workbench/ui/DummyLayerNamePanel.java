package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;

/**
 * Implements a minimal LayerNamePanel in a JPanel.
 */
public class DummyLayerNamePanel extends JPanel implements LayerNamePanel {

    private ArrayList selectedCategories = new ArrayList();
    private ArrayList selectedNodes = new ArrayList();
    private Layer[] selectedLayers = new Layer[]{};

    public Collection getSelectedCategories() {
        return selectedCategories;
    }

    public Collection selectedNodes(Class c) {
        return selectedNodes;
    }

    public Layer[] getSelectedLayers() {
        return selectedLayers;
    }

    public Layer chooseEditableLayer() {
        return TreeLayerNamePanel.chooseEditableLayer(this);
    }

    public void addListener(LayerNamePanelListener listener) {
    }

    public void removeListener(LayerNamePanelListener listener) {
    }

    public void dispose() {
    }

    public LayerManager getLayerManager() {
        return null;
    }
}
