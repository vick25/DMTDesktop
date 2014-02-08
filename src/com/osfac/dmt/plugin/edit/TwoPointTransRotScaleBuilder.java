package com.osfac.dmt.plugin.edit;

import com.osfac.dmt.geom.AffineTransformation;
import com.osfac.dmt.geom.Angle;
import com.vividsolutions.jts.geom.*;

/**
 * Class used by {@link AffineTransformation} to build a transformation from a
 * src Coordinate[2] array and a dest Coordinate[2] array.
 *
 * @author Martin Davis
 */
class TwoPointTransRotScaleBuilder extends TransRotScaleBuilder {

    /**
     * Creates a builder from two Coordinate[2] arrays defining the src and dest
     * vectors.
     *
     * @param srcVector the two Coordinates defining the src vector
     * @param destVector the two Coordinates defining the dest vector
     */
    TwoPointTransRotScaleBuilder(Coordinate[] srcVector, Coordinate[] destVector) {
        super(srcVector, destVector);
    }

    protected void compute(Coordinate[] srcVector, Coordinate[] destVector) {
        originX = srcVector[0].x;
        originY = srcVector[0].y;

        double srcLen = srcVector[0].distance(srcVector[1]);
        double destLen = destVector[0].distance(destVector[1]);

        boolean isZeroLength = (srcLen == 0.0 || destLen == 0.0);

        if (!isZeroLength) {
            scaleX = destLen / srcLen;
            scaleY = scaleX;

            double angleSrc = Angle.angle(srcVector[0], srcVector[1]);
            double angleDest = Angle.angle(destVector[0], destVector[1]);
            double angleRad = angleDest - angleSrc;
            angle = Math.toDegrees(angleRad);
        }

        dx = destVector[0].x - srcVector[0].x;
        dy = destVector[0].y - srcVector[0].y;
    }
}
