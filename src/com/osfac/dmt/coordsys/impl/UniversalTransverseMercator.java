package com.osfac.dmt.coordsys.impl;

import com.osfac.dmt.coordsys.Geographic;
import com.osfac.dmt.coordsys.Planar;
import com.osfac.dmt.coordsys.Projection;
import com.osfac.dmt.coordsys.Spheroid;
import com.vividsolutions.jts.util.Assert;

/**
 * This class implements the Universal Transverse Mercator Projection.
 *
 * @version $Revision: 672 $
 * @author $Author: michaudm $  <pre>
 *  $Id: UniversalTransverseMercator.java 672 2007-04-07 13:17:05Z michaudm $
 *  $Date: 2007-04-07 15:17:05 +0200 (sam., 07 avr. 2007) $
 *  $Log$
 *  Revision 1.2  2007/04/07 13:17:03  michaudm
 *  52 javadoc tag warnings fixed
 *
 *  Revision 1.1  2005/06/16 15:25:29  javamap
 *  *** empty log message ***
 *
 *  Revision 1.2  2005/05/03 15:23:55  javamap
 *  *** empty log message ***
 *
 *  Revision 1.2  2003/11/05 05:18:44  dkim
 *  Added global header; cleaned up Javadoc.
 *
 *  Revision 1.1  2003/09/15 20:26:11  jaquino
 *  Reprojection
 *
 *  Revision 1.2  2003/07/25 17:01:03  gkostadinov
 *  Moved classses reponsible for performing the basic projection to a new
 *  package -- base.
 *
 *  Revision 1.1  2003/07/24 23:14:43  gkostadinov
 *  adding base projection classes
 *
 *  Revision 1.1  2003/06/20 18:34:30  gkostadinov
 *  Entering the source code into the CVS.
 * </pre>
 */
public class UniversalTransverseMercator extends Projection {

    private final static double SCALE_FACTOR = 0.9996;
    private final static double FALSE_EASTING = 500000.0;
    private final static double FALSE_NORTHING = 0.0;
    private TransverseMercator transverseMercator = new TransverseMercator();

    public UniversalTransverseMercator() {
    }
    private int zone = -1;

    /**
     * @param zone must be between 7 and 11
     */
    public void setParameters(int zone) {

        Assert.isTrue(zone >= 7, "UTM zone " + zone + " not supported");
        Assert.isTrue(zone <= 11, "UTM zone " + zone + " not supported");

        switch (zone) {
            case 7:
                transverseMercator.setParameters(-141.0);
                break;
            case 8:
                transverseMercator.setParameters(-135.0);
                break;
            case 9:
                transverseMercator.setParameters(-129.0);
                break;
            case 10:
                transverseMercator.setParameters(-123.0);
                break;
            case 11:
                transverseMercator.setParameters(-117.0);
                break;
            case 12:
                transverseMercator.setParameters(-111.0);
                break;
            default:
                Assert.shouldNeverReachHere();
        }
        this.zone = zone;
    }

    @Override
    public void setSpheroid(Spheroid s) {
        transverseMercator.setSpheroid(s);
    }

    @Override
    public Geographic asGeographic(Planar p, Geographic q) {

        Assert.isTrue(zone != -1, "Call #setParameters first");

        p.x = (p.x - FALSE_EASTING) / SCALE_FACTOR;
        p.y = (p.y - FALSE_NORTHING) / SCALE_FACTOR;
        transverseMercator.asGeographic(p, q);
        return q;
    }

    @Override
    public Planar asPlanar(Geographic q0, Planar p) {

        Assert.isTrue(zone != -1, "Call #setParameters first");

        transverseMercator.asPlanar(q0, p);
        p.x = SCALE_FACTOR * p.x + FALSE_EASTING;
        p.y = SCALE_FACTOR * p.y + FALSE_NORTHING;
        return p;
    }
}
