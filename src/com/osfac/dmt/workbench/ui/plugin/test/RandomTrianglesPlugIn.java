package com.osfac.dmt.workbench.ui.plugin.test;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RandomTrianglesPlugIn extends AbstractPlugIn {

    private static int dummyLayerCount = 0;
    private GeometryFactory geometryFactory = new GeometryFactory();
    private WKTReader wktReader = new WKTReader(geometryFactory);
    private List cities =
            Arrays.asList(
            new String[]{
                "Alabama",
                "Alaska",
                "Arizona",
                "Arkansas",
                "California",
                "Colorado",
                "Connecticut",
                "Delaware",
                "Florida",
                "Georgia",
                "Hawaii",
                "Idaho",
                "Illinois",
                "Indiana",
                "Iowa",
                "Kansas",
                "Kentucky",
                "Louisiana",
                "Maine",
                "Maryland",
                "Massachusetts",
                "Michigan",
                "Minnesota",
                "Mississippi",
                "Missouri",
                "Montana",
                "Nebraska",
                "Nevada",
                "New Hampshire",
                "New Jersey",
                "New Mexico",
                "New York",
                "North Carolina",
                "North Dakota",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Rhode Island",
                "South Carolina",
                "South Dakota",
                "Tennessee",
                "Texas",
                "Utah",
                "Vermont",
                "Virginia",
                "Washington",
                "West Virginia",
                "Wisconsin",
                "Wyoming"});

    public RandomTrianglesPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addLayerViewMenuItem(
                this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_GENERATE},
                getName());
    }

    public boolean execute(PlugInContext context)
            throws ParseException, IOException {
        return execute(context, 50);
    }

    public boolean execute(PlugInContext context, int layerSize)
            throws ParseException, IOException {
        //    String inputValue = JOptionPane.showInputDialog(
        //        context.getWorkbenchFrame(),
        //        "Number of layers to generate (Default = 2):", getName(), JOptionPane.QUESTION_MESSAGE);
        //    if (inputValue == null) { return false; } //User hit Cancel [Bob Boseko]
        //    int n;
        //    try {
        //      n = Integer.parseInt(inputValue);
        //    }
        //    catch (NumberFormatException e) {
        //      n = 2;
        //    }
        int n = 1;

        for (int i = 0; i < n; i++) {
            generateLayer(context, layerSize);
        }

        return true;
    }

    private void generateLayer(PlugInContext context, int size)
            throws ParseException, IOException {
        dummyLayerCount++;

        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("Geometry", AttributeType.GEOMETRY);
        featureSchema.addAttribute("City", AttributeType.STRING);
        featureSchema.addAttribute("A Code", AttributeType.DATE);

        //Put GEOMETRY in this unusual position to test robustness of
        //AttributeTableModel [Bob Boseko]
        //    featureSchema.addAttribute("Geometry", AttributeType.GEOMETRY);
        featureSchema.addAttribute("B Code", AttributeType.INTEGER);
        featureSchema.addAttribute("C Code", AttributeType.DOUBLE);
        featureSchema.addAttribute("D Code", AttributeType.STRING);
        featureSchema.addAttribute("E Code", AttributeType.STRING);
        featureSchema.addAttribute("F Code", AttributeType.STRING);
        featureSchema.addAttribute("G Code", AttributeType.STRING);
        featureSchema.addAttribute("H Code", AttributeType.STRING);
        featureSchema.addAttribute("I Code", AttributeType.STRING);
        featureSchema.addAttribute("J Code", AttributeType.STRING);
        featureSchema.addAttribute("K Code", AttributeType.STRING);
        featureSchema.addAttribute("L Code", AttributeType.STRING);
        featureSchema.addAttribute("M Code", AttributeType.STRING);
        featureSchema.addAttribute("N Code", AttributeType.STRING);
        featureSchema.addAttribute("O Code", AttributeType.STRING);
        featureSchema.addAttribute("P Code", AttributeType.STRING);

        FeatureCollection featureCollection = new FeatureDataset(featureSchema);
        addFeature(cornerSquare(), featureCollection);

        for (int i = 0; i < size; i++) {
            addFeature(randomTriangle(), featureCollection);
        }

        Layer layer =
                context.addLayer(
                StandardCategoryNames.WORKING,
                I18N.get("ui.test.RandomTriangle.random-triangles"),
                featureCollection);
        layer.setDescription("ABCDE");
    }

    private Geometry cornerSquare() throws ParseException {
        return wktReader.read(
                "POLYGON ((-50 -50, 50 -50, 50 50, -50 50, -50 -50))");
    }

    private void addFeature(
            Geometry geometry,
            FeatureCollection featureCollection) {
        Feature feature =
                new BasicFeature(featureCollection.getFeatureSchema());
        feature.setAttribute("Geometry", geometry);
        feature.setAttribute(
                "City",
                cities.get((int) Math.floor(Math.random() * cities.size())));
        feature.setAttribute("A Code", new Date());
        feature.setAttribute(
                "B Code",
                new Integer((int) (Math.random() * 100000)));
        feature.setAttribute("C Code", new Double(Math.random() * 100000));
        feature.setAttribute(
                "D Code",
                new Date((int) Math.pow(Math.random() * 100000, 20)).toString());
        feature.setAttribute("E Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("F Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("G Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("H Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("I Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("J Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("K Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("L Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("M Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("N Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("O Code", "" + (int) (Math.random() * 100000));
        feature.setAttribute("P Code", "" + (int) (Math.random() * 100000));

        if (Math.random() > 0.8) {
            feature.setAttribute("E Code", null);
        }

        featureCollection.add(feature);
    }

    private Geometry randomTriangle() {
        int perturbation = 30;

        int x = (int) (Math.random() * 700);
        int y = (int) (Math.random() * 700);
        Coordinate firstPoint = perturbedPoint(x, y, perturbation);

        return geometryFactory.createPolygon(
                geometryFactory.createLinearRing(
                new Coordinate[]{
                    firstPoint,
                    perturbedPoint(x, y, perturbation),
                    perturbedPoint(x, y, perturbation),
                    firstPoint}),
                null);
    }

    private Coordinate perturbedPoint(int x, int y, int perturbation) {
        return new Coordinate(
                x + (Math.random() * perturbation),
                y + (Math.random() * perturbation));
    }

    public void setCities(List cities) {
        this.cities = cities;
    }
}
