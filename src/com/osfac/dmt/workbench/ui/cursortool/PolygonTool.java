package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;

public abstract class PolygonTool extends MultiClickTool {

    public PolygonTool() {
        setCloseRing(true);
        setMetricsDisplay(new CoordinateListMetrics());
    }

    /**
     * Callers should check whether the polygon returned is valid.
     */
    protected Polygon getPolygon() throws NoninvertibleTransformException {
        ArrayList closedPoints = new ArrayList(getCoordinates());

        if (!closedPoints.get(0).equals(closedPoints.get(closedPoints.size() - 1))) {
            closedPoints.add(new Coordinate((Coordinate) closedPoints.get(0)));
        }

        return new GeometryFactory().createPolygon(
                new GeometryFactory().createLinearRing(toArray(closedPoints)),
                null);
    }

    protected boolean checkPolygon() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 3) {
            getPanel().getContext().warnUser(I18N.get("ui.cursortool.PolygonTool.the-polygon-must-have-at-least-3-points"));

            return false;
        }

        IsValidOp isValidOp = new IsValidOp(getPolygon());

        if (!isValidOp.isValid()) {
            getPanel().getContext().warnUser(isValidOp.getValidationError().getMessage());

            if (getWorkbench()
                    .getBlackboard()
                    .get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                return false;
            }
        }

        return true;
    }
}
