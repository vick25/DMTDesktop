package com.osfac.dmt.workbench.ui.addremove;

import java.util.Collection;
import java.util.List;

public interface AddRemoveListModel {

    public void add(Object item);

    public void setItems(Collection items);

    public List getItems();

    public void remove(Object item);
}
