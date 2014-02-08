package com.osfac.dmt.workbench.model;

import com.osfac.dmt.util.LangUtil;
import com.osfac.dmt.util.SimpleTreeModel;
import com.osfac.dmt.workbench.ui.renderer.style.BasicStyle;
import com.osfac.dmt.workbench.ui.renderer.style.ColorThemingStyle;
import com.vividsolutions.jts.util.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreePath;

/**
 * JTree model for displaying the Layers, WMSLayers, and other Layerables
 * contained in a LayerManager.
 */
public class LayerTreeModel extends SimpleTreeModel {

    public static class Root {

        private Root() {
        }
    }
    private LayerManagerProxy layerManagerProxy;

    public LayerTreeModel(LayerManagerProxy layerManagerProxy) {
        super(new Root());
        this.layerManagerProxy = layerManagerProxy;
    }

    public static class ColorThemingValue {

        private Object value;
        private BasicStyle style;
        private String label;

        public ColorThemingValue(Object value, BasicStyle style, String label) {
            this.value = value;
            this.style = style;
            Assert.isTrue(label != null);
            this.label = label;
        }

        public String toString() {
            return label;
        }

        public boolean equals(Object other) {
            return other instanceof ColorThemingValue
                    && LangUtil.bothNullOrEqual(value,
                    ((ColorThemingValue) other).value)
                    && style == ((ColorThemingValue) other).style;
        }

        public BasicStyle getStyle() {
            return style;
        }
    }

    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            // ColorThemingValue are value objects. [Bob Boseko]
            if (child instanceof ColorThemingValue
                    && getChild(parent, i) instanceof ColorThemingValue
                    && getChild(parent, i).equals(child)) {
                return i;
            }
        }
        return super.getIndexOfChild(parent, child);
    }

    public List getChildren(Object parent) {
        if (parent == getRoot()) {
            return layerManagerProxy.getLayerManager().getCategories();
        }
        if (parent instanceof Category) {
            return ((Category) parent).getLayerables();
        }
        if (parent instanceof Layer
                && ColorThemingStyle.get((Layer) parent).isEnabled()) {
            Map attributeValueToBasicStyleMap = ColorThemingStyle.get(
                    (Layer) parent).getAttributeValueToBasicStyleMap();
            Map attributeValueToLabelMap = ColorThemingStyle
                    .get((Layer) parent).getAttributeValueToLabelMap();
            List colorThemingValues = new ArrayList();
            for (Iterator i = attributeValueToBasicStyleMap.keySet().iterator(); i
                    .hasNext();) {
                Object value = (Object) i.next();
                colorThemingValues.add(new ColorThemingValue(value,
                        (BasicStyle) attributeValueToBasicStyleMap.get(value),
                        (String) attributeValueToLabelMap.get(value)));
            }
            return colorThemingValues;
        }
        if (parent instanceof ColorThemingValue) {
            return Collections.EMPTY_LIST;
        }
        if (parent instanceof Layerable) {
            return new ArrayList();
        }
        Assert.shouldNeverReachHere(parent.getClass().getName());
        return null;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        if (path.getLastPathComponent() instanceof Layerable) {
            ((Layerable) path.getLastPathComponent()).setName((String) newValue);
        } else if (path.getLastPathComponent() instanceof Category) {
            ((Category) path.getLastPathComponent()).setName((String) newValue);
        } else {
            Assert.shouldNeverReachHere();
        }
    }
}
