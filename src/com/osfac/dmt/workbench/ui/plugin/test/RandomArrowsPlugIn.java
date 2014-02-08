package com.osfac.dmt.workbench.ui.plugin.test;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.geom.CoordUtil;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.renderer.style.ArrowLineStringEndpointStyle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.util.ArrayList;

public class RandomArrowsPlugIn extends AbstractPlugIn {

    private static final int FEATURE_COUNT = 20;
    private static final double LAYER_SIDE_LENGTH = 100;
    private static final int MAX_SEGMENT_COUNT = 3;
    private static final double MAX_SEGMENT_LENGTH = 20;
    private GeometryFactory geometryFactory = new GeometryFactory();

    public RandomArrowsPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addLayerViewMenuItem(this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_GENERATE}, getName());
    }

    public boolean execute(PlugInContext context) throws Exception {
        FeatureSchema schema = new FeatureSchema();
        schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);

        FeatureDataset dataset = new FeatureDataset(schema);

        for (int i = 0; i < FEATURE_COUNT; i++) {
            dataset.add(createFeature(schema));
        }

        addLayer(dataset, context);

        return true;
    }

    private void addLayer(FeatureCollection featureCollection,
            PlugInContext context) {
        Layer layer = new Layer(I18N.get("ui.test.RandomArrowsPlugIn.random-arrows"),
                context.getLayerManager().generateLayerFillColor(),
                featureCollection, context.getLayerManager());
        //Can't fire events because this Layer hasn't been added to the
        //LayerManager yet. [Bob Boseko]    
        boolean firingEvents = context.getLayerManager().isFiringEvents();
        context.getLayerManager().setFiringEvents(false);
        try {
            layer.addStyle(new ArrowLineStringEndpointStyle.NarrowSolidEnd());
        } finally {
            context.getLayerManager().setFiringEvents(firingEvents);
        }

        context.getLayerManager().addLayer(StandardCategoryNames.WORKING, layer);
    }

    private Feature createFeature(FeatureSchema schema) {
        ArrayList coordinates = new ArrayList();
        coordinates.add(CoordUtil.add(
                new Coordinate(LAYER_SIDE_LENGTH / 2d, LAYER_SIDE_LENGTH / 2d),
                randomCoordinate(LAYER_SIDE_LENGTH / 2d)));

        int walkMax = (int) Math.ceil(Math.random() * MAX_SEGMENT_LENGTH);
        int segmentCount = (int) Math.ceil(Math.random() * MAX_SEGMENT_COUNT);

        for (int i = 0; i < segmentCount; i++) {
            Coordinate prevCoordinate = (Coordinate) coordinates.get(coordinates.size()
                    - 1);
            coordinates.add(CoordUtil.add(prevCoordinate,
                    randomCoordinate(walkMax)));
        }

        LineString lineString = geometryFactory.createLineString((Coordinate[]) coordinates.toArray(
                new Coordinate[]{}));
        Feature feature = new BasicFeature(schema);
        feature.setGeometry(lineString);

        return feature;
    }

    private Coordinate randomCoordinate(double walkMax) {
        return CoordUtil.add(new Coordinate(-walkMax / 2d, -walkMax / 2d),
                new Coordinate(Math.random() * walkMax, Math.random() * walkMax));
    }
}
