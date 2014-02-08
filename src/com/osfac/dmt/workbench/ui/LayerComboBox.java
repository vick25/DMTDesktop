package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn;
import java.awt.Color;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * Stays in sync with the LayerManager.
 */
public class LayerComboBox extends JComboBox {
    //For the layer name, use " " rather than "" so that the renderer is sized normally. 
    //See comments in #updateModel. [Bob Boseko]

    private static final Layer DUMMY_LAYER = new Layer(" ",
            new Color(0, 0, 0, 0),
            AddNewLayerPlugIn.createBlankFeatureCollection(), new LayerManager());
    private LayerManager layerManager = new LayerManager();

    public Object getSelectedItem() {
        return (super.getSelectedItem() != DUMMY_LAYER)
                ? super.getSelectedItem() : null;
    }
    private LayerListener listener = new LayerListener() {
        public void featuresChanged(FeatureEvent e) {
        }

        public void layerChanged(LayerEvent e) {
            updateModel();
        }

        public void categoryChanged(CategoryEvent e) {
        }
    };

    public LayerComboBox() {
        super(new DefaultComboBoxModel());
        setRenderer(new LayerNameRenderer());
    }

    private DefaultComboBoxModel getMyModel() {
        return (DefaultComboBoxModel) getModel();
    }

    private void updateModel() {
        Layer selectedLayer = getSelectedLayer();
        getMyModel().removeAllElements();

        for (Iterator i = layerManager.iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            getMyModel().addElement(layer);
        }

        if (getMyModel().getSize() == 0) {
            //Don't leave the combobox empty -- an empty combobox is rendered a few 
            //pixels larger than a non-empty one. If it is in a packed frame's GridBagLayout,
            //the GridBagLayout will collapse all components to minimum size
            //(see Java bug 4247013). [Bob Boseko] 
            getMyModel().addElement(DUMMY_LAYER);
        }

        if (-1 != getMyModel().getIndexOf(selectedLayer)) {
            getMyModel().setSelectedItem(selectedLayer);
        }
    }

    public Layer getSelectedLayer() {
        return (Layer) getSelectedItem();
    }

    public void setLayerManager(LayerManager layerManager) {
        this.layerManager.removeLayerListener(listener);
        this.layerManager = layerManager;
        this.layerManager.addLayerListener(listener);
        updateModel();
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    public void setSelectedLayer(Layer candidateLayer) {
        setSelectedItem(candidateLayer);
    }
}
