package com.osfac.dmt.plugin.edit;

import com.osfac.dmt.geom.AffineTransformation;
import com.osfac.dmt.geom.Angle;
import com.vividsolutions.jts.geom.*;

/**
 * Class used by {@link AffineTransformation} to build a transformation from a
 * src Coordinate[3] array and a dest Coordinate[3] array.
 *
 * @author Martin Davis
 */
class TriPointTransRotScaleBuilder extends TransRotScaleBuilder {

    /**
     * Creates a builder from two Coordinate[3] arrays defining the src and dest
     * control points
     *
     * @param srcVector the two Coordinates defining the src vector
     * @param destVector the two Coordinates defining the dest vector
     */
    TriPointTransRotScaleBuilder(Coordinate[] srcPt, Coordinate[] destPt) {
        super(srcPt, destPt);
    }

    protected void compute(Coordinate[] srcPt, Coordinate[] destPt) {
        /**
         * For now just extract a Y scale from the third pt. In future could do
         * shear too.
         */

        /*
         AffineTransformationBuilder atBuilder = new AffineTransformationBuilder(
         srcPt[0],
         srcPt[1],
         srcPt[2],
         destPt[0],
         destPt[1],
         destPt[2]
         );
         */
        originX = srcPt[1].x;
        originY = srcPt[1].y;

        double srcLenBase = srcPt[1].distance(srcPt[2]);
        double destLenBase = destPt[1].distance(destPt[2]);

        double srcLenSide = srcPt[0].distance(srcPt[1]);
        double destLenSide = destPt[0].distance(destPt[1]);

        boolean isZeroLength = (srcLenBase == 0.0
                || destLenBase == 0.0
                || srcLenSide == 0.0
                || destLenSide == 0.0);


        if (!isZeroLength) {
            scaleX = destLenBase / srcLenBase;
            scaleY = destLenSide / srcLenSide;

            double angleSrc = Angle.angle(srcPt[1], srcPt[2]);
            double angleDest = Angle.angle(destPt[1], destPt[2]);
            double angleRad = angleDest - angleSrc;
            angle = Math.toDegrees(angleRad);
        }

        dx = destPt[1].x - srcPt[1].x;
        dy = destPt[1].y - srcPt[1].y;
    }
}
