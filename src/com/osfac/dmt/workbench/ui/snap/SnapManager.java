package com.osfac.dmt.workbench.ui.snap;

import com.vividsolutions.jts.geom.Coordinate;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Inputs and outputs are in model space, not view space.
 */
public class SnapManager {

    private static final String TOLERANCE_IN_PIXELS_KEY = SnapManager.class.getName()
            + " - TOLERANCE IN PIXELS";
    private ArrayList policies = new ArrayList();
    private boolean snapCoordinateFound;

    public SnapManager() {
    }

    public Coordinate snap(LayerViewPanel panel, Coordinate originalCoordinate) {
        for (Iterator i = policies.iterator(); i.hasNext();) {
            SnapPolicy policy = (SnapPolicy) i.next();
            Coordinate snapCoordinate = policy.snap(panel, originalCoordinate);

            if (snapCoordinate != null) {
                snapCoordinateFound = true;

                return snapCoordinate;
            }
        }

        snapCoordinateFound = false;

        return originalCoordinate;
    }

    public void addPolicies(Collection policies) {
        this.policies.addAll(policies);
    }

    public boolean wasSnapCoordinateFound() {
        return snapCoordinateFound;
    }

    public static int getToleranceInPixels(Blackboard blackboard) {
        return blackboard.get(TOLERANCE_IN_PIXELS_KEY, 10);
    }

    public static void setToleranceInPixels(int toleranceInPixels, Blackboard blackboard) {
        blackboard.put(TOLERANCE_IN_PIXELS_KEY, toleranceInPixels);
    }
}
