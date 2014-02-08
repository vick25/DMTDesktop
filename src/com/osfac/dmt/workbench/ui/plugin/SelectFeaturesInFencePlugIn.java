package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.FenceLayerFinder;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.vividsolutions.jts.geom.Geometry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SelectFeaturesInFencePlugIn extends AbstractPlugIn {

    public SelectFeaturesInFencePlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getSelectionManager().clear();
        execute(context.getLayerViewPanel(), context.getLayerNamePanel(),
                context.getLayerViewPanel().getFence(), true, false);

        return true;
    }

    public static void execute(
            LayerViewPanel layerViewPanel,
            LayerNamePanel layerNamePanel,
            Geometry fence,
            boolean skipUnselectedLayers,
            boolean mentionModifierHelp) {
        Collection selectedLayers = Arrays.asList(layerNamePanel.getSelectedLayers());
        Map layerToFeaturesInFenceMap = layerViewPanel.visibleLayerToFeaturesInFenceMap(fence);

        for (Iterator i = layerToFeaturesInFenceMap.keySet().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (layer == new FenceLayerFinder(layerViewPanel).getLayer()) {
                continue;
            }

            if (skipUnselectedLayers && !selectedLayers.contains(layer)) {
                continue;
            }

            layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(
                    layer,
                    (Collection) layerToFeaturesInFenceMap.get(layer));
        }

    }

    public static MultiEnableCheck createEnableCheck(
            WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createFenceMustBeDrawnCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                1));
    }
}
