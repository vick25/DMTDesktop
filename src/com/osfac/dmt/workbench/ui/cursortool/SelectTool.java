package com.osfac.dmt.workbench.ui.cursortool;

import com.vividsolutions.jts.geom.Geometry;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.geom.EnvelopeUtil;
import com.osfac.dmt.workbench.model.FenceLayerFinder;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.AbstractSelection;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;

public abstract class SelectTool extends DragTool {

    public Cursor getCursor() {
        return Cursor.getDefaultCursor();
    }

    public void mouseClicked(MouseEvent e) {
        try {
            super.mouseClicked(e);
            setViewSource(e.getPoint());
            setViewDestination(e.getPoint());
            fireGestureFinished();
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();

        if (!wasShiftPressed()) {
            getPanel().getSelectionManager().clear();
        }

        Map layerToFeaturesInFenceMap =
                getPanel().visibleLayerToFeaturesInFenceMap(
                EnvelopeUtil.toGeometry(getBoxInModelCoordinates()));

        Collection layers = layerToFeaturesInFenceMap.keySet();
        if (selectedLayersOnly()) {
            layers.retainAll(Arrays.asList(getTaskFrame().getLayerNamePanel().getSelectedLayers()));
        }
        for (Iterator i = layers.iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (layer.getName().equals(FenceLayerFinder.LAYER_NAME)) {
                continue;
            }

            //Disable panel updates -- we'll manually repaint the selection and
            //fire the selection-changed event. [Bob Boseko]
            boolean originalPanelUpdatesEnabled =
                    getPanel().getSelectionManager().arePanelUpdatesEnabled();
            getPanel().getSelectionManager().setPanelUpdatesEnabled(false);
            try {
                Map<Feature, List<Geometry>> featureToItemsToSelectMap =
                        featureToItemsInFenceMap(
                        (Collection) layerToFeaturesInFenceMap.get(layer),
                        layer,
                        false);
                Map<Feature, List<Geometry>> featureToItemsToUnselectMap =
                        featureToItemsInFenceMap(
                        (Collection) layerToFeaturesInFenceMap.get(layer),
                        layer,
                        true);
                selection.selectItems(layer, featureToItemsToSelectMap);
                if (wasShiftPressed()) {
                    selection.unselectItems(layer, featureToItemsToUnselectMap);
                }
            } finally {
                getPanel().getSelectionManager().setPanelUpdatesEnabled(
                        originalPanelUpdatesEnabled);
            }
        }

        getPanel().getSelectionManager().updatePanel();
    }

    protected boolean selectedLayersOnly() {
        return wasControlPressed();
    }
    private String rendererID;

    protected SelectTool(String rendererID) {
        this.rendererID = rendererID;
    }
    protected AbstractSelection selection;

    /**
     * @param selected whether to return selected items or deselected items
     */
    private Map<Feature, List<Geometry>> featureToItemsInFenceMap(
            Collection features, Layer layer, boolean selected)
            throws NoninvertibleTransformException {
        Map<Feature, List<Geometry>> featureToSelectedItemsMap =
                selection.getFeatureToSelectedItemCollectionMap(layer);
        Map<Feature, List<Geometry>> featureToItemsInFenceMap =
                new HashMap<>();
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            List<Geometry> selectedItems = featureToSelectedItemsMap.get(feature);
            if (selectedItems == null) {
                selectedItems = Collections.EMPTY_LIST;
            }
            List<Geometry> itemsToReturn = itemsInFence(feature);
            if (selected) {
                itemsToReturn.retainAll(selectedItems);
            } else {
                itemsToReturn.removeAll(selectedItems);
            }
            featureToItemsInFenceMap.put(feature, itemsToReturn);
        }
        return featureToItemsInFenceMap;
    }

    private List<Geometry> itemsInFence(Feature feature) throws NoninvertibleTransformException {
        List<Geometry> itemsInFence = new ArrayList<>(1);
        Geometry fence = EnvelopeUtil.toGeometry(getBoxInModelCoordinates());
        for (Geometry selectedItem : selection.items(feature.getGeometry())) {
            if (LayerViewPanel.intersects(selectedItem, fence)) {
                itemsInFence.add(selectedItem);
            }
        }
        return itemsInFence;
    }

    public Icon getIcon() {
        return null;
    }
}
