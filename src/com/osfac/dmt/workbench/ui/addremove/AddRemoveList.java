package com.osfac.dmt.workbench.ui.addremove;

import com.osfac.dmt.workbench.ui.InputChangedListener;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.List;

public interface AddRemoveList {

    public AddRemoveListModel getModel();

    public void add(InputChangedListener listener);

    public void add(MouseListener listener);

    public List getSelectedItems();

    /**
     * Will only be called if the AddRemovePanel's Move Up and Move Down buttons
     * are visible.
     */
    public void setSelectedItems(Collection items);
}
