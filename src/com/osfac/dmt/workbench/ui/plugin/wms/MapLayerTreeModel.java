package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.wms.MapLayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class MapLayerTreeModel extends DefaultTreeModel {

    private boolean sorted = false;

    public MapLayerTreeModel(MapLayer topLayer) {
        super(new LayerNode(topLayer, null));
        ((LayerNode) getRoot()).mapLayerTreeModel = this;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
        reload();
    }

    public static class LayerNode implements TreeNode, Comparable {

        private MapLayer layer;
        private MapLayerTreeModel mapLayerTreeModel;

        public LayerNode(MapLayer layer, MapLayerTreeModel mapLayerTreeModel) {
            this.layer = layer;
            this.mapLayerTreeModel = mapLayerTreeModel;
        }

        public boolean isContainer() {
            return layer.getName() == null;
        }

        public MapLayer getLayer() {
            return layer;
        }

        public TreeNode getChildAt(int childIndex) {
            return (TreeNode) childList().get(childIndex);
        }

        public int getChildCount() {
            return childList().size();
        }

        public TreeNode getParent() {
            return new LayerNode(layer.getParent(), mapLayerTreeModel);
        }

        public int getIndex(TreeNode node) {
            return childList().indexOf(node);
        }

        public boolean getAllowsChildren() {
            return true;
        }

        public boolean isLeaf() {
            return getChildCount() == 0;
        }

        public Enumeration children() {
            return new Vector(childList()).elements();
        }

        private List childList() {
            ArrayList children = new ArrayList();

            for (Iterator i = layer.getSubLayerList().iterator(); i.hasNext();) {
                MapLayer layer = (MapLayer) i.next();
                children.add(new LayerNode(layer, mapLayerTreeModel));
            }

            if (mapLayerTreeModel.sorted) {
                Collections.sort(children);
            }

            return children;
        }

        public boolean equals(Object o) {
            //Needed for the #contains check in MapLayerPanel, as well as #getIndex. [Bob Boseko]
            LayerNode other = (LayerNode) o;

            return layer == other.layer;
        }

        public int compareTo(Object o) {
            LayerNode other = (LayerNode) o;

            return layer.getTitle().compareTo(other.layer.getTitle());
        }
    }
}
