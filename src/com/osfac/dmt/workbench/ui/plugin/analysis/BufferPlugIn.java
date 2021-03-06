package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.feature.FeatureUtil;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.AttributeTypeFilter;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.MultiTabInputDialog;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.clipboard.PasteItemsPlugIn;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import com.vividsolutions.jts.operation.buffer.BufferParameters;
import com.vividsolutions.jts.operation.union.UnaryUnionOp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;

public class BufferPlugIn extends AbstractThreadedUiPlugIn {

    private String MAIN_OPTIONS;
    private String PROCESSED_DATA;
    private String LAYER;
    private String SELECTION;
    private String SELECTION_HELP;
    private String DISTANCE;
    private String FIXED_DISTANCE;
    private String ATTRIBUTE;
    private String FROM_ATTRIBUTE;
    private String ATTRIBUTE_TOOLTIP;
    private String OTHER_OPTIONS;
    private String QUADRANT_SEGMENTS;
    private String UNION_RESULT;
    private String COPY_ATTRIBUTES;
    private String ADVANCED_OPTIONS;
    private String END_CAP_STYLE;
    private String CAP_FLAT;
    private String CAP_ROUND;
    private String CAP_SQUARE;
    private String JOIN_STYLE_TITLE;
    private String JOIN_STYLE;
    private String JOIN_BEVEL;
    private String JOIN_MITRE;
    private String JOIN_ROUND;
    private String MITRE_LIMIT;
    private String SINGLE_SIDED;
    private List endCapStyles;
    private List joinStyles;
    private Layer layer;
    private double bufferDistance = 1.0;
    private int endCapStyleCode = BufferParameters.CAP_ROUND;
    private int joinStyleCode = BufferParameters.JOIN_ROUND;
    ;
  private double mitreLimit = 10.0;
    private boolean singleSided = false;
    private boolean exceptionThrown = false;
    private boolean useSelected = false;
    private int quadrantSegments = 8;
    private boolean unionResult = false;
    private String sideBarText = "";
    private boolean copyAttributes = true;
    private boolean fromAttribute = false;
    private int attributeIndex = 0;

    public BufferPlugIn() {
        super(I18N.get("com.osfac.dmt.workbench.ui.plugin.analysis.BufferPlugIn") + "...",
                IconLoader.icon("buffer.gif"));
    }
    private String categoryName = StandardCategoryNames.RESULT;

    public void setCategoryName(String value) {
        categoryName = value;
    }

