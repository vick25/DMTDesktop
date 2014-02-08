package com.osfac.dmt.workbench.ui.snap;

import com.vividsolutions.jts.geom.Coordinate;
import com.osfac.dmt.workbench.ui.LayerViewPanel;

public interface SnapPolicy {

    public Coordinate snap(LayerViewPanel panel, Coordinate originalCoordinate);
}
