package com.osfac.dmt.tools;

import com.jidesoft.swing.CheckBoxList;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class AdvancedSelection extends javax.swing.JDialog {

    public AdvancedSelection(java.awt.Frame parent, boolean modal, JTable table) {
        super(parent, modal);
        this.table = table;
        initComponents(I18N.DMTResourceBundle);
        fillComboboxes();
        this.setLocationRelativeTo(parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BG = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        RBCategory = new javax.swing.JRadioButton();
        RBFrom = new javax.swing.JRadioButton();
        RBText = new javax.swing.JRadioButton();
        CBCategory = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBFrom = new javax.swing.JComboBox();
        CBTo = new javax.swing.JComboBox();
        labTo = new javax.swing.JLabel();
        labAnd = new javax.swing.JLabel();
        CBSize2 = new javax.swing.JComboBox();
        RBBetween = new javax.swing.JRadioButton();
        CBSize = new javax.swing.JComboBox();
        TxtText = new javax.swing.JTextField();
        BCancel = new com.jidesoft.swing.JideButton();
        BValidate = new com.jidesoft.swing.JideButton();
        ChKRowCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        setTitle(bundle.getString("AdvancedSelection.title")); // NOI18N

        BG.add(RBCategory);
        RBCategory.setSelected(true);
        RBCategory.setText(bundle.getString("AdvancedSelection.RBCategory.text")); // NOI18N
        RBCategory.setFocusable(false);
        RBCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBCategoryItemStateChanged(evt);
            }
        });

        BG.add(RBFrom);
        RBFrom.setText(bundle.getString("AdvancedSelection.RBFrom.text")); // NOI18N
        RBFrom.setFocusable(false);
        RBFrom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBFromItemStateChanged(evt);
            }
        });

        BG.add(RBText);
        RBText.setText(bundle.getString("AdvancedSelection.RBText.text")); // NOI18N
        RBText.setFocusable(false);
        RBText.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBTextItemStateChanged(evt);
            }
        });

        CBCategory.setEditable(false);
        CBCategory.setFocusable(false);

        CBFrom.setEnabled(false);
        CBFrom.setFocusable(false);

        CBTo.setEnabled(false);
        CBTo.setFocusable(false);

        labTo.setText(bundle.getString("AdvancedSelection.labTo.text")); // NOI18N
        labTo.setEnabled(false);
        labTo.setFocusable(false);

        labAnd.setText(bundle.getString("AdvancedSelection.labAnd.text")); // NOI18N
        labAnd.setEnabled(false);
        labAnd.setFocusable(false);

        CBSize2.setEnabled(false);
        CBSize2.setFocusable(false);

        BG.add(RBBetween);
        RBBetween.setText(bundle.getString("AdvancedSelection.RBBetween.text")); // NOI18N
        RBBetween.setFocusable(false);
        RBBetween.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBBetweenItemStateChanged(evt);
            }
        });

        CBSize.setEnabled(false);
        CBSize.setFocusable(false);

        TxtText.setEnabled(false);

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("AdvancedSelection.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"))); // NOI18N
        BValidate.setText(bundle.getString("AdvancedSelection.BValidate.text")); // NOI18N
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        ChKRowCheck.setSelected(true);
        ChKRowCheck.setText(bundle.getString("AdvancedSelection.ChKRowCheck.text")); // NOI18N
        ChKRowCheck.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(RBText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TxtText, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(RBCategory)
                                    .addComponent(RBFrom)
                                    .addComponent(RBBetween))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(CBFrom, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(labTo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CBTo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(CBSize, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(labAnd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CBSize2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ChKRowCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BCancel, BValidate});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBCategory)
                    .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBFrom)
                    .addComponent(CBFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBBetween)
                    .addComponent(CBSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBSize2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labAnd))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBText)
                    .addComponent(TxtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChKRowCheck))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        BG = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        RBCategory = new javax.swing.JRadioButton();
        RBFrom = new javax.swing.JRadioButton();
        RBText = new javax.swing.JRadioButton();
        CBCategory = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBFrom = new javax.swing.JComboBox();
        CBTo = new javax.swing.JComboBox();
        labTo = new javax.swing.JLabel();
        labAnd = new javax.swing.JLabel();
        CBSize2 = new javax.swing.JComboBox();
        RBBetween = new javax.swing.JRadioButton();
        CBSize = new javax.swing.JComboBox();
        TxtText = new javax.swing.JTextField();
        BCancel = new com.jidesoft.swing.JideButton();
        BValidate = new com.jidesoft.swing.JideButton();
        ChKRowCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("AdvancedSelection.title")); // NOI18N

        BG.add(RBCategory);
        RBCategory.setSelected(true);
        RBCategory.setText(bundle.getString("AdvancedSelection.RBCategory.text")); // NOI18N
        RBCategory.setFocusable(false);
        RBCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBCategoryItemStateChanged(evt);
            }
        });

        BG.add(RBFrom);
        RBFrom.setText(bundle.getString("AdvancedSelection.RBFrom.text")); // NOI18N
        RBFrom.setFocusable(false);
        RBFrom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBFromItemStateChanged(evt);
            }
        });

        BG.add(RBText);
        RBText.setText(bundle.getString("AdvancedSelection.RBText.text")); // NOI18N
        RBText.setFocusable(false);
        RBText.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBTextItemStateChanged(evt);
            }
        });

        CBCategory.setEditable(false);
        CBCategory.setFocusable(false);

        CBFrom.setEnabled(false);
        CBFrom.setFocusable(false);

        CBTo.setEnabled(false);
        CBTo.setFocusable(false);

        labTo.setText(bundle.getString("AdvancedSelection.labTo.text")); // NOI18N
        labTo.setEnabled(false);
        labTo.setFocusable(false);

        labAnd.setText(bundle.getString("AdvancedSelection.labAnd.text")); // NOI18N
        labAnd.setEnabled(false);
        labAnd.setFocusable(false);

        CBSize2.setEnabled(false);
        CBSize2.setFocusable(false);

        BG.add(RBBetween);
        RBBetween.setText(bundle.getString("AdvancedSelection.RBBetween.text")); // NOI18N
        RBBetween.setFocusable(false);
        RBBetween.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBBetweenItemStateChanged(evt);
            }
        });

        CBSize.setEnabled(false);
        CBSize.setFocusable(false);

        TxtText.setEnabled(false);

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("AdvancedSelection.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"))); // NOI18N
        BValidate.setText(bundle.getString("AdvancedSelection.BValidate.text")); // NOI18N
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        ChKRowCheck.setSelected(true);
        ChKRowCheck.setText(bundle.getString("AdvancedSelection.ChKRowCheck.text")); // NOI18N
        ChKRowCheck.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(RBText)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(TxtText, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(RBCategory)
                                                                .addComponent(RBFrom)
                                                                .addComponent(RBBetween))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(CBFrom, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(labTo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(CBTo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(CBSize, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(labAnd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(CBSize2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(ChKRowCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap()));

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{BCancel, BValidate});

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(RBCategory)
                                .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(RBFrom)
                                .addComponent(CBFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(CBTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labTo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(RBBetween)
                                .addComponent(CBSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(CBSize2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labAnd))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(RBText)
                                .addComponent(TxtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ChKRowCheck))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }// </editor-fold>

    private void RBCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBCategoryItemStateChanged
        CBCategory.setEnabled(RBCategory.isSelected());
    }//GEN-LAST:event_RBCategoryItemStateChanged

    private void RBFromItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBFromItemStateChanged
        CBFrom.setEnabled(RBFrom.isSelected());
        CBTo.setEnabled(RBFrom.isSelected());
        labTo.setEnabled(RBFrom.isSelected());
    }//GEN-LAST:event_RBFromItemStateChanged

    private void RBBetweenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBBetweenItemStateChanged
        CBSize.setEnabled(RBBetween.isSelected());
        CBSize2.setEnabled(RBBetween.isSelected());
        labAnd.setEnabled(RBBetween.isSelected());
    }//GEN-LAST:event_RBBetweenItemStateChanged

    private void RBTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBTextItemStateChanged
        TxtText.setEnabled(RBText.isSelected());
        TxtText.requestFocus();
    }//GEN-LAST:event_RBTextItemStateChanged

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_BCancelActionPerformed

    private void BValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValidateActionPerformed
        try {
            boolean message = true;
            if (ChKRowCheck.isSelected()) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt(false, i, 0);
                }
            }
            if (RBCategory.isSelected()) {
                if (CBCategory.getSelectedObjects().length > 0) {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        PreparedStatement ps = Config.con.prepareStatement("SELECT category_name FROM\n"
                                + "dmt_category INNER JOIN dmt_image ON dmt_category.id_category = "
                                + "dmt_image.id_category WHERE id_image = ?");
                        ps.setString(1, table.getValueAt(i, 1).toString());
                        ResultSet res = ps.executeQuery();
                        while (res.next()) {
                            if (manyCriteria(CBCategory.getSelectedObjects()).contains(res.getString(1))) {
                                table.setValueAt(true, i, 0);
                            }
                        }
                    }
                } else {
                    message = false;
                    JOptionPane.showMessageDialog(this, I18N.get("AdvancedSelection.No-category-has-been-selected"),
                            I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
                }
            } else if (RBFrom.isSelected()) {
                for (int i = Integer.parseInt(CBFrom.getSelectedItem().toString());
                        i < Integer.parseInt(CBTo.getSelectedItem().toString()); i++) {
                    table.setValueAt(true, i - 1, 0);
                }
            } else if (RBBetween.isSelected()) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    if ((Double.parseDouble(table.getValueAt(i, table.getColumnCount() - 1).toString())
                            >= Double.parseDouble(CBSize.getSelectedItem().toString()))
                            && (Double.parseDouble(table.getValueAt(i, table.getColumnCount() - 1).toString())
                            <= Double.parseDouble(CBSize2.getSelectedItem().toString()))) {
                        table.setValueAt(true, i, 0);
                    }
                }
            } else if (RBText.isSelected()) {
                if (!TxtText.getText().isEmpty()) {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (table.getValueAt(i, 2).toString().contains(TxtText.getText())) {
                            table.setValueAt(true, i, 0);
                        }
                    }
                } else {
                    message = false;
                    JOptionPane.showMessageDialog(this, I18N.get("AdvancedSelection.empty-filed-text"),
                            I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
                }
            }
            if (message) {
                this.dispose();
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }//GEN-LAST:event_BValidateActionPerformed

    private void fillComboboxes() {
        try {//filling categories
            ArrayList tab = new ArrayList();
            for (int i = 0; i < table.getRowCount(); i++) {
                tab.add(table.getValueAt(i, 1));
            }
            ResultSet res = Config.con.createStatement().executeQuery("SELECT DISTINCT category_name FROM dmt_category "
                    + "INNER JOIN dmt_image ON dmt_category.id_category = dmt_image.id_category "
                    + "WHERE dmt_image.id_image IN (" + manyCriteria(tab.toArray()) + ") ORDER BY category_name");
            DefaultComboBoxModel modelCategory = new DefaultComboBoxModel();
            modelCategory.insertElementAt(CheckBoxList.ALL, 0);
            int index = 1;
            while (res.next()) {
                modelCategory.insertElementAt(res.getString(1), index);
                index++;
            }
            CBCategory.setModel(modelCategory);
            CBCategory.setSelectedIndex(-1);
            //filling number
            ArrayList<Integer> listNumber = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                listNumber.add(i + 1);
            }
            CBFrom.setModel(new DefaultComboBoxModel(listNumber.toArray()));
            CBFrom.setSelectedIndex(0);
            Collections.reverse(listNumber);
            CBTo.setModel(new DefaultComboBoxModel(listNumber.toArray()));
            CBTo.setSelectedIndex(0);
            //filling between size
            ArrayList<Double> listsize = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                if (!listsize.contains(Double.parseDouble(table.getValueAt(i, table.getColumnCount() - 1).toString()))) {
                    listsize.add(Double.parseDouble(table.getValueAt(i, table.getColumnCount() - 1).toString()));
                }
            }
            Collections.sort(listsize);
            CBSize.setModel(new DefaultComboBoxModel(listsize.toArray()));
            CBSize.setSelectedIndex(0);
            Collections.reverse(listsize);
            CBSize2.setModel(new DefaultComboBoxModel(listsize.toArray()));
            CBSize2.setSelectedIndex(0);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private String manyCriteria(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "\'" + list[i].toString() + "\',";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BCancel;
    private javax.swing.ButtonGroup BG;
    private com.jidesoft.swing.JideButton BValidate;
    private com.jidesoft.combobox.CheckBoxListComboBox CBCategory;
    private javax.swing.JComboBox CBFrom;
    private javax.swing.JComboBox CBSize;
    private javax.swing.JComboBox CBSize2;
    private javax.swing.JComboBox CBTo;
    private javax.swing.JCheckBox ChKRowCheck;
    private javax.swing.JRadioButton RBBetween;
    private javax.swing.JRadioButton RBCategory;
    private javax.swing.JRadioButton RBFrom;
    private javax.swing.JRadioButton RBText;
    private javax.swing.JTextField TxtText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labAnd;
    private javax.swing.JLabel labTo;
    // End of variables declaration//GEN-END:variables
    JTable table;
}
