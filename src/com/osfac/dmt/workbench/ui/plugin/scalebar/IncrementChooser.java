package com.osfac.dmt.workbench.ui.plugin.scalebar;

import com.osfac.dmt.util.MathUtil;
import com.vividsolutions.jts.util.Assert;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Chooses a good size for the scale-bar increments.
 */
public class IncrementChooser {

    public IncrementChooser() {
    }

    /**
     * @return a Quantity whose value will be a multiple of 10
     */
    public RoundQuantity chooseGoodIncrement(Collection units,
            double idealIncrement) {
        return goodIncrement(goodUnit(units, idealIncrement), idealIncrement);
    }

    /**
     * @return the Unit that is the fewest orders of magnitude away from the
     * ideal increment, preferably smaller than the ideal increment.
     */
    private Unit goodUnit(Collection units, double idealIncrement) {
        Unit goodUnit = (Unit) Collections.min(units);

        for (Iterator i = units.iterator(); i.hasNext();) {
            Unit candidateUnit = (Unit) i.next();

            if (candidateUnit.getModelValue() > idealIncrement) {
                continue;
            }

            if (distance(candidateUnit.getModelValue(), idealIncrement) < distance(
                    goodUnit.getModelValue(), idealIncrement)) {
                goodUnit = candidateUnit;
            }
        }

        return goodUnit;
    }

    private double distance(double a, double b) {
        return Math.abs(MathUtil.orderOfMagnitude(a)
                - MathUtil.orderOfMagnitude(b));
    }

    /**
     * @return an amount of the form 1 x 10^n, 2 x 10^n or 5 x 10^n that is
     * closest to the ideal increment without exceeding it.
     */
    private RoundQuantity goodIncrement(Unit unit, double idealIncrement) {
        RoundQuantity mantissa1Candidate = new RoundQuantity(1,
                (int) Math.floor(MathUtil.orderOfMagnitude(idealIncrement)
                - MathUtil.orderOfMagnitude(unit.getModelValue())), unit);
        // MD - hack to get around Nan exception
        if (Double.isNaN(idealIncrement)) {
            idealIncrement = mantissa1Candidate.getModelValue();
        }
        Assert.isTrue(mantissa1Candidate.getModelValue() <= idealIncrement, "unit=" + unit.getModelValue() + ", ideal increment=" + idealIncrement);

        RoundQuantity mantissa2Candidate = new RoundQuantity(2,
                mantissa1Candidate.getExponent(), unit);
        RoundQuantity mantissa5Candidate = new RoundQuantity(5,
                mantissa1Candidate.getExponent(), unit);

        if (mantissa5Candidate.getModelValue() <= idealIncrement) {
            return mantissa5Candidate;
        }

        if (mantissa2Candidate.getModelValue() <= idealIncrement) {
            return mantissa2Candidate;
        }

        return mantissa1Candidate;
    }
}
