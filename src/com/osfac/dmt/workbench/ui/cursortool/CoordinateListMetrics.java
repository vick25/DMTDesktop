package com.osfac.dmt.workbench.ui.cursortool;

import com.vividsolutions.jts.geom.Coordinate;
import com.osfac.dmt.I18N;
import com.osfac.dmt.geom.Angle;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.util.*;

/**
 * Generates strings describing metrics for coordinate lists.
 *
 * @author Martin Davis
 * @version 1.0
 */
public class CoordinateListMetrics {

    String sArea = I18N.get("ui.cursortool.CoordinateListMetrics.Area");
    String sAngle = I18N.get("ui.cursortool.CoordinateListMetrics.Angle");
    String sDistance = I18N.get("ui.cursortool.CoordinateListMetrics.Distance");

    public CoordinateListMetrics() {
    }

    public void displayMetrics(List coordinates, LayerViewPanel panel) {
        displayMetrics(coordinates, panel, false);
    }

    /**
     * Display the coordinates metrics with the option to compute the distance
     * for a closed geometry.
     *
     * @param coordinates
     * @param panel
     * @param closedDistance
     */
    public void displayMetrics(List coordinates, LayerViewPanel panel, boolean closedDistance) {
        panel.getContext().setStatusMessage(getMetricsString(coordinates, panel, closedDistance));
    }

    public String getMetricsString(List coordinates, LayerViewPanel panel) {
        return getMetricsString(coordinates, panel, false);
    }

    /**
     * Get's the the coordinates metrics with the option to compute the distance
     * for a closed geometry.
     *
     * @param coordinates
     * @param panel
     * @param closedDistance
     * @return the String representing the geometry metrics
     */
    public String getMetricsString(List coordinates, LayerViewPanel panel, boolean closedDistance) {
        double dist = distance(coordinates, closedDistance);
        String dispStr = sDistance + ": " + panel.format(dist);

        double angle = angle(coordinates);
        dispStr += "   " + sAngle + ": " + panel.format(angle);

        if (coordinates.size() > 2) {
            double area = area(coordinates);
            dispStr += "   " + sArea + ": " + panel.format(area);
        }
        return dispStr;
    }

    public static double distance(List coordinates) {
        return distance(coordinates, false);
    }

    /**
     * Computes the distance with the option to compute the distance for a
     * closed geometry.
     *
     * @param coordinates
     * @param closedDistance
     * @return the sum of the distance between coordinates
     */
    public static double distance(List coordinates, boolean closedDistance) {
        double distance = 0;
        for (int i = 1; i < coordinates.size(); i++) {
            distance += ((Coordinate) coordinates.get(i - 1)).distance((Coordinate) coordinates.get(
                    i));
        }
        // compute the last distance part from the last coordinate to the first, if we are in closed mode
        if (coordinates.size() > 2 && closedDistance) {
            distance += ((Coordinate) coordinates.get(coordinates.size() - 1)).distance((Coordinate) coordinates.get(0));
        }
        return distance;
    }

    /**
     * Computes the angle between the last 2 segments in the coordinates list
     *
     * @param coordinates
     * @return the angle in degrees
     */
    public static double angle(List coordinates) {
        int size = coordinates.size();
        if (size <= 1) {
            return 0.0;
        }
        Coordinate p1 = (Coordinate) coordinates.get(size - 2);
        Coordinate p2 = (Coordinate) coordinates.get(size - 1);
        // if only 2 coords, compute angle relative to X axis
        Coordinate p0 = null;
        if (size > 2) {
            p0 = (Coordinate) coordinates.get(size - 3);
        } else {
            p0 = new Coordinate(p1.x + 1.0, p1.y);
        }

        double angRad = Angle.angleBetween(p1, p0, p2);
        return Math.toDegrees(angRad);
    }

    /**
     * Computes the area for the coordinates list. The area is forcd to be
     * positive. The coordinate list can be open, and the closing coordinate is
     * supplied
     */
    private static double area(List coordinates) {
        if (coordinates.size() < 3) {
            return 0.0;
        }
        double sum = 0.0;
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate b = (Coordinate) coordinates.get(i);
            int nexti = i + 1;
            if (nexti > coordinates.size() - 1) {
                nexti = 0;
            }
            Coordinate c = (Coordinate) coordinates.get(nexti);
            sum += (b.x + c.x) * (c.y - b.y);
        }
        double signedArea = -sum / 2.0;
        if (signedArea >= 0) {
            return signedArea;
        }
        return -signedArea;
    }
}