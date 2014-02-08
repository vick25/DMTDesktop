package com.osfac.dmt.workbench.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class SplashPanel extends JPanel {

    private GridBagLayout gridBagLayout = new GridBagLayout();
    private JLabel captionLabel = new JLabel();
    private JLabel imageLabel = new JLabel();
    private Border border1;
    private Border border2;

    public SplashPanel(Icon image, String caption) {
        try {
            jbInit();
            imageLabel.setIcon(image);
            captionLabel.setText(caption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        border2 =
                BorderFactory.createBevelBorder(
                BevelBorder.RAISED,
                Color.white,
                Color.white,
                new Color(103, 101, 98),
                new Color(148, 145, 140));

        CompoundBorder compoundBorder =
                new CompoundBorder(BorderFactory.createLineBorder(Color.black), border2);
        this.setLayout(gridBagLayout);
        captionLabel.setFont(captionLabel.getFont().deriveFont(java.awt.Font.BOLD, 15.0f));
        this.setBackground(Color.white);
        captionLabel.setForeground(Color.red);
        captionLabel.setBorder(border1);
        captionLabel.setText("Version 1.0");
        this.setBorder(compoundBorder);
        this.add(
                imageLabel,
                new GridBagConstraints(
                0,
                0,
                1,
                1,
                1,
                1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0,
                0));
        this.add(
                captionLabel,
                new GridBagConstraints(
                0,
                1,
                1,
                1,
                0,
                0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 10),
                0,
                0));
    }
}
