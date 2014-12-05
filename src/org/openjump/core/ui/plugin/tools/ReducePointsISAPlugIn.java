package org.openjump.core.ui.plugin.tools;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.DefaultCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.openjump.core.geomutils.GeoUtils;

public class ReducePointsISAPlugIn extends AbstractPlugIn {

    private final static String sSimplifyISA = I18N.get("org.openjump.core.ui.plugin.tools.ReducePointsISAPlugIn.Simplify-ISA-algorithm");
    private final static String sPointsReducedFrom = I18N.get("org.openjump.core.ui.plugin.tools.ReducePointsISAPlugIn.Points-reduced-from");
    private final static String sTo = I18N.get("org.openjump.core.ui.plugin.tools.ReducePointsISAPlugIn.to");
    private final static String sReducePointsInSelectedFeatures = I18N.get("org.openjump.core.ui.plugin.tools.ReducePointsISAPlugIn.Reduce-points-in-selected-features");
    private final static String sTheReducePointsTolerance = I18N.get("org.openjump.core.ui.plugin.tools.ReducePointsISAPlugIn.The-reduce-points-tolerance");
    private WorkbenchContext workbenchContext;
    private final static String TOLERANCE = I18N.get("org.openjump.core.ui.plugin.tools.ReducePointsISAPlugIn.Tolerance");
    ;
    private double tolerance = 0.1;
    PlugInContext gContext;

    public void initialize(PlugInContext context) throws Exception {
        workbenchContext = context.getWorkbenchContext();
        /*
         gContext = context;
         FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
         JPopupMenu popupMenu = LayerViewPanel.popupMenu();
         featureInstaller.addPopupMenuItem(popupMenu,
         this, sSimplifyISA,
         false, null, 
         this.createEnableCheck(workbenchContext));
         */

        context.getFeatureInstaller().addMainMenuItemWithJava14Fix(
                this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_GENERALIZATION},
                sSimplifyISA,
                false,
                null,
                this.createEnableCheck(workbenchContext));

    }

    public boolean execute(final PlugInContext context) throws Exception {
        final ArrayList transactions = new ArrayList();
        reportNothingToUndoYet(context);
        MultiInputDialog dialog = new MultiInputDialog(
                context.getWorkbenchFrame(), getName(), true);
        setDialogValues(dialog, context);
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()) {
            return false;
        }
        getDialogValues(dialog);

        Collection layers = context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems();
        Collection selectedFeatures = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
        int startNumPts = 0;
        for (Iterator i = selectedFeatures.iterator(); i.hasNext();) {
            Geometry geo = ((Feature) i.next()).getGeometry();
            startNumPts += geo.getNumPoints();
        }

        Geometry geo = ((Feature) selectedFeatures.iterator().next()).getGeometry();
        for (Iterator j = layers.iterator(); j.hasNext();) {
            Layer layer = (Layer) j.next();
            transactions.add(createTransaction(layer, tolerance));
        }

        EditTransaction.commit(transactions);

        selectedFeatures = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
        int endNumPts = 0;
        for (Iterator i = selectedFeatures.iterator(); i.hasNext();) {
            geo = ((Feature) i.next()).getGeometry();
            endNumPts += geo.getNumPoints();
        }
        context.getWorkbenchFrame().setStatusMessage(sPointsReducedFrom + " " + startNumPts + " " + sTo + " " + endNumPts);
        return true;
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.addLabel(sReducePointsInSelectedFeatures);
        dialog.addDoubleField(TOLERANCE, tolerance, 6, sTheReducePointsTolerance);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        tolerance = dialog.getDouble(TOLERANCE);
    }

    private EditTransaction createTransaction(Layer layer, final double tolerance) {
        EditTransaction transaction =
                EditTransaction.createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {
                Geometry geo = reducePoints(geometryWithSelectedItems, tolerance);
                return geo;
            }
        }, workbenchContext.getLayerViewPanel(), workbenchContext.getLayerViewPanel().getContext(), getName(), layer, false, false);
        return transaction;
    }

    private Geometry reducePoints(Geometry geometry, double tolerance) {
        if (geometry instanceof GeometryCollection) {
            GeometryFactory geoFac = geometry.getFactory();
            GeometryCollection gc = (GeometryCollection) geometry;
            Geometry[] geos = new Geometry[gc.getNumGeometries()];
            if (!gc.isEmpty()) {
                for (int i = 0; i < gc.getNumGeometries(); i++) {
                    geos[i] = reduceGeo(gc.getGeometryN(i), tolerance);
                }
                return new GeometryCollection(geos, geoFac);
            } else {
                return geometry;
            }
        } else {
            return reduceGeo(geometry, tolerance);
        }
    }

    private Geometry reduceGeo(Geometry geometry, double tolerance) {
        if (geometry instanceof LineString) //open poly
        {
            return GeoUtils.reducePoints(geometry, tolerance);
        } else if (geometry instanceof LinearRing) //closed poly (no holes)
        {
            return GeoUtils.reducePoints(geometry, tolerance);
        } else if (geometry instanceof Polygon) //poly with 0 or more holes
        {
            return GeoUtils.reducePoints(geometry, tolerance);
        } else if (geometry instanceof MultiLineString) {
            MultiLineString mls = (MultiLineString) geometry;
            LineString[] lineStrings = new LineString[mls.getNumGeometries()];
            GeometryFactory geoFac = geometry.getFactory();

            if (!mls.isEmpty()) {
                for (int i = 0; i < mls.getNumGeometries(); i++) {
                    lineStrings[i] = (LineString) GeoUtils.reducePoints(mls.getGeometryN(i), tolerance);
                }
                return new MultiLineString(lineStrings, geoFac);
            } else {
                return geometry;
            }
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygon mp = (MultiPolygon) geometry;
            Polygon[] polys = new Polygon[mp.getNumGeometries()];
            GeometryFactory geoFac = geometry.getFactory();
            DefaultCoordinateSequenceFactory dcsf = DefaultCoordinateSequenceFactory.instance();

            if (!mp.isEmpty()) {
                for (int i = 0; i < mp.getNumGeometries(); i++) {
                    Polygon poly = (Polygon) GeoUtils.reducePoints(mp.getGeometryN(i), tolerance);
                    CoordinateSequence cs = dcsf.create(poly.getCoordinates());
                    polys[i] = new Polygon(new LinearRing(cs, geoFac), null, geoFac);
                }
                return new MultiPolygon(polys, geoFac);
            } else {
                return geometry;
            }
        } else {
            return geometry;
        }
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNFeaturesMustHaveSelectedItemsCheck(1))
                .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck());
    }
}
