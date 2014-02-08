package com.osfac.dmt.workbench.imagery.geotiff;

import com.vividsolutions.jts.geom.Envelope;
import com.osfac.dmt.DMTException;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.imagery.ReferencedImage;
import com.osfac.dmt.workbench.ui.Viewport;

/**
 * <p> </p> <p> </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */
public class GeoTIFFImage
        implements ReferencedImage {

    private GeoTIFFRaster gtr;
    private RasterPainter rasterPainter;

    public GeoTIFFImage(String location) throws DMTException {
        init(location);
    }

    public Envelope getEnvelope() {
        return gtr.getEnvelope();
    }

    private void init(String location) throws DMTException {
        try {
            gtr = new GeoTIFFRaster(location);
            rasterPainter = new RasterPainter(gtr);
        } catch (Exception ex) {
            gtr = null;
            throw new DMTException(ex.getMessage());
        }
    }

    public void paint(Feature f, java.awt.Graphics2D g, Viewport viewport) throws DMTException {
        try {
            rasterPainter.paint(g, viewport);
        } catch (Exception ex) {
            throw new DMTException(ex.getMessage());
        }
    }

    public String getType() {
        return "GeoTiff";
    }
}