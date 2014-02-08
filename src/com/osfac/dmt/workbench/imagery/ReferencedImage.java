package com.osfac.dmt.workbench.imagery;

import com.osfac.dmt.DMTException;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.geom.Envelope;

/**
 * An image which is referenced to a specific coordinate envelope
 */
public interface ReferencedImage {

    Envelope getEnvelope();

    void paint(Feature f, java.awt.Graphics2D g, Viewport viewport) throws DMTException;

    String getType();
}