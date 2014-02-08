package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.dmt.coordsys.CoordinateSystem;
import com.osfac.dmt.coordsys.impl.PredefinedCoordinateSystems;

public class SRSUtils {

    //
    // If the coordinate system string has the form "EPSG:someNumber" then see
    // if we can get that number and create a more human readable string.
    //
    public static String getName(String srsCode) {
        final String epsg = "EPSG:";
        String stringToShow = srsCode;

        if (srsCode.startsWith(epsg)) {
            String intPart = srsCode.substring(5, srsCode.length());

            try {
                int epsgCode = Integer.parseInt(intPart);
                CoordinateSystem cs = PredefinedCoordinateSystems.getCoordinateSystem(epsgCode);

                if (cs != null) {
                    stringToShow = cs.getName();
                }
            } catch (Exception ignored) {
                // do nothing
            }
        }

        return stringToShow;
    }
}