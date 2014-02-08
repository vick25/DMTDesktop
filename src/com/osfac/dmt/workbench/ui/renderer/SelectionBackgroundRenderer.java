package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SelectionBackgroundRenderer extends AbstractSelectionRenderer {

    public final static String CONTENT_ID = "SELECTION_BACKGROUND";

    public SelectionBackgroundRenderer(LayerViewPanel panel) {
        super(CONTENT_ID, panel, Color.yellow, false, true);
    }

    protected Map<Feature, List<Geometry>> featureToSelectedItemsMap(Layer layer) {
        //Use Set because PartSelection and LineStringSelection may share features. [Bob Boseko]
        Set featuresNeedingBackground = new HashSet();
        featuresNeedingBackground.addAll(panel.getSelectionManager().getPartSelection().getFeaturesWithSelectedItems(layer));
        featuresNeedingBackground.addAll(panel.getSelectionManager().getLineStringSelection().getFeaturesWithSelectedItems(layer));
        //Don't need to remove FeatureSelection features, because if a feature were
        //selected, its parts and linestrings would not be selected. [Bob Boseko]
        Map<Feature, List<Geometry>> map = new HashMap<>();
        for (Iterator i = featuresNeedingBackground.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            List<Geometry> list = map.get(feature);
            if (list == null) {
                list = new ArrayList<>(1);
                map.put(feature, list);
            }
            list.add(feature.getGeometry());
        }
        return map;
    }
}
