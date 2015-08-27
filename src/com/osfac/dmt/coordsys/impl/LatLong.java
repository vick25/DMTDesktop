package com.osfac.dmt.coordsys.impl;

import com.osfac.dmt.coordsys.Geographic;
import com.osfac.dmt.coordsys.Planar;
import com.osfac.dmt.coordsys.Projection;

/**
 * Implements the Polyconic projection. * @author $Author: javamap $
 *
 * @version $Revision: 4 $
 */
public class LatLong extends Projection {

    @Override
    public Geographic asGeographic(Planar p, Geographic g) {
        g.lon = p.x;
        g.lat = p.y;
        return g;
    }

    @Override
    public Planar asPlanar(Geographic g, Planar p) {
        p.x = g.lon;
        p.y = g.lat;
        return p;
    }
}
