package com.osfac.dmt.tools;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.IndexedFeatureCollection;
import com.osfac.dmt.task.TaskMonitor;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Takes two FeatureCollections and returns their overlay, which is a new
 * FeatureCollection containing the intersections of all pairs of input
 * features.
 */
public class OverlayEngine {

    private static Logger LOG = Logger.getLogger(OverlayEngine.class);
    private boolean splittingGeometryCollections = true;
    private boolean allowingPolygonsOnly = true;

    /**
     * Creates a new OverlayEngine.
     */
    public OverlayEngine() {
    }

    /**
     * Creates the overlay of the two datasets. The attributes from both
     * datasets will be transferred to the overlay.
     *
     * @param a the first dataset involved in the overlay
     * @param b the second dataset involved in the overlay
     * @return intersections of all pairs of input features
     */
    public FeatureCollection overlay(FeatureCollection a, FeatureCollection b,
            TaskMonitor monitor) {
        return overlay(a, b,
                new AttributeMapping(a.getFeatureSchema(), b.getFeatureSchema()),
                monitor);
    }

    /**
     * Creates the overlay of the two datasets. The attributes from the datasets
     * will be transferred as specified by the AttributeMapping.
     *
     * @param a the first dataset involved in the overlay
     * @param b the second dataset involved in the overlay
     * @param mapping specifies which attributes are transferred
     * @return intersections of all pairs of input features
     */
    public FeatureCollection overlay(FeatureCollection a, FeatureCollection b,
            AttributeMapping mapping, TaskMonitor monitor) {
        monitor.allowCancellationRequests();
        monitor.report(I18N.get("tools.OverlayEngine.indexing-second-feature-collection"));

        IndexedFeatureCollection indexedB = new IndexedFeatureCollection(b);
        monitor.report(I18N.get("tools.OverlayEngine.overlaying-feature-collections"));

        FeatureDataset overlay = new FeatureDataset(mapping.createSchema("GEOMETRY"));
        List aFeatures = a.getFeatures();

        for (int i = 0; (i < aFeatures.size()) && !monitor.isCancelRequested();
                i++) {
            Feature aFeature = (Feature) aFeatures.get(i);

            for (Iterator j = indexedB.query(aFeature.getGeometry()
                    .getEnvelopeInternal())
                    .iterator();
                    j.hasNext() && !monitor.isCancelRequested();) {
                Feature bFeature = (Feature) j.next();
                addIntersection(aFeature, bFeature, mapping, overlay, monitor);
            }

            monitor.report(i + 1, a.size(), "features");
        }

        return overlay;
    }

    private void addIntersection(Feature a, Feature b,
            AttributeMapping mapping, FeatureCollection overlay, TaskMonitor monitor) {
        if (!a.getGeometry().getEnvelope().intersects(b.getGeometry()
                .getEnvelope())) {
            return;
        }

        Geometry intersection = null;

        try {
            intersection = EnhancedPrecisionOp.intersection(a.getGeometry(),
                    b.getGeometry());
        } catch (Exception ex) {
            monitor.report(ex);
            LOG.error(a.getGeometry());
            LOG.error(b.getGeometry());
        }

        if ((intersection == null) || intersection.isEmpty()) {
            return;
        }

        addFeature(intersection, overlay, mapping, a, b);
    }

    protected void addFeature(Geometry intersection, FeatureCollection overlay,
            AttributeMapping mapping, Feature a, Feature b) {
        if (splittingGeometryCollections
                && intersection instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) intersection;

            for (int i = 0; i < gc.getNumGeometries(); i++) {
                addFeature(gc.getGeometryN(i), overlay, mapping, a, b);
            }

            return;
        }

        if (allowingPolygonsOnly && !(intersection instanceof Polygon || intersection instanceof MultiPolygon)) {
            return;
        }

        Feature feature = new BasicFeature(overlay.getFeatureSchema());
        mapping.transferAttributes(a, b, feature);
        feature.setGeometry(intersection);
        overlay.add(feature);
    }

    public void setSplittingGeometryCollections(
            boolean splittingGeometryCollections) {
        this.splittingGeometryCollections = splittingGeometryCollections;
    }

    public void setAllowingPolygonsOnly(boolean allowingPolygonsOnly) {
        this.allowingPolygonsOnly = allowingPolygonsOnly;
    }
}
