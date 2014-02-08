package com.osfac.dmt.plugin.edit;

import com.vividsolutions.jts.geom.*;
import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.geom.precision.GeometryPrecisionReducer;
import com.osfac.dmt.geom.precision.NumberPrecisionReducer;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.ColorUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerStyleUtil;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;

public class PrecisionReducerPlugIn extends AbstractThreadedUiPlugIn {

    private static final double EXAMPLE_VALUE = 1234567.123123123123;
    private final static String LAYER = I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Layer");
    private final static String DECIMAL_PLACES = I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Decimal-Places");
    private final static String SCALE_FACTOR = I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Scale-Factor");
    private JTextField decimalPlacesField;
    private JTextField scaleFactorField;
    private JLabel exampleLabel;
    private String layerName;
    private int decimalPlaces = 0;
    private int scaleFactor = 1;

    public PrecisionReducerPlugIn() {
    }

    /**
     * Returns a very brief description of this task.
     *
     * @return the name of this task
     */
    public String getName() {
        return I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Precision-Reducer");
    }

    public void initialize(PlugInContext context) throws Exception {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_EDIT_GEOMETRY},
                this, new JMenuItem(getName() + "..."),
                createEnableCheck(context.getWorkbenchContext()), -1);
    }

    public EnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1))
                .add(checkFactory.createAtLeastNLayersMustBeEditableCheck(1));
    }

    public boolean execute(PlugInContext context) throws Exception {
        MultiInputDialog dialog = new MultiInputDialog(
                context.getWorkbenchFrame(), getName(), true);
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
        monitor.report(I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Reducing-Precision") + "...");

        Layer layer = context.getLayerManager().getLayer(layerName);
        FeatureCollection fc = layer.getFeatureCollectionWrapper();

        List[] bad = reducePrecision(fc, monitor);
        layer.fireAppearanceChanged();

        if (monitor.isCancelRequested()) {
            return;
        }

        if (bad[0].size() > 0) {
            Layer lyr = context.getLayerManager().addLayer(StandardCategoryNames.QA,
                    I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Invalid-Input-Geometries"), FeatureDatasetFactory.createFromGeometry(bad[0]));
            LayerStyleUtil.setLinearStyle(lyr, Color.red, 2, 0);
            lyr.fireAppearanceChanged();

            Layer lyr2 = context.getLayerManager().addLayer(StandardCategoryNames.QA,
                    I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Invalid-Reduced-Geometries"), FeatureDatasetFactory.createFromGeometry(bad[1]));
            lyr2.getBasicStyle().setFillColor(ColorUtil.GOLD);
            lyr2.getBasicStyle().setLineColor(Layer.defaultLineColor(ColorUtil.GOLD));
            lyr2.fireAppearanceChanged();
        }
    }

    private NumberPrecisionReducer createNumberPrecisionReducer() {
        double sf = scaleFactor;
        // scaleFactor and decimalPlaces should be in synch, but if they are not use decimalPlaces
        if (scaleFactor != NumberPrecisionReducer.scaleFactorForDecimalPlaces(decimalPlaces)) {
            sf = NumberPrecisionReducer.scaleFactorForDecimalPlaces(decimalPlaces);
        }

        return new NumberPrecisionReducer(sf);
    }

    /**
     * @return an array of two Lists. The first contains the geometries which
     * reduced to invalid geometries. The second contains the invalid geometries
     * created
     */
    private List[] reducePrecision(FeatureCollection fc, TaskMonitor monitor) {
        List[] bad = {new ArrayList(), new ArrayList()};
        int total = fc.size();
        int count = 0;
        for (Iterator i = fc.iterator(); i.hasNext();) {
            monitor.report(count++, total, I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.features"));

            Feature f = (Feature) i.next();
            Geometry g = f.getGeometry();
            Geometry g2 = (Geometry) g.clone();
            GeometryPrecisionReducer pr = new GeometryPrecisionReducer(createNumberPrecisionReducer());
            pr.reduce(g2);
            if (g2.isValid()) {
                f.setGeometry(g2);
            } else {
                bad[0].add(g.clone());
                bad[1].add(g2);
            }
        }
        return bad;
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.setSideBarImage(new ImageIcon(getClass().getResource("PrecisionReducer.png")));
        dialog.setSideBarDescription(I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Reduces-the-precision-of-the-coordinates-in-a-layer"));
        String fieldName = LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, context.getCandidateLayer(0), null, context.getLayerManager());

        scaleFactorField = dialog.addIntegerField(SCALE_FACTOR, scaleFactor, 8,
                I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.The-scale-factor-to-multiply-by-before-rounding-(-Negative-for-left-of-decimal-point-,-0-if-not-used-)"));
        scaleFactorField.getDocument().addDocumentListener(new ScaleFactorDocumentListener());

        decimalPlacesField = dialog.addIntegerField(DECIMAL_PLACES, decimalPlaces, 4,
                I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.The-number-of-decimal-places-to-round-to-(-Negative-for-left-of-decimal-point-)"));
        decimalPlacesField.getDocument().addDocumentListener(new DecimalPlacesDocumentListener());

        dialog.addLabel("");
        dialog.addLabel(I18N.get("ui.plugin.edit.PrecisionReducerPlugIn.Example") + "  " + EXAMPLE_VALUE);
        exampleLabel = dialog.addLabel("");

        updateExample();
    }

    private int parseValidInt(String text) {
        int i = 0;
        try {
            i = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            // leave decPlaces value as 0
        }
        return i;
    }

    private void decimalPlacesChanged() {
        decimalPlaces = parseValidInt(decimalPlacesField.getText());
        double sf = NumberPrecisionReducer.scaleFactorForDecimalPlaces(decimalPlaces);
        scaleFactorField.setText("" + (int) sf);
        updateExample();
    }

    private void scaleFactorChanged() {
        scaleFactor = parseValidInt(scaleFactorField.getText());
        // can't update decimalPlaces because it will cause an event cycle
        //decimalPlacesField.setText("");
        updateExample();
    }

    private void updateExample() {
        NumberPrecisionReducer cpr = new NumberPrecisionReducer(scaleFactor);
        double exampleOutput = cpr.reducePrecision(EXAMPLE_VALUE);
        exampleLabel.setText("      ==>  " + exampleOutput);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        Layer layer = dialog.getLayer(LAYER);
        layerName = layer.getName();
        decimalPlaces = dialog.getInteger(DECIMAL_PLACES);
        scaleFactor = dialog.getInteger(SCALE_FACTOR);
    }

    class DecimalPlacesDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            decimalPlacesChanged();
        }

        public void removeUpdate(DocumentEvent e) {
            decimalPlacesChanged();
        }

        public void changedUpdate(DocumentEvent e) {
            decimalPlacesChanged();
        }
    }

    class ScaleFactorDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            scaleFactorChanged();
        }

        public void removeUpdate(DocumentEvent e) {
            scaleFactorChanged();
        }

        public void changedUpdate(DocumentEvent e) {
            scaleFactorChanged();
        }
    }
}
