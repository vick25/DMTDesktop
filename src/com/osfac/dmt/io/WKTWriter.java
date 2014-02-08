package com.osfac.dmt.io;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * WKTWriter is a {@link JUMPWriter} specialized to write WTK (Well Known Text)
 * files.
 *
 * <p> DataProperties for the JUMPWriter write(featureSchema,DataProperties)
 * interface:<br> </p>
 *
 * <p> <table border='1' cellspacing='0' cellpadding='4'> <tr>
 * <th>Parameter</th> <th>Meaning</th> </tr> <tr> <td>OutputFile or
 * DefaultValue</td> <td>File name for output .wkt file</td> </tr> </table> <br>
 * </p>
 */
public class WKTWriter implements JUMPWriter {

    private com.vividsolutions.jts.io.WKTWriter wktWriter =
            new com.vividsolutions.jts.io.WKTWriter(3);

    /**
     * constuctor
     */
    public WKTWriter() {
    }

    /**
     * Main method - writes a list of wkt features (no attributes).
     *
     * @param featureCollection features to write
     * @param dp 'OutputFile' or 'DefaultValue' to specify the output file.
     */
    public void write(FeatureCollection featureCollection, DriverProperties dp)
            throws IllegalParametersException, Exception {
        String outputFname;

        outputFname = dp.getProperty("File");

        if (outputFname == null) {
            outputFname = dp.getProperty("DefaultValue");
        }

        if (outputFname == null) {
            throw new IllegalParametersException(
                    "call to WKTWrite.write() has DataProperties w/o a OutputFile specified");
        }

        java.io.Writer w;

        w = new java.io.FileWriter(outputFname);
        this.write(featureCollection, w);
        w.close();
    }

    /**
     * Function that actually does the writing
     *
     * @param featureCollection features to write
     * @param writer where to write
     */
    public void write(FeatureCollection featureCollection, Writer writer)
            throws IOException {
        for (Iterator i = featureCollection.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            wktWriter.writeFormatted(feature.getGeometry(), writer);

            if (i.hasNext()) {
                writer.write("\n\n");
            }
        }
    }
}
