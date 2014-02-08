package com.osfac.dmt.plugin.edit;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.CoordinateArrays;
import com.vividsolutions.jts.geom.*;
import java.util.*;

/**
 * Extracts the unique segments from a FeatureCollection.
 *
 * This class has been replaced by SegmentExtracter, which is able to return
 * segments occuring between minOccur and maxOccur times in the dataset [Michael
 * Michaud 2007-05-15]
 *
 * @author Martin Davis
 * @version 1.0
 */
public class UniqueSegmentsExtracter {

    private static final GeometryFactory factory = new GeometryFactory();
    private Set segmentSet = new TreeSet();
    private LineSegment querySegment = new LineSegment();
    private boolean countZeroLengthSegments = true;
    private TaskMonitor monitor;
    private Geometry fence = null;
//  private LineSegmentEnvelopeIntersector lineEnvInt;

    public UniqueSegmentsExtracter() {
    }

    /**
     * Creates a new counter.
     *
     * @param monitor
     */
    public UniqueSegmentsExtracter(TaskMonitor monitor) {
        this.monitor = monitor;
    }

    /*
     public void setFence(Geometry fence)
     {
     this.fence = fence;
     //lineEnvInt = new LineSegmentEnvelopeIntersector();
     }
     */
    public void add(FeatureCollection fc) {
        monitor.allowCancellationRequests();
        int totalFeatures = fc.size();
        int j = 0;
        for (Iterator i = fc.iterator(); i.hasNext() && !monitor.isCancelRequested();) {
            Feature feature = (Feature) i.next();
            j++;
            monitor.report(j, totalFeatures, "features");
            add(feature);
        }
    }

    public void add(Feature f) {
        Geometry g = f.getGeometry();
        // skip if using fence and feature is not in fence
        if (fence != null && !g.intersects(fence)) {
            return;
        }

        List coordArrays = CoordinateArrays.toCoordinateArrays(g, true);
        for (Iterator i = coordArrays.iterator(); i.hasNext();) {
            Coordinate[] coord = (Coordinate[]) i.next();
            for (int j = 0; j < coord.length - 1; j++) {
                // skip if using fence AND seg is not in fence
      /*
                 if (fence != null) {
                 LineString segLine = factory.createLineString(new Coordinate[] { coord[j], coord[j + 1] });
                 if (! fence.intersects(segLine))
                 continue;
                 }
                 */
                add(coord[j], coord[j + 1]);
            }
        }
    }

    public void add(Coordinate p0, Coordinate p1) {
        // check for zero-length segment
        boolean isZeroLength = p0.equals(p1);
        if (!countZeroLengthSegments && isZeroLength) {
            return;
        }

        LineSegment lineseg = new LineSegment(p0, p1);
        lineseg.normalize();

        segmentSet.add(lineseg);
    }

    public Collection getSegments() {
        return segmentSet;
    }
}