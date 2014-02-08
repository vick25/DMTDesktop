package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import org.openjump.core.ui.images.IconLoader;

public class CombineSelectedFeaturesPlugIn extends AbstractPlugIn {

    public static ImageIcon ICON = IconLoader.icon("features_combine.png");

    public boolean execute(final PlugInContext context) throws Exception {
        final ArrayList originalFeatures =
                new ArrayList(
                context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems());
        final Feature combinedFeature = combine(originalFeatures);
        final Layer layer =
                (Layer) context
                .getLayerViewPanel()
                .getSelectionManager()
                .getLayersWithSelectedItems()
                .iterator()
                .next();
        execute(new UndoableCommand(getName()) {
            public void execute() {
                layer.getFeatureCollectionWrapper().removeAll(originalFeatures);
                layer.getFeatureCollectionWrapper().add(combinedFeature);
            }

            public void unexecute() {
                layer.getFeatureCollectionWrapper().remove(combinedFeature);
                layer.getFeatureCollectionWrapper().addAll(originalFeatures);
            }
        }, context);
        //Select outside #execute so it's not done on a redo. [Bob Boseko]
        context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(
                layer,
                combinedFeature);
        return true;
    }

    private Feature combine(Collection originalFeatures) {
        GeometryFactory factory = new GeometryFactory();
        Feature feature = (Feature) ((Feature) originalFeatures.iterator().next()).clone();
        Class narrowestCollectionClass = narrowestCollectionClass(originalFeatures);
        if (narrowestCollectionClass == MultiPoint.class) {
            feature.setGeometry(
                    factory.createMultiPoint(
                    (Point[]) FeatureUtil.toGeometries(originalFeatures).toArray(
                    new Point[originalFeatures.size()])));
        } else if (narrowestCollectionClass == MultiLineString.class) {
            feature.setGeometry(
                    factory.createMultiLineString(
                    (LineString[]) FeatureUtil.toGeometries(originalFeatures).toArray(
                    new LineString[originalFeatures.size()])));
        } else if (narrowestCollectionClass == MultiPolygon.class) {
            feature.setGeometry(
                    factory.createMultiPolygon(
                    (Polygon[]) FeatureUtil.toGeometries(originalFeatures).toArray(
                    new Polygon[originalFeatures.size()])));
        } else {
            feature.setGeometry(
                    factory.createGeometryCollection(
                    (Geometry[]) FeatureUtil.toGeometries(originalFeatures).toArray(
                    new Geometry[originalFeatures.size()])));
        }
        return feature;
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayersMustHaveSelectedItemsCheck(1))
                .add(checkFactory.createAtLeastNFeaturesMustHaveSelectedItemsCheck(2))
                .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck());
    }

    private Class narrowestCollectionClass(Collection features) {
        boolean hasPoints = false;
        boolean hasLineStrings = false;
        boolean hasPolygons = false;
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            if (feature.getGeometry() instanceof Point) {
                hasPoints = true;
            } else if (feature.getGeometry() instanceof LineString) {
                hasLineStrings = true;
            } else if (feature.getGeometry() instanceof Polygon) {
                hasPolygons = true;
            } else {
                return GeometryCollection.class;
            }
        }
        if (hasPoints && !hasLineStrings && !hasPolygons) {
            return MultiPoint.class;
        }
        if (!hasPoints && hasLineStrings && !hasPolygons) {
            return MultiLineString.class;
        }
        if (!hasPoints && !hasLineStrings && hasPolygons) {
            return MultiPolygon.class;
        }
        return GeometryCollection.class;
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
