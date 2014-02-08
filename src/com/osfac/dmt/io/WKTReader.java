package com.osfac.dmt.io;

import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import java.io.*;

/**
 * WKTReader is a {@link JUMPReader} specialized to read WTK (Well Known Text)
 * files.
 *
 * <p> DataProperties for the JUMPReader load(DataProperties) interface:<br>
 * </p>
 *
 * <p> <table border='1' cellspacing='0' cellpadding='4'> <tr>
 * <th>Parameter</th> <th>Meaning</th> </tr> <tr> <td>File or DefaultValue</td>
 * <td>File name for the input WKT file</td> </tr> <tr> <td>CompressedFile</td>
 * <td>File name (a .zip or .gz) with a .jml/.xml/.gml inside (specified by
 * File)</td> </tr> </table> <br> </p>
 *
 */
public class WKTReader implements JUMPReader {

    private GeometryFactory geometryFactory = new GeometryFactory();
    private com.vividsolutions.jts.io.WKTReader wktReader = new com.vividsolutions.jts.io.WKTReader(geometryFactory);

    /**
     * constructor*
     */
    public WKTReader() {
    }

    /**
     * Main function -read in a file containing a list of WKT geometries
     *
     * @param dp 'InputFile' or 'DefaultValue' to specify where the WKT file is.
     */
    public FeatureCollection read(DriverProperties dp) throws IllegalParametersException, Exception {
        FeatureCollection fc;

        String inputFname;
        boolean isCompressed;
        Reader fileReader;

        isCompressed = (dp.getProperty("CompressedFile") != null);

        inputFname = dp.getProperty("File");

        if (inputFname == null) {
            inputFname = dp.getProperty("DefaultValue");
        }

        if (inputFname == null) {
            throw new IllegalParametersException(
                    "call to WKTReader.read() has DataProperties w/o a InputFile specified");
        }

        if (isCompressed) {
            fileReader = new InputStreamReader(CompressedFile.openFile(
                    inputFname, dp.getProperty("CompressedFile")));
        } else {
            fileReader = new FileReader(inputFname);
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            try {
                fc = read(bufferedReader);
            } finally {
                bufferedReader.close();
            }
        } finally {
            fileReader.close();
        }

        return fc;
    }

    /**
     * Reads in the actual WKT geometries
     *
     * @param reader where to read the geometries from
     */
    public FeatureCollection read(Reader reader) throws Exception {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("Geometry", AttributeType.GEOMETRY);

        FeatureCollection featureCollection = new FeatureDataset(featureSchema);
        BufferedReader bufferedReader = new BufferedReader(reader);

        try {
            while (!isAtEndOfFile(bufferedReader)) {
                featureCollection.add(nextFeature(bufferedReader, featureSchema));
            }
        } finally {
            bufferedReader.close();
        }

        return featureCollection;
    }

    /**
     * returns true if at the end of the file.
     */
    private boolean isAtEndOfFile(BufferedReader bufferedReader)
            throws IOException, ParseException {
        bufferedReader.mark(1000);

        try {
            StreamTokenizer tokenizer = new StreamTokenizer(bufferedReader);
            int type = tokenizer.nextToken();

            if (type == StreamTokenizer.TT_EOF) {
                return true;
            }

            if (type == StreamTokenizer.TT_WORD) {
                return false;
            }

            throw new ParseException(
                    "Expected word or end-of-file but encountered StreamTokenizer type "
                    + type);
        } finally {
            bufferedReader.reset();
        }
    }

    /**
     * Reads 1 feature
     */
    private Feature nextFeature(Reader reader, FeatureSchema featureSchema)
            throws ParseException {
        Feature feature = new BasicFeature(featureSchema);
        feature.setGeometry(wktReader.read(reader));

        return feature;
    }
}
