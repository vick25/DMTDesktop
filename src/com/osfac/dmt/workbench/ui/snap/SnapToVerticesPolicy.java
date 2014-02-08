package com.osfac.dmt.workbench.ui.snap;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.osfac.dmt.geom.CoordUtil;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.plugin.VerticesInFencePlugIn;
import java.util.ArrayList;
import java.util.Iterator;

public class SnapToVerticesPolicy implements SnapPolicy {

    private GeometryFactory factory = new GeometryFactory();
    //On-screen features are cached. The cache is built lazily. [Bob Boseko]
    private Blackboard blackboard;

    public SnapToVerticesPolicy(Blackboard blackboard) {
        this.blackboard = blackboard;
    }
    public static final String ENABLED_KEY = SnapToVerticesPolicy.class.getName() + " - ENABLED";

    public Coordinate snap(LayerViewPanel panel, Coordinate originalPoint) {
        if (!blackboard.get(ENABLED_KEY, false)) {
            return null;
        }
        Geometry bufferedTransformedCursorLocation;
        bufferedTransformedCursorLocation =
                factory.createPoint(originalPoint).buffer(SnapManager.getToleranceInPixels(blackboard) / panel.getViewport().getScale());
        ArrayList vertices = new ArrayList();
        for (Iterator i =
                VisiblePointsAndLinesCache
                .instance(panel)
                .getTree()
                .query(bufferedTransformedCursorLocation.getEnvelopeInternal())
                .iterator();
                i.hasNext();) {
            Geometry pointsAndLines = (Geometry) i.next();
            vertices.addAll(
                    VerticesInFencePlugIn
                    .verticesInFence(pointsAndLines, bufferedTransformedCursorLocation, true)
                    .getCoordinates());
        }
        if (vertices.isEmpty()) {
            return null;
        }
        return CoordUtil.closest(vertices, originalPoint);
    }
}
