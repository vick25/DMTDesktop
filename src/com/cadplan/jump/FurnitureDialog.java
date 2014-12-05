package com.cadplan.jump;

import com.cadplan.designer.GridBagDesigner;
import com.osfac.dmt.workbench.DMTWorkbench;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FurnitureDialog extends JDialog implements ActionListener, ChangeListener {

    JButton cancelButton, applyButton, closeButton;
    FurnitureTitlePanel titlePanel;
    FurnitureScalePanel scalePanel;
    FurnitureBorderPanel borderPanel;
    FurnitureNorthPanel northPanel;
    FurnitureNotePanel notePanel;
    FurnitureLegendPanel legendPanel;
    LayerLegendPanel layerLegendPanel;
    FurnitureImagePanel imagePanel;
    FurnitureTitle title;
    FurnitureScale scaleItem;
    FurnitureBorder border;
    Vector<FurnitureBorder> borders;
    FurnitureNorth north;
    FurnitureNote note;
    Vector<FurnitureNote> notes;
    FurnitureLegend legend;
    LayerLegend layerLegend;
    Vector<FurnitureImage> imageItems;
    PrinterSetup parent;
    JTabbedPane tabbedPane;
    I18NPlug iPlug;

    public FurnitureDialog(JDialog parent, FurnitureTitle title, FurnitureScale scaleItem,
            FurnitureBorder border, Vector<FurnitureBorder> borders, FurnitureNorth north, Vector<FurnitureNote> notes, FurnitureLegend legend,
            LayerLegend layerLegend, Vector<FurnitureImage> imageItems, I18NPlug iPlug) {
        super(parent, iPlug.get("JumpPrinter.Furniture"), false);
        this.title = title;
        this.scaleItem = scaleItem;
        this.border = border;
        this.borders = borders;
        this.north = north;
        this.notes = notes;
        this.legend = legend;
        this.layerLegend = layerLegend;
        this.imageItems = imageItems;
        this.parent = (PrinterSetup) parent;
        this.iPlug = iPlug;

        init();
    }

    private void init() {

        GridBagDesigner gb = new GridBagDesigner(this);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener(this);


        titlePanel = new FurnitureTitlePanel(title, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.Title"), titlePanel);

        scalePanel = new FurnitureScalePanel(scaleItem, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.Scale"), scalePanel);

        borderPanel = new FurnitureBorderPanel(this, border, borders, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.Border"), borderPanel);

        northPanel = new FurnitureNorthPanel(north, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.North"), northPanel);

        FurnitureNote note = notes.elementAt(0);
        notePanel = new FurnitureNotePanel(notes, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.Note"), notePanel);

        legendPanel = new FurnitureLegendPanel(legend, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.Legend"), legendPanel);

        layerLegendPanel = new LayerLegendPanel(layerLegend, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.LayerLegend"), layerLegendPanel);

        imagePanel = new FurnitureImagePanel(this, imageItems, iPlug);
        tabbedPane.addTab(iPlug.get("JumpPrinter.Furniture.Image"), imagePanel);

        gb.setPosition(0, 0);
        gb.setFill(GridBagConstraints.BOTH);
        gb.setWeight(1.0, 1.0);
        gb.setSpan(3, 1);
        gb.addComponent(tabbedPane);


        cancelButton = new JButton(iPlug.get("JumpPrinter.Furniture.Cancel"));
        gb.setPosition(0, 1);
        gb.setInsets(0, 10, 0, 10);
        gb.addComponent(cancelButton);
        cancelButton.addActionListener(this);

        applyButton = new JButton(iPlug.get("JumpPrinter.Furniture.Apply"));
        gb.setPosition(1, 1);
        gb.setInsets(0, 10, 0, 10);
        gb.addComponent(applyButton);
        applyButton.addActionListener(this);

        closeButton = new JButton(iPlug.get("JumpPrinter.Furniture.Close"));
        gb.setPosition(2, 1);
        gb.setInsets(0, 10, 0, 10);
        gb.setAnchor(GridBagConstraints.WEST);
        gb.setWeight(1.0, 0.0);
        gb.addComponent(closeButton);
        closeButton.addActionListener(this);

        pack();
        this.setLocationRelativeTo(DMTWorkbench.frame);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == cancelButton) {
            dispose();
        }
        if (ev.getSource() == applyButton) {

            title = titlePanel.getTitle();
            //System.err.println("Apply button getting scale");
            scaleItem = scalePanel.getScale();
            if (!scaleItem.validItem) {
                JOptionPane.showMessageDialog(this, iPlug.get("JumpPrinter.Furniture.Scale.Message4"),
                        iPlug.get("JumpPrinter.Error"), JOptionPane.ERROR_MESSAGE);
                //return;
            }
            border = borderPanel.getBorderItem();
            borders = borderPanel.getBorders();
            north = northPanel.getNorth();
            note = notePanel.getNote();
            legend = legendPanel.getLegend();
            layerLegend = layerLegendPanel.getLegend();
            imageItems = imagePanel.getImageItems();

            parent.updateDrawing();

        }
        if (ev.getSource() == closeButton) {
            title = titlePanel.getTitle();
            scaleItem = scalePanel.getScale();
            border = borderPanel.getBorderItem();
            borders = borderPanel.getBorders();
            north = northPanel.getNorth();
            note = notePanel.getNote();
            legend = legendPanel.getLegend();
            layerLegend = layerLegendPanel.getLegend();
            imageItems = imagePanel.getImageItems();

            parent.updateDrawing();
            dispose();
        }
    }

    public void stateChanged(ChangeEvent ev) {
        if (ev.getSource() == tabbedPane) {
            JPanel selection = (JPanel) ((JTabbedPane) ev.getSource()).getSelectedComponent();

            if (selection == titlePanel) // MEASURING
            {
            }

            if (selection == scalePanel) {
            }
        }
    }
}
