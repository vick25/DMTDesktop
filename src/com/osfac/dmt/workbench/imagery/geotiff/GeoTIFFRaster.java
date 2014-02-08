package com.osfac.dmt.workbench.imagery.geotiff;

import com.vividsolutions.jts.geom.Coordinate;
import com.osfac.dmt.util.FileUtil;
import java.awt.geom.AffineTransform;
import java.util.List;
import org.geotiff.image.jai.GeoTIFFDescriptor;
import org.geotiff.image.jai.GeoTIFFDirectory;
import org.libtiff.jai.codec.XTIFF;
import org.libtiff.jai.codec.XTIFFField;

public class GeoTIFFRaster extends GeoReferencedRaster {

    private final String MSG_GENERAL = "This is not a valid GeoTIFF file.";
    String fileName;
    boolean hoPatch = false;

    /**
     * Called by Java2XML
     */
    public GeoTIFFRaster(String imageFileLocation) throws Exception {
        super(imageFileLocation);
        fileName = imageFileLocation;
        registerWithJAI();
        readRasterfile();
    }

    private void registerWithJAI() {
        // Register the GeoTIFF descriptor with JAI.
        GeoTIFFDescriptor.register();
    }

    private void parseGeoTIFFDirectory(GeoTIFFDirectory dir) throws Exception {
        // Find the ModelTiePoints field
        XTIFFField fieldModelTiePoints = dir.getField(XTIFF.TIFFTAG_GEO_TIEPOINTS);
        if (fieldModelTiePoints == null) {
            throw new Exception("Missing tiepoints-tag in file.\n" + MSG_GENERAL);
        }
        // Get the number of modeltiepoints
//    int numModelTiePoints = fieldModelTiePoints.getCount() / 6;
        // ToDo: alleen numModelTiePoints == 1 ondersteunen.
        // Map the modeltiepoints from raster to model space
        // Read the tiepoints
        setCoorRasterTiff_tiepointLT(new Coordinate(fieldModelTiePoints.getAsDouble(0), fieldModelTiePoints.getAsDouble(1), 0));
        setCoorModel_tiepointLT(new Coordinate(fieldModelTiePoints.getAsDouble(3), fieldModelTiePoints.getAsDouble(4), 0));
        setEnvelope();
        // Find the ModelPixelScale field
        XTIFFField fieldModelPixelScale = dir.getField(XTIFF.TIFFTAG_GEO_PIXEL_SCALE);
        if (fieldModelPixelScale == null) {
            throw new Exception("Missing pixelscale-tag in file." + "\n" + MSG_GENERAL);
        }
        setDblModelUnitsPerRasterUnit_X(fieldModelPixelScale.getAsDouble(0));
        setDblModelUnitsPerRasterUnit_Y(fieldModelPixelScale.getAsDouble(1));
    }

    /**
     * @return filename of the tiff worldfile
     */
    private String worldFileName() {
        int posDot = fileName.lastIndexOf('.');
        if (posDot == -1) {
            posDot = fileName.length();
        }
        return fileName.substring(0, posDot) + ".tfw";
    }

    private void parseWorldFile() throws Exception {
        // Get the name of the tiff worldfile.
        String name = worldFileName();

        // Read the tags from the tiff worldfile.
        List lines = FileUtil.getContents(name);
        double[] tags = new double[6];
        for (int i = 0; i < 6; i++) {
            String line = (String) lines.get(i);
            tags[i] = Double.parseDouble(line);
        }
        setCoorRasterTiff_tiepointLT(new Coordinate(0, 0));
        setCoorModel_tiepointLT(new Coordinate(0, 0));
        setAffineTransformation(new AffineTransform(tags));
    }

    protected void readRasterfile() throws Exception {
//    ImageCodec originalCodec = ImageCodec.getCodec("tiff");
        super.readRasterfile();

        // Get access to the tags and geokeys.
        // First, get the TIFF directory
        GeoTIFFDirectory dir = (GeoTIFFDirectory) src.getProperty("tiff.directory");
        if (dir == null) {
            throw new Exception("This is not a (geo)tiff file.");
        }

        try {
            // Try to parse any embedded geotiff tags.
            parseGeoTIFFDirectory(dir);
        } catch (Exception E) {
            // Embedded geotiff tags have not been found. Try
            // to use a geotiff world file.
            try {
                parseWorldFile();
            } catch (Exception e) {
                throw new Exception("Neither geotiff tags nor valid worldfile found.\n" + MSG_GENERAL);
            }
        }
    }
}