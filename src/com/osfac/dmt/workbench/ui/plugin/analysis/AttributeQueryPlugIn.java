package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.DMTWorkbench;
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
import com.osfac.dmt.workbench.ui.SelectionManager;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.WorkbenchToolBar;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

/**
 * Queries a layer by a spatial predicate.
 */
public class AttributeQueryPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static String ATTR_GEOMETRY_AREA = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.Area");
    private static String ATTR_GEOMETRY_LENGTH = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.Length");
    private static String ATTR_GEOMETRY_NUMPOINTS = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.NumPoints");
    private static String ATTR_GEOMETRY_NUMCOMPONENTS = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.NumComponents");
    private static String ATTR_GEOMETRY_NUMHOLES = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.NumHoles");
    private static String ATTR_GEOMETRY_ISCLOSED = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.IsClosed");
    private static String ATTR_GEOMETRY_ISEMPTY = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.IsEmpty");
    private static String ATTR_GEOMETRY_ISSIMPLE = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.IsSimple");
    private static String ATTR_GEOMETRY_ISVALID = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.IsValid");
    private static String ATTR_GEOMETRY_TYPE = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.Type");
    private static String ATTR_GEOMETRY_DIMENSION = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Geometry.Dimension");

    // MD - could easily add this later
    //private final static String DIALOG_COMPLEMENT = "Complement Result";
    private Collection functionNames;
    private MultiInputDialog dialog;
    private Layer srcLayer;
    private String attrName;
    private String funcNameToRun;
    private String value = "";
    private boolean complementResult = false;
    private boolean caseInsensitive = false;
    private boolean exceptionThrown = false;
    private JRadioButton updateSourceRB;
    private JRadioButton createNewLayerRB;
    private boolean createLayer = true;

    public AttributeQueryPlugIn() {
        functionNames = AttributePredicate.getNames();
    }

    private String categoryName = StandardCategoryNames.RESULT;

    public void setCategoryName(String value) {
        categoryName = value;
    }

    @Override
    public void initialize(PlugInContext context) throws Exception {
        DMTWorkbench workbench = context.getWorkbenchContext().getWorkbench();
        WorkbenchFrame frame = workbench.getFrame();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
//        featureInstaller.addMainMenuItem(
//                this,
//                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_QUERIES},
//                new JMenuItem(this.getName() + "..."),
//                createEnableCheck(context.getWorkbenchContext()));
        //[Added by Vick]
        featureInstaller.addMainMenuItemWithJava14Fix(this, new String[]{MenuNames.TOOLS, MenuNames.TOOLS_QUERIES},
                new StringBuilder(this.getName()).append("...").toString(), false, this.getIcon(),
                createEnableCheck(context.getWorkbenchContext()));

        // Add tool-bar Icon [Victor Kadiata]
        WorkbenchToolBar toolBar = frame.getToolBar();
        toolBar.addPlugIn(getIcon(), this, createEnableCheck(context.getWorkbenchContext()), context.getWorkbenchContext());
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    @Override
    public String getName() {
        return I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Attribute-Query");
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("fugue/regular-expression-search.png");
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {

        dialog = new MultiInputDialog(context.getWorkbenchFrame(), getName(), true);
        setDialogValues(dialog, context);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        getDialogValues(dialog);

        // input-proofing
        if (srcLayer == null) {
            return false;
        }
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        if (StringUtil.isEmpty(attrName)) {
            return false;
        }
        if (StringUtil.isEmpty(funcNameToRun)) {
            return false;
        }

        return true;
    }

    @Override
    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        monitor.allowCancellationRequests();

        monitor.report(I18N.get("ui.plugin.analysis.SpatialQueryPlugIn.Executing-query") + "...");

        FeatureCollection sourceFC = srcLayer.getFeatureCollectionWrapper();

        if (monitor.isCancelRequested()) {
            return;
        }

        FeatureCollection resultFC = executeQuery(sourceFC, attrName, value);

        if (createLayer) {
            String outputLayerName = srcLayer.getName() + "_"
                    + attrName.replaceAll(".*\\.", "") + "_" + // remove first part of geometry.****
                    funcNameToRun + "_" + value;
            context.getLayerManager().addCategory(categoryName);
            context.addLayer(categoryName, outputLayerName, resultFC);
        } else {
            SelectionManager selectionManager = context.getLayerViewPanel().getSelectionManager();
            selectionManager.clear();
            selectionManager.getFeatureSelection().selectItems(srcLayer, resultFC.getFeatures());
        }

        if (exceptionThrown) {
            context.getWorkbenchFrame().warnUser(I18N.get("ui.plugin.analysis.SpatialQueryPlugIn.Errors-found-while-executing-query"));
        }
    }

    private FeatureCollection executeQuery(
            FeatureCollection sourceFC,
            String attrName,
            String value) {
        AttributePredicate pred = AttributePredicate.getPredicate(funcNameToRun, caseInsensitive);
        FeatureCollection resultFC = new FeatureDataset(sourceFC.getFeatureSchema());

        for (Iterator i = sourceFC.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Object fVal = getValue(f, attrName);

            boolean predResult = pred.isTrue(fVal, value);

            if (complementResult) {
                predResult = !predResult;
            }

            if (predResult) {
                if (createLayer) {
                    resultFC.add(f.clone(true));
                } else {
                    resultFC.add(f);
                }
            }
        }

        return resultFC;
    }

    private Object getValue(Feature f, String attrName) {
        if (attrName.equals(ATTR_GEOMETRY_AREA)) {
            Geometry g = f.getGeometry();
            double area = (g == null) ? 0.0 : g.getArea();
            return new Double(area);
        }
        if (attrName.equals(ATTR_GEOMETRY_LENGTH)) {
            Geometry g = f.getGeometry();
            double len = (g == null) ? 0.0 : g.getLength();
            return new Double(len);
        }
        if (attrName.equals(ATTR_GEOMETRY_NUMPOINTS)) {
            Geometry g = f.getGeometry();
            double len = (g == null) ? 0.0 : g.getNumPoints();
            return new Double(len);
        }
        if (attrName.equals(ATTR_GEOMETRY_NUMCOMPONENTS)) {
            Geometry g = f.getGeometry();
            double len = (g == null) ? 0.0 : g.getNumGeometries();
            return new Double(len);
        }

        if (attrName == ATTR_GEOMETRY_NUMHOLES) {
            Geometry g = f.getGeometry();
            int numHoles = 0;
            for (int i = 0; i < g.getNumGeometries(); i++) {
                if (g.getGeometryN(i) instanceof Polygon) {
                    numHoles += ((Polygon) g.getGeometryN(i)).getNumInteriorRing();
                }
            }
            return numHoles;
        }
        if (attrName == ATTR_GEOMETRY_ISCLOSED) {
            Geometry g = f.getGeometry();
            if (g instanceof LineString) {
                return ((LineString) g).isClosed();
            }
            if (g instanceof MultiLineString) {
                return ((MultiLineString) g).isClosed();
            }
            return false;
        }
        if (attrName == ATTR_GEOMETRY_ISEMPTY) {
            Geometry g = f.getGeometry();
            boolean bool = g.isEmpty();
            return new Boolean(bool);
        }
        if (attrName == ATTR_GEOMETRY_ISSIMPLE) {
            Geometry g = f.getGeometry();
            boolean bool = g.isSimple();
            return bool;
        }
        if (attrName.equals(ATTR_GEOMETRY_ISVALID)) {
            Geometry g = f.getGeometry();
            boolean bool = g.isValid();
            return bool;
        }
        if (attrName.equals(ATTR_GEOMETRY_TYPE)) {
            Geometry g = f.getGeometry();
            return StringUtil.classNameWithoutQualifiers(g.getClass().getName());
        }
        if (attrName == ATTR_GEOMETRY_DIMENSION) {
            Geometry g = f.getGeometry();
            return g.getDimension();
        }
        return f.getAttribute(attrName);
    }
    private static String LAYER = GenericNames.SOURCE_LAYER;
    private static String ATTRIBUTE = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Attribute");
    private static String PREDICATE = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Condition");
    private static String VALUE = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Value");
    private static String DIALOG_CASE_INSENSITIVE = I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Case-Insensitive");
    private static String DIALOG_COMPLEMENT = I18N.get("ui.plugin.analysis.SpatialQueryPlugIn.Complement-Result");

    private static String UPDATE_SRC = I18N.get("ui.plugin.analysis.SpatialQueryPlugIn.Select-features-in-the-source-layer");
    private static String CREATE_LYR = I18N.get("ui.plugin.analysis.SpatialQueryPlugIn.Create-a-new-layer-for-the-results");

    private JComboBox attrComboBox;

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.setSideBarDescription(
                I18N.get("ui.plugin.analysis.AttributeQueryPlugIn.Finds-the-Source-features-which-have-attribute-values-satisfying-a-given-condition"));

        //Set initial layer values to the first and second layers in the layer list.
        //In #initialize we've already checked that the number of layers >= 1. [Bob Boseko]
        Layer initLayer = (srcLayer == null) ? context.getCandidateLayer(0) : srcLayer;

        JComboBox lyrCombo = dialog.addLayerComboBox(LAYER, initLayer, context.getLayerManager());
        lyrCombo.addItemListener(new LayerItemListener());
        attrComboBox = dialog.addComboBox(ATTRIBUTE, attrName, functionNames, null);
        final JComboBox dropBox = dialog.addComboBox(PREDICATE, funcNameToRun, functionNames, null);
        dialog.addTextField(VALUE, value, 20, null, null);

        final JCheckBox caseBox = dialog.addCheckBox(DIALOG_CASE_INSENSITIVE, caseInsensitive);
        caseBox.setEnabled(caseBoxEnabled(dropBox));

        // en/disable caseInsensitive box by user input
        dropBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = caseBoxEnabled(dropBox);
                caseBox.setEnabled(enabled);
            }
        });

        dialog.addCheckBox(DIALOG_COMPLEMENT, complementResult);

        final String OUTPUT_GROUP = "OUTPUT_GROUP";
        createNewLayerRB = dialog.addRadioButton(CREATE_LYR, OUTPUT_GROUP, createLayer, CREATE_LYR);
        updateSourceRB = dialog.addRadioButton(UPDATE_SRC, OUTPUT_GROUP, !createLayer, UPDATE_SRC);

        updateUI(initLayer);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        srcLayer = dialog.getLayer(LAYER);
        attrName = dialog.getText(ATTRIBUTE);
        funcNameToRun = dialog.getText(PREDICATE);
        value = dialog.getText(VALUE);
        caseInsensitive = dialog.getBoolean(DIALOG_CASE_INSENSITIVE);
        complementResult = dialog.getBoolean(DIALOG_COMPLEMENT);
        createLayer = dialog.getBoolean(CREATE_LYR);
    }

    private void updateUI(Layer lyr) {
        List attrNames;
        if (lyr != null) {
            FeatureCollection fc = lyr.getFeatureCollectionWrapper();
            FeatureSchema fs = fc.getFeatureSchema();

            attrNames = getAttributeNames(fs);
        } else {
            attrNames = new ArrayList();
        }
        attrComboBox.setModel(new DefaultComboBoxModel(new Vector(attrNames)));
        attrComboBox.setSelectedItem(attrName);
    }

    private static boolean caseBoxEnabled(JComboBox<String> dropBox) {
        Object selectedItem = dropBox.getSelectedItem();
        boolean enabled = false;
        for (String function : AttributePredicate.getNamesCI()) {
            if (selectedItem != null && selectedItem.equals(function)) {
                enabled = true;
                break;
            }
        }
        return enabled;
    }

    private static List getAttributeNames(FeatureSchema fs) {
        List names = new ArrayList();
        for (int i = 0; i < fs.getAttributeCount(); i++) {
            if (fs.getAttributeType(i) != AttributeType.GEOMETRY) {
                names.add(fs.getAttributeName(i));
            }
        }
        names.add(ATTR_GEOMETRY_AREA);
        names.add(ATTR_GEOMETRY_LENGTH);
        names.add(ATTR_GEOMETRY_NUMPOINTS);
        names.add(ATTR_GEOMETRY_NUMCOMPONENTS);
        names.add(ATTR_GEOMETRY_NUMHOLES);
        names.add(ATTR_GEOMETRY_ISCLOSED);
        names.add(ATTR_GEOMETRY_ISEMPTY);
        names.add(ATTR_GEOMETRY_ISSIMPLE);
        names.add(ATTR_GEOMETRY_ISVALID);
        names.add(ATTR_GEOMETRY_TYPE);
        names.add(ATTR_GEOMETRY_DIMENSION);

        return names;
    }

    private class LayerItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            updateUI((Layer) e.getItem());
        }
    }
}
