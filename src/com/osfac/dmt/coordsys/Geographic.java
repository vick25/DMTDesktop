package com.osfac.dmt.coordsys;

/**
 * This class is a base for geographic coordinate systems.
 *
 * @author $Author: javamap $
 * @version $Revision: 4 $
 *
 * <pre>
 *  $Id: Geographic.java 4 2005-06-16 15:27:48Z javamap $
 *  $Date: 2005-06-16 17:27:48 +0200 (jeu., 16 juin 2005) $
 *  $Log$
 *  Revision 1.1  2005/06/16 15:24:52  javamap
 *  *** empty log message ***
 *
 *  Revision 1.2  2005/05/03 15:13:05  javamap
 *  *** empty log message ***
 *
 *  Revision 1.2  2003/11/05 05:20:42  dkim
 *  Added global header; cleaned up Javadoc.
 *
 *  Revision 1.1  2003/09/15 20:26:11  jaquino
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
public class Geographic {

    public double lat, lon, hgt;

    public Geographic() {
        lat = 0;
        lon = 0;
        hgt = 0;
    }

    public Geographic(double _lat, double _lon) {
        lat = _lat;
        lon = _lon;
        hgt = 0;
    }

    public String toString() {
        return lat + ", " + lon;
    }
}
