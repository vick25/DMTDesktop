package com.osfac.dmt.coordsys.impl;

import com.osfac.dmt.coordsys.Geographic;
import com.osfac.dmt.coordsys.Planar;
import com.osfac.dmt.coordsys.Projection;

/**
 * This class implements the Mercator projection.
 *
 * @version $Revision: 4 $
 * @author $Author: javamap $
 *
 * <pre>
 *  $Id: Mercator.java 4 2005-06-16 15:27:48Z javamap $
 *  $Date: 2005-06-16 17:27:48 +0200 (jeu., 16 juin 2005) $
 *
 *  $Log$
 *  Revision 1.1  2005/06/16 15:25:29  javamap
 *  *** empty log message ***
 *
 *  Revision 1.2  2005/05/03 15:23:55  javamap
 *  *** empty log message ***
 *
 *  Revision 1.2  2003/11/05 05:12:52  dkim
 *  Added global header; cleaned up Javadoc.
 *
 *  Revision 1.1  2003/09/15 20:26:12  jaquino
 *  Reprojection
 *
 *  Revision 1.2  2003/07/25 17:01:04  gkostadinov
 *  Moved classses reponsible for performing the basic projection to a new
 *  package -- base.
 *
 *  Revision 1.1  2003/07/24 23:14:44  gkostadinov
 *  adding base projection classes
 *
 *  Revision 1.1  2003/06/20 18:34:31  gkostadinov
 *  Entering the source code into the CVS.
 * </pre>
 *
 */
public class Mercator extends Projection {

    double L0;// central meridian
    double X0;// false Easting
    double Y0;// false Northing
    Geographic q = new Geographic();

    public Mercator() {
        super();
    }

    /**
     * @param centralMeridian in degrees
     * @param falseEasting in metres
     * @param falseNorthing in metres
     */
    public void setParameters(double centralMeridian,
            double falseEasting,
            double falseNorthing) {
        L0 = centralMeridian / 180.0 * Math.PI;
        X0 = falseEasting;
        Y0 = falseNorthing;
    }

    @Override
    public Planar asPlanar(Geographic q0, Planar p) {
        q.lat = q0.lat / 180.0 * Math.PI;
        q.lon = q0.lon / 180.0 * Math.PI;
        forward(q, p);
        return p;
    }

    @Override
    public Geographic asGeographic(Planar p, Geographic q) {
        inverse(p, q);
        q.lat = q.lat * 180.0 / Math.PI;
        q.lon = q.lon * 180.0 / Math.PI;
        return q;
    }

    void forward(Geographic q, Planar p) {
        double a;
        double e;
        a = currentSpheroid.getA();
        e = currentSpheroid.getE();
        p.x = a * (q.lon - L0);
        p.y = (a / 2.0) * Math.log(
                ((1.0 + Math.sin(q.lat)) / (1.0 - Math.sin(q.lat)))
                * Math.pow(((1.0 - e * Math.sin(q.lat)) / (1.0 + e * Math.sin(q.lat))), e));
    }

    void inverse(Planar p, Geographic q) {
        double t;
        double delta;
        double phiI;
        double phi;
        double lambda;
        double a;
        double e;
        a = currentSpheroid.getA();
        e = currentSpheroid.getE();
        t = Math.exp(-p.y / a);
        //phi = Math.PI / 2.0 - 2.0 * Math.tan(t); -- transcription error
        phi = Math.PI / 2.0 - 2.0 * Math.atan(t);
        delta = 10000.0;
        do {
            phiI = Math.PI / 2.0 - 2.0 * Math.atan(
                    t * Math.pow(((1.0 - e * Math.sin(phi)) / (1.0 + e * Math.sin(phi))),
                            (e / 2.0)));
            delta = Math.abs(phiI - phi);
            phi = phiI;
        } while (delta > 1.0e-014);
        lambda = p.x / a + L0;
        q.lat = phi;
        q.lon = lambda;
    }
}
