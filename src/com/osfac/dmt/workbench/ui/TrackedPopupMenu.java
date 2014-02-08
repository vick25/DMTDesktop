package com.osfac.dmt.workbench.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.JPopupMenu;

public class TrackedPopupMenu extends JPopupMenu {

    private static Collection trackedPopupMenus = new ArrayList();

    public static Collection trackedPopupMenus() {
        return Collections.unmodifiableCollection(trackedPopupMenus);
    }

    public TrackedPopupMenu() {
        super();
        trackedPopupMenus.add(this);
    }

    public TrackedPopupMenu(String label) {
        super(label);
        trackedPopupMenus.add(this);
    }
}
