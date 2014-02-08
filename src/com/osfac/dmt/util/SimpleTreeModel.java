package com.osfac.dmt.util;

import com.vividsolutions.jts.util.Assert;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class SimpleTreeModel implements TreeModel {

    public static abstract class Folder {

        private Class childrenClass;
        private String name;
        private Object parent;

        public Folder(String name, Object parent, Class childrenClass) {
            this.name = name;
            this.parent = parent;
            this.childrenClass = childrenClass;
        }

        public abstract List getChildren();

        public String toString() {
            return name;
        }

        public int hashCode() {
            //JTree puts nodes in a Hashtable. To keep things simple, just return 0,
            //which will cause linear searches (fine for small trees). [Bob Boseko]
            return 0;
        }

        public boolean equals(Object other) {
            //Folders are value objects. [Bob Boseko]
            if (!(other instanceof Folder)) {
                return false;
            }
            Folder otherFolder = (Folder) other;
            return parent == otherFolder.parent && name.equals(otherFolder.name);
        }

        public Class getChildrenClass() {
            return childrenClass;
        }

        public Object getParent() {
            return parent;
        }
    }
    private Object root;

    public SimpleTreeModel(Object root) {
        this.root = root;
    }

    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        return !(node instanceof Folder) && getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            //Folders are value objects. [Bob Boseko]
            if (child instanceof Folder
                    && getChild(parent, i) instanceof Folder
                    && getChild(parent, i).toString().equals(child.toString())) {
                return i;
            }
            if (getChild(parent, i) == child) {
                return i;
            }
        }
        Assert.shouldNeverReachHere(parent + ", " + child);
        return -1;
    }
    private ArrayList listeners = new ArrayList();
    private boolean firingEvents = true;

    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * No need to handle Folders
     *
     * @param parent not a Folder
     */
    public abstract List getChildren(Object parent);

    public Object getChild(Object parent, int index) {
        return children(parent).get(index);
    }

    private List children(Object parent) {
        return parent instanceof Folder
                ? ((Folder) parent).getChildren()
                : getChildren(parent);
    }

    public int getChildCount(Object parent) {
        return children(parent).size();
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        if (!firingEvents) {
            return;
        }
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            TreeModelListener l = (TreeModelListener) i.next();
            l.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e) {
        if (!firingEvents) {
            return;
        }
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            TreeModelListener l = (TreeModelListener) i.next();
            l.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e) {
        if (!firingEvents) {
            return;
        }
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            TreeModelListener l = (TreeModelListener) i.next();
            l.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e) {
        if (!firingEvents) {
            return;
        }
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            TreeModelListener l = (TreeModelListener) i.next();
            l.treeStructureChanged(e);
        }
    }

    public void setFiringEvents(boolean firingEvents) {
        this.firingEvents = firingEvents;
    }
}
