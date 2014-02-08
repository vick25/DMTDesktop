package org.openjump.core.ui.plugin.raster;

import java.awt.geom.Point2D;

import org.openjump.core.apitools.LayerTools;
import org.openjump.core.rasterimage.RasterImageLayer;
import org.openjump.core.rasterimage.sextante.OpenJUMPSextanteRasterLayer;
import org.openjump.core.rasterimage.sextante.rasterWrappers.GridWrapperNotInterpolated;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedPlugIn;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;

/**
 * Creates a lattice for the current selected raster image
 *
 * TODO : I was going todo this as a normal plugin, but this won't work since
 * raster images are Layerables and not layer objects, so the drop down list
 * doesn't display them
 *
 * @author sstein
 *
 *
 */
public class CreateLatticeFromSelectedImageLayerPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private PlugInContext context = null;
    GeometryFactory gfactory = new GeometryFactory();
    private String sName = "Create Lattice from Raster";
    private String sBand = "band";
    private String sLattice = "lattice";
    private String sCreatePoints = "creating points";

    public void initialize(PlugInContext context) throws Exception {

        this.sName = I18N.get("org.openjump.core.ui.plugin.raster.CreateLatticeFromSelectedImageLayerPlugIn.Create-Lattice-from-Raster");
        this.sBand = I18N.get("org.openjump.core.ui.plugin.raster.CreatePolygonGridFromSelectedImageLayerPlugIn.band");
        this.sLattice = I18N.get("org.openjump.core.ui.plugin.raster.CreateLatticeFromSelectedImageLayerPlugIn.lattice");
        this.sCreatePoints = I18N.get("org.openjump.core.ui.plugin.raster.CreateLatticeFromSelectedImageLayerPlugIn.creating-points");

        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(
                this, //exe
                new String[]{MenuNames.RASTER}, //menu path
                sName,
                false, //checkbox
                null, //icon
                createEnableCheck(context.getWorkbenchContext())); //enable check
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createAtLeastNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class));
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return null;
    }

    public boolean execute(PlugInContext context) throws Exception {
        //-- not used here
        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        monitor.allowCancellationRequests();
        GeometryFactory gf = new GeometryFactory();
        //-- get the rasterimage layer
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);
        //System.out.println(rLayer);

        if (rLayer == null) {
            context.getWorkbenchFrame().warnUser(I18N.get("pirol.plugIns.EditAttributeByFormulaPlugIn.no-layer-selected"));
            return;
        }

        //-- create a sextante raster layer since it is easier to handle
        OpenJUMPSextanteRasterLayer rstLayer = new OpenJUMPSextanteRasterLayer();
        rstLayer.create(rLayer);
        // create a gridwrapper to later access the cells
        GridWrapperNotInterpolated gwrapper = new GridWrapperNotInterpolated(rstLayer, rstLayer.getLayerGridExtent());
        //-- create the FeatureSchema
        FeatureSchema fs = new FeatureSchema();
        fs.addAttribute("geometry", AttributeType.GEOMETRY);
        fs.addAttribute("cellid_x", AttributeType.INTEGER);
        fs.addAttribute("cellid_y", AttributeType.INTEGER);
        int numBands = rstLayer.getBandsCount();
        for (int i = 0; i < numBands; i++) {
            fs.addAttribute(sBand + "_" + i, AttributeType.DOUBLE);
        }
        //-- create a new empty dataset
        FeatureCollection fd = new FeatureDataset(fs);
        //-- create points
        monitor.report(sCreatePoints);
        int nx = rstLayer.getLayerGridExtent().getNX();
        int ny = rstLayer.getLayerGridExtent().getNY();
        //int numPoints = nx * ny;
        for (int x = 0; x < nx; x++) {//cols
            for (int y = 0; y < ny; y++) {//rows
                Feature ftemp = new BasicFeature(fs);
                Point2D pt = rstLayer.getLayerGridExtent().getWorldCoordsFromGridCoords(x, y);
                Geometry centerPoint = gf.createPoint(new Coordinate(pt.getX(), pt.getY()));
                ftemp.setGeometry(centerPoint);
                for (int i = 0; i < numBands; i++) {
                    double value = gwrapper.getCellValueAsDouble(x, y, i);
                    ftemp.setAttribute(sBand + "_" + i, value);
                }
                ftemp.setAttribute("cellid_x", x);
                ftemp.setAttribute("cellid_y", y);
                //-- add the feature
                fd.add(ftemp);
                //-- check if user wants to stop
                if (monitor.isCancelRequested()) {
                    if (fd.size() > 0) {
                        context.addLayer(StandardCategoryNames.RESULT, rstLayer.getName() + "_cancel_" + sLattice, fd);
                    }
                    return;
                }
            }
        }
        //-- output
        if (fd.size() > 0) {
            context.addLayer(StandardCategoryNames.RESULT, rstLayer.getName() + "_" + sLattice, fd);
        }
    }
}
