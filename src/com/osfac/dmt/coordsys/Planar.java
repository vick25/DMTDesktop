package com.osfac.dmt.coordsys;

/**
 *
 * @author $Author: javamap $
 * @version $Revision: 4 $
 *
 * <pre>
 * $Id: Planar.java 4 2005-06-16 15:27:48Z javamap $
 * $Date: 2005-06-16 17:27:48 +0200 (jeu., 16 juin 2005) $
 *
 * $Log$
 * Revision 1.1  2005/06/16 15:24:52  javamap
 * *** empty log message ***
 *
 * Revision 1.2  2005/05/03 15:13:29  javamap
 * *** empty log message ***
 *
 * Revision 1.2  2003/11/05 05:22:17  dkim
 * Added global header; cleaned up Javadoc.
 *
 * Revision 1.1  2003/09/15 20:26:11  jaquino
 * Reprojection
 *
 * Revision 1.2  2003/07/25 17:01:04  gkostadinov
 * Moved classses reponsible for performing the basic projection to a new
 * package -- base.
 *
 * Revision 1.1  2003/07/24 23:14:44  gkostadinov
 * adding base projection classes
 *
 * Revision 1.1  2003/06/20 18:34:31  gkostadinov
 * Entering the source code into the CVS.
 *
 * </pre>
 */
/**
 * A base class for planar coordinate systems.
 */
public class Planar {

    public double x, y, z;

    public Planar() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Planar(double _x, double _y) {
        x = _x;
        y = _y;
        z = 0;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
