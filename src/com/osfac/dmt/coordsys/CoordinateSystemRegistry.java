package com.osfac.dmt.coordsys;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.osfac.dmt.coordsys.impl.PredefinedCoordinateSystems;
import com.osfac.dmt.util.Blackboard;

/**
 * Implements a registry for {@link CoordinateSystem}s.
 */
public class CoordinateSystemRegistry {

    private CoordinateSystemRegistry() {
        add(PredefinedCoordinateSystems.BC_ALBERS_NAD_83);
        add(PredefinedCoordinateSystems.GEOGRAPHICS_WGS_84);
        add(CoordinateSystem.UNSPECIFIED);
        add(PredefinedCoordinateSystems.UTM_07N_WGS_84);
        add(PredefinedCoordinateSystems.UTM_08N_WGS_84);
        add(PredefinedCoordinateSystems.UTM_09N_WGS_84);
        add(PredefinedCoordinateSystems.UTM_10N_WGS_84);
        add(PredefinedCoordinateSystems.UTM_11N_WGS_84);
    }

    public void add(CoordinateSystem coordinateSystem) {
        nameToCoordinateSystemMap.put(coordinateSystem.getName(), coordinateSystem);
    }

    public Collection getCoordinateSystems() {
        return Collections.unmodifiableCollection(nameToCoordinateSystemMap.values());
    }

    public CoordinateSystem get(String name) {
        return (CoordinateSystem) nameToCoordinateSystemMap.get(name);
    }
    private HashMap nameToCoordinateSystemMap = new HashMap();

    public static CoordinateSystemRegistry instance(Blackboard blackboard) {
        String COORDINATE_SYSTEMS_KEY = CoordinateSystemRegistry.class.getName()
                + " - COORDINATE SYSTEMS";

        if (blackboard.get(COORDINATE_SYSTEMS_KEY) == null) {
            blackboard.put(COORDINATE_SYSTEMS_KEY, new CoordinateSystemRegistry());
        }

        return (CoordinateSystemRegistry) blackboard.get(COORDINATE_SYSTEMS_KEY);
    }
}
