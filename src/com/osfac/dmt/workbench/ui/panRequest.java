package com.osfac.dmt.workbench.ui;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.HyperlinkTableCellEditorRenderer;
import com.jidesoft.grid.RolloverTableUtils;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.form.ReviewDataRequestForm;
import com.osfac.dmt.workbench.DMTWorkbench;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class panRequest extends javax.swing.JPanel {

    public panRequest() {
        initComponents(I18N.DMTResourceBundle);
        BClear.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/editclear(7).png")));
        executed.setIcon(good);
        notExecuted.setIcon(bad);
        createTable();
        fillRequestTable();
        pan.setViewportView(table);
        executed.setHorizontalAlignment(SwingConstants.CENTER);
        notExecuted.setHorizontalAlignment(SwingConstants.CENTER);
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() < 1) {
                    BClear.setEnabled(false);
                    RolloverTableUtils.uninstall(table);
                } else {
                    BClear.setEnabled(true);
                    RolloverTableUtils.install(table);
                }
            }
        });
        timer.start();
        BClear.setVisible(Config.isLiteVersion());
    }

    public static void fillRequestTable() {
        try {
            cleanTable();
            PreparedStatement ps = null;
            String sql = "SELECT * FROM dmt_delivery WHERE id_user = ?";
            if (Config.isLiteVersion()) {
                ps = Config.con.prepareStatement(sql);
                ps.setInt(1, WorkbenchFrame.idUser);
            } else {
                sql += " AND confirm_email_sent = ?";
                ps = Config.con.prepareStatement(sql);
                ps.setInt(1, WorkbenchFrame.idUser);
                ps.setString(2, "No");
            }
            ResultSet res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                if (i >= table.getRowCount()) {
                    model.addNewRow();
                }
                table.setValueAt(res.getString("dmt_delivery.id_delivery"), i, 0);
                table.setValueAt(Config.dateFormat.format(res.getDate("request_date")) + "/"
                        + I18N.get("Text.REQUEST") + "(" + (i + 1) + ")", i, 1);
                i++;
            }
            TableUtils.autoResizeAllColumns(table);
            TableUtils.autoResizeAllRows(table);
        } catch (SQLException ex) {
//            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
//                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private void createTable() {
        model = new MyTableModel();
        table = new CellStyleTable(model);

        HyperlinkTableCellEditorRenderer renderer = new HyperlinkTableCellEditorRenderer();
        renderer.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReviewDataRequestForm(DMTWorkbench.frame, true, Integer.parseInt(
                        table.getValueAt(table.getRolloverRow(), 0).toString())).setVisible(true);
            }
        });
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);
        table.getColumnModel().getColumn(1).setCellEditor(renderer);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
//        int size = 23;
//        table.getColumnModel().getColumn(0).setPreferredWidth(size);
//        table.getColumnModel().getColumn(0).setMaxWidth(size);
//        table.getColumnModel().getColumn(0).setMinWidth(size);
//
//        int columnWidth = 50;
//        table.getColumnModel().getColumn(2).setPreferredWidth(columnWidth);
//        table.getColumnModel().getColumn(2).setMaxWidth(columnWidth);
//        table.getColumnModel().getColumn(2).setMinWidth(columnWidth);
        table.setRowHeight(14);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
    }

    private boolean isRequestPerformed(int row) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT confirm_email_sent FROM dmt_delivery "
                    + "WHERE id_delivery = ?");
            String value = table.getValueAt(row, 0).toString();
            ps.setString(1, value);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return !res.getString(1).equals("No");
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return false;
    }

    private class MyTableModel extends AbstractTableModel implements StyleModel {

        private final String[] COLUMN_NAMES = {I18N.get("Text.ID"), I18N.get("Text.REQUEST"),
            I18N.get("Text.SENT")};
        private final ArrayList[] DATA;

        public MyTableModel() {
            DATA = new ArrayList[COLUMN_NAMES.length];
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i] = new ArrayList();
            }
        }

        public boolean isNavigationOn() {
            return true;
        }

        @Override
        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == table.getColumnCount() - 1 && !table.getValueAt(rowIndex, 1).toString().equals("")) {
                if (isRequestPerformed(rowIndex)) {
                    return executed;
                } else {
                    return notExecuted;
                }
            } else {
                return null;
            }
        }

        @Override
        public boolean isCellStyleOn() {
            return true;
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

    private static void cleanTable() {
        int row = table.getRowCount();
        while (row > 0) {
            model.removeNewRow();
            row--;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan = new javax.swing.JScrollPane();
        BClear = new com.jidesoft.swing.JideButton();

        setOpaque(false);

        pan.setBorder(null);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        BClear.setText(bundle.getString("panRequest.BClear.text")); // NOI18N
        BClear.setFocusable(false);
        BClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BClear, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(pan, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        pan = new javax.swing.JScrollPane();
        BClear = new com.jidesoft.swing.JideButton();

        setOpaque(false);

        pan.setBorder(null);

        BClear.setText(bundle.getString("panRequest.BClear.text")); // NOI18N
        BClear.setFocusable(false);
        BClear.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pan, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BClear, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(pan, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private void BClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BClearActionPerformed
        if (JOptionPane.showConfirmDialog(DMTWorkbench.frame, I18N.get("panRequest.Cofirm-message-clear"),
                I18N.get("Text.Confirm"), 0) == 0) {
            try {
                PreparedStatement ps = Config.con.prepareStatement("TRUNCATE TABLE `dmt_requester`");
                int result = ps.executeUpdate();
                ps = Config.con.prepareStatement("TRUNCATE TABLE `dmt_delivery`");
                result = ps.executeUpdate();
                cleanTable();
            } catch (SQLException ex) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        ex.getMessage(), null, null, ex, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BClearActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BClear;
    private javax.swing.JScrollPane pan;
    // End of variables declaration//GEN-END:variables
    static MyTableModel model;
    static CellStyleTable table;
    CellStyle notExecuted = new CellStyle();
    CellStyle executed = new CellStyle();
    final ImageIcon good = new ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"));
    final ImageIcon bad = new ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit12.png"));
}
