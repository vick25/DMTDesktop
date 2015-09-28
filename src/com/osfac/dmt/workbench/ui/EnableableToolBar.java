package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.workbench.plugin.EnableCheck;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.openjump.swing.listener.InvokeMethodActionListener;

/**
 * Extends JToolBar to create an {@link JToolBar} with certain buttons enabled (for saving state).
 */
public class EnableableToolBar extends JToolBar {

    protected HashMap buttonToEnableCheckMap = new HashMap();
    private InvokeMethodActionListener updateStateListener = new InvokeMethodActionListener(this, "updateEnabledState");

    public EnableCheck getEnableCheck(AbstractButton button) {
        return (EnableCheck) buttonToEnableCheckMap.get(button);
    }

    public void setEnableCheck(AbstractButton button, EnableCheck check) {
        buttonToEnableCheckMap.put(button, check);
    }

    public EnableableToolBar() {
    }

    public void updateEnabledState() {
        for (Iterator i = buttonToEnableCheckMap.keySet().iterator();
                i.hasNext();) {
            JComponent component = (JComponent) i.next();
            EnableCheck enableCheck
                    = (EnableCheck) buttonToEnableCheckMap.get(component);
            component.setEnabled(enableCheck.check(component) == null);
        }
    }

    /**
     * Unlike #addSeparator, works for vertical toolbars.
     */
    public void addSpacer() {
        JPanel filler = new JPanel();
        filler.setPreferredSize(new Dimension(5, 5));
        filler.setMinimumSize(new Dimension(5, 5));
        filler.setMaximumSize(new Dimension(5, 5));
        add(filler);
    }

    public void add(
            AbstractButton button,
            String tooltip,
            Icon icon,
            ActionListener actionListener,
            EnableCheck enableCheck) {
        if (enableCheck != null) {
            buttonToEnableCheckMap.put(button, enableCheck);
        }
        button.setFocusable(false);
        button.setIcon(icon);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText(tooltip);
        button.addActionListener(actionListener);
        button.addActionListener(updateStateListener);
        add(button);
    }

    public void add(final int index, final AbstractButton button,
            final String tooltip, final Icon icon,
            final ActionListener actionListener, final EnableCheck enableCheck) {
        if (enableCheck != null) {
            buttonToEnableCheckMap.put(button, enableCheck);
        }
        button.setIcon(icon);
        button.setFocusable(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText(tooltip);
        button.addActionListener(actionListener);
        button.addActionListener(updateStateListener);
        add(button, index);
    }
}
