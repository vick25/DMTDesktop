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
 * Replace UniqueSegmentExtracter, adding the capability to return segments
 * occuring between minOccur and maxOccur times in the dataset. Now, this class
 * can do the same as FeatureSegmentCounter in JCS and should replace it
 * [Michael Michaud 2007-05-15]
 *
 * @author Martin Davis / Michael Michaud
 * @version 1.1
 */
public class SegmentsExtracter {

    private static final GeometryFactory factory = new GeometryFactory();
    private Set segmentSet = new TreeSet();
    // Segments are added to a TreeMap (LineSegment is Comparable)
    private Map segmentMap = new TreeMap();
    // private LineSegment querySegment = new LineSegment();
    private boolean countZeroLengthSegments = true;
    private TaskMonitor monitor;
    private Geometry fence = null;
    // private LineSegmentEnvelopeIntersector lineEnvInt;

    public SegmentsExtracter() {
    }

    /**
     * Creates a new counter.
     *
     * @param monitor
     */
    public SegmentsExtracter(TaskMonitor monitor) {
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

        SegmentCount count = (SegmentCount) segmentMap.get(lineseg);
        if (count == null) {
            segmentMap.put(lineseg, new SegmentCount(1));
        } else {
            count.increment();
        }

        //segmentSet.add(lineseg);
    }

    public Collection getSegments() {
        return segmentMap.keySet();
    }

    public Collection getSegments(int minOccurs, int maxOccurs) {
        List segmentList = new ArrayList();
        for (Iterator it = segmentMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            LineSegment ls = (LineSegment) entry.getKey();
            int count = ((SegmentCount) entry.getValue()).getCount();
            if (count >= minOccurs && count <= maxOccurs) {
                segmentList.add(ls);
            }
        }
        return segmentList;
    }

    public class SegmentCount {

        private int count = 0;

        public SegmentCount(int value) {
            this.count = value;
        }

        public int getCount() {
            return count;
        }

        public void increment() {
            count++;
        }
    }
}