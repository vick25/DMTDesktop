package com.osfac.dmt.workbench.ui.addremove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.tree.TreeModel;

/**
 * A read-only tree appropriate for the left-hand panel of an AddRemovePanel.
 * Not for use as the right-hand panel because it is read-only -- it does not
 * have any logic for adding a node to the tree (or removing a node, for that
 * matter).
 */
public class TreeAddRemoveListModel implements AddRemoveListModel {

    private TreeModel treeModel;

    public TreeAddRemoveListModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public void add(Object item) {
        //Do nothing [Bob Boseko]
    }

    public void setItems(Collection items) {
        //Do nothing [Bob Boseko]
    }

    public List getItems() {
        return new ArrayList();
    }

    public void remove(Object item) {
        //Do nothing [Bob Boseko]
    }
}
