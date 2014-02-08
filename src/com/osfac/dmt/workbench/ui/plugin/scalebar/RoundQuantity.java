package com.osfac.dmt.workbench.ui.plugin.scalebar;

import java.text.DecimalFormat;

/**
 * Numbers with one or two significant digits, like 1 x 10^3, or 5 x 10^2.
 */
public class RoundQuantity {

    private Unit unit;
    private int mantissa;
    private int exponent;

    public RoundQuantity(int mantissa, int exponent, Unit unit) {
        this.mantissa = mantissa;
        this.exponent = exponent;
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getMantissa() {
        return mantissa;
    }

    public String toString() {
        return getAmountString() + " " + getUnit();
    }

    public String getAmountString() {
        if (getMantissa() == 0) {
            return "0";
        }

        if ((0 <= getExponent()) && (getExponent() <= 3)) {
            return new DecimalFormat("#").format(getAmount());
        }

        if ((-4 <= getExponent()) && (getExponent() < 0)) {
            return new DecimalFormat("#.####").format(getAmount());
        }

        return getMantissa() + "E" + getExponent();
    }

    public int getExponent() {
        return exponent;
    }

    public double getAmount() {
        return mantissa * Math.pow(10, exponent);
    }

    public double getModelValue() {
        return getAmount() * unit.getModelValue();
    }
}
