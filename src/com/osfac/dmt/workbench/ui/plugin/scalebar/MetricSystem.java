package com.osfac.dmt.workbench.ui.plugin.scalebar;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This will eventually implement an interface called MeasurementSystem.
 */
public class MetricSystem {

    private double modelUnitsPerMetre;

    public MetricSystem(double modelUnitsPerMetre) {
        this.modelUnitsPerMetre = modelUnitsPerMetre;
    }

    public Collection createUnits() {
        ArrayList units = new ArrayList();

        //Leave out the lesser known units, like fm. [Bob Boseko]
        units.add(new Unit("km", 1E3 * modelUnitsPerMetre));
        units.add(new Unit("m", 1E0 * modelUnitsPerMetre));
        units.add(new Unit("mm", 1E-3 * modelUnitsPerMetre));
        units.add(new Unit("um", 1E-6 * modelUnitsPerMetre));
        units.add(new Unit("nm", 1E-9 * modelUnitsPerMetre));
        units.add(new Unit("pm", 1E-12 * modelUnitsPerMetre));

        return units;
    }
}