    @Override
    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItem(
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_ANALYSIS},
                this,
                createEnableCheck(context.getWorkbenchContext()));
        //FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        //featureInstaller.addMainMenuItem(
        //    this,					//exe
        //    new String[] {MenuNames.TOOLS, MenuNames.TOOLS_ANALYSIS}, 	//menu path
        //    this.getName() + "...", //name methode .getName received by AbstractPlugIn
        //    false,			        //checkbox
        //    ICON,			        //icon
        //    createEnableCheck(context.getWorkbenchContext()));
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        //[sstein, 16.07.2006] set again to obtain correct language
        //[LDB: 31.08.2007] moved all initialization of strings here
        MAIN_OPTIONS = I18N.get("ui.plugin.analysis.BufferPlugIn.main-options");
        PROCESSED_DATA = I18N.get("ui.plugin.analysis.BufferPlugIn.processed-data");
        LAYER = I18N.get("ui.plugin.analysis.BufferPlugIn.layer");
        SELECTION = I18N.get("ui.plugin.analysis.BufferPlugIn.selection");
        SELECTION_HELP = I18N.get("ui.plugin.analysis.BufferPlugIn.selection-help");

        DISTANCE = I18N.get("ui.plugin.analysis.BufferPlugIn.distance");
        FIXED_DISTANCE = I18N.get("ui.plugin.analysis.BufferPlugIn.fixed-distance");
        FROM_ATTRIBUTE = I18N.get("ui.plugin.analysis.BufferPlugIn.get-distance-from-attribute-value");
        ATTRIBUTE = I18N.get("ui.plugin.analysis.BufferPlugIn.attribute-to-use");
        ATTRIBUTE_TOOLTIP = I18N.get("ui.plugin.analysis.BufferPlugIn.attribute-to-use-tooltip");

        OTHER_OPTIONS = I18N.get("ui.plugin.analysis.BufferPlugIn.other-options");
        QUADRANT_SEGMENTS = I18N.get("org.openjump.core.ui.plugin.edittoolbox.cursortools.DrawCircleWithGivenRadiusTool.Number-of-segments-per-circle-quarter");
        UNION_RESULT = I18N.get("ui.plugin.analysis.UnionPlugIn.union");
        COPY_ATTRIBUTES = I18N.get("ui.plugin.analysis.BufferPlugIn.preserve-attributes");

        ADVANCED_OPTIONS = I18N.get("ui.plugin.analysis.BufferPlugIn.advanced-options");
        END_CAP_STYLE = I18N.get("ui.plugin.analysis.BufferPlugIn.end-cap-style");
        CAP_FLAT = I18N.get("ui.plugin.analysis.BufferPlugIn.cap-flat");
        CAP_ROUND = I18N.get("ui.plugin.analysis.BufferPlugIn.cap-round");
        CAP_SQUARE = I18N.get("ui.plugin.analysis.BufferPlugIn.cap-square");

        JOIN_STYLE_TITLE = I18N.get("ui.plugin.analysis.BufferPlugIn.join-style-subtitle");
        JOIN_STYLE = I18N.get("ui.plugin.analysis.BufferPlugIn.join-style");
        JOIN_BEVEL = I18N.get("ui.plugin.analysis.BufferPlugIn.join-bevel");
        JOIN_MITRE = I18N.get("ui.plugin.analysis.BufferPlugIn.join-mitre");
        JOIN_ROUND = I18N.get("ui.plugin.analysis.BufferPlugIn.join-round");
        MITRE_LIMIT = I18N.get("ui.plugin.analysis.BufferPlugIn.mitre-join-limit");

        SINGLE_SIDED = I18N.get("ui.plugin.analysis.BufferPlugIn.single-sided");

        endCapStyles = new ArrayList();
        endCapStyles.add(CAP_FLAT);
        endCapStyles.add(CAP_ROUND);
        endCapStyles.add(CAP_SQUARE);

        joinStyles = new ArrayList();
        joinStyles.add(JOIN_BEVEL);
        joinStyles.add(JOIN_MITRE);
        joinStyles.add(JOIN_ROUND);

        MultiTabInputDialog dialog = new MultiTabInputDialog(
                context.getWorkbenchFrame(), getName(), MAIN_OPTIONS, true);
        int n = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems().size();
        useSelected = (n > 0);
        if (useSelected) {
            sideBarText = SELECTION;
        } else {
            sideBarText = I18N.get("ui.plugin.analysis.BufferPlugIn.buffers-all-geometries-in-the-input-layer");
        }
        setDialogValues(dialog, context);
        updateControls(dialog);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()) {
            return false;
        }
        getDialogValues(dialog);
        return true;
    }

    @Override
    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        monitor.allowCancellationRequests();
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        FeatureCollection resultFC = new FeatureDataset(featureSchema);
        Collection inputC;
        if (useSelected) {
            inputC = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
            Feature feature = (Feature) inputC.iterator().next();
            featureSchema = feature.getSchema();
            inputC = PasteItemsPlugIn.conform(inputC, featureSchema);
        } else {
            inputC = layer.getFeatureCollectionWrapper().getFeatures();
            featureSchema = layer.getFeatureCollectionWrapper().getFeatureSchema();
            resultFC = new FeatureDataset(featureSchema);
        }
        FeatureDataset inputFD = new FeatureDataset(inputC, featureSchema);
        if (inputFD.isEmpty()) {
            context.getWorkbenchFrame()
                    .warnUser(I18N.get("ui.plugin.analysis.BufferPlugIn.empty-result-set"));
            return;
        }
        Collection resultGeomColl = runBuffer(monitor, inputFD);
        if (copyAttributes) {
            FeatureCollection resultFeatureColl = new FeatureDataset(featureSchema);
            Iterator iResult = resultGeomColl.iterator();
            for (Iterator iSource = inputFD.iterator(); iSource.hasNext();) {
                Feature sourceFeature = (Feature) iSource.next();
                Geometry gResult = (Geometry) iResult.next();
                if (!(gResult == null || gResult.isEmpty())) {
                    Feature newFeature = sourceFeature.clone(true);
                    newFeature.setGeometry(gResult);
                    resultFeatureColl.add(newFeature);
                }
                if (monitor.isCancelRequested()) {
                    break;
                }
            }
            resultFC = resultFeatureColl;
        } else {
            resultFC = FeatureDatasetFactory.createFromGeometry(resultGeomColl);
        }
        if (unionResult) {
            monitor.report(I18N.get("ui.plugin.analysis.BufferPlugIn.union-buffered-features"));
            Collection geoms = FeatureUtil.toGeometries(resultFC.getFeatures());
            Geometry g = UnaryUnionOp.union(geoms);
            geoms.clear();
            if (!(g == null || g.isEmpty())) {
                geoms.add(g);
            }
            resultFC = FeatureDatasetFactory.createFromGeometry(geoms);
        }
        if (resultFC.isEmpty()) {
            context.getWorkbenchFrame()
                    .warnUser(I18N.get("ui.plugin.analysis.BufferPlugIn.empty-result-set"));
            return;
        }
        context.getLayerManager().addCategory(categoryName);
        String name;
        if (!useSelected) {
            name = layer.getName();
        } else {
            name = I18N.get("ui.MenuNames.SELECTION");
        }
        name = I18N.get("com.osfac.dmt.workbench.ui.plugin.analysis.BufferPlugIn") + "-" + name;
        if (endCapStyleCode != BufferParameters.CAP_ROUND) {
            name = name + "-" + endCapStyle(endCapStyleCode);
        }
        context.addLayer(categoryName, name, resultFC);
        if (exceptionThrown) {
            context.getWorkbenchFrame().warnUser(I18N.get("ui.plugin.analysis.BufferPlugIn.errors-found-while-executing-buffer"));
        }
    }

    private Collection runBuffer(TaskMonitor monitor, FeatureCollection fcA) {
        exceptionThrown = false;
        int total = fcA.size();
        int count = 0;
        Collection resultColl = new ArrayList();
        BufferParameters bufferParameters
                = new BufferParameters(quadrantSegments, endCapStyleCode, joinStyleCode, mitreLimit);
        bufferParameters.setSingleSided(singleSided);
        for (Iterator ia = fcA.iterator(); ia.hasNext();) {
            monitor.report(count++, total, I18N.get("com.osfac.dmt.qa.diff.DiffGeometry.features"));
            if (monitor.isCancelRequested()) {
                break;
            }
            Feature fa = (Feature) ia.next();
            Geometry ga = fa.getGeometry();
            if (fromAttribute) {
                Object o = fa.getAttribute(attributeIndex);
                if (o instanceof Double) {
                    bufferDistance = ((Double) o).doubleValue();
                } else if (o instanceof Integer) {
                    bufferDistance = ((Integer) o).doubleValue();
                }
            }
            Geometry result = runBuffer(ga, bufferParameters);
            if (result != null) {
                resultColl.add(result);
            }
        }
        return resultColl;
    }

    private Geometry runBuffer(Geometry a, BufferParameters param) {
        Geometry result;
        try {
            BufferOp bufOp = new BufferOp(a, param);
            result = bufOp.getResultGeometry(bufferDistance);
            return result;
        } catch (RuntimeException ex) {
            // simply eat exceptions and report them by returning null
            exceptionThrown = true;
        }
        return null;
    }

    private void setDialogValues(final MultiTabInputDialog dialog, PlugInContext context) {

        dialog.setSideBarDescription(sideBarText);

        try {
            updateIcon(dialog);
        } catch (Exception ex) {
        }

        dialog.addSubTitle(PROCESSED_DATA);
        final JComboBox layerComboBox = dialog.addLayerComboBox(LAYER, context.getCandidateLayer(0), context.getLayerManager());
        dialog.addLabel(SELECTION);
        dialog.addLabel(SELECTION_HELP);

        dialog.addSeparator();
        dialog.addSubTitle(DISTANCE);
        final JTextField bufferDistanceTextField = dialog.addDoubleField(FIXED_DISTANCE, bufferDistance, 10, null);
        final JCheckBox fromAttributeCheckBox = dialog.addCheckBox(FROM_ATTRIBUTE, false, ATTRIBUTE_TOOLTIP);
        final JComboBox attributeComboBox = dialog.addAttributeComboBox(ATTRIBUTE, LAYER, AttributeTypeFilter.NUMERIC_FILTER, ATTRIBUTE_TOOLTIP);

        dialog.addSeparator();
        dialog.addSubTitle(OTHER_OPTIONS);
        final JTextField quadrantSegmentsIntegerField = dialog.addIntegerField(QUADRANT_SEGMENTS, quadrantSegments, 3, null);
        final JCheckBox unionCheckBox = dialog.addCheckBox(UNION_RESULT, unionResult);
        final JCheckBox copyAttributesCheckBox = dialog.addCheckBox(COPY_ATTRIBUTES, copyAttributes);

        dialog.addPane(ADVANCED_OPTIONS);

        //dialog.addSubTitle(END_CAP_STYLE);
        final JComboBox endCapComboBox = dialog.addComboBox(END_CAP_STYLE, endCapStyle(endCapStyleCode), endCapStyles, null);

        dialog.addSeparator();
        //dialog.addSubTitle(JOIN_STYLE);
        final JComboBox joinStyleComboBox = dialog.addComboBox(JOIN_STYLE, joinStyle(joinStyleCode), joinStyles, null);
        final JTextField mitreLimitTextField = dialog.addDoubleField(MITRE_LIMIT, mitreLimit, 10, null);

        dialog.addSeparator();
        //dialog.addSubTitle(SINGLE_SIDED);
        final JCheckBox singleSidedCheckBox = dialog.addCheckBox(SINGLE_SIDED, singleSided);
        dialog.addRow(new javax.swing.JPanel());

        mitreLimitTextField.setEnabled(joinStyleCode == BufferParameters.JOIN_MITRE);
        quadrantSegmentsIntegerField.setEnabled(
                (endCapStyleCode == BufferParameters.CAP_ROUND)
                || (joinStyleCode == BufferParameters.JOIN_ROUND));
        endCapComboBox.setEnabled(!singleSided);
        copyAttributesCheckBox.setEnabled(!unionResult);

        updateIcon(dialog);

        layerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ActionListener listener : layerComboBox.getActionListeners()) {
                    // execute other ActionListener methods before this one
                    if (listener != this) {
                        listener.actionPerformed(e);
                    }
                }
                updateControls(dialog);
            }
        });
        fromAttributeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls(dialog);
            }
        });
        unionCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls(dialog);
            }
        });
        endCapComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls(dialog);
            }
        });
        joinStyleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls(dialog);
            }
        });
        singleSidedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls(dialog);
            }
        });

    }

    private void getDialogValues(MultiInputDialog dialog) {
        if (!useSelected) {
            layer = dialog.getLayer(LAYER);
        }
        bufferDistance = dialog.getDouble(FIXED_DISTANCE);
        endCapStyleCode = endCapStyleCode(dialog.getText(END_CAP_STYLE));
        quadrantSegments = dialog.getInteger(QUADRANT_SEGMENTS);
        joinStyleCode = joinStyleCode(dialog.getText(JOIN_STYLE));
        mitreLimit = dialog.getDouble(MITRE_LIMIT);
        singleSided = dialog.getBoolean(SINGLE_SIDED);
        unionResult = dialog.getBoolean(UNION_RESULT);
        copyAttributes = dialog.getBoolean(COPY_ATTRIBUTES);
        if (!useSelected) {
            boolean hasNumericAttributes = AttributeTypeFilter.NUMERIC_FILTER
                    .filter(layer.getFeatureCollectionWrapper().getFeatureSchema()).size() > 0;
            fromAttribute = dialog.getBoolean(FROM_ATTRIBUTE);
            if (fromAttribute && dialog.getCheckBox(FROM_ATTRIBUTE).isEnabled() && hasNumericAttributes) {
                FeatureSchema schema = layer.getFeatureCollectionWrapper().getFeatureSchema();
                String attributeName = dialog.getText(ATTRIBUTE);
                attributeIndex = schema.getAttributeIndex(attributeName);
            } else {
                dialog.getCheckBox(FROM_ATTRIBUTE).setSelected(false);
                fromAttribute = false;
            }
        }
    }

    private int endCapStyleCode(String capStyle) {
        if (capStyle.equals(CAP_FLAT)) {
            return BufferParameters.CAP_FLAT;
        }
        if (capStyle.equals(CAP_SQUARE)) {
            return BufferParameters.CAP_SQUARE;
        }
        return BufferParameters.CAP_ROUND;
    }

    private String endCapStyle(int capStyleCode) {
        if (capStyleCode == BufferParameters.CAP_FLAT) {
            return CAP_FLAT;
        }
        if (capStyleCode == BufferParameters.CAP_SQUARE) {
            return CAP_SQUARE;
        }
        return CAP_ROUND;
    }

    private int joinStyleCode(String joinStyle) {
        if (joinStyle.equals(JOIN_BEVEL)) {
            return BufferParameters.JOIN_BEVEL;
        }
        if (joinStyle.equals(JOIN_MITRE)) {
            return BufferParameters.JOIN_MITRE;
        }
        return BufferParameters.JOIN_ROUND;
    }

    private String joinStyle(int joinStyleCode) {
        if (joinStyleCode == BufferParameters.JOIN_BEVEL) {
            return JOIN_BEVEL;
        }
        if (joinStyleCode == BufferParameters.JOIN_MITRE) {
            return JOIN_MITRE;
        }
        return JOIN_ROUND;
    }

    private Feature combine(Collection originalFeatures) {
        GeometryFactory factory = new GeometryFactory();
        Feature feature = (Feature) ((Feature) originalFeatures.iterator().next()).clone();
        feature.setGeometry(factory.createGeometryCollection(
                (Geometry[]) FeatureUtil.toGeometries(originalFeatures).toArray(
                        new Geometry[originalFeatures.size()])));
        return feature;
    }

    protected void updateControls(final MultiInputDialog dialog) {
        getDialogValues(dialog);
        updateIcon(dialog);
        boolean hasNumericAttributes = !useSelected && AttributeTypeFilter.NUMERIC_FILTER
                .filter(layer.getFeatureCollectionWrapper().getFeatureSchema()).size() > 0;
        dialog.setFieldVisible(LAYER, !useSelected);
        dialog.setFieldVisible(SELECTION, useSelected);
        dialog.setFieldVisible(SELECTION_HELP, useSelected);
        dialog.setFieldEnabled(FIXED_DISTANCE, useSelected || !fromAttribute || !hasNumericAttributes);
        dialog.setFieldEnabled(FROM_ATTRIBUTE, !useSelected && hasNumericAttributes);
        dialog.setFieldEnabled(ATTRIBUTE, !useSelected && fromAttribute && hasNumericAttributes);
        dialog.setFieldEnabled(COPY_ATTRIBUTES, !unionResult);
        dialog.setFieldEnabled(QUADRANT_SEGMENTS,
                (endCapStyleCode == BufferParameters.CAP_ROUND)
                || (joinStyleCode == BufferParameters.JOIN_ROUND));
        dialog.setFieldEnabled(MITRE_LIMIT,
                joinStyleCode == BufferParameters.JOIN_MITRE);
        dialog.setFieldEnabled(END_CAP_STYLE, !singleSided);
    }

    private void updateIcon(MultiInputDialog dialog) {
        StringBuilder fileName = new StringBuilder("Buffer");
        if (unionResult) {
            fileName.append("Union");
        }
        if (singleSided) {
            fileName.append("SingleSided");
        }
        if (!singleSided && endCapStyleCode == BufferParameters.CAP_FLAT) {
            fileName.append("Flat");
        } else if (!singleSided && endCapStyleCode == BufferParameters.CAP_ROUND) {
            fileName.append("Round");
        } else if (!singleSided && endCapStyleCode == BufferParameters.CAP_SQUARE) {
            fileName.append("Square");
        }
        if (joinStyleCode == BufferParameters.JOIN_BEVEL) {
            fileName.append("Bevel");
        } else if (joinStyleCode == BufferParameters.JOIN_ROUND) {
            fileName.append("Round");
        } else if (joinStyleCode == BufferParameters.JOIN_MITRE) {
            fileName.append("Mitre");
        }
        dialog.setSideBarImage(
                new javax.swing.ImageIcon(IconLoader.image(fileName.toString() + ".png")
                        .getScaledInstance((int) (216.0 * 0.8), (int) (159.0 * 0.8), java.awt.Image.SCALE_SMOOTH)));
    }
}
