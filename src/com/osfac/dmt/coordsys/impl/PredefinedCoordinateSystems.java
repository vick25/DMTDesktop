package com.osfac.dmt.coordsys.impl;

import com.osfac.dmt.coordsys.CoordinateSystem;
import com.osfac.dmt.coordsys.Radius;
import com.osfac.dmt.coordsys.Spheroid;
import com.vividsolutions.jts.util.Assert;

/**
 * Provides a number of named coordinate systems.
 */
public class PredefinedCoordinateSystems {

    public static final CoordinateSystem BC_ALBERS_NAD_83 = new CoordinateSystem("BC Albers",
            42102,
            new Albers() {
                {
                    setSpheroid(new Spheroid(new Radius(Radius.GRS80)));
                    setParameters(-126.0, 50.0, 58.5, 45.0, 1000000.0, 0.0);
                }
            });
    public static final CoordinateSystem GEOGRAPHICS_WGS_84 = new CoordinateSystem("Geographics",
            4326, new LatLong());
    public static final CoordinateSystem UTM_07N_WGS_84 = createUTMNorth(7);
    public static final CoordinateSystem UTM_08N_WGS_84 = createUTMNorth(8);
    public static final CoordinateSystem UTM_09N_WGS_84 = createUTMNorth(9);
    public static final CoordinateSystem UTM_10N_WGS_84 = createUTMNorth(10);
    public static final CoordinateSystem UTM_11N_WGS_84 = createUTMNorth(11);

    private PredefinedCoordinateSystems() {
    }

    private static CoordinateSystem createUTMNorth(final int zone) {
        Assert.isTrue(1 <= zone && zone <= 60);
        //Pad with zero to facilitate sorting [Bob Boseko]
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N", 32600 + zone,
                new UniversalTransverseMercator() {
                    {
                        setSpheroid(new Spheroid(new Radius(Radius.GRS80)));
                        setParameters(zone);
                    }
                });
    }

    public static CoordinateSystem getCoordinateSystem(int epsgCode) {
        CoordinateSystem cs = null;

        if (epsgCode == GEOGRAPHICS_WGS_84.getEPSGCode()) {
            cs = GEOGRAPHICS_WGS_84;
        } else if (epsgCode == BC_ALBERS_NAD_83.getEPSGCode()) {
            cs = BC_ALBERS_NAD_83;
        } else if (epsgCode == UTM_07N_WGS_84.getEPSGCode()) {
            cs = UTM_07N_WGS_84;
        } else if (epsgCode == UTM_08N_WGS_84.getEPSGCode()) {
            cs = UTM_08N_WGS_84;
        } else if (epsgCode == UTM_09N_WGS_84.getEPSGCode()) {
            cs = UTM_09N_WGS_84;
        } else if (epsgCode == UTM_10N_WGS_84.getEPSGCode()) {
            cs = UTM_10N_WGS_84;
        } else if (epsgCode == UTM_11N_WGS_84.getEPSGCode()) {
            cs = UTM_11N_WGS_84;
        } else {
            // don't do an assertion - it should be alright if the EPSG code
            // is one of the predefined ones.
        }

        return cs;
    }
}
