package com.osfac.dmt.plugin.edit;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.GenericNames;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;

public class LineNoderPlugIn extends AbstractThreadedUiPlugIn {

    private final static String SRC_LAYER = I18N.get("jump.plugin.edit.LineNoderPlugIn.Line-Layer");
    private final static String SELECTED_ONLY = GenericNames.USE_SELECTED_FEATURES_ONLY;
    private boolean useSelected = false;
    private String layerName;
    private GeometryFactory fact = new GeometryFactory();

    public LineNoderPlugIn() {
    }

    /**
     * Returns a very brief description of this task.
     *
     * @return the name of this task
     */
    public String getName() {
        return I18N.get("jump.plugin.edit.LineNoderPlugIn.Node-Lines");
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
        monitor.report(I18N.get("jump.plugin.edit.LineNoderPlugIn.Noding"));

        Layer layer = context.getLayerManager().getLayer(layerName);

        Collection inputFeatures = getFeaturesToProcess(layer, context);

        Collection lines = getLines(inputFeatures);

        monitor.report(I18N.get("jump.plugin.edit.LineNoderPlugIn.Noding-input-lines"));
        Geometry nodedGeom = nodeLines((List) lines);
        Collection nodedLines = toLines(nodedGeom);

        if (monitor.isCancelRequested()) {
            return;
        }
        createLayer(context, nodedLines);
    }

    private Collection getFeaturesToProcess(Layer lyr, PlugInContext context) {
        if (useSelected) {
            return context.getLayerViewPanel()
                    .getSelectionManager().getFeaturesWithSelectedItems(lyr);
        }
        return lyr.getFeatureCollectionWrapper().getFeatures();
    }

    private Collection getLines(Collection inputFeatures) {
        List linesList = new ArrayList();
        LinearComponentExtracter lineFilter = new LinearComponentExtracter(linesList);
        for (Iterator i = inputFeatures.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Geometry g = f.getGeometry();
            g.apply(lineFilter);
        }
        return linesList;
    }

    /**
     * Nodes a collection of linestrings. Noding is done via JTS union, which is
     * reasonably effective but may exhibit robustness failures.
     *
     * @param lines the linear geometries to node
     * @return a collection of linear geometries, noded together
     */
    private Geometry nodeLines(Collection lines) {
        Geometry linesGeom = fact.createMultiLineString(fact.toLineStringArray(lines));

        Geometry unionInput = fact.createMultiLineString(null);
        // force the unionInput to be non-empty if possible, to ensure union is not optimized away
        Geometry minLine = extractPoint(lines);
        if (minLine != null) {
            unionInput = minLine;
        }

        Geometry noded = linesGeom.union(unionInput);
        return noded;
    }

    private static List toLines(Geometry geom) {
        List linesList = new ArrayList();
        LinearComponentExtracter lineFilter = new LinearComponentExtracter(linesList);
        geom.apply(lineFilter);
        return linesList;
    }

    private Geometry extractPoint(Collection lines) {
        int minPts = Integer.MAX_VALUE;
        Geometry point = null;
        // extract first point from first non-empty geometry
        for (Iterator i = lines.iterator(); i.hasNext();) {
            Geometry g = (Geometry) i.next();
            if (!g.isEmpty()) {
                Coordinate p = g.getCoordinate();
                point = g.getFactory().createPoint(p);
            }
        }
        return point;
    }

    private void createLayer(PlugInContext context, Collection nodedLines)
            throws Exception {
        FeatureCollection polyFC = FeatureDatasetFactory.createFromGeometry(nodedLines);
        context.addLayer(
                StandardCategoryNames.RESULT,
                layerName + " " + I18N.get("jump.plugin.edit.LineNoderPlugIn.Noded-Lines"),
                polyFC);
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.setSideBarImage(new ImageIcon(getClass().getResource("Polygonize.png")));
        dialog.setSideBarDescription(I18N.get("jump.plugin.edit.LineNoderPlugIn.Nodes-the-lines-in-a-layer"));
        String fieldName = SRC_LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, context.getCandidateLayer(0), null, context.getLayerManager());
        dialog.addCheckBox(SELECTED_ONLY, useSelected);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        Layer layer = dialog.getLayer(SRC_LAYER);
        layerName = layer.getName();
        useSelected = dialog.getBoolean(SELECTED_ONLY);
    }
}
