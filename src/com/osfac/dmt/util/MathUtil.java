package com.osfac.dmt.util;

/**
 * Additional math utilities.
 *
 * @see Math
 */
public class MathUtil {

    public MathUtil() {
    }

    public static double orderOfMagnitude(double x) {
        return base10Log(x);
    }

    public static double base10Log(double x) {
        return Math.log(x) / Math.log(10);
    }

    public static int mostSignificantDigit(double x) {
        return (int) (x / Math.pow(10, Math.floor(MathUtil.orderOfMagnitude(x))));
    }

    /**
     * Returns the average of two doubles
     *
     * @param a one of the doubles to average
     * @param b the other double to average
     * @return the average of two doubles
     */
    public static double avg(double a, double b) {
        return (a + b) / 2d;
    }
}
