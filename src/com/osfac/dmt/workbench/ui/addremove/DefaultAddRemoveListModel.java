package com.osfac.dmt.workbench.ui.addremove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

public class DefaultAddRemoveListModel implements AddRemoveListModel {

    private DefaultListModel listModel;
    private boolean sorted = false;

    public DefaultAddRemoveListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    public ListModel getListModel() {
        return listModel;
    }

    public void add(Object item) {
        listModel.addElement(item);

        if (sorted) {
            sort();
        }
    }

    private void setItemsWithoutSorting(Collection items) {
        listModel.clear();

        for (Iterator i = items.iterator(); i.hasNext();) {
            listModel.addElement(i.next());
        }
    }

    public void setItems(Collection items) {
        setItemsWithoutSorting(items);

        if (sorted) {
            sort();
        }
    }

    private void sort() {
        ArrayList items = new ArrayList(getItems());
        Collections.sort(items);
        setItemsWithoutSorting(items);
    }

    public List getItems() {
        return Arrays.asList(listModel.toArray());
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void remove(Object item) {
        listModel.removeElement(item);
    }
}
