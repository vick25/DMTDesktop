package com.osfac.wms;

import static com.osfac.dmt.workbench.ui.plugin.wms.URLWizardPanel.*;
import java.util.*;

/**
 * Represents the capabilities WMS XML.
 *
 * @author Chris Hodgson chodgson@refractions.net
 */
public class Capabilities {

    private MapLayer topLayer;
    private String title;
    private ArrayList mapFormats;
    private WMService service;
    private String getMapURL, getFeatureInfoURL;

    /**
     * Creates a new WMS Capabilities object. Should generally only be used by
     * the Parser.
     *
     * @param service the WMService to which these Capabilites belong
     * @param title the title of this WMService
     * @param topLayer the top MapLayer of the entire layer tree
     * @param mapFormats the Collection of supported formats
     */
    public Capabilities(WMService service, String title, MapLayer topLayer, Collection mapFormats) {
        this.service = service;
        this.title = title;
        this.topLayer = topLayer;
        this.mapFormats = new ArrayList(mapFormats);
        this.getMapURL = service.getServerUrl();
        this.getFeatureInfoURL = service.getServerUrl();
    }

    public Capabilities(WMService service, String title, MapLayer topLayer, Collection mapFormats, String getMapURL, String getFeatureInfoURL) {
        this(service, title, topLayer, mapFormats);
        this.getMapURL = fixUrlForWMService(getMapURL);
        this.getFeatureInfoURL = fixUrlForWMService(getFeatureInfoURL);
    }

    /**
     * Gets a reference to the service which these Capabilities describe.
     *
     * @return the WMService which these Capabilities describe
     */
    public WMService getService() {
        return service;
    }

    /**
     * Gets the top layer for these Capabilities.
     *
     * @return the top MapLayer for these Capabilities
     */
    public MapLayer getTopLayer() {
        return topLayer;
    }

    /**
     * Gets the title of the Capabilities.
     *
     * @return the title of the map described by these Capabilities
     */
    public String getTitle() {
        return this.title;
    }

    public String getGetMapURL() {
        return getMapURL;
    }

    public String getGetFeatureInfoURL() {
        return getFeatureInfoURL;
    }

    public void setGetMapURL(String url) {
        getMapURL = url;
    }

    /**
     * Gets a copy of the list of formats supported by this getMap requests for
     * this map.
     *
     * @return an array containing the formats supported by getMap requests for
     * this map
     */
    public String[] getMapFormats() {
        String[] formats = new String[mapFormats.size()];
        Iterator it = mapFormats.iterator();
        int i = 0;
        while (it.hasNext()) {
            formats[i++] = (String) it.next();
        }
        return formats;
    }
}
