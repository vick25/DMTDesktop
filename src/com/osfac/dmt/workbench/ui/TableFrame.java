package com.osfac.dmt.workbench.ui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableFrame extends JInternalFrame {

    private JScrollPane scrollPane = new JScrollPane();
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);

    public TableFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(400, 200);
    }

    public DefaultTableModel getModel() {
        return model;
    }

    private void jbInit() throws Exception {
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().add(table, null);
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
    }
}
