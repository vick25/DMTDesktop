package com.osfac.dmt.setting.panel;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.tools.statistic.Statistic;
import java.awt.Color;
import javax.swing.JOptionPane;

public class ChartFeaturePanel extends javax.swing.JPanel {

    public ChartFeaturePanel(AbstractDialogPage page) {
        this.page = page;
        initComponents(I18N.DMTResourceBundle);
        initValues();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        RBLineLabel = new javax.swing.JRadioButton();
        SLAngle = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ChBExplodedSegment = new javax.swing.JCheckBox();
        RBNoLabel = new javax.swing.JRadioButton();
        RBSimpleLabel = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        BarChBVLine = new javax.swing.JCheckBox();
        BarChBHLine = new javax.swing.JCheckBox();
        OneColor = new javax.swing.JRadioButton();
        RandomColor = new javax.swing.JRadioButton();
        CBColor2 = new com.jidesoft.combobox.ColorComboBox();
        jPanel4 = new javax.swing.JPanel();
        LineChBVLine = new javax.swing.JCheckBox();
        LineChBHLine = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        CBColor = new com.jidesoft.combobox.ColorComboBox();
        jLabel5 = new javax.swing.JLabel();
        SPLineWidth = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        ChBShadow = new javax.swing.JCheckBox();
        RBFlat = new javax.swing.JRadioButton();
        RBRaised = new javax.swing.JRadioButton();
        RB3D = new javax.swing.JRadioButton();
        ChBRollover = new javax.swing.JCheckBox();
        ChBOutline = new javax.swing.JCheckBox();
        ChBSelectionOutline = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        jButton1.setText(bundle.getString("ChartFeaturePanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel1.border.title"))); // NOI18N

