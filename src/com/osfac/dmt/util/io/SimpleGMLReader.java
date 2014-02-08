package com.osfac.dmt.util.io;

import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureUtil;
import com.osfac.dmt.io.FMEGMLReader;
import com.osfac.dmt.io.GMLInputTemplate;
import com.osfac.dmt.io.GMLReader;
import com.osfac.dmt.io.ParseException;
import java.io.*;
import java.util.List;

/**
 * Provides an easy way to read spatial data from a GML document. Attributes are
 * not read. Simply pass in a Reader on the GML, and the names of the various
 * tags. A List of Geometries will be returned.
 */
public class SimpleGMLReader {

    public SimpleGMLReader() {
    }

    /**
     * @param gml a Reader on an XML document containing GML
     * @param collectionElement the name of the feature-collection tag
     * @param featureElement the name of the feature tag
     * @param geometryElement the name of the geometry tag
     * @return a List of Geometries
     */
    public List toGeometries(Reader gml, String collectionElement,
            String featureElement, String geometryElement)
            throws Exception {
        GMLInputTemplate template = template(collectionElement, featureElement,
                geometryElement);
        GMLReader gmlReader = new GMLReader();
        gmlReader.setInputTemplate(template);

        return FeatureUtil.toGeometries(gmlReader.read(gml).getFeatures());
    }

    private GMLInputTemplate template(String collectionElement,
            String featureElement, String geometryElement)
            throws IOException, ParseException {
        String s = "";
        s += "<?xml version='1.0' encoding='UTF-8'?>";
        s += "<JCSGMLInputTemplate>";
        s += ("<CollectionElement>" + collectionElement
                + "</CollectionElement>");
        s += ("<FeatureElement>" + featureElement + "</FeatureElement>");
        s += ("<GeometryElement>" + geometryElement + "</GeometryElement>");
        s += "<ColumnDefinitions></ColumnDefinitions>"; //no attributes read
        s += "</JCSGMLInputTemplate>";

        GMLInputTemplate template = new GMLInputTemplate();
        StringReader sr = new StringReader(s);

        try {
            template.load(sr);
        } finally {
            sr.close();
        }

        return template;
    }

    /**
     * @param gml
     * @see #toGeometries(Reader, String, String, String)
     */
    public List toGeometries(String gml, String collectionElement,
            String featureElement, String geometryElement)
            throws Exception {
        StringReader r = new StringReader(gml);

        try {
            return toGeometries(r, collectionElement, featureElement,
                    geometryElement);
        } finally {
            r.close();
        }
    }

    /**
     * Reads a GML file that is in FME format.
     *
     * @return the contents of the file, including both spatial and attribute
     * data
     */
    public FeatureCollection readFMEFile(File file) throws Exception {
        FMEGMLReader fmeGMLReader = new FMEGMLReader();
        FileReader fileReader = new FileReader(file);
        GMLInputTemplate inputTemplate;

        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            try {
                inputTemplate = fmeGMLReader.getGMLInputTemplate(bufferedReader,
                        file.getPath());
            } finally {
                bufferedReader.close();
            }
        } finally {
            fileReader.close();
        }

        GMLReader gmlReader = new GMLReader();
        gmlReader.setInputTemplate(inputTemplate);

        FeatureCollection fc;
        fileReader = new FileReader(file);

        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            try {
                fc = gmlReader.read(bufferedReader);
            } finally {
                bufferedReader.close();
            }
        } finally {
            fileReader.close();
        }

        return fc;
    }
}
