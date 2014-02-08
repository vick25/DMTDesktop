package com.osfac.dmt.workbench.model;

import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.ui.LayerNameRenderer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.renderer.RenderingManager;
import com.osfac.wms.BoundingBox;
import com.osfac.wms.MapRequest;
import com.osfac.wms.WMService;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;

/**
 * A Layerable that retrieves images from a Web Map Server.
 */
public class WMSLayer extends AbstractLayerable implements Cloneable {

    private String format;
    private List<String> layerNames = new ArrayList<String>();
    private String srs;
    private int alpha = 255;
    private WMService service;
    //-- [sstein 03.Mai.2008] added field to be able to zoom to MrSID layers
    protected Envelope totalBounds = new Envelope();
    private String wmsVersion = WMService.WMS_1_0_0;
    protected Reference oldImage;
    protected URL oldURL;

    /**
     * Called by Java2XML
     */
    public WMSLayer() {
        init();
    }

    public WMSLayer(LayerManager layerManager, String serverURL, String srs,
            List layerNames, String format, String version) throws IOException {
        this(layerManager, initializedService(serverURL, version), srs, layerNames,
                format);
    }

    private static WMService initializedService(String serverURL, String version)
            throws IOException {
        WMService initializedService = new WMService(serverURL, version);
        initializedService.initialize();
        return initializedService;
    }

    public WMSLayer(LayerManager layerManager, WMService initializedService,
            String srs, List layerNames, String format) throws IOException {
        this(layerManager, initializedService, srs, layerNames, format, initializedService.getVersion());
    }

    public WMSLayer(String title, LayerManager layerManager, WMService initializedService,
            String srs, List<String> layerNames, String format) throws IOException {
        this(title, layerManager, initializedService, srs, layerNames, format, initializedService.getVersion());
    }

    public WMSLayer(String title, LayerManager layerManager, WMService initializedService,
            String srs, List<String> layerNames, String format, String version) {
        super(title, layerManager);
        setService(initializedService);
        setSRS(srs);
        this.layerNames = new ArrayList<>(layerNames);
        setFormat(format);
        init();
        this.wmsVersion = version;
    }

    public WMSLayer(LayerManager layerManager, WMService initializedService,
            String srs, List<String> layerNames, String format, String version) {
        this(layerNames.get(0), layerManager, initializedService, srs, layerNames, format, version);
    }

    protected void init() {
        getBlackboard().put(
                RenderingManager.USE_MULTI_RENDERING_THREAD_QUEUE_KEY, true);
        getBlackboard().put(LayerNameRenderer.USE_CLOCK_ANIMATION_KEY, true);
    }

    private void setService(WMService service) {
        this.service = service;
        this.serverURL = service.getServerUrl();
    }

    public int getAlpha() {
        return alpha;
    }

    /**
     * @param alpha 0-255 (255 is opaque)
     */
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public Image createImage(LayerViewPanel panel) throws IOException {

        MapRequest request = createRequest(panel);
        URL newURL = request.getURL();

        Image image;

        // look if last request equals new one.
        // if it does take the image from the cache.
        if (oldURL == null
                || !newURL.equals(oldURL)
                || oldImage == null
                || (image = (Image) oldImage.get()) == null) {
            image = request.getImage();
            MediaTracker mt = new MediaTracker(new JButton());
            mt.addImage(image, 0);

            try {
                mt.waitForID(0);
            } catch (InterruptedException e) {
                Assert.shouldNeverReachHere();
            }
            oldImage = new SoftReference(image);
            oldURL = newURL;
        }

        return image;
    }

    private BoundingBox toBoundingBox(String srs, Envelope e) {
        return new BoundingBox(srs, e.getMinX(), e.getMinY(), e.getMaxX(), e
                .getMaxY());
    }

    public MapRequest createRequest(LayerViewPanel panel) throws IOException {
        MapRequest request = getService().createMapRequest();
        request.setBoundingBox(toBoundingBox(srs, panel.getViewport()
                .getEnvelopeInModelCoordinates()));
        request.setFormat(format);
        request.setImageWidth(panel.getWidth());
        request.setImageHeight(panel.getHeight());
        request.setLayers(layerNames);
        request.setTransparent(true);

        return request;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void addLayerName(String layerName) {
        layerNames.add(layerName);
    }

    public List<String> getLayerNames() {
        return Collections.unmodifiableList(layerNames);
    }

    public void setSRS(String srs) {
        this.srs = srs;
    }

    public String getSRS() {
        return srs;
    }

    public Object clone() throws java.lang.CloneNotSupportedException {
        WMSLayer clone = (WMSLayer) super.clone();
        clone.layerNames = new ArrayList(this.layerNames);

        return clone;
    }

    public void removeAllLayerNames() {
        layerNames.clear();
    }
    private Blackboard blackboard = new Blackboard();
    private String serverURL;

    public Blackboard getBlackboard() {
        return blackboard;
    }

    public WMService getService() throws IOException {
        if (service == null) {
            Assert.isTrue(serverURL != null);
            setService(initializedService(serverURL, wmsVersion));
        }
        return service;
    }

    public String getServerURL() {
        //Called by Java2XML [Bob Boseko 2004-02-23]
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        //Called by Java2XML [Bob Boseko 2004-02-23]
        this.serverURL = serverURL;
    }

    public String getWmsVersion() {
        return wmsVersion;
    }

    public void setWmsVersion(String wmsVersion) {
        this.wmsVersion = wmsVersion;
    }

    //-- [sstein 03.Mai.2008] added method to be able to zoom to MrSID layers
    //   it will probably not work for WMSLayers
    public Envelope getEnvelope() {
        return totalBounds;

        /*WMService serv;
         try
         {
         serv = getService();
         }
         catch (IOException ex)
         {
         return null;
         }
    	
         BoundingBox bb = serv.getCapabilities().getTopLayer().getBoundingBox();
    	
         //don't know if WMS always returns a bounding box
         //so check for the usual failure modes
         if (bb == null)
         return null;
         if ((bb.getMaxX() - bb.getMinX()) <= 0.0)
         return null;
         if ((bb.getMaxY() - bb.getMinY()) <= 0.0)
         return null;

         return new Envelope(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY());*/
    }
}
