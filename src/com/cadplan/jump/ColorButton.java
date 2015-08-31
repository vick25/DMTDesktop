package com.cadplan.jump;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;

public class ColorButton extends Button implements ActionListener {

    Color color;
    Furniture item;

    public ColorButton(Furniture item) {
        super("      ");
        this.item = item;
        color = item.color;
        setBackground(color);
        addActionListener(this);
    }

    public Color getColor() {
        return color;
    }

    public void setItem(Furniture item) {
        this.item = item;
        color = item.color;
        setBackground(color);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Color newColor = JColorChooser.showDialog(this, "Select color", color);
        if (newColor != null) {
            color = newColor;
            setBackground(color);
            item.color = color;
        }
    }
}
