package com.osfac.dmt.coordsys;

import com.osfac.dmt.I18N;
import com.vividsolutions.jts.util.Assert;
import java.io.Serializable;

/**
 * This class represents a coordinate system.
 */
public class CoordinateSystem implements Comparable, Serializable {

    private static final long serialVersionUID = -811718450919581831L;
    private Projection projection;
    private String name;
    private int epsgCode;
    public static final CoordinateSystem UNSPECIFIED = new CoordinateSystem(I18N.get("coordsys.CoordinateSystem.unspecified"),
            0, null) {
                private static final long serialVersionUID = -811718450919581831L;

                @Override
                public Projection getProjection() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getEPSGCode() {
                    throw new UnsupportedOperationException();
                }
            };

    /**
     * @see <a
     * href="http://www.javaworld.com/javaworld/javatips/jw-javatip122.html">www.javaworld.com</a>
     */
    private Object readResolve() {
        return name.equals(UNSPECIFIED.name) ? UNSPECIFIED : this;
    }

    public CoordinateSystem(String name, int epsgCode, Projection projection) {
        this.name = name;
        this.projection = projection;
        this.epsgCode = epsgCode;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Projection getProjection() {
        return projection;
    }

    public int getEPSGCode() {
        return epsgCode;
    }

    @Override
    public int compareTo(Object o) {
        Assert.isTrue(o instanceof CoordinateSystem);
        if (this == o) {
            return 0;
        }
        if (this == UNSPECIFIED) {
            return -1;
        }
        if (o == UNSPECIFIED) {
            return 1;
        }
        return toString().compareTo(o.toString());
    }
}
