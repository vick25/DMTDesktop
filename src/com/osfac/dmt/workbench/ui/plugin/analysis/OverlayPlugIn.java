package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.tools.AttributeMapping;
import com.osfac.dmt.tools.OverlayEngine;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedPlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

/**
 *
 * Creates a new layer containing intersections of all pairs of features from
 * two input layers. Splits {@link
 * com.vividsolutions.jts.geom.MultiPolygon Multipolygons} and {@link
 * com.vividsolutions.jts.geom.GeometryCollection
 * GeometryCollections}, and filters out non-Polygons.
 */
public class OverlayPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private String POLYGON_OUTPUT = I18N.get("ui.plugin.analysis.OverlayPlugIn.limit-output-to-polygons-only");
    private String FIRST_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.first-layer");
    private String SECOND_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.second-layer");
    private String TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.transfer-attributes-from-first-layer");
    private String TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.transfer-attributes-from-second-layer");
    private MultiInputDialog dialog;
    private OverlayEngine overlayEngine;

    public OverlayPlugIn() {
    }
    private String categoryName = StandardCategoryNames.RESULT;

    public void setCategoryName(String value) {
        categoryName = value;
    }

    public void initialize(PlugInContext context) throws Exception {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(
                this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_ANALYSIS},
                new JMenuItem(this.getName() + "..."),
                createEnableCheck(context.getWorkbenchContext()));
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(2));
    }

    public boolean execute(PlugInContext context) throws Exception {
        //[sstein, 15.07.2006] placed here again otherwise language settings wont work for i18n 
        POLYGON_OUTPUT = I18N.get("ui.plugin.analysis.OverlayPlugIn.limit-output-to-polygons-only");
        FIRST_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.first-layer");
        SECOND_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.second-layer");
        TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.transfer-attributes-from-first-layer");
        TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER = I18N.get("ui.plugin.analysis.OverlayPlugIn.transfer-attributes-from-second-layer");

        overlayEngine = prompt(context);

        return overlayEngine != null;
    }

    private OverlayEngine prompt(PlugInContext context) {
        //Unlike ValidatePlugIn, here we always call #initDialog because we want
        //to update the layer comboboxes. [Bob Boseko]
        initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return null;
        }

        OverlayEngine e = new OverlayEngine();
        e.setAllowingPolygonsOnly(dialog.getBoolean(POLYGON_OUTPUT));
        e.setSplittingGeometryCollections(dialog.getBoolean(POLYGON_OUTPUT));

        return e;
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(context.getWorkbenchFrame(),
                getName(), true);
        dialog.setSideBarImage(IconLoader.icon("Overlay.gif"));
        dialog.setSideBarDescription(I18N.get("ui.plugin.analysis.OverlayPlugIn.create-new-layer-containing-intersections-of-all-pairs-of-input-features"));
        String fieldName = FIRST_LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, context.getCandidateLayer(0), null, context.getLayerManager());
        String fieldName1 = SECOND_LAYER;
        JComboBox addLayerComboBox1 = dialog.addLayerComboBox(fieldName1, context.getCandidateLayer(1), null, context.getLayerManager());
        dialog.addCheckBox(POLYGON_OUTPUT, true, I18N.get("ui.plugin.analysis.OverlayPlugIn.splits-multipolygons-and-geometry-and-filters-out-non-polygons"));
        dialog.addCheckBox(TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER,
                true);
        dialog.addCheckBox(TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER,
                true);
        GUIUtil.centreOnWindow(dialog);
    }

    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        FeatureCollection a = dialog.getLayer(FIRST_LAYER).getFeatureCollectionWrapper();
        FeatureCollection b = dialog.getLayer(SECOND_LAYER)
                .getFeatureCollectionWrapper();
        FeatureCollection overlay = overlayEngine.overlay(a, b, mapping(a, b),
                monitor);
        context.getLayerManager().addCategory(categoryName);
        context.addLayer(categoryName, I18N.get("ui.plugin.analysis.OverlayPlugIn.overlay"), overlay);
    }

    private AttributeMapping mapping(FeatureCollection a, FeatureCollection b) {
        return new AttributeMapping(dialog.getBoolean(
                TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER) ? a.getFeatureSchema()
                : new FeatureSchema(),
                dialog.getBoolean(TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER)
                ? b.getFeatureSchema() : new FeatureSchema());
    }
}
