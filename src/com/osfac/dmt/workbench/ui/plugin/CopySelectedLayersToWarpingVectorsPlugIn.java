package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.warp.Triangulator;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerViewPanelContext;
import com.osfac.dmt.workbench.ui.plugin.clipboard.PasteItemsPlugIn;
import com.osfac.dmt.workbench.ui.warp.WarpingVectorLayerFinder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComponent;

public class CopySelectedLayersToWarpingVectorsPlugIn extends AbstractPlugIn {

    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createTaskWindowMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1))
                .add(new EnableCheck() {
            public String check(JComponent component) {
                return workbenchContext.getLayerNamePanel().getSelectedLayers().length == 1
                        && workbenchContext.getLayerNamePanel().getSelectedLayers()[0] == new WarpingVectorLayerFinder(workbenchContext).getLayer()
                        ? I18N.get("ui.plugin.CopySelectedLayersToWarpingVectorsPlugIn.a-layer-other-than") + "'" + new WarpingVectorLayerFinder(workbenchContext).getLayerName() + "' " + I18N.get("ui.plugin.CopySelectedLayersToWarpingVectorsPlugIn.must-be-selected") : null;
            }
        });
    }

    public static Collection removeNonVectorFeaturesAndWarn(Collection features, LayerViewPanelContext context) {
        ArrayList newFeatures = new ArrayList(features);
        Collection nonVectorFeatures = nonVectorFeatures(newFeatures);
        if (!nonVectorFeatures.isEmpty()) {
            context.warnUser(I18N.get("ui.plugin.CopySelectedLayersToWarpingVectorsPlugIn.skipped") + " " + nonVectorFeatures.size() + " non-two-point-linestring" + StringUtil.s(nonVectorFeatures.size()) + " e.g. " + ((Feature) nonVectorFeatures.iterator().next()).getGeometry().toText());
            newFeatures.removeAll(nonVectorFeatures);
        }
        return newFeatures;
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        Collection newWarpingVectors = new ArrayList();
        Layer[] selectedLayers = context.getSelectedLayers();
        for (int i = 0; i < selectedLayers.length; i++) {
            if (selectedLayers[i] == new WarpingVectorLayerFinder(context).getLayer()) {
                continue;
            }
            newWarpingVectors.addAll(selectedLayers[i].getFeatureCollectionWrapper().getFeatures());
        }
        newWarpingVectors = removeNonVectorFeaturesAndWarn(newWarpingVectors, context.getWorkbenchFrame());
        final Collection finalNewWarpingVectors = newWarpingVectors;
        final WarpingVectorLayerFinder finder = new WarpingVectorLayerFinder(context);
        execute(Layer.addUndo(finder.getLayerName(), context, new UndoableCommand(getName()) {
            public void execute() {
                if (finder.getLayer() == null) {
                    finder.createLayer();
                }
                finder.getLayer().getFeatureCollectionWrapper().addAll(
                        PasteItemsPlugIn.conform(
                        finalNewWarpingVectors,
                        finder.getLayer().getFeatureCollectionWrapper().getFeatureSchema()));
            }

            public void unexecute() {
            }
        }), context);
        return true;
    }

    private static Collection nonVectorFeatures(Collection candidates) {
        ArrayList nonVectorFeatures = new ArrayList();
        for (Iterator i = candidates.iterator(); i.hasNext();) {
            Feature candidate = (Feature) i.next();
            if (!Triangulator.vector(candidate.getGeometry())) {
                nonVectorFeatures.add(candidate);
            }
        }
        return nonVectorFeatures;
    }
}
