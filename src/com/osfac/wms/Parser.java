package com.osfac.wms;

import com.osfac.dmt.I18N;
import com.osfac.wms.util.XMLTools;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Pulls WMS objects out of the XML
 *
 * @author Chris Hodgson chodgson@refractions.net
 */
public class Parser {

    private static Logger LOG = Logger.getLogger(Parser.class);

    /**
     * Creates a Parser for dealing with WMS XML.
     */
    public Parser() {
    }

    /**
     * Parses the WMT_MS_Capabilities XML from the given InputStream into a
     * Capabilities object.
     *
     * @param service the WMService from which this MapDescriptor is derived
     * @param inStream the inputStream containing the WMT_MS_Capabilities XML to
     * parse
     * @return the MapDescriptor object created from the specified XML
     * InputStream
     */
    public Capabilities parseCapabilities(WMService service, InputStream inStream) throws IOException {
        if (WMService.WMS_1_1_1.equals(service.getVersion())
                || WMService.WMS_1_1_0.equals(service.getVersion())) {
            return parseCapabilities_1_1_1(service, inStream);
        }

        return parseCapabilities_1_0_0(service, inStream);
    }

    /**
     * Traverses the DOM tree underneath the specified Node and generates a
     * corresponding WMSLayer object tree. The returned WMSLayer will be set to
     * have the specified parent.
     *
     * @param layerNode a DOM Node which is a <layer> XML element
     * @return a WMSLayer with complete subLayer tree that corresponds to the
     * DOM Node provided
     */
    public MapLayer wmsLayerFromNode(Node layerNode) {
        String name = null;
        String title = null;
        LinkedList<String> srsList = new LinkedList<>();
        LinkedList<MapLayer> subLayers = new LinkedList<>();
        BoundingBox bbox = null;

// I think, bbox is LatLonBoundingBox.
// I need a new variable for BoundingBox.
// It must be a list because in the OGC document
// stands that Layers may have zero or more <BoundingBox> [uwe dalluege]
//    BoundingBox boundingBox = null;
        ArrayList<BoundingBox> boundingBoxList = new ArrayList<>();

        NodeList nl = layerNode.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            try {

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    switch (n.getNodeName()) {
                        case "Name":
                            name = ((CharacterData) n.getFirstChild()).getData();
                            break;
                        case "Title":
                            title = ((CharacterData) n.getFirstChild()).getData();
                            break;
                        case "SRS":
                            String srsStr = ((CharacterData) n.getFirstChild()).getData();
                            // split the srs String on spaces
                            while (srsStr.length() > 0) {
                                int ws = srsStr.indexOf(' ');
                                if (ws > 0) {
                                    srsList.add(srsStr.substring(0, ws));
                                    srsStr = srsStr.substring(ws + 1);
                                } else {
                                    if (srsStr.length() > 0) {
                                        srsList.add(srsStr);
                                        srsStr = "";
                                    }
                                }
                            }
                            break;
                        case "LatLonBoundingBox":
                            bbox = boundingBoxFromNode(n);
                            boundingBoxList.add(bbox);

                            // Check for BoundingBox [uwe dalluege]
                            break;
                        case "BoundingBox":
                            bbox = boundingBoxFromNode(n);
                            boundingBoxList.add(bbox);
                            break;
                        case "Layer":
                            subLayers.add(wmsLayerFromNode(n));
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("Exception caught in wmsLayerFromNode(): " + e.toString());
            }
        }

// call the new constructor with boundingBoxList in MapLayer [uwe dalluege]
        return new MapLayer(name, title, srsList, subLayers, bbox, boundingBoxList);
    }

    /**
     * Creates a new BoundingBox object based on the DOM Node given.
     *
     * @param n the DOM Node to create the Bounding box from, must be either a
     * LatLonBoundingBox element or a BoundingBox element
     * @return a new BoundingBox object based on the DOM Node provided
     */
    public BoundingBox boundingBoxFromNode(Node n) throws Exception {
        try {
            String srs = "";
            NamedNodeMap nm = n.getAttributes();
            switch (n.getNodeName()) {
                case "LatLonBoundingBox":
                    srs = "LatLon";
                    break;
                case "BoundingBox":
                    srs = nm.getNamedItem("SRS").getNodeValue();
                    break;
                default:
                    break;
            }

            // could not parse when values equal "inf"
            //	double minx = Double.parseDouble(nm.getNamedItem("minx").getNodeValue());
            //	double miny = Double.parseDouble(nm.getNamedItem("miny").getNodeValue());
            //	double maxx = Double.parseDouble(nm.getNamedItem("maxx").getNodeValue());
            //	double maxy = Double.parseDouble(nm.getNamedItem("maxy").getNodeValue());

            // change "inf" values with +/-"Infinity"
            double minx;
            if (nm.getNamedItem("minx").getNodeValue().equals("inf")) {
                minx = Double.NEGATIVE_INFINITY;
            } else {
                minx = Double.parseDouble(nm.getNamedItem("minx").getNodeValue());
            }

            double miny;
            if (nm.getNamedItem("miny").getNodeValue().equals("inf")) {
                miny = Double.NEGATIVE_INFINITY;
            } else {
                miny = Double.parseDouble(nm.getNamedItem("miny").getNodeValue());
            }
            double maxx;

            if (nm.getNamedItem("maxx").getNodeValue().equals("inf")) {
                maxx = Double.POSITIVE_INFINITY;
            } else {
                maxx = Double.parseDouble(nm.getNamedItem("maxx").getNodeValue());
            }

            double maxy;
            if (nm.getNamedItem("maxy").getNodeValue().equals("inf")) {
                maxy = Double.POSITIVE_INFINITY;
            } else {
                maxy = Double.parseDouble(nm.getNamedItem("maxy").getNodeValue());
            }

            return new BoundingBox(srs, minx, miny, maxx, maxy);

        } catch (DOMException | NumberFormatException e) {
            // possible NullPointerException from getNamedItem returning a null
            // also possible NumberFormatException
            e.printStackTrace();
            throw new Exception(I18N.get("com.vividsolutions.wms.Parser.invalid-bounding-box-element-node") + ": " + e.toString());
        }
    }

