package com.osfac.dmt.plugin.edit;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.geom.LineSegmentUtil;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;

public class ExtractSegmentsPlugIn extends AbstractThreadedUiPlugIn {

    private final static String LAYER = I18N.get("ui.MenuNames.LAYER");

    private static Collection toLineStrings(Collection segments) {
        GeometryFactory fact = new GeometryFactory();
        List lineStringList = new ArrayList();
        for (Iterator i = segments.iterator(); i.hasNext();) {
            LineSegment seg = (LineSegment) i.next();
            LineString ls = LineSegmentUtil.asGeometry(fact, seg);
            lineStringList.add(ls);
        }
        return lineStringList;
    }

    private static Collection toMergedLineStrings(Collection segments) {
        GeometryFactory fact = new GeometryFactory();
        LineMerger lineMerger = new LineMerger();
        for (Iterator i = segments.iterator(); i.hasNext();) {
            LineSegment seg = (LineSegment) i.next();
            lineMerger.add(LineSegmentUtil.asGeometry(fact, seg));
        }
        return lineMerger.getMergedLineStrings();
    }
    //private MultiInputDialog dialog;
    private String layerName;
    private boolean uniqueSegmentsOnly;
    private boolean mergeResultingSegments;
    private int inputEdgeCount = 0;
    private int uniqueSegmentCount = 0;

    public ExtractSegmentsPlugIn() {
    }

    /**
     * Returns a very brief description of this task.
     *
     * @return the name of this task
     */
    public String getName() {
        return I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Extract-Segments");
    }

    public void initialize(PlugInContext context) throws Exception {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_EDIT_GEOMETRY, MenuNames.CONVERT},
                this,
                new JMenuItem(getName() + "..."),
                createEnableCheck(context.getWorkbenchContext()),
                -1);
    }

    public EnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
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

        monitor.report(I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Extracting-Segments"));

        Layer layer = context.getLayerManager().getLayer(layerName);
        FeatureCollection lineFC = layer.getFeatureCollectionWrapper();
        inputEdgeCount = lineFC.size();

        //UniqueSegmentsExtracter extracter = new UniqueSegmentsExtracter(monitor);
        SegmentsExtracter extracter = new SegmentsExtracter(monitor);
        extracter.add(lineFC);
        Collection uniqueFSList = uniqueSegmentsOnly ? extracter.getSegments(1, 1)
                : extracter.getSegments();
        uniqueSegmentCount = uniqueFSList.size();
        Collection linestringList = mergeResultingSegments ? toMergedLineStrings(uniqueFSList)
                : toLineStrings(uniqueFSList);

        if (monitor.isCancelRequested()) {
            return;
        }
        createLayers(context, linestringList);
    }

    private void createLayers(PlugInContext context, Collection linestringList)
            throws Exception {
        FeatureCollection lineStringFC = FeatureDatasetFactory.createFromGeometry(linestringList);
        context.addLayer(
                StandardCategoryNames.RESULT,
                layerName + " " + I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Extracted-Segs"),
                lineStringFC);
        createOutput(context);
    }

    private void createOutput(PlugInContext context) {
        context.getOutputFrame().createNewDocument();
        context.getOutputFrame().addHeader(1,
                I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Extract-Segments"));
        context.getOutputFrame().addField(I18N.get("ui.MenuNames.LAYER") + ":", layerName);

        context.getOutputFrame().addText(" ");
        context.getOutputFrame().addField(
                I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Number-of-unique-segments-extracted"),
                "" + uniqueSegmentCount);
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.setSideBarImage(new ImageIcon(getClass().getResource("ExtractSegments.png")));
        dialog.setSideBarDescription(I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Extracts-all-unique-line-segments-from-a-dataset"));
        JComboBox layerComboBox = dialog.addLayerComboBox(LAYER, context.getCandidateLayer(0), null, context.getLayerManager());
        JCheckBox oneTimeCheckBox = dialog.addCheckBox(
                I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Remove-doubled-segments"), false);
        JCheckBox mergeCheckBox = dialog.addCheckBox(
                I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Merge-resulting-segments"), false);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        Layer layer = dialog.getLayer(LAYER);
        layerName = layer.getName();
        uniqueSegmentsOnly = dialog.getBoolean(
                I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Remove-doubled-segments"));
        mergeResultingSegments = dialog.getBoolean(
                I18N.get("jump.plugin.edit.ExtractSegmentsPlugIn.Merge-resulting-segments"));
    }
}
