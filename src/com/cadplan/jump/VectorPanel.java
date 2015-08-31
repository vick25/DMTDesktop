package com.cadplan.jump;

import com.cadplan.designer.GridBagDesigner;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

/**
 * User: geoff Date: 17/06/2007 Time: 08:47:17 Copyright 2005 Geoffrey G Roy.
 */
public class VectorPanel extends JPanel {

    SymbolPanel symbolPanel;
    ButtonGroup group;
    I18NPlug iPlug;

    public VectorPanel(ButtonGroup group, I18NPlug iPlug) {
        this.group = group;
        this.iPlug = iPlug;
        init();
    }

    public void init() {
        GridBagDesigner gb = new GridBagDesigner(this);
        symbolPanel = new SymbolPanel(group);
        symbolPanel.setBackground(Color.WHITE);
        gb.setPosition(0, 0);
        gb.setInsets(10, 10, 10, 10);
        gb.setWeight(1.0, 1.0);
        gb.setAnchor(GridBagConstraints.WEST);
        gb.setFill(GridBagConstraints.BOTH);
        gb.setSpan(3, 1);
        gb.addComponent(symbolPanel);
    }
}
