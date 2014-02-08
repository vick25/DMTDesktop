package com.osfac.dmt.workbench.plugin;

import javax.swing.JComponent;

/**
 * A test for whether to enable or disable a menu, toolbar button, or other
 * component. Provides a handy message about why a component is disabled.
 *
 * @see EnableCheckFactory
 */
public interface EnableCheck {

    /**
     * Returns a non-null value if the check failed. Sometimes the return value
     * is used (for example, it is displayed as a tooltip for menu-items);
     * sometimes it is not (for example, toolbar buttons don't do anything with
     * the return value). An advanced use of an EnableCheck is simply to change
     * some property of a menu item (such as the text), as it is called when
     * menu items are displayed.
     *
     * @return an error message if the check failed, or null if the check passed
     */
    public String check(JComponent component);
}
