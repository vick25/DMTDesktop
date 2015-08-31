package com.cadplan.jump;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;

/**
 * User: geoff Date: 16/01/2007 Time: 14:22:56 Copyright 2005 Geoffrey G Roy.
 */
public class VertexColorButton extends Button implements ActionListener {

    Color color;

    public VertexColorButton(Color color) {
        super("      ");

        this.color = color;
        setBackground(color);
        addActionListener(this);
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Color newColor = JColorChooser.showDialog(this, "Select color", color);
        if (newColor != null) {
            color = newColor;
            setBackground(color);
        }
    }
}
