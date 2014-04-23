package com.cadplan.jump;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ColorButtonB extends Button implements ActionListener {

    Color color;
    Furniture item;

    public ColorButtonB(Furniture item) {
        super("      ");
        this.item = item;
        color = item.bcolor;
        setBackground(color);
        addActionListener(this);
    }

    public Color getColor() {
        return color;
    }

    public void setItem(Furniture item) {
        this.item = item;
        color = item.bcolor;
        setBackground(color);
    }

    public void actionPerformed(ActionEvent ev) {
        Color newColor = JColorChooser.showDialog(this, "Select color", color);
        if (newColor != null) {
            color = newColor;
            setBackground(color);
            item.bcolor = color;
        }
    }
}
