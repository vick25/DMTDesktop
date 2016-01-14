package com.osfac.dmt.tools.search;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

public class GeoCriteria extends javax.swing.JPanel {

    public GeoCriteria() {
        initComponents(I18N.DMTResourceBundle);
        tableModel = new MyTableModel();
        table = new SortableTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSortable(false);
        table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{Config.getColorFromKey(Config.pref.get(
            SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")), Config.getColorFromKey(Config.pref.get(
            SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
        TableUtils.autoResizeAllColumns(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Config.centerTableHeadAndBold(table);
        table.setColumnResizable(true);
        ScrllTable.setViewportView(table);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgShape = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        RBPoint = new javax.swing.JRadioButton();
        RBLine = new javax.swing.JRadioButton();
        RBPolygon = new javax.swing.JRadioButton();
        ScrllTable = new javax.swing.JScrollPane();
        SNPoint = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        BImport = new com.jidesoft.swing.JideButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        bgShape.add(RBPoint);
        RBPoint.setSelected(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        RBPoint.setText(bundle.getString("GeoCriteria.RBPoint.text")); // NOI18N
        RBPoint.setFocusable(false);
        RBPoint.setOpaque(false);
        RBPoint.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBPointItemStateChanged(evt);
            }
        });

        bgShape.add(RBLine);
        RBLine.setText(bundle.getString("GeoCriteria.RBLine.text")); // NOI18N
        RBLine.setFocusable(false);
        RBLine.setOpaque(false);
        RBLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBLineItemStateChanged(evt);
            }
        });

        bgShape.add(RBPolygon);
        RBPolygon.setText(bundle.getString("GeoCriteria.RBPolygon.text")); // NOI18N
        RBPolygon.setFocusable(false);
        RBPolygon.setOpaque(false);
        RBPolygon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBPolygonItemStateChanged(evt);
            }
        });

        SNPoint.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        SNPoint.setEnabled(false);
        SNPoint.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SNPointStateChanged(evt);
            }
        });

        jLabel1.setText(bundle.getString("GeoCriteria.jLabel1.text")); // NOI18N

        BImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/excel.png"))); // NOI18N
        BImport.setText(bundle.getString("GeoCriteria.BImport.text")); // NOI18N
        BImport.setAlwaysShowHyperlink(true);
        BImport.setButtonStyle(3);
        BImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(RBPolygon, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(RBLine, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(RBPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(SNPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrllTable, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RBPoint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RBLine)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RBPolygon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SNPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ScrllTable, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        bgShape = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        RBPoint = new javax.swing.JRadioButton();
        RBLine = new javax.swing.JRadioButton();
        RBPolygon = new javax.swing.JRadioButton();
        ScrllTable = new javax.swing.JScrollPane();
        SNPoint = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        BImport = new com.jidesoft.swing.JideButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        bgShape.add(RBPoint);
        RBPoint.setSelected(true);
        RBPoint.setText(bundle.getString("GeoCriteria.RBPoint.text")); // NOI18N
        RBPoint.setFocusable(false);
        RBPoint.setOpaque(false);
        RBPoint.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBPointItemStateChanged(evt);
            }
        });

        bgShape.add(RBLine);
        RBLine.setText(bundle.getString("GeoCriteria.RBLine.text")); // NOI18N
        RBLine.setFocusable(false);
        RBLine.setOpaque(false);
        RBLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBLineItemStateChanged(evt);
            }
        });

        bgShape.add(RBPolygon);
        RBPolygon.setText(bundle.getString("GeoCriteria.RBPolygon.text")); // NOI18N
        RBPolygon.setFocusable(false);
        RBPolygon.setOpaque(false);
        RBPolygon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBPolygonItemStateChanged(evt);
            }
        });

        SNPoint.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        SNPoint.setEnabled(false);
        SNPoint.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SNPointStateChanged(evt);
            }
        });

        jLabel1.setText(bundle.getString("GeoCriteria.jLabel1.text")); // NOI18N

        BImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/excel.png"))); // NOI18N
        BImport.setText(bundle.getString("GeoCriteria.BImport.text")); // NOI18N
        BImport.setAlwaysShowHyperlink(true);
        BImport.setButtonStyle(3);
        BImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(RBPolygon, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(RBLine, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(RBPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(20, 20, 20))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addComponent(SNPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(BImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ScrllTable, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                        .addGap(4, 4, 4)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(RBPoint)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(RBLine)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(RBPolygon)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(SNPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(ScrllTable, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }// </editor-fold>

    private void SNPointStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SNPointStateChanged
        if (Integer.parseInt(SNPoint.getValue().toString()) > tableModel.getRowCount()) {
            while (Integer.parseInt(SNPoint.getValue().toString()) > tableModel.getRowCount()) {
                tableModel.addNewRow();
            }
        } else if (tableModel.getRowCount() > Integer.parseInt(SNPoint.getValue().toString())) {
            int nbTable = tableModel.getRowCount();
            while (nbTable > Integer.parseInt(SNPoint.getValue().toString())) {
                tableModel.removeNewRow();
                nbTable--;
            }
        }
    }//GEN-LAST:event_SNPointStateChanged

    private void RBPointItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBPointItemStateChanged
        setSNPoint(1, 1);
        SNPoint.setEnabled(false);
    }//GEN-LAST:event_RBPointItemStateChanged

    private void RBLineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBLineItemStateChanged
        setSNPoint(2, 2);
        SNPoint.setEnabled(false);
    }//GEN-LAST:event_RBLineItemStateChanged

    private void RBPolygonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBPolygonItemStateChanged
        setSNPoint(5, 4);
        SNPoint.setEnabled(true);
    }//GEN-LAST:event_RBPolygonItemStateChanged

    private void BImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BImportActionPerformed
        RBPolygon.setSelected(true);
        new ImportValue(DMTWorkbench.frame, true, tableModel, SNPoint).setVisible(true);
    }//GEN-LAST:event_BImportActionPerformed

    private void setSNPoint(int value, int minimium) {
        SNPoint.setValue(value);
        SNPoint.setModel(new SpinnerNumberModel(Integer.valueOf(value), Integer.valueOf(minimium),
                null, Integer.valueOf(1)));
    }

    public class MyTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES = {I18N.get("GeoCriteria.Text-Latitude"), I18N.get("GeoCriteria.Text-Longitude")};
        private final ArrayList[] DATA;

        public MyTableModel() {
            DATA = new ArrayList[COLUMN_NAMES.length];
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i] = new ArrayList();
            }
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                DATA[i].add("");
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
            return (true);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BImport;
    public static javax.swing.JRadioButton RBLine;
    public static javax.swing.JRadioButton RBPoint;
    public static javax.swing.JRadioButton RBPolygon;
    private javax.swing.JSpinner SNPoint;
    private javax.swing.JScrollPane ScrllTable;
    private javax.swing.ButtonGroup bgShape;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    public static SortableTable table;
    MyTableModel tableModel;
}
