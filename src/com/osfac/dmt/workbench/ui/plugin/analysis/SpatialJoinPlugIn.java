package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedPlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.GenericNames;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * Queries a layer by a spatial predicate.
 */
public class SpatialJoinPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private Layer srcLayerA;
    private Layer srcLayerB;
    private JTextField paramField;
    private Collection functionNames;
    private MultiInputDialog dialog;
    private String funcNameToRun;
    private GeometryPredicate functionToRun = null;
    private boolean exceptionThrown = false;
    private double[] params = new double[2];

    public SpatialJoinPlugIn() {
        functionNames = GeometryPredicate.getNames();
    }
    private String categoryName = StandardCategoryNames.RESULT;

    public String getName() {
        //exchanged plugin with SIGLE plugin
        return I18N.get("ui.plugin.analysis.SpatialJoinPlugIn.Spatial-Join");
        //return I18N.get("org.openjump.sigle.plugin.SpatialJoinPlugIn.Transfer-Attributes");
    }

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
        dialog = new MultiInputDialog(context.getWorkbenchFrame(), getName(), true);
        setDialogValues(dialog, context);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()) {
            return false;
        }
        getDialogValues(dialog);
        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        monitor.allowCancellationRequests();

        // input-proofing
        if (functionToRun == null) {
            return;
        }
        if (srcLayerA == null) {
            return;
        }
        if (srcLayerB == null) {
            return;
        }

        monitor.report(
                I18N.get("ui.plugin.analysis.SpatialJoinPlugIn.Executing-join")
                + " " + functionToRun.getName() + "...");

        FeatureCollection srcAFC = srcLayerA.getFeatureCollectionWrapper();
        FeatureCollection srcBFC = srcLayerB.getFeatureCollectionWrapper();
        //[sstein 28.Mar.2008] reversed order of input
        //(to be able to read from top to down the spatial relations) 
        SpatialJoinExecuter executer = new SpatialJoinExecuter(srcBFC, srcAFC);
        FeatureCollection resultFC = executer.getResultFC();
        executer.execute(monitor, functionToRun, params, resultFC);

        if (monitor.isCancelRequested()) {
            return;
        }

        String outputLayerName = I18N.get("ui.plugin.analysis.SpatialJoinPlugIn.Join") + "-" + funcNameToRun;
        context.getLayerManager().addCategory(categoryName);
        context.addLayer(categoryName, outputLayerName, resultFC);

        if (exceptionThrown) {
            context.getWorkbenchFrame()
                    .warnUser("Errors found while executing query");
        }
    }
    private final static String LAYER_A = GenericNames.LAYER_A + " (" + GenericNames.TARGET_LAYER + ")";
    private final static String LAYER_B = GenericNames.LAYER_B + " (" + GenericNames.SOURCE_LAYER + ")";
    private final static String PREDICATE = GenericNames.RELATION;
    private final static String PARAM = GenericNames.PARAMETER;

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {

        //dialog.setSideBarImage(new ImageIcon(getClass().getResource("DiffSegments.png")));
        //[sstein 31March2008] replaced sidebar description by better description use in SIGLE plugin  
        /*
         dialog.setSideBarDescription(
         I18N.get("ui.plugin.analysis.SpatialJoinPlugIn.Joins-two-layers-on-a-given-spatial-relationship")
         + " (" + I18N.get("ui.plugin.analysis.SpatialJoinPlugIn.example") +")");
         */
        dialog.setSideBarDescription(
                I18N.get("org.openjump.sigle.plugin.SpatialJoinPlugIn.Transfers-the-attributes-of-Layer-B-to-Layer-A-using-a-spatial-criterion"));

        //Set initial layer values to the first and second layers in the layer list.
        //In #initialize we've already checked that the number of layers >= 1. [Bob Boseko]
        Layer initLayer1 = (srcLayerA == null) ? context.getCandidateLayer(0) : srcLayerA;
        Layer initLayer2 = (srcLayerB == null) ? context.getCandidateLayer(1) : srcLayerB;

        dialog.addLayerComboBox(LAYER_A, initLayer1, context.getLayerManager());

        JComboBox functionComboBox = dialog.addComboBox(PREDICATE, funcNameToRun, functionNames, null);
        functionComboBox.addItemListener(new MethodItemListener());
        paramField = dialog.addDoubleField(PARAM, params[0], 10);

        dialog.addLayerComboBox(LAYER_B, initLayer2, context.getLayerManager());

        updateUIForFunction(funcNameToRun);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        srcLayerA = dialog.getLayer(LAYER_A);
        srcLayerB = dialog.getLayer(LAYER_B);
        funcNameToRun = dialog.getText(PREDICATE);
        functionToRun = GeometryPredicate.getPredicate(funcNameToRun);
        params[0] = dialog.getDouble(PARAM);
    }

    private void updateUIForFunction(String funcName) {
        boolean paramUsed = false;
        GeometryPredicate func = GeometryPredicate.getPredicate(funcName);
        if (func != null) {
            paramUsed = func.getParameterCount() > 0;
        }
        paramField.setEnabled(paramUsed);
        // this has the effect of making the background gray (disabled)
        paramField.setOpaque(paramUsed);
    }

    private class MethodItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            updateUIForFunction((String) e.getItem());
        }
    }
}
