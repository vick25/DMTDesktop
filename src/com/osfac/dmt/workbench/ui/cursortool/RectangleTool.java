package com.osfac.dmt.workbench.ui.cursortool;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.osfac.dmt.geom.CoordUtil;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class RectangleTool extends DragTool {

    public RectangleTool() {
    }

    protected Polygon getRectangle() throws NoninvertibleTransformException {
        Envelope e = new Envelope(
                getModelSource().x,
                getModelDestination().x,
                getModelSource().y,
                getModelDestination().y);

        return new GeometryFactory().createPolygon(
                new GeometryFactory().createLinearRing(
                new Coordinate[]{
                    new Coordinate(e.getMinX(), e.getMinY()),
                    new Coordinate(e.getMinX(), e.getMaxY()),
                    new Coordinate(e.getMaxX(), e.getMaxY()),
                    new Coordinate(e.getMaxX(), e.getMinY()),
                    new Coordinate(e.getMinX(), e.getMinY())}),
                null);
    }

    private Collection verticesToSnap(Coordinate source, Coordinate destination) {
        ArrayList verticesToSnap = new ArrayList();
        verticesToSnap.add(destination);
        verticesToSnap.add(new Coordinate(source.x, destination.y));
        verticesToSnap.add(new Coordinate(destination.x, source.y));

        return verticesToSnap;
    }

    protected void setModelDestination(Coordinate modelDestination) {
        for (Iterator i = verticesToSnap(getModelSource(), modelDestination).iterator(); i.hasNext();) {
            Coordinate vertex = (Coordinate) i.next();
            Coordinate snappedVertex = snap(vertex);

            if (getSnapManager().wasSnapCoordinateFound()) {
                this.modelDestination = CoordUtil.add(modelDestination, CoordUtil.subtract(snappedVertex, vertex));
                return;
            }

        }
        this.modelDestination = modelDestination;
        return;
    }

    protected void setModelSource(Coordinate modelSource) {
        this.modelSource = snap(modelSource);
    }
}
