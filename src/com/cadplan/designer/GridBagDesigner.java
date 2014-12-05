package com.cadplan.designer;

import java.awt.*;
import javax.swing.*;

public class GridBagDesigner {

    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private Container container;

    /**
     * Constructor for GridBagDesgner
     *
     * @param container
     */
    public GridBagDesigner(Container container) {
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        container.setLayout(layout);
        this.container = container;
        setDefaults();
    }

    /**
     * Adds a component using currrent settings
     *
     * @param component
     */
    public void addComponent(Component component) {
        layout.setConstraints(component, constraints);
        container.add(component);
        setDefaults();
    }

    public void addComponentRetain(Component component) {
        layout.setConstraints(component, constraints);
        container.add(component);
    }

    /**
     * Adds a JComponent using current settings and fixes size to xSize * ySize
     *
     * @param component
     * @param xSize
     * @param ySize
     */
    public void addComponent(JComponent component, int xSize, int ySize) {
        Dimension size = new Dimension(xSize, ySize);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        layout.setConstraints(component, constraints);
        container.add(component);
        setDefaults();
    }

    public void addComponentRetain(JComponent component, int xSize, int ySize) {
        Dimension size = new Dimension(xSize, ySize);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        layout.setConstraints(component, constraints);
        container.add(component);
    }

    public void resetLayout() {
        setDefaults();
    }

    /**
     * Sets gid position Default is (0,0);
     *
     * @param gridx
     * @param gridy
     */
    public void setPosition(int gridx, int gridy) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
    }

    /**
     * Sets grid span Defaults are (1,1)
     *
     * @param gridwidth
     * @param gridheight
     */
    public void setSpan(int gridwidth, int gridheight) {
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
    }

    /**
     * Sets cell weights Defaults are (0.0,0.0)
     *
     * @param weightx
     * @param weighty
     */
    public void setWeight(double weightx, double weighty) {
        constraints.weightx = weightx;
        constraints.weighty = weighty;
    }

    /**
     * Sets cell fill type as one of: GridBagConstraints.{NONE, VERTICAL,
     * HORIZONTAL, BOTH} Default is NONE
     *
     * @param fill
     */
    public void setFill(int fill) {
        constraints.fill = fill;
    }

    /**
     * Sets cell anchor type as one of: GridBagConstraints.{NORTH, NORTHEAST,
     * EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST, CENTER} Default is
     * CENTER
     *
     * @param anchor
     */
    public void setAnchor(int anchor) {
        constraints.anchor = anchor;
    }

    /**
     * Sets cell insets Defaults are (0,0,0,0)
     *
     * @param top
     * @param left
     * @param bottom
     * @param right
     */
    public void setInsets(int top, int left, int bottom, int right) {
        constraints.insets = new Insets(top, left, bottom, right);
    }

    /**
     * Sets cell defaults
     *
     */
    private void setDefaults() {
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.insets = new Insets(0, 0, 0, 0);

    }
}