        buttonGroup1.add(RBLineLabel);
        RBLineLabel.setSelected(true);
        RBLineLabel.setText(bundle.getString("ChartFeaturePanel.RBLineLabel.text")); // NOI18N
        RBLineLabel.setFocusable(false);
        RBLineLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBLineLabelItemStateChanged(evt);
            }
        });

        SLAngle.setMajorTickSpacing(90);
        SLAngle.setMaximum(360);
        SLAngle.setMinorTickSpacing(10);
        SLAngle.setPaintLabels(true);
        SLAngle.setPaintTicks(true);
        SLAngle.setValue(0);
        SLAngle.setFocusable(false);
        SLAngle.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SLAngleStateChanged(evt);
            }
        });

        jLabel1.setText(bundle.getString("ChartFeaturePanel.jLabel1.text")); // NOI18N

        jLabel3.setText(bundle.getString("ChartFeaturePanel.jLabel3.text")); // NOI18N

        ChBExplodedSegment.setText(bundle.getString("ChartFeaturePanel.ChBExplodedSegment.text")); // NOI18N
        ChBExplodedSegment.setFocusable(false);
        ChBExplodedSegment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBExplodedSegmentItemStateChanged(evt);
            }
        });

        buttonGroup1.add(RBNoLabel);
        RBNoLabel.setText(bundle.getString("ChartFeaturePanel.RBNoLabel.text")); // NOI18N
        RBNoLabel.setFocusable(false);
        RBNoLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBNoLabelItemStateChanged(evt);
            }
        });

        buttonGroup1.add(RBSimpleLabel);
        RBSimpleLabel.setText(bundle.getString("ChartFeaturePanel.RBSimpleLabel.text")); // NOI18N
        RBSimpleLabel.setFocusable(false);
        RBSimpleLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBSimpleLabelItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ChBExplodedSegment)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SLAngle, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RBLineLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RBSimpleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RBNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBLineLabel)
                    .addComponent(RBSimpleLabel)
                    .addComponent(RBNoLabel)
                    .addComponent(jLabel1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(SLAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(ChBExplodedSegment)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel3.border.title"))); // NOI18N

        BarChBVLine.setText(bundle.getString("ChartFeaturePanel.BarChBVLine.text")); // NOI18N
        BarChBVLine.setFocusable(false);
        BarChBVLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BarChBVLineItemStateChanged(evt);
            }
        });

        BarChBHLine.setSelected(true);
        BarChBHLine.setText(bundle.getString("ChartFeaturePanel.BarChBHLine.text")); // NOI18N
        BarChBHLine.setFocusable(false);
        BarChBHLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BarChBHLineItemStateChanged(evt);
            }
        });

        buttonGroup2.add(OneColor);
        OneColor.setText(bundle.getString("ChartFeaturePanel.OneColor.text")); // NOI18N
        OneColor.setFocusable(false);
        OneColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OneColorItemStateChanged(evt);
            }
        });

        buttonGroup2.add(RandomColor);
        RandomColor.setSelected(true);
        RandomColor.setText(bundle.getString("ChartFeaturePanel.RandomColor.text")); // NOI18N
        RandomColor.setFocusable(false);
        RandomColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RandomColorItemStateChanged(evt);
            }
        });

        CBColor2.setEditable(false);
        CBColor2.setEnabled(false);
        CBColor2.setFocusable(false);
        CBColor2.setSelectedColor(new java.awt.Color(0, 204, 0));
        CBColor2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBColor2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(BarChBVLine, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BarChBHLine, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(OneColor, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CBColor2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(RandomColor, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(194, 194, 194))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BarChBVLine)
                    .addComponent(BarChBHLine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBColor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OneColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RandomColor))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel4.border.title"))); // NOI18N

        LineChBVLine.setSelected(true);
        LineChBVLine.setText(bundle.getString("ChartFeaturePanel.LineChBVLine.text")); // NOI18N
        LineChBVLine.setFocusable(false);
        LineChBVLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LineChBVLineItemStateChanged(evt);
            }
        });

        LineChBHLine.setSelected(true);
        LineChBHLine.setText(bundle.getString("ChartFeaturePanel.LineChBHLine.text")); // NOI18N
        LineChBHLine.setFocusable(false);
        LineChBHLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LineChBHLineItemStateChanged(evt);
            }
        });

        jLabel4.setText(bundle.getString("ChartFeaturePanel.jLabel4.text")); // NOI18N

        CBColor.setEditable(false);
        CBColor.setFocusable(false);
        CBColor.setSelectedColor(new java.awt.Color(255, 0, 0));
        CBColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBColorItemStateChanged(evt);
            }
        });

        jLabel5.setText(bundle.getString("ChartFeaturePanel.jLabel5.text")); // NOI18N

        SPLineWidth.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        SPLineWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SPLineWidthStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBColor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SPLineWidth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(LineChBVLine)
                        .addGap(18, 18, 18)
                        .addComponent(LineChBHLine, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LineChBVLine)
                    .addComponent(LineChBHLine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(SPLineWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel5.border.title"))); // NOI18N

        ChBShadow.setSelected(true);
        ChBShadow.setText(bundle.getString("ChartFeaturePanel.ChBShadow.text")); // NOI18N
        ChBShadow.setFocusable(false);
        ChBShadow.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBShadowItemStateChanged(evt);
            }
        });

        buttonGroup3.add(RBFlat);
        RBFlat.setSelected(true);
        RBFlat.setText(bundle.getString("ChartFeaturePanel.RBFlat.text")); // NOI18N
        RBFlat.setFocusable(false);
        RBFlat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBFlatItemStateChanged(evt);
            }
        });

        buttonGroup3.add(RBRaised);
        RBRaised.setText(bundle.getString("ChartFeaturePanel.RBRaised.text")); // NOI18N
        RBRaised.setFocusable(false);
        RBRaised.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBRaisedItemStateChanged(evt);
            }
        });

        buttonGroup3.add(RB3D);
        RB3D.setText(bundle.getString("ChartFeaturePanel.RB3D.text")); // NOI18N
        RB3D.setFocusable(false);
        RB3D.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RB3DItemStateChanged(evt);
            }
        });

        ChBRollover.setText(bundle.getString("ChartFeaturePanel.ChBRollover.text")); // NOI18N
        ChBRollover.setFocusable(false);
        ChBRollover.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBRolloverItemStateChanged(evt);
            }
        });

        ChBOutline.setText(bundle.getString("ChartFeaturePanel.ChBOutline.text")); // NOI18N
        ChBOutline.setFocusable(false);
        ChBOutline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBOutlineItemStateChanged(evt);
            }
        });

        ChBSelectionOutline.setText(bundle.getString("ChartFeaturePanel.ChBSelectionOutline.text")); // NOI18N
        ChBSelectionOutline.setFocusable(false);
        ChBSelectionOutline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBSelectionOutlineItemStateChanged(evt);
            }
        });

        jLabel2.setText(bundle.getString("ChartFeaturePanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ChBShadow, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ChBSelectionOutline, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ChBRollover, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ChBOutline, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RBFlat)
                        .addGap(18, 18, 18)
                        .addComponent(RBRaised)
                        .addGap(18, 18, 18)
                        .addComponent(RB3D, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBFlat)
                    .addComponent(RBRaised)
                    .addComponent(RB3D)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ChBShadow)
                    .addComponent(ChBRollover))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ChBSelectionOutline)
                    .addComponent(ChBOutline)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        RBLineLabel = new javax.swing.JRadioButton();
        SLAngle = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ChBExplodedSegment = new javax.swing.JCheckBox();
        RBNoLabel = new javax.swing.JRadioButton();
        RBSimpleLabel = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        BarChBVLine = new javax.swing.JCheckBox();
        BarChBHLine = new javax.swing.JCheckBox();
        OneColor = new javax.swing.JRadioButton();
        RandomColor = new javax.swing.JRadioButton();
        CBColor2 = new com.jidesoft.combobox.ColorComboBox();
        jPanel4 = new javax.swing.JPanel();
        LineChBVLine = new javax.swing.JCheckBox();
        LineChBHLine = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        CBColor = new com.jidesoft.combobox.ColorComboBox();
        jLabel5 = new javax.swing.JLabel();
        SPLineWidth = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        ChBShadow = new javax.swing.JCheckBox();
        RBFlat = new javax.swing.JRadioButton();
        RBRaised = new javax.swing.JRadioButton();
        RB3D = new javax.swing.JRadioButton();
        ChBRollover = new javax.swing.JCheckBox();
        ChBOutline = new javax.swing.JCheckBox();
        ChBSelectionOutline = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();

        jButton1.setText(bundle.getString("ChartFeaturePanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel1.border.title"))); // NOI18N

        buttonGroup1.add(RBLineLabel);
        RBLineLabel.setSelected(true);
        RBLineLabel.setText(bundle.getString("ChartFeaturePanel.RBLineLabel.text")); // NOI18N
        RBLineLabel.setFocusable(false);
        RBLineLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBLineLabelItemStateChanged(evt);
            }
        });

        SLAngle.setMajorTickSpacing(90);
        SLAngle.setMaximum(360);
        SLAngle.setMinorTickSpacing(10);
        SLAngle.setPaintLabels(true);
        SLAngle.setPaintTicks(true);
        SLAngle.setValue(0);
        SLAngle.setFocusable(false);
        SLAngle.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SLAngleStateChanged(evt);
            }
        });

        jLabel1.setText(bundle.getString("ChartFeaturePanel.jLabel1.text")); // NOI18N

        jLabel3.setText(bundle.getString("ChartFeaturePanel.jLabel3.text")); // NOI18N

        ChBExplodedSegment.setText(bundle.getString("ChartFeaturePanel.ChBExplodedSegment.text")); // NOI18N
        ChBExplodedSegment.setFocusable(false);
        ChBExplodedSegment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBExplodedSegmentItemStateChanged(evt);
            }
        });

        buttonGroup1.add(RBNoLabel);
        RBNoLabel.setText(bundle.getString("ChartFeaturePanel.RBNoLabel.text")); // NOI18N
        RBNoLabel.setFocusable(false);
        RBNoLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBNoLabelItemStateChanged(evt);
            }
        });

        buttonGroup1.add(RBSimpleLabel);
        RBSimpleLabel.setText(bundle.getString("ChartFeaturePanel.RBSimpleLabel.text")); // NOI18N
        RBSimpleLabel.setFocusable(false);
        RBSimpleLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBSimpleLabelItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ChBExplodedSegment)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SLAngle, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RBLineLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RBSimpleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RBNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(RBLineLabel)
                .addComponent(RBSimpleLabel)
                .addComponent(RBNoLabel)
                .addComponent(jLabel1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel3)
                .addComponent(SLAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(ChBExplodedSegment)
                .addContainerGap()));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel3.border.title"))); // NOI18N

        BarChBVLine.setText(bundle.getString("ChartFeaturePanel.BarChBVLine.text")); // NOI18N
        BarChBVLine.setFocusable(false);
        BarChBVLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BarChBVLineItemStateChanged(evt);
            }
        });

        BarChBHLine.setSelected(true);
        BarChBHLine.setText(bundle.getString("ChartFeaturePanel.BarChBHLine.text")); // NOI18N
        BarChBHLine.setFocusable(false);
        BarChBHLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BarChBHLineItemStateChanged(evt);
            }
        });

        buttonGroup2.add(OneColor);
        OneColor.setText(bundle.getString("ChartFeaturePanel.OneColor.text")); // NOI18N
        OneColor.setFocusable(false);
        OneColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OneColorItemStateChanged(evt);
            }
        });

        buttonGroup2.add(RandomColor);
        RandomColor.setSelected(true);
        RandomColor.setText(bundle.getString("ChartFeaturePanel.RandomColor.text")); // NOI18N
        RandomColor.setFocusable(false);
        RandomColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RandomColorItemStateChanged(evt);
            }
        });

        CBColor2.setEditable(false);
        CBColor2.setEnabled(false);
        CBColor2.setFocusable(false);
        CBColor2.setSelectedColor(new java.awt.Color(0, 204, 0));
        CBColor2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBColor2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(BarChBVLine, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BarChBHLine, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(OneColor, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CBColor2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(RandomColor, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(194, 194, 194)))));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(BarChBVLine)
                .addComponent(BarChBHLine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(CBColor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(OneColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RandomColor)));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel4.border.title"))); // NOI18N

        LineChBVLine.setSelected(true);
        LineChBVLine.setText(bundle.getString("ChartFeaturePanel.LineChBVLine.text")); // NOI18N
        LineChBVLine.setFocusable(false);
        LineChBVLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LineChBVLineItemStateChanged(evt);
            }
        });

        LineChBHLine.setSelected(true);
        LineChBHLine.setText(bundle.getString("ChartFeaturePanel.LineChBHLine.text")); // NOI18N
        LineChBHLine.setFocusable(false);
        LineChBHLine.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LineChBHLineItemStateChanged(evt);
            }
        });

        jLabel4.setText(bundle.getString("ChartFeaturePanel.jLabel4.text")); // NOI18N

        CBColor.setEditable(false);
        CBColor.setFocusable(false);
        CBColor.setSelectedColor(new java.awt.Color(255, 0, 0));
        CBColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBColorItemStateChanged(evt);
            }
        });

        jLabel5.setText(bundle.getString("ChartFeaturePanel.jLabel5.text")); // NOI18N

        SPLineWidth.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        SPLineWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SPLineWidthStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CBColor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPLineWidth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(LineChBVLine)
                .addGap(18, 18, 18)
                .addComponent(LineChBHLine, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(LineChBVLine)
                .addComponent(LineChBHLine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(CBColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4)
                .addComponent(jLabel5)
                .addComponent(SPLineWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE)));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ChartFeaturePanel.jPanel5.border.title"))); // NOI18N

        ChBShadow.setSelected(true);
        ChBShadow.setText(bundle.getString("ChartFeaturePanel.ChBShadow.text")); // NOI18N
        ChBShadow.setFocusable(false);
        ChBShadow.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBShadowItemStateChanged(evt);
            }
        });

        buttonGroup3.add(RBFlat);
        RBFlat.setSelected(true);
        RBFlat.setText(bundle.getString("ChartFeaturePanel.RBFlat.text")); // NOI18N
        RBFlat.setFocusable(false);
        RBFlat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBFlatItemStateChanged(evt);
            }
        });

        buttonGroup3.add(RBRaised);
        RBRaised.setText(bundle.getString("ChartFeaturePanel.RBRaised.text")); // NOI18N
        RBRaised.setFocusable(false);
        RBRaised.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBRaisedItemStateChanged(evt);
            }
        });

        buttonGroup3.add(RB3D);
        RB3D.setText(bundle.getString("ChartFeaturePanel.RB3D.text")); // NOI18N
        RB3D.setFocusable(false);
        RB3D.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RB3DItemStateChanged(evt);
            }
        });

        ChBRollover.setText(bundle.getString("ChartFeaturePanel.ChBRollover.text")); // NOI18N
        ChBRollover.setFocusable(false);
        ChBRollover.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBRolloverItemStateChanged(evt);
            }
        });

        ChBOutline.setText(bundle.getString("ChartFeaturePanel.ChBOutline.text")); // NOI18N
        ChBOutline.setFocusable(false);
        ChBOutline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBOutlineItemStateChanged(evt);
            }
        });

        ChBSelectionOutline.setText(bundle.getString("ChartFeaturePanel.ChBSelectionOutline.text")); // NOI18N
        ChBSelectionOutline.setFocusable(false);
        ChBSelectionOutline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChBSelectionOutlineItemStateChanged(evt);
            }
        });

        jLabel2.setText(bundle.getString("ChartFeaturePanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ChBShadow, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ChBSelectionOutline, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ChBRollover, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ChBOutline, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RBFlat)
                .addGap(18, 18, 18)
                .addComponent(RBRaised)
                .addGap(18, 18, 18)
                .addComponent(RB3D, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(RBFlat)
                .addComponent(RBRaised)
                .addComponent(RB3D)
                .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ChBShadow)
                .addComponent(ChBRollover))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ChBSelectionOutline)
                .addComponent(ChBOutline))));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap()));

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING));
    }// </editor-fold>

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (JOptionPane.showConfirmDialog(this, I18N.get("ChartFeaturePanel.message-restore-default-settings"), I18N.get("Text.Confirm"), 0) == 0) {
            RBLineLabel.setSelected(true);
            RBSimpleLabel.setSelected(false);
            RBNoLabel.setSelected(false);
            SLAngle.setValue(0);
            ChBExplodedSegment.setSelected(false);

            OneColor.setSelected(false);
            RandomColor.setSelected(true);
            CBColor2.setSelectedColor(getColorFromKey("0, 204, 0"));

            BarChBVLine.setSelected(false);
            BarChBHLine.setSelected(true);
            LineChBVLine.setSelected(true);
            LineChBHLine.setSelected(true);
            CBColor.setSelectedColor(getColorFromKey("255, 0, 0"));
            SPLineWidth.setValue(2);

            RBFlat.setSelected(true);
            RBRaised.setSelected(false);
            RB3D.setSelected(false);
            ChBShadow.setSelected(true);
            ChBRollover.setSelected(false);
            ChBOutline.setSelected(false);
            ChBSelectionOutline.setSelected(false);

            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBLineLabel, ChartFeaturePanel.RBLineLabel.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBSimpleLabel, ChartFeaturePanel.RBSimpleLabel.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBNoLabel, ChartFeaturePanel.RBNoLabel.isSelected());
            Config.pref.putInt(SettingKeyFactory.ChartFeatures.SLAngle, ChartFeaturePanel.SLAngle.getValue());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBExplodedSegment, ChartFeaturePanel.ChBExplodedSegment.isSelected());

            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.BarChBVLine, ChartFeaturePanel.BarChBVLine.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.BarChBHLine, ChartFeaturePanel.BarChBHLine.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.LineChBVLine, ChartFeaturePanel.LineChBVLine.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.LineChBHLine, ChartFeaturePanel.LineChBHLine.isSelected());
            setColorFromKey(ChartFeaturePanel.CBColor.getSelectedColor(), SettingKeyFactory.ChartFeatures.CBColor);

            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.OneColor, ChartFeaturePanel.OneColor.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RandomColor, ChartFeaturePanel.RandomColor.isSelected());
            setColorFromKey(ChartFeaturePanel.CBColor2.getSelectedColor(), SettingKeyFactory.ChartFeatures.CBColor2);

            Config.pref.putInt(SettingKeyFactory.ChartFeatures.SPLineWidth, Integer.parseInt(ChartFeaturePanel.SPLineWidth.getValue().toString()));

            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBFlat, ChartFeaturePanel.RBFlat.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RBRaised, ChartFeaturePanel.RBRaised.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.RB3D, ChartFeaturePanel.RB3D.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBShadow, ChartFeaturePanel.ChBShadow.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBRollover, ChartFeaturePanel.ChBRollover.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, ChartFeaturePanel.ChBOutline.isSelected());
            Config.pref.putBoolean(SettingKeyFactory.ChartFeatures.ChBSelectionOutline, ChartFeaturePanel.ChBSelectionOutline.isSelected());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void RBLineLabelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBLineLabelItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_RBLineLabelItemStateChanged

    private void RBSimpleLabelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBSimpleLabelItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_RBSimpleLabelItemStateChanged

    private void RBNoLabelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBNoLabelItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_RBNoLabelItemStateChanged

    private void ChBExplodedSegmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChBExplodedSegmentItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ChBExplodedSegmentItemStateChanged

    private void SLAngleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SLAngleStateChanged
        if (Statistic.BPieChart != null) {
            if (!Statistic.BPieChart.isEnabled() && Statistic.stylePieChart != null && Statistic.chart != null) {
                Statistic.stylePieChart.setPieOffsetAngle(SLAngle.getValue());
                Statistic.chart.repaint();
            }
        }
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_SLAngleStateChanged

    private void BarChBVLineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_BarChBVLineItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_BarChBVLineItemStateChanged

    private void BarChBHLineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_BarChBHLineItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_BarChBHLineItemStateChanged

    private void OneColorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OneColorItemStateChanged
        if (OneColor.isSelected()) {
            CBColor2.setEnabled(true);
        }
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_OneColorItemStateChanged

    private void RandomColorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RandomColorItemStateChanged
        if (RandomColor.isSelected()) {
            CBColor2.setEnabled(false);
        }
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_RandomColorItemStateChanged

    private void CBColor2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBColor2ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_CBColor2ItemStateChanged

    private void LineChBVLineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LineChBVLineItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_LineChBVLineItemStateChanged

    private void LineChBHLineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LineChBHLineItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_LineChBHLineItemStateChanged

    private void CBColorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBColorItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_CBColorItemStateChanged

    private void SPLineWidthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SPLineWidthStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_SPLineWidthStateChanged

    private void ChBShadowItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChBShadowItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_ChBShadowItemStateChanged

    private void RBFlatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBFlatItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_RBFlatItemStateChanged

    private void RBRaisedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBRaisedItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_RBRaisedItemStateChanged

    private void RB3DItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RB3DItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_RB3DItemStateChanged

    private void ChBRolloverItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChBRolloverItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ChBRolloverItemStateChanged

    private void ChBOutlineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChBOutlineItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ChBOutlineItemStateChanged

    private void ChBSelectionOutlineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChBSelectionOutlineItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ChBSelectionOutlineItemStateChanged

    private void initValues() {
        RBLineLabel.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBLineLabel, true));
        RBSimpleLabel.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBSimpleLabel, false));
        RBNoLabel.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBNoLabel, false));
        SLAngle.setValue(Config.pref.getInt(SettingKeyFactory.ChartFeatures.SLAngle, 0));
        ChBExplodedSegment.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBExplodedSegment, false));

        BarChBVLine.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.BarChBVLine, false));
        BarChBHLine.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.BarChBHLine, true));
        LineChBVLine.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.LineChBVLine, true));
        LineChBHLine.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.LineChBHLine, true));
        CBColor.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.ChartFeatures.CBColor, "255, 0, 0")));
        SPLineWidth.setValue(Config.pref.getInt(SettingKeyFactory.ChartFeatures.SPLineWidth, 2));

        OneColor.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.OneColor, false));
        RandomColor.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RandomColor, true));
        CBColor2.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.ChartFeatures.CBColor2, "0, 204, 0")));

        RBFlat.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBFlat, true));
        RBRaised.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RBRaised, false));
        RB3D.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.RB3D, false));
        ChBShadow.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBShadow, true));
        ChBRollover.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBRollover, false));
        ChBOutline.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBOutline, false));
        ChBSelectionOutline.setSelected(Config.pref.getBoolean(SettingKeyFactory.ChartFeatures.ChBSelectionOutline, false));
    }

    private void setColorFromKey(Color color, String key) {
        Config.pref.put(key, color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
    }

    private Color getColorFromKey(String value) {
        String tab[] = value.split(", ");
        return new Color(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JCheckBox BarChBHLine;
    public static javax.swing.JCheckBox BarChBVLine;
    public static com.jidesoft.combobox.ColorComboBox CBColor;
    public static com.jidesoft.combobox.ColorComboBox CBColor2;
    public static javax.swing.JCheckBox ChBExplodedSegment;
    public static javax.swing.JCheckBox ChBOutline;
    public static javax.swing.JCheckBox ChBRollover;
    public static javax.swing.JCheckBox ChBSelectionOutline;
    public static javax.swing.JCheckBox ChBShadow;
    public static javax.swing.JCheckBox LineChBHLine;
    public static javax.swing.JCheckBox LineChBVLine;
    public static javax.swing.JRadioButton OneColor;
    public static javax.swing.JRadioButton RB3D;
    public static javax.swing.JRadioButton RBFlat;
    public static javax.swing.JRadioButton RBLineLabel;
    public static javax.swing.JRadioButton RBNoLabel;
    public static javax.swing.JRadioButton RBRaised;
    public static javax.swing.JRadioButton RBSimpleLabel;
    public static javax.swing.JRadioButton RandomColor;
    public static javax.swing.JSlider SLAngle;
    public static javax.swing.JSpinner SPLineWidth;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    AbstractDialogPage page;
}
