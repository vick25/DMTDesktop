package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.util.LangUtil;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColorThemingStyle implements Style {

    public ColorThemingStyle() {
        //Parameterless constructor for Java2XML. [Bob Boseko]
    }

    /**
     * Call this method after calling #setAttributeValueToBasicStyleMap rather
     * than before.
     */
    public void setAlpha(int alpha) {
        defaultStyle.setAlpha(alpha);
        for (Iterator i = attributeValueToBasicStyleMap.values().iterator(); i
                .hasNext();) {
            BasicStyle style = (BasicStyle) i.next();
            style.setAlpha(alpha);
        }
    }

    /**
     * Call this method after calling #setAttributeValueToBasicStyleMap rather
     * than before.
     */
    public void setLineWidth(int lineWidth) {
        defaultStyle.setLineWidth(lineWidth);
        for (Iterator i = attributeValueToBasicStyleMap.values().iterator(); i
                .hasNext();) {
            BasicStyle style = (BasicStyle) i.next();
            style.setLineWidth(lineWidth);
        }
    }

    /**
     * @param defaultStyle <code>null</code> to prevent drawing features with a
     * null attribute value
     */
    public ColorThemingStyle(String attributeName,
            Map attributeValueToBasicStyleMap, BasicStyle defaultStyle) {
        this(attributeName, attributeValueToBasicStyleMap,
                attributeValueToLabelMap(attributeValueToBasicStyleMap),
                defaultStyle);
        // [sstein: 2.Dec.06] i guess this constructor comes from Erwan to
        // allow different types of classing
    }

    public ColorThemingStyle(String attributeName,
            Map attributeValueToBasicStyleMap, Map attributeValueToLabelMap, BasicStyle defaultStyle) {
        setAttributeName(attributeName);
        setAttributeValueToBasicStyleMap(attributeValueToBasicStyleMap);
        setAttributeValueToLabelMap(attributeValueToLabelMap);
        setDefaultStyle(defaultStyle);
    }

    private static Map attributeValueToLabelMap(Map attributeValueToBasicStyleMap) {
        // Be sure to use the same Map class -- it may be a RangeTreeMap [Bob Boseko 2005-07-30]
        Map attributeValueToLabelMap = (Map) LangUtil.newInstance(attributeValueToBasicStyleMap.getClass());
        for (Iterator i = attributeValueToBasicStyleMap.keySet().iterator(); i.hasNext();) {
            Object value = i.next();
            attributeValueToLabelMap.put(value, value.toString());
        }
        return attributeValueToLabelMap;
    }
    private BasicStyle defaultStyle;

    public void paint(Feature f, Graphics2D g, Viewport viewport)
            throws Exception {
        getStyle(f).paint(f, g, viewport);
    }

    private BasicStyle getStyle(Feature feature) {
        //Attribute name will be null if a layer has only a spatial attribute.
        // [Bob Boseko]
        //If we can't find an attribute with this name, just use the
        //defaultStyle. The attribute may have been deleted. [Bob Boseko]
        // If the attribute data type for color theming has been changed -
        // throws multiple exceptions and the layer dissappears due to the 
        // fact that it can't find the style in the valuetobasicstyle map.
        // Solved here by catching the exception and returning the default style 
        // (just like when the attribute name has been changed). [Ed Deen]
        BasicStyle style = null;
        try {
            style = attributeName != null
                    && feature.getSchema().hasAttribute(attributeName)
                    && feature.getAttribute(attributeName) != null ? (BasicStyle) attributeValueToBasicStyleMap
                    .get(trimIfString(feature.getAttribute(attributeName)))
                    : defaultStyle;
        } catch (ClassCastException e) {
            // Do Nothing
        }; /*try*/

        return style == null ? defaultStyle : style;
    }

    public static Object trimIfString(Object object) {
        return object != null && object instanceof String ? ((String) object)
                .trim() : object;
    }
    private Layer layer;
    private Map attributeValueToBasicStyleMap = new HashMap(); //[sstein 2.Dec.06] added = new Hashmap
    private Map attributeValueToLabelMap;
    private String attributeName;
    //[sstein 2.Dec.06] note: some things here are different. I am not sure if the changes
    // come from changes by VividSolution or preparations for different classing by Erwan  

    public Object clone() {
        try {
            ColorThemingStyle clone = (ColorThemingStyle) super.clone();
            //Deep-copy the map, to facilitate undo. [Bob Boseko]
            clone.attributeValueToBasicStyleMap = (Map) attributeValueToBasicStyleMap.getClass()
                    .newInstance();
            for (Iterator i = attributeValueToBasicStyleMap.keySet().iterator(); i
                    .hasNext();) {
                Object attribute = (Object) i.next();
                clone.attributeValueToBasicStyleMap.put(attribute,
                        ((BasicStyle) attributeValueToBasicStyleMap
                        .get(attribute)).clone());
            }
            clone.attributeValueToLabelMap = (Map) attributeValueToLabelMap.getClass().newInstance();
            clone.attributeValueToLabelMap.putAll(attributeValueToLabelMap);
            return clone;
        } catch (InstantiationException e) {
            Assert.shouldNeverReachHere();
            return null;
        } catch (IllegalAccessException e) {
            Assert.shouldNeverReachHere();
            return null;
        } catch (CloneNotSupportedException e) {
            Assert.shouldNeverReachHere();
            return null;
        }
    }

    /**
     * @return null if the layer has no non-spatial attributes
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * You can set the keys to Ranges if the Map is a Range.RangeTreeMap. But
     * don't mix Ranges and non-Ranges -- the UI expects homogeneity in this
     * regard (i.e. to test whether or not there are ranges, only the first
     * attribute value is tested).
     */
    public void setAttributeValueToBasicStyleMap(Map attributeValueToBasicStyleMap) {
        this.attributeValueToBasicStyleMap = attributeValueToBasicStyleMap;
    }

    /**
     * You can set the keys to Ranges if the Map is a Range.RangeTreeMap. But
     * don't mix Ranges and non-Ranges -- the UI expects homogeneity in this
     * regard (i.e. to test whether or not there are ranges, only the first
     * attribute value is tested).
     */
    public void setAttributeValueToLabelMap(Map attributeValueToLabelMap) {
        this.attributeValueToLabelMap = attributeValueToLabelMap;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Map getAttributeValueToBasicStyleMap() {
        return attributeValueToBasicStyleMap;
    }

    public Map getAttributeValueToLabelMap() {
        return attributeValueToLabelMap;
    }
    private boolean enabled = false;

    public void initialize(Layer layer) {
        this.layer = layer;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static ColorThemingStyle get(Layer layer) {
        if ((ColorThemingStyle) layer.getStyle(ColorThemingStyle.class) == null) {
            ColorThemingStyle colorThemingStyle = new ColorThemingStyle(
                    pickNonSpatialAttributeName(layer
                    .getFeatureCollectionWrapper().getFeatureSchema()),
                    new HashMap(), new BasicStyle(Color.lightGray));
            layer.addStyle(colorThemingStyle);
        }
        return (ColorThemingStyle) layer.getStyle(ColorThemingStyle.class);
    }

    private static String pickNonSpatialAttributeName(FeatureSchema schema) {
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            if (schema.getGeometryIndex() != i) {
                return schema.getAttributeName(i);
            }
        }
        return null;
    }

    public BasicStyle getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(BasicStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
    }
}