package org.openjump.core.ui.swing.listener;

import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.plugin.MenuItemShownListener;
import javax.swing.JMenuItem;

public class EnableCheckMenuItemShownListener implements MenuItemShownListener {

    private EnableCheck enableCheck;
    private WorkbenchContext workbenchContext;
    private String enabledMessage;

    public EnableCheckMenuItemShownListener(WorkbenchContext workbenchContext,
            EnableCheck enableCheck) {
        this.workbenchContext = workbenchContext;
        this.enableCheck = enableCheck;
    }

    public EnableCheckMenuItemShownListener(WorkbenchContext workbenchContext,
            EnableCheck enableCheck, String enabledMessage) {
        this.workbenchContext = workbenchContext;
        this.enableCheck = enableCheck;
        this.enabledMessage = enabledMessage;
    }

    public void menuItemShown(JMenuItem menuItem) {
        String errorMessage = null;
        try {
            errorMessage = enableCheck.check(menuItem);
        } catch (Exception e) {
            DMTWorkbench workbench = workbenchContext.getWorkbench();
            WorkbenchFrame frame = workbench.getFrame();
            frame.log(menuItem.getText());
            frame.handleThrowable(e);
        }
        if (errorMessage != null) {
            menuItem.setEnabled(false);
            menuItem.setToolTipText(errorMessage);
        } else {
            menuItem.setEnabled(true);
            menuItem.setToolTipText(enabledMessage);
        }
    }
}
