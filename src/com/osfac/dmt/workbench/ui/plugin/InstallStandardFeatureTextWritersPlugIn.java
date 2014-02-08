package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.io.GMLGeometryWriter;
import com.osfac.dmt.util.Fmt;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.AbstractFeatureTextWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.WKTWriter;

public class InstallStandardFeatureTextWritersPlugIn extends AbstractPlugIn {

    public void initialize(PlugInContext context) throws Exception {
        context.getWorkbenchContext().getFeatureTextWriterRegistry().register(
                WKT_WRITER);
        context.getWorkbenchContext().getFeatureTextWriterRegistry().register(
                GML_WRITER);
        context.getWorkbenchContext().getFeatureTextWriterRegistry().register(
                COORDINATE_WRITER);
    }
    private static final AbstractFeatureTextWriter COORDINATE_WRITER = new AbstractFeatureTextWriter(
            false, "CL", "Coordinate List") {
        public String write(Feature feature) {
            StringBuilder s = new StringBuilder();
            String className = StringUtil.classNameWithoutQualifiers(feature
                    .getGeometry().getClass().getName());
            s.append(className).append("\n");
            Coordinate[] coordinates = feature.getGeometry().getCoordinates();
            for (int i = 0; i < coordinates.length; i++) {
                s.append("[").append(Fmt.fmt(i, 10)).append("] ");
                s.append(coordinates[i].x).append(", ").append(coordinates[i].y).append("\n");
            }
            return s.toString().trim();
        }
    };
    private static final AbstractFeatureTextWriter GML_WRITER = new AbstractFeatureTextWriter(
            false, "GML", "Geography Markup Language") {
        public String write(Feature feature) {
            return writer.write(feature.getGeometry());
        }
        private GMLGeometryWriter writer = new GMLGeometryWriter();
    };
    private static final AbstractFeatureTextWriter WKT_WRITER = new AbstractFeatureTextWriter(
            true, "WKT", "Well-Known Text") {
        public String write(Feature feature) {
            return wktWriter.write(feature.getGeometry()).trim();
        }
        private WKTWriter wktWriter = new WKTWriter();
    };
}
