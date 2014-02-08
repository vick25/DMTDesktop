package com.osfac.dmt.setting.panel;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;

public class FontColorPan extends javax.swing.JPanel {

    public FontColorPan(AbstractDialogPage page) {
        this.page = page;
        initComponents(I18N.DMTResourceBundle);
        initValues();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BDefault = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        RStripe21Color1 = new com.jidesoft.combobox.ColorComboBox();
        jLabel8 = new javax.swing.JLabel();
        RStripe21Color2 = new com.jidesoft.combobox.ColorComboBox();
        RStripe22Color1 = new com.jidesoft.combobox.ColorComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        RStripe22Color2 = new com.jidesoft.combobox.ColorComboBox();
        RStripe3Color1 = new com.jidesoft.combobox.ColorComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        RStripe3Color2 = new com.jidesoft.combobox.ColorComboBox();
        jLabel15 = new javax.swing.JLabel();
        RStripe3Color3 = new com.jidesoft.combobox.ColorComboBox();
        jLabel16 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        BDefault.setText(bundle.getString("FontColorPan.BDefault.text")); // NOI18N
        BDefault.setFocusable(false);
        BDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDefaultActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("FontColorPan.jLabel4.text")); // NOI18N

        jLabel7.setText(bundle.getString("FontColorPan.jLabel7.text")); // NOI18N

