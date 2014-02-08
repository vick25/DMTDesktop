package com.osfac.dmt.workbench.imagery;

import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureSchema;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.Map;
import java.util.WeakHashMap;

public class ImageryLayerDataset {

    public static final String ATTR_GEOMETRY = "GEOMETRY";
    public static final String ATTR_FILE = "IMAGEFILE";
    public static final String ATTR_FORMAT = "IMAGEFORMAT";
    public static final String ATTR_ERROR = "IMAGEERROR";
    public static final String ATTR_TYPE = "IMAGETYPE";
    public static final String ATTR_FACTORY = "IMAGEFACT";
    public static FeatureSchema SCHEMA = new FeatureSchema() {
        {
            addAttribute(ATTR_GEOMETRY, AttributeType.GEOMETRY);
            addAttribute(ATTR_FILE, AttributeType.STRING);
            addAttribute(ATTR_FORMAT, AttributeType.STRING);
            addAttribute(ATTR_FACTORY, AttributeType.STRING);
            addAttribute(ATTR_ERROR, AttributeType.STRING);
            addAttribute(ATTR_TYPE, AttributeType.STRING);
        }
    };

    public static FeatureSchema getSchema() {
        return SCHEMA;
    }

    public void createImage(Feature feature) throws Exception {
        String factoryClassPath = (String) feature.getString(ATTR_FACTORY);
        String imageFilePath = (String) feature.getString(ATTR_FILE);
        GeometryFactory geometryFactory = new GeometryFactory();

        // experimental change, handle errors further up [ede] 12.09.2011 
        //try {
        ReferencedImageFactory imageFactory = (ReferencedImageFactory) Class.forName(factoryClassPath).newInstance();
        ReferencedImage referencedImage = imageFactory.createImage(imageFilePath);

        featureToReferencedImageMap.put(feature, referencedImage);
        Envelope env = referencedImage.getEnvelope();
        Geometry boundingBox = geometryFactory.toGeometry(env);
        feature.setGeometry(boundingBox);

        feature.setAttribute(ATTR_TYPE, referencedImage.getType());
        //}catch (Exception e) {
        //  always throw ECWLoadExceptions as they are fatal
        //	if ( e instanceof ECWLoadException )
        //		throw e;
        //    feature.setAttribute(ATTR_ERROR, e.toString());
        //   e.printStackTrace();
        //}
    }

    public ReferencedImage referencedImage(Feature feature) throws Exception {
        if (!(feature.getString(ATTR_ERROR) == null
                || feature.getString(ATTR_ERROR).equals(""))) {
            return null;
        }
        if (!featureToReferencedImageMap.containsKey(feature)) {
            createImage(feature);
        }
        // Will be null if an exception occurs [Bob Boseko 2005-04-12]
        return (ReferencedImage) featureToReferencedImageMap.get(feature);
    }
    private Map featureToReferencedImageMap = new WeakHashMap();
}