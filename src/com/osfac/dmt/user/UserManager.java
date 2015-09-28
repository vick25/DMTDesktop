package com.osfac.dmt.user;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class UserManager extends javax.swing.JPanel {

    public UserManager() {
        tableModel = new MyTableModelUser();
        initComponents(I18N.DMTResourceBundle);
        filterField.setTableModel(tableModel);
        table = new SortableTable();
        table.setTableStyleProvider(new RowStripeTableStyleProvider(
                new Color[]{Config.getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color1,
                                    "253, 253, 244")), Config.getColorFromKey(Config.pref
                            .get(SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255")),
                    Config.getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe3Color3,
                                    "210, 255, 210"))}));
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (!table.getValueAt(table.getSelectedRow(), 1).toString().equals("")) {
                        BPassword.setEnabled(true);
//                        BStatus.setEnabled(true);
                        BDelete.setEnabled(true);
                    } else {
                        BPassword.setEnabled(false);
                        BStatus.setEnabled(false);
                        BDelete.setEnabled(false);
                        BSendEmail.setEnabled(false);
                        BRefresh.setEnabled(false);
                    }
                } catch (Exception ex) {
                    BPassword.setEnabled(false);
                    BStatus.setEnabled(false);
                    BDelete.setEnabled(false);
                    BSendEmail.setEnabled(false);
                    BRefresh.setEnabled(false);
                }
            }
        });
        Config.centerTableHeadAndBold(table);
        filterField.setTable(table);
        table.setModel(tableModel);
        BSendEmail.setEnabled(false);
        BRefresh.setEnabled(false);
        waitFilling();
    }

    private void waitFilling() {
        WorkbenchFrame.progress.setProgressStatus(I18N.get("UserManager.loading-user"));
        WorkbenchFrame.progress.setIndeterminate(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                fillTable();
            }
        }, 5000);
    }

    private void waitFilling(JXBusyLabel lab) {
        jScrollPane1.setViewportView(lab);
        waitFilling();
    }

    private void fillTable() {
        try {
            int nbRow = 0;
            PreparedStatement ps = Config.con.prepareStatement("SELECT * FROM dmt_user WHERE id_user NOT IN (?,?)");
            ps.setInt(1, 3);
            ps.setInt(2, 4);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                if (nbRow >= table.getRowCount()) {
                    tableModel.addNewRow();
                }
                table.setValueAt(res.getString("id_user"), nbRow, 0);
                table.setValueAt(res.getString("firstname"), nbRow, 1);
                table.setValueAt(res.getString("familyname"), nbRow, 2);
                table.setValueAt(res.getString("email"), nbRow, 3);
                table.setValueAt(res.getString("privilege"), nbRow, 4);
//                table.setValueAt(res.getString(1), nbRow, 5);
                nbRow++;
            }
            jScrollPane1.setViewportView(table);
            filterField.setTableModel(tableModel);
            table.setModel(filterField.getDisplayTableModel());
            TableUtils.autoResizeAllColumns(table);
            TableUtils.autoResizeAllRows(table);
            WorkbenchFrame.progress.setProgress(100);
            BSendEmail.setEnabled(true);
            BRefresh.setEnabled(true);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private void cleanTable() {
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
    }

    private class MyTableModelUser extends AbstractTableModel {

        private final String[] COLUMN_NAMES = {I18N.get("UserManager.ID"), I18N.get("UserManager.First-name"),
            I18N.get("UserManager.Family-name"), I18N.get("UserManager.Email"), I18N.get("UserManager.Privilege")};
        private final ArrayList[] DATA;

        public MyTableModelUser() {
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

    private void updateTable() {
        BPassword.setEnabled(false);
        BStatus.setEnabled(false);
        BDelete.setEnabled(false);
        BSendEmail.setEnabled(false);
        BRefresh.setEnabled(false);
        cleanTable();
        waitFilling(LBWait);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        LBWait = new org.jdesktop.swingx.JXBusyLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        BCreate = new com.jidesoft.swing.JideButton();
        BDelete = new com.jidesoft.swing.JideButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        BRefresh = new com.jidesoft.swing.JideButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        BSendEmail = new com.jidesoft.swing.JideButton();
        BPassword = new com.jidesoft.swing.JideButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        BStatus = new com.jidesoft.swing.JideButton();
        filterField = new com.jidesoft.grid.QuickTableFilterField();

        setBackground(new java.awt.Color(255, 255, 255));

        LBWait.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        LBWait.setText(bundle.getString("UserManager.LBWait.text")); // NOI18N
        LBWait.setBusy(true);
        jScrollPane1.setViewportView(LBWait);

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setRollover(true);

        BCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/user-info(4).png"))); // NOI18N
        BCreate.setText(bundle.getString("UserManager.BCreate.text")); // NOI18N
        BCreate.setFocusable(false);
        BCreate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BCreate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCreateActionPerformed(evt);
            }
        });
        jToolBar1.add(BCreate);

        BDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/dialog_close(2).png"))); // NOI18N
        BDelete.setText(bundle.getString("UserManager.BDelete.text")); // NOI18N
        BDelete.setEnabled(false);
        BDelete.setFocusable(false);
        BDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(BDelete);
        jToolBar1.add(jSeparator3);

        BRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-clear.png"))); // NOI18N
        BRefresh.setText(bundle.getString("UserManager.BRefresh.text")); // NOI18N
        BRefresh.setFocusable(false);
        BRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(BRefresh);
        jToolBar1.add(jSeparator1);

        BSendEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/mail.png"))); // NOI18N
        BSendEmail.setText(bundle.getString("UserManager.BSendEmail.text")); // NOI18N
        BSendEmail.setFocusable(false);
        BSendEmail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSendEmail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSendEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSendEmailActionPerformed(evt);
            }
        });
        jToolBar1.add(BSendEmail);

        BPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/gpgsm(9).png"))); // NOI18N
        BPassword.setText(bundle.getString("UserManager.BPassword.text")); // NOI18N
        BPassword.setEnabled(false);
        BPassword.setFocusable(false);
        BPassword.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BPassword.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPasswordActionPerformed(evt);
            }
        });
        jToolBar1.add(BPassword);
        jToolBar1.add(jSeparator2);

        BStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-newsfeeds.png"))); // NOI18N
        BStatus.setText(bundle.getString("UserManager.BStatus.text")); // NOI18N
        BStatus.setEnabled(false);
        BStatus.setFocusable(false);
        BStatus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BStatus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(BStatus);

        jToolBar2.add(jToolBar1);

        filterField.setHintText(bundle.getString("UserManager.filterField.hintText")); // NOI18N
        filterField.setShowMismatchColor(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        jScrollPane1 = new javax.swing.JScrollPane();
        LBWait = new org.jdesktop.swingx.JXBusyLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        BCreate = new com.jidesoft.swing.JideButton();
        BDelete = new com.jidesoft.swing.JideButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        BRefresh = new com.jidesoft.swing.JideButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        BSendEmail = new com.jidesoft.swing.JideButton();
        BPassword = new com.jidesoft.swing.JideButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        BStatus = new com.jidesoft.swing.JideButton();
        filterField = new com.jidesoft.grid.QuickTableFilterField();

        setBackground(new java.awt.Color(255, 255, 255));

        LBWait.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LBWait.setText(bundle.getString("UserManager.LBWait.text")); // NOI18N
        LBWait.setBusy(true);
        jScrollPane1.setViewportView(LBWait);

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setRollover(true);

        BCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/user-info(4).png"))); // NOI18N
        BCreate.setText(bundle.getString("UserManager.BCreate.text")); // NOI18N
        BCreate.setFocusable(false);
        BCreate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BCreate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCreateActionPerformed(evt);
            }
        });
        jToolBar1.add(BCreate);

        BDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/dialog_close(2).png"))); // NOI18N
        BDelete.setText(bundle.getString("UserManager.BDelete.text")); // NOI18N
        BDelete.setEnabled(false);
        BDelete.setFocusable(false);
        BDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(BDelete);
        jToolBar1.add(jSeparator3);

        BRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-clear.png"))); // NOI18N
        BRefresh.setText(bundle.getString("UserManager.BRefresh.text")); // NOI18N
        BRefresh.setFocusable(false);
        BRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(BRefresh);
        jToolBar1.add(jSeparator1);

        BSendEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/mail.png"))); // NOI18N
        BSendEmail.setText(bundle.getString("UserManager.BSendEmail.text")); // NOI18N
        BSendEmail.setFocusable(false);
        BSendEmail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSendEmail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSendEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSendEmailActionPerformed(evt);
            }
        });
        jToolBar1.add(BSendEmail);

        BPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/gpgsm(9).png"))); // NOI18N
        BPassword.setText(bundle.getString("UserManager.BPassword.text")); // NOI18N
        BPassword.setEnabled(false);
        BPassword.setFocusable(false);
        BPassword.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BPassword.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPasswordActionPerformed(evt);
            }
        });
        jToolBar1.add(BPassword);
        jToolBar1.add(jSeparator2);

        BStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-newsfeeds.png"))); // NOI18N
        BStatus.setText(bundle.getString("UserManager.BStatus.text")); // NOI18N
        BStatus.setEnabled(false);
        BStatus.setFocusable(false);
        BStatus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BStatus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(BStatus);

        jToolBar2.add(jToolBar1);

        filterField.setHintText(bundle.getString("UserManager.filterField.hintText")); // NOI18N
        filterField.setShowMismatchColor(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                        .addGap(10, 10, 10)));
    }// </editor-fold>

    private void BCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCreateActionPerformed
        new CreateUser(DMTWorkbench.frame, true, true).setVisible(true);
    }//GEN-LAST:event_BCreateActionPerformed

    private void BRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRefreshActionPerformed
        updateTable();
    }//GEN-LAST:event_BRefreshActionPerformed

    private void BDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(DMTWorkbench.frame, new StringBuilder(I18N.get("UserManager.This-will-delete"))
                .append(" \"").append(table.getValueAt(table.getSelectedRow(), 1)).append(" ")
                .append(table.getValueAt(table.getSelectedRow(), 2)).append("\".").append(" ")
                .append(I18N.get("UserManager.This-will-delete2")).toString(), I18N.get("Text.Confirm"), 0) == 0) {
            try {
                PreparedStatement ps = Config.con.prepareStatement("DELETE FROM dmt_user WHERE id_user = ?");
                ps.setInt(1, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
                int res1 = ps.executeUpdate();
                if (res1 == 1) {
                    tableModel.removeNewRow(table.getSelectedRow());
                    updateTable();
                }
            } catch (SQLException ex) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        ex.getMessage(), null, null, ex, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BDeleteActionPerformed

    private void BPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPasswordActionPerformed
        if (WorkbenchFrame.idUser != Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())) {
            JOptionPane.showMessageDialog(DMTWorkbench.frame, I18N.get("UserManager.message-not-allowed-to-change-password"),
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            new ChangePassword(DMTWorkbench.frame, true, table.getValueAt(table.getSelectedRow(), 3).toString()).setVisible(true);
        }
    }//GEN-LAST:event_BPasswordActionPerformed

    private void BSendEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSendEmailActionPerformed
        ArrayList<String> userList = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            userList.add(table.getValueAt(i, 3).toString());
        }
        new SendEmailToUsers(DMTWorkbench.frame, true, userList.toArray()).setVisible(true);
    }//GEN-LAST:event_BSendEmailActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BCreate;
    private com.jidesoft.swing.JideButton BDelete;
    private com.jidesoft.swing.JideButton BPassword;
    private com.jidesoft.swing.JideButton BRefresh;
    private com.jidesoft.swing.JideButton BSendEmail;
    private com.jidesoft.swing.JideButton BStatus;
    private org.jdesktop.swingx.JXBusyLabel LBWait;
    private com.jidesoft.grid.QuickTableFilterField filterField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
    SortableTable table;
    MyTableModelUser tableModel;
}
