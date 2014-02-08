package com.osfac.dmt.workbench.ui;

import com.vividsolutions.jts.geom.Envelope;

public interface ViewportListener {

    public void zoomChanged(Envelope modelEnvelope);
}
