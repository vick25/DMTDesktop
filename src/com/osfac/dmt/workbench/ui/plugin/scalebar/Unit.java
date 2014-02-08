package com.osfac.dmt.workbench.ui.plugin.scalebar;

/**
 * Examples: meter, inch, angstrom!
 */
public class Unit implements Comparable {

    private double modelValue;
    private String name;

    public Unit(String name, double modelValue) {
        this.name = name;
        this.modelValue = modelValue;
    }

    public String toString() {
        return getName();
    }

    /**
     * @return width of one unit, in model-space coordinates
     */
    public double getModelValue() {
        return modelValue;
    }

    public String getName() {
        return name;
    }

    public int compareTo(Object o) {
        Unit other = (Unit) o;

        if (modelValue == other.modelValue) {
            return 0;
        }

        if (modelValue < other.modelValue) {
            return -1;
        }

        return +1;
    }
}
