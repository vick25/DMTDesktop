package com.osfac.dmt.workbench.ui.plugin;

import javax.swing.JMenuItem;

/**
 * An object that is notified when a particular menu item is shown e.g. to set
 * its enabled state.
 */
//TODO - Refactoring: Move into FeatureInstaller, which is the only class that
//refers to this interface [Bob Boseko 10/22/2003]
public interface MenuItemShownListener {

    public void menuItemShown(JMenuItem menuItem);
}
