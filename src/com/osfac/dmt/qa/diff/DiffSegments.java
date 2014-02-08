package com.osfac.dmt.qa.diff;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.task.TaskMonitor;
import java.util.*;

/**
 * Finds all line segments in two FeatureCollections which occur once only.
 */
public class DiffSegments {

    private FeatureCollection[] fc = new FeatureCollection[2];
    private UnmatchedEdgeExtracter[] uee = new UnmatchedEdgeExtracter[2];
    private TaskMonitor monitor;

    public DiffSegments(TaskMonitor monitor) {
        this.monitor = monitor;
    }

    public void setSegments(int index, FeatureCollection fc) {
        this.fc[index] = fc;
        uee[index] = new UnmatchedEdgeExtracter();
        for (Iterator it = fc.getFeatures().iterator(); it.hasNext();) {
            Feature f = (Feature) it.next();
            uee[index].add(f.getGeometry());
        }
    }

    /**
     * Returns all the subedges from fc which are unmatched.
     */
    public FeatureCollection computeDiffEdges(int index) {
        List diffEdges = new ArrayList();
        UnmatchedEdgeExtracter otherUee = uee[1 - index];
        for (Iterator i = fc[index].getFeatures().iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            otherUee.getDiffEdges(f.getGeometry(), diffEdges);
        }
        return FeatureDatasetFactory.createFromGeometry(diffEdges);
    }
}