    private Capabilities parseCapabilities_1_0_0(WMService service, InputStream inStream) throws IOException {
        MapLayer topLayer;
        String title = null;
        LinkedList<String> formatList = new LinkedList<>();
        Document doc;

        try {
            DOMParser parser = new DOMParser();
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.parse(new InputSource(inStream));
            doc = parser.getDocument();
            // DEBUG: XMLTools.printNode( doc, "" );
        } catch (SAXException saxe) {
            throw new IOException(saxe.toString());
        }

        // get the title
        try {
            title = ((CharacterData) XMLTools.simpleXPath(doc, "WMT_MS_Capabilities/Service/Title").getFirstChild()).getData();
        } catch (Exception e) {
            // possible NullPointerException if there is no firstChild()
            // also possible miscast causing an Exception

            // 	[uwe dalluege]
            throw new IOException("Maybe wrong Capabilities Version! ");
        }

        // get the supported file formats
        Node formatNode = XMLTools.simpleXPath(doc, "WMT_MS_Capabilities/Capability/Request/Map/Format");
        NodeList nl = formatNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                formatList.add(n.getNodeName());
            }
        }

        // get the top layer
        topLayer = wmsLayerFromNode(XMLTools.simpleXPath(doc, "WMT_MS_Capabilities/Capability/Layer"));

        return new Capabilities(service, title, topLayer, formatList);
    }

    //UT TODO move this into a common method (
    // private Capabilities parseCapabilities( WMService service, InputStream inStream, 
    // String version)
    private Capabilities parseCapabilities_1_1_1(WMService service, InputStream inStream) throws IOException {
        MapLayer topLayer;
        String title = null;
        String getMapURL, getFeatureInfoURL;
        LinkedList<String> formatList = new LinkedList<>();
        Document doc;

        try {
            DOMParser parser = new DOMParser();
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            //was throwing java.io.UTFDataFormatException: Invalid byte 2 of 3-byte UTF-8 sequence.
//          parser.parse( new InputSource( inStream ) );
            InputStreamReader ireader = new InputStreamReader(inStream);

            parser.parse(new InputSource(ireader));
            doc = parser.getDocument();
        } catch (SAXException saxe) {
            throw new IOException(saxe.toString());
        }

        // get the title
        try {
            title = ((CharacterData) XMLTools.simpleXPath(doc, "WMT_MS_Capabilities/Service/Title").getFirstChild()).getData();
        } catch (Exception e) {
            // possible NullPointerException if there is no firstChild()
            // also possible miscast causing an Exception
            e.printStackTrace();
        }

        // get the supported file formats			// UT was "WMT_MS_Capabilities/Capability/Request/Map/Format"
        final Node formatNode = XMLTools.simpleXPath(doc, "WMT_MS_Capabilities/Capability/Request/GetMap");

        NodeList nl = formatNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && "Format".equals(n.getNodeName())) {
                formatList.add(n.getFirstChild().getNodeValue());
            }
        }

        // get the possible URLs
        String xp = "DCPType/HTTP/Get/OnlineResource";
        String xlink = "http://www.w3.org/1999/xlink";
        Element e = (Element) XMLTools.simpleXPath(formatNode, xp);
        getMapURL = e.getAttributeNS(xlink, "href");

        xp = "WMT_MS_Capabilities/Capability/Request/GetFeatureInfo/DCPType/HTTP/Get/OnlineResource";
        e = (Element) XMLTools.simpleXPath(doc, xp);
        if (e != null) {
            getFeatureInfoURL = e.getAttributeNS(xlink, "href");
        } else {
            getFeatureInfoURL = "";
        }

        // get the top layer
        topLayer = wmsLayerFromNode(XMLTools.simpleXPath(doc, "WMT_MS_Capabilities/Capability/Layer"));

        return new Capabilities(service, title, topLayer, formatList, getMapURL, getFeatureInfoURL);
    }
}
