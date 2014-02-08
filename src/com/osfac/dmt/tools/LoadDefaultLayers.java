package com.osfac.dmt.tools;

import com.jidesoft.grid.BooleanCheckBoxCellEditor;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigationSortableTable;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.tooltip.ExpandedTipUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTIconsFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import org.openjump.core.ui.plugin.file.OpenFilePlugIn;

public class LoadDefaultLayers extends javax.swing.JDialog {

    public LoadDefaultLayers(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents(I18N.DMTResourceBundle);
        loadingShapeFile();
        this.setLocationRelativeTo(parent);
        this.setIconImage(DMTIconsFactory.getImageIcon(DMTIconsFactory.DMTIcon.LAYER2).getImage());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Scrll = new javax.swing.JScrollPane();
        BLLoading = new org.jdesktop.swingx.JXBusyLabel();
        BCancel = new com.jidesoft.swing.JideButton();
        BValidate = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        setTitle(bundle.getString("LoadDefaultLayers.title")); // NOI18N

        BLLoading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BLLoading.setText(bundle.getString("LoadDefaultLayers.BLLoading.text")); // NOI18N
        Scrll.setViewportView(BLLoading);

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("LoadDefaultLayers.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"))); // NOI18N
        BValidate.setText(bundle.getString("LoadDefaultLayers.BValidate.text")); // NOI18N
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Scrll, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BCancel, BValidate});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(Scrll, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BCancel, BValidate});

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

    private void initComponents(java.util.ResourceBundle bundle) {

        jPanel1 = new javax.swing.JPanel();
        Scrll = new javax.swing.JScrollPane();
        BLLoading = new org.jdesktop.swingx.JXBusyLabel();
        BCancel = new com.jidesoft.swing.JideButton();
        BValidate = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("LoadDefaultLayers.title")); // NOI18N

        BLLoading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BLLoading.setText(bundle.getString("LoadDefaultLayers.BLLoading.text")); // NOI18N
        Scrll.setViewportView(BLLoading);

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("LoadDefaultLayers.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"))); // NOI18N
        BValidate.setText(bundle.getString("LoadDefaultLayers.BValidate.text")); // NOI18N
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Scrll, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(5, 5, 5)));

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{BCancel, BValidate});

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(Scrll, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)));

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{BCancel, BValidate});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_BCancelActionPerformed

    private void BValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValidateActionPerformed
        this.dispose();
        OpenFilePlugIn filePlugin = new OpenFilePlugIn(DMTWorkbench.frame.getContext(), getPathOfFilesChecked());
        filePlugin.actionPerformed(new ActionEvent(this, 0, ""));
    }//GEN-LAST:event_BValidateActionPerformed

    private void loadingShapeFile() {
        new Thread() {
            @Override
            public void run() {
                BLLoading.setBusy(true);
                Scrll.setViewportView(BLLoading);
                File[] tab = new File("default layers").listFiles();
                for (int i = 0; i < tab.length; i++) {
                    if (tab[i].getName().endsWith("shp")) {
                        tableModel.addNewRow();
                        tableModel.setValueAt(tab[i].getName(), tableModel.getRowCount() - 1, 1);
                        tableModel.setValueAt(tab[i].getAbsolutePath(), tableModel.getRowCount() - 1, 2);
                    }
                }
                table.setModel(tableModel);
                table.setSortable(false);
                table.getTableHeader().setReorderingAllowed(false);
                table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{Config.getColorFromKey(Config.pref.get(
                    SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")), Config.getColorFromKey(Config.pref.get(
                    SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
                table.setColumnResizable(true);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                table.setShowGrid(false);
                table.getColumnModel().getColumn(0).setMinWidth(23);
                table.getColumnModel().getColumn(0).setMaxWidth(23);
                TableUtils.autoResizeAllColumns(table);
                TableUtils.autoResizeAllRows(table);
                Config.centerTableHeadAndBold(table);
                ExpandedTipUtils.install(table);
                Scrll.setViewportView(table);
                new Timer(300, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        validateEnabled();
                    }
                }).start();
            }
        }.start();
    }

    private File[] getPathOfFilesChecked() {
        File[] tab = new File[getNumberRowChecked()];
        int j = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (Boolean.valueOf(table.getValueAt(i, 0).toString()) == true) {
                tab[j] = new File(table.getValueAt(i, 2).toString());
                j++;
            }
        }
        return tab;
    }

    private class MyTableModel extends TreeTableModel {

        private String[] columnNames = {"", I18N.get("LoadDefaultLayers.File"), I18N.get("LoadDefaultLayers.File-path")};
        private ArrayList[] Data;

        public MyTableModel() {
            Data = new ArrayList[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                Data[i] = new ArrayList();
            }
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return Data[0].size();
        }

        @Override
        public EditorContext getEditorContextAt(int row, int column) {
            return column == 0 ? BooleanCheckBoxCellEditor.CONTEXT : null;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return Data[col].get(row);
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 0 ? true : false;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            Data[col].set(row, value);
            fireTableCellUpdated(row, col);
        }

        public void addNewRow() {
            for (int i = 0; i < columnNames.length; i++) {
                Data[i].add(i == 0 ? false : "");
            }
            this.fireTableRowsInserted(0, Data[0].size() - 1);
        }

        public void removeNewRow() {
            for (int i = 0; i < columnNames.length; i++) {
                Data[i].remove(Data[i].size() - 1);
            }
            this.fireTableRowsDeleted(0, Data[0].size() - 1);
        }

        public void removeNewRow(int index) {
            for (int i = 0; i < columnNames.length; i++) {
                Data[i].remove(index);
            }
            this.fireTableRowsDeleted(0, Data[0].size() - 1);
        }
    }

    private void validateEnabled() {
        boolean available = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (Boolean.valueOf(table.getValueAt(i, 0).toString()) == true) {
                BValidate.setEnabled(true);
                available = true;
                break;
            }
        }
        if (!available) {
            BValidate.setEnabled(false);
        }
    }

    private int getNumberRowChecked() {
        int nRow = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (Boolean.valueOf(table.getValueAt(i, 0).toString()) == true) {
                nRow++;
            }
        }
        return nRow;
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoadDefaultLayers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoadDefaultLayers dialog = new LoadDefaultLayers(new javax.swing.JFrame(), true);
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
    private com.jidesoft.swing.JideButton BCancel;
    private org.jdesktop.swingx.JXBusyLabel BLLoading;
    private com.jidesoft.swing.JideButton BValidate;
    private javax.swing.JScrollPane Scrll;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    MyTableModel tableModel = new MyTableModel();
    NavigationSortableTable table = new NavigationSortableTable();
}
