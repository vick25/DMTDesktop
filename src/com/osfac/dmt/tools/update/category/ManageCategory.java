package com.osfac.dmt.tools.update.category;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.setting.SettingKeyFactory;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ManageCategory extends javax.swing.JDialog {

    public ManageCategory(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = (JFrame) parent;
        model = new MyTableModel();
        table = new SortableTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{
            getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")),
            getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255")),
            getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe3Color3, "210, 255, 210"))}));
        Config.centerTableHeadAndBold(table);
        initComponents();
        updateTable();

        jScrollPane1.setViewportView(table);
        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() > -1) {
                    BEdit.setEnabled(true);
                }
            }
        });
        timer.start();
        this.setIconImage(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/view_multicolumn(10).png")).getImage());
        this.setLocationRelativeTo(parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BAdd = new com.jidesoft.swing.JideButton();
        BEdit = new com.jidesoft.swing.JideButton();
        BClose = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Category manager");

        BAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/edit_add(8).png"))); // NOI18N
        BAdd.setText("New Category");
        BAdd.setFocusable(false);
        BAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAddActionPerformed(evt);
            }
        });

        BEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/editor.png"))); // NOI18N
        BEdit.setText("Edit Category");
        BEdit.setEnabled(false);
        BEdit.setFocusable(false);
        BEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEditActionPerformed(evt);
            }
        });

        BClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BClose.setText("Close");
        BClose.setFocusable(false);
        BClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(BAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(BClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_BCloseActionPerformed

    private void BAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAddActionPerformed
        new AddCategory(parent, true).setVisible(true);
        updateTable();
    }//GEN-LAST:event_BAddActionPerformed

    private void BEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEditActionPerformed
        new EditCategory(parent, true, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())).setVisible(true);
        updateTable();
    }//GEN-LAST:event_BEditActionPerformed

    private void fillTable() {
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT * FROM dmt_category");
            ResultSet res = ps.executeQuery();
            int row = 0;
            while (res.next()) {
                if (row >= table.getRowCount()) {
                    model.addNewRow();
                }
                table.setValueAt(res.getString("id_category"), row, 0);
                table.setValueAt(res.getString("category_name"), row, 1);
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTable() {
        model = new MyTableModel();
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        fillTable();
        BEdit.setEnabled(false);
        TableUtils.autoResizeAllColumns(table);
    }

    private class MyTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES = {"ID", "Category",};
        private final ArrayList[] DATA;

        public MyTableModel() {
            DATA = new ArrayList[COLUMN_NAMES.length];
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i] = new ArrayList();
            }
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return DATA[0].size();
        }

        @Override
        public String getColumnName(int col) {
            return COLUMN_NAMES[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return DATA[col].get(row);
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return (false);
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            DATA[col].set(row, value);
            fireTableCellUpdated(row, col);
        }

        public void addNewRow() {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].add("");
            }
            this.fireTableRowsInserted(0, DATA[0].size() - 1);
        }

        public void removeNewRow() {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].remove(DATA[i].size() - 1);
            }
            this.fireTableRowsDeleted(0, DATA[0].size() - 1);
        }

        public void removeNewRow(int index) {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].remove(index);
            }
            this.fireTableRowsDeleted(0, DATA[0].size() - 1);
        }
    }

    private Color getColorFromKey(String value) {
        String tab[] = value.split(", ");
        return new Color(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Config dbConnection = new Config();
                ManageCategory dialog = new ManageCategory(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BAdd;
    private com.jidesoft.swing.JideButton BClose;
    private com.jidesoft.swing.JideButton BEdit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    JFrame parent;
    private SortableTable table;
    private MyTableModel model;
}
