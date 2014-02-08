package com.osfac.dmt.workbench.ui;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A popup menu with a title row.
 */
public class TitledPopupMenu extends TrackedPopupMenu {

    private JLabel titleLabel = new JLabel();

    public TitledPopupMenu() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(3, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);
        addSeparator();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
