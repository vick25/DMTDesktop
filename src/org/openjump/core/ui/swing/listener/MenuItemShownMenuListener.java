package org.openjump.core.ui.swing.listener;

import com.osfac.dmt.workbench.ui.plugin.MenuItemShownListener;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MenuItemShownMenuListener implements MenuListener {

    private MenuItemShownListener menuItemShownListener;
    private JMenuItem menuItem;

    public MenuItemShownMenuListener(JMenuItem menuItem,
            MenuItemShownListener menuItemShownListener) {
        this.menuItemShownListener = menuItemShownListener;
        this.menuItem = menuItem;
    }

    public void menuSelected(final MenuEvent e) {
        menuItemShownListener.menuItemShown(menuItem);
    }

    public void menuDeselected(final MenuEvent e) {
    }

    public void menuCanceled(final MenuEvent e) {
    }
}
