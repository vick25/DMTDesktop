package org.openjump.core.ui.plugin.layer.pirolraster;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.vividsolutions.jts.geom.Geometry;
import org.openjump.core.apitools.LayerTools;
import org.openjump.core.apitools.objecttyperoles.RoleOutline;
import org.openjump.core.rasterimage.RasterImageLayer;

/**
 * PlugIn to export the bounding box of the RasterImageLayer as a geometry
 * layer, so it can be changed, transformed to a fence and be re-applied to the
 * RasterImage.<br> This enables all geometry operations for RasterImages.
 *
 * @author Ole Rahn <br> <br>FH Osnabr&uuml;ck - University of Applied Sciences
 * Osnabr&uuml;ck, <br>Project: PIROL (2006), <br>Subproject: Daten- und
 * Wissensmanagement
 *
 * @version $Rev: 2509 $ [sstein] - 22.Feb.2009 - modified to work in OpenJUMP
 */
public class ExportEnvelopeAsGeometryPlugIn extends AbstractPlugIn {

    protected static FeatureSchema defaultSchema = null;

    public ExportEnvelopeAsGeometryPlugIn() {
        //super(new PersonalLogger(DebugUserIds.OLE));

        if (ExportEnvelopeAsGeometryPlugIn.defaultSchema == null) {
            ExportEnvelopeAsGeometryPlugIn.defaultSchema = new FeatureSchema();

            ExportEnvelopeAsGeometryPlugIn.defaultSchema.addAttribute("geometry", AttributeType.GEOMETRY);
        }
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return null;
    }

    /**
     * @inheritDoc
     */
    public String getName() {
        return I18N.get("org.openjump.core.ui.plugin.layer.pirolraster.ExportEnvelopeAsGeometryPlugIn.Export-Envelope-As-Geometry");
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);

        if (rLayer == null) {
            context.getWorkbenchFrame().warnUser(I18N.get("pirol.plugIns.EditAttributeByFormulaPlugIn.no-layer-selected"));
            return false;
        }

        Geometry geom = rLayer.getEnvelopeAsGeometry();

        if (geom == null) {
            return false;
        }

        FeatureCollection newFeaturecollection = new FeatureDataset((FeatureSchema) ExportEnvelopeAsGeometryPlugIn.defaultSchema.clone());

        BasicFeature feature = new BasicFeature((FeatureSchema) ExportEnvelopeAsGeometryPlugIn.defaultSchema.clone());

        feature.setAttribute("geometry", geom);

        newFeaturecollection.add(feature);

        LayerTools.addStandardResultLayer(I18N.get("org.openjump.core.ui.plugin.layer.pirolraster.ExportEnvelopeAsGeometryPlugIn.Geometry") + "-" + rLayer.getName(), newFeaturecollection, context, new RoleOutline());

        return false;
    }
}
