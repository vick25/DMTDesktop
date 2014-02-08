package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.FeatureEventType;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.model.LayerManagerProxy;

/**
 * Displays and stays in sync with a single Layer.
 */
public class OneLayerAttributeTab extends AttributeTab {

    public OneLayerAttributeTab(WorkbenchContext context, TaskFrame taskFrame,
            LayerManagerProxy layerManagerProxy) {
        super(new InfoModel(), context, taskFrame, layerManagerProxy, true);
        context.getLayerManager().addLayerListener(new LayerListener() {
            public void featuresChanged(FeatureEvent e) {
                if (getLayerTableModel() == null) {
                    //Get here after attribute viewer window is closed [Bob Boseko]
                    return;
                }
                if ((e.getLayer() == getLayerTableModel().getLayer())
                        && (e.getType() == FeatureEventType.ADDED)) {
                    //DELETED events are already handled in LayerTableModel
                    getLayerTableModel().addAll(e.getFeatures());
                }
            }

            public void layerChanged(LayerEvent e) {
            }

            public void categoryChanged(CategoryEvent e) {
            }
        });
    }

    public OneLayerAttributeTab setLayer(Layer layer) {
        if (!getModel().getLayers().isEmpty()) {
            getModel().remove(getLayer());
        }

        //InfoModel#add must be called after the AttributeTab is created; otherwise
        //layer won't be added to the Attribute Tab -- the AttributeTab listens for
        //the event fired by InfoModel#add. [Bob Boseko]
        getModel().add(layer, layer.getFeatureCollectionWrapper().getFeatures());

        return this;
    }

    public Layer getLayer() {
        //null LayerTableModel if for example the user has just removed the layer
        //from the LayerManager [Bob Boseko]
        return (getLayerTableModel() != null) ? getLayerTableModel().getLayer()
                : null;
    }

    public LayerTableModel getLayerTableModel() {
        return (!getModel().getLayerTableModels().isEmpty())
                ? (LayerTableModel) getModel().getLayerTableModels().iterator().next()
                : null;
    }
}
