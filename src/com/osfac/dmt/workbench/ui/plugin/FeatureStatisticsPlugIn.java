package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import java.util.Iterator;

/**
 * Computes various statistics for selected layers.
 */
public class FeatureStatisticsPlugIn extends AbstractPlugIn {

    public static final String nPtsAttr = "nPts";
    public static final String nHolesAttr = "nHoles";
    public static final String nCompsAttr = "nComponents";
    public static final String areaAttr = "area";
    public static final String lengthAttr = "length";
    public static final String typeAttr = "type";
    private static final String jtsGeometryClassPackagePrefix = "com.vividsolutions.jts.geom";

    public FeatureStatisticsPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(
                this, //exe
                new String[]{MenuNames.TOOLS, MenuNames.STATISTICS}, //menu path
                this.getName() + "...", //name methode .getName recieved by AbstractPlugIn 
                false, //checkbox
                null, //icon
                createEnableCheck(context.getWorkbenchContext())); //enable check  
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    public static FeatureSchema getStatisticsSchema() {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        featureSchema.addAttribute(nPtsAttr, AttributeType.INTEGER);
        featureSchema.addAttribute(nHolesAttr, AttributeType.INTEGER);
        featureSchema.addAttribute(nCompsAttr, AttributeType.INTEGER);
        featureSchema.addAttribute(areaAttr, AttributeType.DOUBLE);
        featureSchema.addAttribute(lengthAttr, AttributeType.DOUBLE);
        featureSchema.addAttribute(typeAttr, AttributeType.STRING);

        return featureSchema;
    }

    /**
     * Removes the JTS geometry package prefix from a classname
     *
     * @param fullClassName
     * @return the simplified class name
     */
    public static String removeGeometryPackage(String fullClassName) {
        if (fullClassName.startsWith(jtsGeometryClassPackagePrefix)) {
            return StringUtil.classNameWithoutQualifiers(fullClassName);
        } else {
            return fullClassName;
        }
    }

    public boolean execute(PlugInContext context) throws Exception {
        //Call #getSelectedLayers before #clear, because #clear will surface
        //output window. [Bob Boseko]
        Layer[] selectedLayers = context.getSelectedLayers();

        for (int i = 0; i < selectedLayers.length; i++) {
            featureStatistics(selectedLayers[i], context);
        }

        return true;
    }

    private void featureStatistics(final Layer layer, PlugInContext context) {
        FeatureSchema statsSchema = getStatisticsSchema();
        FeatureDataset statsFC = new FeatureDataset(statsSchema);

        for (Iterator i = layer.getFeatureCollectionWrapper().iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Geometry g = f.getGeometry();
            double area = g.getArea();
            double length = g.getLength();

            // these both need work - need to recurse into geometries
            // work done by mmichaud on 2010-12-12
            //int comps = 0;
            //int holes = 0;
            int[] comps_and_holes = new int[]{0, 0};
            comps_and_holes = recurse(g, comps_and_holes);
            int comps = comps_and_holes[0];
            int holes = comps_and_holes[1];
            //if (g instanceof GeometryCollection) {
            //    comps = ((GeometryCollection) g).getNumGeometries();
            //}

            Coordinate[] pts = g.getCoordinates();
            //int holes = 0;
            //
            //if (g instanceof Polygon) {
            //    holes = ((Polygon) g).getNumInteriorRing();
            //}

            Feature statsf = new BasicFeature(statsSchema);

            // this aliases the geometry of the input feature, but this shouldn't matter,
            // since if geometries are edited they should be completely replaced
            statsf.setAttribute("GEOMETRY", g);
            statsf.setAttribute(nPtsAttr, new Integer(pts.length));
            statsf.setAttribute(nHolesAttr, new Integer(holes));
            statsf.setAttribute(nCompsAttr, new Integer(comps));
            statsf.setAttribute(areaAttr, new Double(area));
            statsf.setAttribute(lengthAttr, new Double(length));
            statsf.setAttribute(typeAttr,
                    removeGeometryPackage(g.getClass().getName()));
            statsFC.add(statsf);
        }

        Layer statsLayer = context.addLayer(StandardCategoryNames.QA,
                "Statistics-" + layer.getName(), statsFC);
        statsLayer.setStyles(layer.cloneStyles());
    }

    private int[] recurse(Geometry g, int[] comps_holes) {
        if (g instanceof GeometryCollection) {
            for (int i = 0; i < g.getNumGeometries(); i++) {
                comps_holes = recurse(g.getGeometryN(i), comps_holes);
            }
        } else {
            comps_holes[0]++;
            if (g instanceof Polygon) {
                comps_holes[1] += ((Polygon) g).getNumInteriorRing();
            }
        }
        return comps_holes;
    }
}
