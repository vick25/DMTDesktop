package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.feature.FeatureUtil;
import com.osfac.dmt.task.TaskMonitor;
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
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.union.UnaryUnionOp;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

public class UnionPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private String LAYER = I18N.get("ui.plugin.analysis.UnionPlugIn.layer");
    private String SELECTED_ONLY = I18N.get("ui.plugin.analysis.UnionPlugIn.selected-features-only");
    private boolean useSelected = false;
    private MultiInputDialog dialog;
    private JComboBox addLayerComboBox;

    public UnionPlugIn() {
    }

    @Override
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
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        //[sstein, 16.07.2006] put here again to load correct language
        //[mmichaud 2007-05-20] move to UnionPlugIn constructor to load the string only once
        //LAYER = I18N.get("ui.plugin.analysis.UnionPlugIn.layer");
        //Unlike ValidatePlugIn, here we always call #initDialog because we want
        //to update the layer comboboxes. [Bob Boseko]
        int n = context.getLayerViewPanel().getSelectionManager()
                .getFeaturesWithSelectedItems().size();
        useSelected = (n > 0);
        initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        return true;
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(context.getWorkbenchFrame(), I18N.get("ui.plugin.analysis.UnionPlugIn.union"), true);

        //dialog.setSideBarImage(IconLoader.icon("Overlay.gif"));
        if (useSelected) {
            dialog.setSideBarDescription(
                    I18N.get("ui.plugin.analysis.UnionPlugIn.creates-a-new-layer-containing-the-union-of-selected-features-in-the-input-layer"));
        } else {
            dialog.setSideBarDescription(
                    I18N.get("ui.plugin.analysis.UnionPlugIn.creates-a-new-layer-containing-the-union-of-all-the-features-in-the-input-layer"));
        }
        String fieldName = LAYER;
        if (useSelected) {
            dialog.addLabel(SELECTED_ONLY);
        } else {
            addLayerComboBox = dialog.addLayerComboBox(fieldName, context.getCandidateLayer(0), null, context.getLayerManager());
        }
        GUIUtil.centreOnWindow(dialog);
    }

    @Override
    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        FeatureCollection a;
        Collection inputC;
        if (useSelected) {
            inputC = context.getLayerViewPanel()
                    .getSelectionManager()
                    .getFeaturesWithSelectedItems();
            FeatureSchema featureSchema = new FeatureSchema();
            featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
            a = new FeatureDataset(inputC, featureSchema);
        } else {
            a = dialog.getLayer(LAYER).getFeatureCollectionWrapper();
        }

        Collection geoms = FeatureUtil.toGeometries(a.getFeatures());
        Geometry g = UnaryUnionOp.union(geoms);
        geoms.clear();
        geoms.add(g);
        FeatureCollection fc = FeatureDatasetFactory.createFromGeometry(geoms);

        context.getLayerManager().addCategory(StandardCategoryNames.RESULT);
        context.addLayer(StandardCategoryNames.RESULT, I18N.get("ui.plugin.analysis.UnionPlugIn.union"), fc);
    }
}