        RStripe21Color1.setEditable(false);
        RStripe21Color1.setFocusable(false);
        RStripe21Color1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe21Color1ItemStateChanged(evt);
            }
        });

        jLabel8.setText(bundle.getString("FontColorPan.jLabel8.text")); // NOI18N

        RStripe21Color2.setEditable(false);
        RStripe21Color2.setFocusable(false);
        RStripe21Color2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe21Color2ItemStateChanged(evt);
            }
        });

        RStripe22Color1.setEditable(false);
        RStripe22Color1.setFocusable(false);
        RStripe22Color1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe22Color1ItemStateChanged(evt);
            }
        });

        jLabel9.setText(bundle.getString("FontColorPan.jLabel9.text")); // NOI18N

        jLabel10.setText(bundle.getString("FontColorPan.jLabel10.text")); // NOI18N

        jLabel11.setText(bundle.getString("FontColorPan.jLabel11.text")); // NOI18N

        RStripe22Color2.setEditable(false);
        RStripe22Color2.setFocusable(false);
        RStripe22Color2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe22Color2ItemStateChanged(evt);
            }
        });

        RStripe3Color1.setEditable(false);
        RStripe3Color1.setFocusable(false);
        RStripe3Color1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe3Color1ItemStateChanged(evt);
            }
        });

        jLabel12.setText(bundle.getString("FontColorPan.jLabel12.text")); // NOI18N

        jLabel14.setText(bundle.getString("FontColorPan.jLabel14.text")); // NOI18N

        RStripe3Color2.setEditable(false);
        RStripe3Color2.setFocusable(false);
        RStripe3Color2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe3Color2ItemStateChanged(evt);
            }
        });

        jLabel15.setText(bundle.getString("FontColorPan.jLabel15.text")); // NOI18N

        RStripe3Color3.setEditable(false);
        RStripe3Color3.setFocusable(false);
        RStripe3Color3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe3Color3ItemStateChanged(evt);
            }
        });

        jLabel16.setText(bundle.getString("FontColorPan.jLabel16.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, 0)
                        .addComponent(RStripe3Color3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, 0)
                        .addComponent(RStripe22Color1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addGap(0, 0, 0)
                        .addComponent(RStripe22Color2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, 0)
                        .addComponent(RStripe3Color1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addGap(0, 0, 0)
                        .addComponent(RStripe3Color2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(0, 0, 0)
                            .addComponent(RStripe21Color1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel8)
                            .addGap(0, 0, 0)
                            .addComponent(RStripe21Color2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel10))
                .addGap(0, 48, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {RStripe21Color1, RStripe21Color2, RStripe22Color1, RStripe22Color2, RStripe3Color1, RStripe3Color2, RStripe3Color3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(RStripe21Color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RStripe21Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(RStripe22Color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RStripe22Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(RStripe3Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(RStripe3Color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(RStripe3Color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(BDefault)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {
        BDefault = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        RStripe21Color1 = new com.jidesoft.combobox.ColorComboBox();
        jLabel8 = new javax.swing.JLabel();
        RStripe21Color2 = new com.jidesoft.combobox.ColorComboBox();
        RStripe22Color1 = new com.jidesoft.combobox.ColorComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        RStripe22Color2 = new com.jidesoft.combobox.ColorComboBox();
        RStripe3Color1 = new com.jidesoft.combobox.ColorComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        RStripe3Color2 = new com.jidesoft.combobox.ColorComboBox();
        jLabel15 = new javax.swing.JLabel();
        RStripe3Color3 = new com.jidesoft.combobox.ColorComboBox();
        jLabel16 = new javax.swing.JLabel();
        BDefault.setText(bundle.getString("FontColorPan.BDefault.text")); // NOI18N
        BDefault.setFocusable(false);
        BDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDefaultActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("FontColorPan.jLabel4.text")); // NOI18N

        jLabel7.setText(bundle.getString("FontColorPan.jLabel7.text")); // NOI18N

        RStripe21Color1.setEditable(false);
        RStripe21Color1.setFocusable(false);
        RStripe21Color1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe21Color1ItemStateChanged(evt);
            }
        });

        jLabel8.setText(bundle.getString("FontColorPan.jLabel8.text")); // NOI18N

        RStripe21Color2.setEditable(false);
        RStripe21Color2.setFocusable(false);
        RStripe21Color2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe21Color2ItemStateChanged(evt);
            }
        });

        RStripe22Color1.setEditable(false);
        RStripe22Color1.setFocusable(false);
        RStripe22Color1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe22Color1ItemStateChanged(evt);
            }
        });

        jLabel9.setText(bundle.getString("FontColorPan.jLabel9.text")); // NOI18N

        jLabel10.setText(bundle.getString("FontColorPan.jLabel10.text")); // NOI18N

        jLabel11.setText(bundle.getString("FontColorPan.jLabel11.text")); // NOI18N

        RStripe22Color2.setEditable(false);
        RStripe22Color2.setFocusable(false);
        RStripe22Color2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe22Color2ItemStateChanged(evt);
            }
        });

        RStripe3Color1.setEditable(false);
        RStripe3Color1.setFocusable(false);
        RStripe3Color1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe3Color1ItemStateChanged(evt);
            }
        });

        jLabel12.setText(bundle.getString("FontColorPan.jLabel12.text")); // NOI18N

        jLabel14.setText(bundle.getString("FontColorPan.jLabel14.text")); // NOI18N

        RStripe3Color2.setEditable(false);
        RStripe3Color2.setFocusable(false);
        RStripe3Color2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe3Color2ItemStateChanged(evt);
            }
        });

        jLabel15.setText(bundle.getString("FontColorPan.jLabel15.text")); // NOI18N

        RStripe3Color3.setEditable(false);
        RStripe3Color3.setFocusable(false);
        RStripe3Color3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RStripe3Color3ItemStateChanged(evt);
            }
        });

        jLabel16.setText(bundle.getString("FontColorPan.jLabel16.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 0, 0)
                .addComponent(RStripe3Color3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 0, 0)
                .addComponent(RStripe22Color1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addGap(0, 0, 0)
                .addComponent(RStripe22Color2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 0, 0)
                .addComponent(RStripe3Color1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addGap(0, 0, 0)
                .addComponent(RStripe3Color2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 0, 0)
                .addComponent(RStripe21Color1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(0, 0, 0)
                .addComponent(RStripe21Color2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jLabel10))
                .addGap(0, 48, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{RStripe21Color1, RStripe21Color2, RStripe22Color1, RStripe22Color2, RStripe3Color1, RStripe3Color2, RStripe3Color3});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel7)
                .addComponent(RStripe21Color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(RStripe21Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel9)
                .addComponent(RStripe22Color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(RStripe22Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(RStripe3Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel15)
                .addComponent(RStripe3Color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel16)
                .addComponent(RStripe3Color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(BDefault)
                .addContainerGap()));
    }// </editor-fold>

    private void BDefaultActionPerformed(ActionEvent evt) {//GEN-FIRST:event_BDefaultActionPerformed
        if (JOptionPane.showConfirmDialog(this, I18N.get("ChartFeaturePanel.message-restore-default-settings"), I18N.get("Text.Confirm"), 0) == 0) {
            RStripe21Color1.setSelectedColor(getColorFromKey("253, 253, 244"));
            RStripe21Color2.setSelectedColor(getColorFromKey("230, 230, 255"));
            RStripe22Color1.setSelectedColor(getColorFromKey("217, 234, 248"));
            RStripe22Color2.setSelectedColor(getColorFromKey("227, 248, 210"));
            RStripe3Color1.setSelectedColor(getColorFromKey("253, 253, 244"));
            RStripe3Color2.setSelectedColor(getColorFromKey("230, 230, 255"));
            RStripe3Color3.setSelectedColor(getColorFromKey("210, 255, 210"));

            setColorFromKey(RStripe21Color1.getSelectedColor(), SettingKeyFactory.FontColor.RStripe21Color1);
            setColorFromKey(RStripe21Color2.getSelectedColor(), SettingKeyFactory.FontColor.RStripe21Color2);
            setColorFromKey(RStripe22Color1.getSelectedColor(), SettingKeyFactory.FontColor.RStripe22Color1);
            setColorFromKey(RStripe22Color2.getSelectedColor(), SettingKeyFactory.FontColor.RStripe22Color2);
            setColorFromKey(RStripe3Color1.getSelectedColor(), SettingKeyFactory.FontColor.RStripe3Color1);
            setColorFromKey(RStripe3Color2.getSelectedColor(), SettingKeyFactory.FontColor.RStripe3Color2);
            setColorFromKey(RStripe3Color3.getSelectedColor(), SettingKeyFactory.FontColor.RStripe3Color3);
        }
    }//GEN-LAST:event_BDefaultActionPerformed

    private void RStripe21Color1ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe21Color1ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe21Color1ItemStateChanged

    private void RStripe21Color2ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe21Color2ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe21Color2ItemStateChanged

    private void RStripe22Color1ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe22Color1ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe22Color1ItemStateChanged

    private void RStripe22Color2ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe22Color2ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe22Color2ItemStateChanged

    private void RStripe3Color1ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe3Color1ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe3Color1ItemStateChanged

    private void RStripe3Color2ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe3Color2ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe3Color2ItemStateChanged

    private void RStripe3Color3ItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_RStripe3Color3ItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);// TODO add your handling code here:
    }//GEN-LAST:event_RStripe3Color3ItemStateChanged

    private void initValues() {
        RStripe21Color1.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")));
        RStripe21Color2.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255")));
        RStripe22Color1.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe22Color1, "217, 234, 248")));
        RStripe22Color2.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe22Color2, "227, 248, 210")));
        RStripe3Color1.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe3Color1, "253, 253, 244")));
        RStripe3Color2.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe3Color2, "230, 230, 255")));
        RStripe3Color3.setSelectedColor(getColorFromKey(Config.pref.get(SettingKeyFactory.FontColor.RStripe3Color3, "210, 255, 210")));
    }

    private Color getColorFromKey(String value) {
        String tab[] = value.split(", ");
        return new Color(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }

    private void setColorFromKey(Color color, String key) {
        Config.pref.put(key, color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BDefault;
    public static com.jidesoft.combobox.ColorComboBox RStripe21Color1;
    public static com.jidesoft.combobox.ColorComboBox RStripe21Color2;
    public static com.jidesoft.combobox.ColorComboBox RStripe22Color1;
    public static com.jidesoft.combobox.ColorComboBox RStripe22Color2;
    public static com.jidesoft.combobox.ColorComboBox RStripe3Color1;
    public static com.jidesoft.combobox.ColorComboBox RStripe3Color2;
    public static com.jidesoft.combobox.ColorComboBox RStripe3Color3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
    AbstractDialogPage page;
}
