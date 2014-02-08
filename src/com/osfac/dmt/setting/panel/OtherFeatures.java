package com.osfac.dmt.setting.panel;

import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.authen.ChangeIP;
import com.osfac.dmt.setting.SettingKeyFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class OtherFeatures extends javax.swing.JPanel {

    public OtherFeatures(AbstractDialogPage page) {
        this.page = page;
        initComponents(I18N.DMTResourceBundle);
        RBHostProvider.setText(I18N.get("com.osfac.dmt.Config.Server-Text") + " http://" + Config.host);
        if (Config.isLiteVersion()) {
            PanIPAddress.setVisible(false);
            RBHostProvider.setVisible(false);
        }
        initValues();
        if (Config.isLiteVersion()) {
            RBWebsiteProvider.setSelected(true);
        }
        if (RBHostProvider.isSelected()) {
            Config.pref.put(SettingKeyFactory.OtherFeatures.HOST, "http://" + Config.host);
        } else {
            Config.pref.put(SettingKeyFactory.OtherFeatures.HOST, "http://www.osfac.net");
        }
//        OtherFeatures.BChangeIP.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        RBHostProvider = new javax.swing.JRadioButton();
        RBWebsiteProvider = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jideTabbedPane1 = new com.jidesoft.swing.JideTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        EBTopLeft = new com.jidesoft.swing.JideToggleButton();
        EBTopRight = new com.jidesoft.swing.JideToggleButton();
        EBTop = new com.jidesoft.swing.JideToggleButton();
        EBLeft = new com.jidesoft.swing.JideToggleButton();
        EBBottomLeft = new com.jidesoft.swing.JideToggleButton();
        EBBottom = new com.jidesoft.swing.JideToggleButton();
        EBBottomRight = new com.jidesoft.swing.JideToggleButton();
        EBRight = new com.jidesoft.swing.JideToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        EnBNorthWest = new com.jidesoft.swing.JideToggleButton();
        EnBNorthEast = new com.jidesoft.swing.JideToggleButton();
        EnBNorth = new com.jidesoft.swing.JideToggleButton();
        EnBWest = new com.jidesoft.swing.JideToggleButton();
        EnBSouthWest = new com.jidesoft.swing.JideToggleButton();
        EnBSouth = new com.jidesoft.swing.JideToggleButton();
        EnBSouthEast = new com.jidesoft.swing.JideToggleButton();
        EnBEast = new com.jidesoft.swing.JideToggleButton();
        EnBCenter = new com.jidesoft.swing.JideToggleButton();
        jPanel6 = new javax.swing.JPanel();
        EnCBEffect = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        EnCBSpeed = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        EnCBSmooth = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        ExBNorthWest = new com.jidesoft.swing.JideToggleButton();
        ExBNorthEast = new com.jidesoft.swing.JideToggleButton();
        ExBNorth = new com.jidesoft.swing.JideToggleButton();
        ExBWest = new com.jidesoft.swing.JideToggleButton();
        ExBSouthWest = new com.jidesoft.swing.JideToggleButton();
        ExBSouth = new com.jidesoft.swing.JideToggleButton();
        ExBSouthEast = new com.jidesoft.swing.JideToggleButton();
        ExBEast = new com.jidesoft.swing.JideToggleButton();
        ExBCenter = new com.jidesoft.swing.JideToggleButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ExBTopLeft = new com.jidesoft.swing.JideToggleButton();
        ExBTopRight = new com.jidesoft.swing.JideToggleButton();
        ExBTop = new com.jidesoft.swing.JideToggleButton();
        ExBLeft = new com.jidesoft.swing.JideToggleButton();
        ExBBottomLeft = new com.jidesoft.swing.JideToggleButton();
        ExBBottom = new com.jidesoft.swing.JideToggleButton();
        ExBBottomRight = new com.jidesoft.swing.JideToggleButton();
        ExBRight = new com.jidesoft.swing.JideToggleButton();
        jPanel10 = new javax.swing.JPanel();
        ExCBEffect = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        ExCBSpeed = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        ExCBSmooth = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        PanIPAddress = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtIP = new com.jidesoft.field.IPTextField();
        BChangeIP = new com.jidesoft.swing.JideButton();
        jPanel11 = new javax.swing.JPanel();
        ChKLayer = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.jPanel1.border.title_1"))); // NOI18N

        buttonGroup1.add(RBHostProvider);
        RBHostProvider.setSelected(true);
        RBHostProvider.setText(bundle.getString("OtherFeatures.RBHostProvider.text")); // NOI18N
        RBHostProvider.setFocusable(false);
        RBHostProvider.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBHostProviderItemStateChanged(evt);
            }
        });

        buttonGroup1.add(RBWebsiteProvider);
        RBWebsiteProvider.setText(bundle.getString("OtherFeatures.RBWebsiteProvider.text_1")); // NOI18N
        RBWebsiteProvider.setFocusable(false);
        RBWebsiteProvider.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBWebsiteProviderItemStateChanged(evt);
            }
        });

        jRadioButton1.setText(bundle.getString("OtherFeatures.jRadioButton1.text_1")); // NOI18N
        jRadioButton1.setEnabled(false);
        jRadioButton1.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RBHostProvider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RBWebsiteProvider)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton1)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBHostProvider)
                    .addComponent(RBWebsiteProvider)
                    .addComponent(jRadioButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.jPanel2.border.title_1"))); // NOI18N

        jideTabbedPane1.setFocusable(false);
        jideTabbedPane1.setTabShape(3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        EBTopLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/se.png"))); // NOI18N
        buttonGroup4.add(EBTopLeft);
        EBTopLeft.setFocusable(false);
        EBTopLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBTopLeftItemStateChanged(evt);
            }
        });

        EBTopRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/sw.png"))); // NOI18N
        buttonGroup4.add(EBTopRight);
        EBTopRight.setFocusable(false);
        EBTopRight.setSelected(true);
        EBTopRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBTopRightItemStateChanged(evt);
            }
        });

        EBTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/s.png"))); // NOI18N
        buttonGroup4.add(EBTop);
        EBTop.setFocusable(false);
        EBTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBTopItemStateChanged(evt);
            }
        });

        EBLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/e.png"))); // NOI18N
        buttonGroup4.add(EBLeft);
        EBLeft.setFocusable(false);
        EBLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBLeftItemStateChanged(evt);
            }
        });

        EBBottomLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/ne.png"))); // NOI18N
        buttonGroup4.add(EBBottomLeft);
        EBBottomLeft.setFocusable(false);
        EBBottomLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBBottomLeftItemStateChanged(evt);
            }
        });

        EBBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/n.png"))); // NOI18N
        buttonGroup4.add(EBBottom);
        EBBottom.setFocusable(false);
        EBBottom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBBottomItemStateChanged(evt);
            }
        });

        EBBottomRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/nw.png"))); // NOI18N
        buttonGroup4.add(EBBottomRight);
        EBBottomRight.setFocusable(false);
        EBBottomRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBBottomRightItemStateChanged(evt);
            }
        });

        EBRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/w.png"))); // NOI18N
        buttonGroup4.add(EBRight);
        EBRight.setFocusable(false);
        EBRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBRightItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(EBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(EBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(EBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(bundle.getString("OtherFeatures.jLabel1.text_1")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(bundle.getString("OtherFeatures.jLabel2.text_1")); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        EnBNorthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBNorthWest);
        EnBNorthWest.setFocusable(false);
        EnBNorthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBNorthWestItemStateChanged(evt);
            }
        });

        EnBNorthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBNorthEast);
        EnBNorthEast.setFocusable(false);
        EnBNorthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBNorthEastItemStateChanged(evt);
            }
        });

        EnBNorth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBNorth);
        EnBNorth.setFocusable(false);
        EnBNorth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBNorthItemStateChanged(evt);
            }
        });

        EnBWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBWest);
        EnBWest.setFocusable(false);
        EnBWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBWestItemStateChanged(evt);
            }
        });

        EnBSouthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBSouthWest);
        EnBSouthWest.setFocusable(false);
        EnBSouthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBSouthWestItemStateChanged(evt);
            }
        });

        EnBSouth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBSouth);
        EnBSouth.setFocusable(false);
        EnBSouth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBSouthItemStateChanged(evt);
            }
        });

        EnBSouthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBSouthEast);
        EnBSouthEast.setFocusable(false);
        EnBSouthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBSouthEastItemStateChanged(evt);
            }
        });

        EnBEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBEast);
        EnBEast.setFocusable(false);
        EnBEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBEastItemStateChanged(evt);
            }
        });

        EnBCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBCenter);
        EnBCenter.setFocusable(false);
        EnBCenter.setSelected(true);
        EnBCenter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBCenterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(EnBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(EnBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EnBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(EnBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EnBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EnBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EnBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EnBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EnBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        EnCBEffect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FLY", "ZOOM", "FADE" }));
        EnCBEffect.setFocusable(false);
        EnCBEffect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnCBEffectItemStateChanged(evt);
            }
        });

        jLabel3.setText(bundle.getString("OtherFeatures.jLabel3.text_1")); // NOI18N

        EnCBSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "VERY SLOW", "SLOW", "MEDIUM", "FAST", "VERY FAST" }));
        EnCBSpeed.setSelectedIndex(2);
        EnCBSpeed.setFocusable(false);
        EnCBSpeed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnCBSpeedItemStateChanged(evt);
            }
        });

        jLabel4.setText(bundle.getString("OtherFeatures.jLabel4.text_1")); // NOI18N

        EnCBSmooth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "VERY SMOOTH", "SMOOTH", "MEDIUM", "ROUGH", "VERY ROUGH" }));
        EnCBSmooth.setSelectedIndex(2);
        EnCBSmooth.setFocusable(false);
        EnCBSmooth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnCBSmoothItemStateChanged(evt);
            }
        });

        jLabel5.setText(bundle.getString("OtherFeatures.jLabel5.text_1")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(EnCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(EnCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(EnCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(EnCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(EnCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(EnCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jideTabbedPane1.addTab(bundle.getString("OtherFeatures.jPanel3.TabConstraints.tabTitle_1"), jPanel3); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(bundle.getString("OtherFeatures.jLabel6.text_1")); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ExBNorthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBNorthWest);
        ExBNorthWest.setFocusable(false);
        ExBNorthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBNorthWestItemStateChanged(evt);
            }
        });

        ExBNorthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBNorthEast);
        ExBNorthEast.setFocusable(false);
        ExBNorthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBNorthEastItemStateChanged(evt);
            }
        });

        ExBNorth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBNorth);
        ExBNorth.setFocusable(false);
        ExBNorth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBNorthItemStateChanged(evt);
            }
        });

        ExBWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBWest);
        ExBWest.setFocusable(false);
        ExBWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBWestItemStateChanged(evt);
            }
        });

        ExBSouthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBSouthWest);
        ExBSouthWest.setFocusable(false);
        ExBSouthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBSouthWestItemStateChanged(evt);
            }
        });

        ExBSouth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBSouth);
        ExBSouth.setFocusable(false);
        ExBSouth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBSouthItemStateChanged(evt);
            }
        });

        ExBSouthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBSouthEast);
        ExBSouthEast.setFocusable(false);
        ExBSouthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBSouthEastItemStateChanged(evt);
            }
        });

        ExBEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBEast);
        ExBEast.setFocusable(false);
        ExBEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBEastItemStateChanged(evt);
            }
        });

        ExBCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBCenter);
        ExBCenter.setFocusable(false);
        ExBCenter.setSelected(true);
        ExBCenter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBCenterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(ExBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(ExBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ExBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(ExBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ExBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(bundle.getString("OtherFeatures.jLabel7.text_1")); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ExBTopLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/nw.png"))); // NOI18N
        buttonGroup2.add(ExBTopLeft);
        ExBTopLeft.setFocusable(false);
        ExBTopLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBTopLeftItemStateChanged(evt);
            }
        });

        ExBTopRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/ne.png"))); // NOI18N
        buttonGroup2.add(ExBTopRight);
        ExBTopRight.setFocusable(false);
        ExBTopRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBTopRightItemStateChanged(evt);
            }
        });

        ExBTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/n.png"))); // NOI18N
        buttonGroup2.add(ExBTop);
        ExBTop.setFocusable(false);
        ExBTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBTopItemStateChanged(evt);
            }
        });

        ExBLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/w.png"))); // NOI18N
        buttonGroup2.add(ExBLeft);
        ExBLeft.setFocusable(false);
        ExBLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBLeftItemStateChanged(evt);
            }
        });

        ExBBottomLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/sw.png"))); // NOI18N
        buttonGroup2.add(ExBBottomLeft);
        ExBBottomLeft.setFocusable(false);
        ExBBottomLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBBottomLeftItemStateChanged(evt);
            }
        });

        ExBBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/s.png"))); // NOI18N
        buttonGroup2.add(ExBBottom);
        ExBBottom.setFocusable(false);
        ExBBottom.setSelected(true);
        ExBBottom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBBottomItemStateChanged(evt);
            }
        });

        ExBBottomRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/se.png"))); // NOI18N
        buttonGroup2.add(ExBBottomRight);
        ExBBottomRight.setFocusable(false);
        ExBBottomRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBBottomRightItemStateChanged(evt);
            }
        });

        ExBRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/e.png"))); // NOI18N
        buttonGroup2.add(ExBRight);
        ExBRight.setFocusable(false);
        ExBRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBRightItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(ExBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(ExBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ExBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ExBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        ExCBEffect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FLY", "ZOOM", "FADE" }));
        ExCBEffect.setFocusable(false);
        ExCBEffect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExCBEffectItemStateChanged(evt);
            }
        });

        jLabel8.setText(bundle.getString("OtherFeatures.jLabel8.text_1")); // NOI18N

        ExCBSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "VERY SLOW", "SLOW", "MEDIUM", "FAST", "VERY FAST" }));
        ExCBSpeed.setSelectedIndex(2);
        ExCBSpeed.setFocusable(false);
        ExCBSpeed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExCBSpeedItemStateChanged(evt);
            }
        });

        jLabel9.setText(bundle.getString("OtherFeatures.jLabel9.text_1")); // NOI18N

        ExCBSmooth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "VERY SMOOTH", "SMOOTH", "MEDIUM", "ROUGH", "VERY ROUGH" }));
        ExCBSmooth.setSelectedIndex(2);
        ExCBSmooth.setFocusable(false);
        ExCBSmooth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExCBSmoothItemStateChanged(evt);
            }
        });

        jLabel10.setText(bundle.getString("OtherFeatures.jLabel10.text_1")); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ExCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ExCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(ExCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(ExCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(ExCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(ExCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jideTabbedPane1.addTab(bundle.getString("OtherFeatures.jPanel7.TabConstraints.tabTitle_1"), jPanel7); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jideTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jideTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanIPAddress.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.PanIPAddress.border.title"))); // NOI18N

        jLabel11.setText(bundle.getString("OtherFeatures.jLabel11.text")); // NOI18N

        txtIP.setEnabled(false);

        BChangeIP.setText(bundle.getString("OtherFeatures.BChangeIP.text")); // NOI18N
        BChangeIP.setButtonStyle(1);
        BChangeIP.setFocusable(false);
        BChangeIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BChangeIPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanIPAddressLayout = new javax.swing.GroupLayout(PanIPAddress);
        PanIPAddress.setLayout(PanIPAddressLayout);
        PanIPAddressLayout.setHorizontalGroup(
            PanIPAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanIPAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(BChangeIP, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanIPAddressLayout.setVerticalGroup(
            PanIPAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanIPAddressLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(PanIPAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BChangeIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.jPanel11.border.title"))); // NOI18N

        ChKLayer.setSelected(true);
        ChKLayer.setText(bundle.getString("OtherFeatures.ChKLayer.text")); // NOI18N
        ChKLayer.setFocusable(false);
        ChKLayer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChKLayerItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(ChKLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ChKLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PanIPAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        RBHostProvider = new javax.swing.JRadioButton();
        RBWebsiteProvider = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jideTabbedPane1 = new com.jidesoft.swing.JideTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        EBTopLeft = new com.jidesoft.swing.JideToggleButton();
        EBTopRight = new com.jidesoft.swing.JideToggleButton();
        EBTop = new com.jidesoft.swing.JideToggleButton();
        EBLeft = new com.jidesoft.swing.JideToggleButton();
        EBBottomLeft = new com.jidesoft.swing.JideToggleButton();
        EBBottom = new com.jidesoft.swing.JideToggleButton();
        EBBottomRight = new com.jidesoft.swing.JideToggleButton();
        EBRight = new com.jidesoft.swing.JideToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        EnBNorthWest = new com.jidesoft.swing.JideToggleButton();
        EnBNorthEast = new com.jidesoft.swing.JideToggleButton();
        EnBNorth = new com.jidesoft.swing.JideToggleButton();
        EnBWest = new com.jidesoft.swing.JideToggleButton();
        EnBSouthWest = new com.jidesoft.swing.JideToggleButton();
        EnBSouth = new com.jidesoft.swing.JideToggleButton();
        EnBSouthEast = new com.jidesoft.swing.JideToggleButton();
        EnBEast = new com.jidesoft.swing.JideToggleButton();
        EnBCenter = new com.jidesoft.swing.JideToggleButton();
        jPanel6 = new javax.swing.JPanel();
        EnCBEffect = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        EnCBSpeed = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        EnCBSmooth = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        ExBNorthWest = new com.jidesoft.swing.JideToggleButton();
        ExBNorthEast = new com.jidesoft.swing.JideToggleButton();
        ExBNorth = new com.jidesoft.swing.JideToggleButton();
        ExBWest = new com.jidesoft.swing.JideToggleButton();
        ExBSouthWest = new com.jidesoft.swing.JideToggleButton();
        ExBSouth = new com.jidesoft.swing.JideToggleButton();
        ExBSouthEast = new com.jidesoft.swing.JideToggleButton();
        ExBEast = new com.jidesoft.swing.JideToggleButton();
        ExBCenter = new com.jidesoft.swing.JideToggleButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ExBTopLeft = new com.jidesoft.swing.JideToggleButton();
        ExBTopRight = new com.jidesoft.swing.JideToggleButton();
        ExBTop = new com.jidesoft.swing.JideToggleButton();
        ExBLeft = new com.jidesoft.swing.JideToggleButton();
        ExBBottomLeft = new com.jidesoft.swing.JideToggleButton();
        ExBBottom = new com.jidesoft.swing.JideToggleButton();
        ExBBottomRight = new com.jidesoft.swing.JideToggleButton();
        ExBRight = new com.jidesoft.swing.JideToggleButton();
        jPanel10 = new javax.swing.JPanel();
        ExCBEffect = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        ExCBSpeed = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        ExCBSmooth = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        PanIPAddress = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtIP = new com.jidesoft.field.IPTextField();
        BChangeIP = new com.jidesoft.swing.JideButton();
        jPanel11 = new javax.swing.JPanel();
        ChKLayer = new javax.swing.JCheckBox();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.jPanel1.border.title_1"))); // NOI18N

        buttonGroup1.add(RBHostProvider);
        RBHostProvider.setSelected(true);
        RBHostProvider.setText(bundle.getString("OtherFeatures.RBHostProvider.text")); // NOI18N
        RBHostProvider.setFocusable(false);
        RBHostProvider.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBHostProviderItemStateChanged(evt);
            }
        });

        buttonGroup1.add(RBWebsiteProvider);
        RBWebsiteProvider.setText(bundle.getString("OtherFeatures.RBWebsiteProvider.text_1")); // NOI18N
        RBWebsiteProvider.setFocusable(false);
        RBWebsiteProvider.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RBWebsiteProviderItemStateChanged(evt);
            }
        });

        jRadioButton1.setText(bundle.getString("OtherFeatures.jRadioButton1.text_1")); // NOI18N
        jRadioButton1.setEnabled(false);
        jRadioButton1.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RBHostProvider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RBWebsiteProvider)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton1)
                .addGap(26, 26, 26)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(RBHostProvider)
                .addComponent(RBWebsiteProvider)
                .addComponent(jRadioButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.jPanel2.border.title_1"))); // NOI18N

        jideTabbedPane1.setFocusable(false);
        jideTabbedPane1.setTabShape(3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        EBTopLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/se.png"))); // NOI18N
        buttonGroup4.add(EBTopLeft);
        EBTopLeft.setFocusable(false);
        EBTopLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBTopLeftItemStateChanged(evt);
            }
        });

        EBTopRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/sw.png"))); // NOI18N
        buttonGroup4.add(EBTopRight);
        EBTopRight.setFocusable(false);
        EBTopRight.setSelected(true);
        EBTopRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBTopRightItemStateChanged(evt);
            }
        });

        EBTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/s.png"))); // NOI18N
        buttonGroup4.add(EBTop);
        EBTop.setFocusable(false);
        EBTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBTopItemStateChanged(evt);
            }
        });

        EBLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/e.png"))); // NOI18N
        buttonGroup4.add(EBLeft);
        EBLeft.setFocusable(false);
        EBLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBLeftItemStateChanged(evt);
            }
        });

        EBBottomLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/ne.png"))); // NOI18N
        buttonGroup4.add(EBBottomLeft);
        EBBottomLeft.setFocusable(false);
        EBBottomLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBBottomLeftItemStateChanged(evt);
            }
        });

        EBBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/n.png"))); // NOI18N
        buttonGroup4.add(EBBottom);
        EBBottom.setFocusable(false);
        EBBottom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBBottomItemStateChanged(evt);
            }
        });

        EBBottomRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/nw.png"))); // NOI18N
        buttonGroup4.add(EBBottomRight);
        EBBottomRight.setFocusable(false);
        EBBottomRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBBottomRightItemStateChanged(evt);
            }
        });

        EBRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/w.png"))); // NOI18N
        buttonGroup4.add(EBRight);
        EBRight.setFocusable(false);
        EBRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EBRightItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(EBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(EBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(EBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(bundle.getString("OtherFeatures.jLabel1.text_1")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(bundle.getString("OtherFeatures.jLabel2.text_1")); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        EnBNorthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBNorthWest);
        EnBNorthWest.setFocusable(false);
        EnBNorthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBNorthWestItemStateChanged(evt);
            }
        });

        EnBNorthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBNorthEast);
        EnBNorthEast.setFocusable(false);
        EnBNorthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBNorthEastItemStateChanged(evt);
            }
        });

        EnBNorth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBNorth);
        EnBNorth.setFocusable(false);
        EnBNorth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBNorthItemStateChanged(evt);
            }
        });

        EnBWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBWest);
        EnBWest.setFocusable(false);
        EnBWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBWestItemStateChanged(evt);
            }
        });

        EnBSouthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBSouthWest);
        EnBSouthWest.setFocusable(false);
        EnBSouthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBSouthWestItemStateChanged(evt);
            }
        });

        EnBSouth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBSouth);
        EnBSouth.setFocusable(false);
        EnBSouth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBSouthItemStateChanged(evt);
            }
        });

        EnBSouthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBSouthEast);
        EnBSouthEast.setFocusable(false);
        EnBSouthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBSouthEastItemStateChanged(evt);
            }
        });

        EnBEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBEast);
        EnBEast.setFocusable(false);
        EnBEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBEastItemStateChanged(evt);
            }
        });

        EnBCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup5.add(EnBCenter);
        EnBCenter.setFocusable(false);
        EnBCenter.setSelected(true);
        EnBCenter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnBCenterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(EnBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(EnBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(EnBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EnBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EnBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EnBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(EnBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EnBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));

        EnCBEffect.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"FLY", "ZOOM", "FADE"}));
        EnCBEffect.setFocusable(false);
        EnCBEffect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnCBEffectItemStateChanged(evt);
            }
        });

        jLabel3.setText(bundle.getString("OtherFeatures.jLabel3.text_1")); // NOI18N

        EnCBSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"VERY SLOW", "SLOW", "MEDIUM", "FAST", "VERY FAST"}));
        EnCBSpeed.setSelectedIndex(2);
        EnCBSpeed.setFocusable(false);
        EnCBSpeed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnCBSpeedItemStateChanged(evt);
            }
        });

        jLabel4.setText(bundle.getString("OtherFeatures.jLabel4.text_1")); // NOI18N

        EnCBSmooth.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"VERY SMOOTH", "SMOOTH", "MEDIUM", "ROUGH", "VERY ROUGH"}));
        EnCBSmooth.setSelectedIndex(2);
        EnCBSmooth.setFocusable(false);
        EnCBSmooth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnCBSmoothItemStateChanged(evt);
            }
        });

        jLabel5.setText(bundle.getString("OtherFeatures.jLabel5.text_1")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(EnCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(EnCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(EnCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap()));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(EnCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(EnCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(EnCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1)
                .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));

        jideTabbedPane1.addTab(bundle.getString("OtherFeatures.jPanel3.TabConstraints.tabTitle_1"), jPanel3); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(bundle.getString("OtherFeatures.jLabel6.text_1")); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ExBNorthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBNorthWest);
        ExBNorthWest.setFocusable(false);
        ExBNorthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBNorthWestItemStateChanged(evt);
            }
        });

        ExBNorthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBNorthEast);
        ExBNorthEast.setFocusable(false);
        ExBNorthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBNorthEastItemStateChanged(evt);
            }
        });

        ExBNorth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBNorth);
        ExBNorth.setFocusable(false);
        ExBNorth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBNorthItemStateChanged(evt);
            }
        });

        ExBWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBWest);
        ExBWest.setFocusable(false);
        ExBWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBWestItemStateChanged(evt);
            }
        });

        ExBSouthWest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBSouthWest);
        ExBSouthWest.setFocusable(false);
        ExBSouthWest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBSouthWestItemStateChanged(evt);
            }
        });

        ExBSouth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBSouth);
        ExBSouth.setFocusable(false);
        ExBSouth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBSouthItemStateChanged(evt);
            }
        });

        ExBSouthEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBSouthEast);
        ExBSouthEast.setFocusable(false);
        ExBSouthEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBSouthEastItemStateChanged(evt);
            }
        });

        ExBEast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBEast);
        ExBEast.setFocusable(false);
        ExBEast.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBEastItemStateChanged(evt);
            }
        });

        ExBCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/picture.png"))); // NOI18N
        buttonGroup3.add(ExBCenter);
        ExBCenter.setFocusable(false);
        ExBCenter.setSelected(true);
        ExBCenter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBCenterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(ExBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(ExBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(ExBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBNorthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBNorthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBSouthWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBSouthEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));

        jLabel7.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(bundle.getString("OtherFeatures.jLabel7.text_1")); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ExBTopLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/nw.png"))); // NOI18N
        buttonGroup2.add(ExBTopLeft);
        ExBTopLeft.setFocusable(false);
        ExBTopLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBTopLeftItemStateChanged(evt);
            }
        });

        ExBTopRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/ne.png"))); // NOI18N
        buttonGroup2.add(ExBTopRight);
        ExBTopRight.setFocusable(false);
        ExBTopRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBTopRightItemStateChanged(evt);
            }
        });

        ExBTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/n.png"))); // NOI18N
        buttonGroup2.add(ExBTop);
        ExBTop.setFocusable(false);
        ExBTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBTopItemStateChanged(evt);
            }
        });

        ExBLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/w.png"))); // NOI18N
        buttonGroup2.add(ExBLeft);
        ExBLeft.setFocusable(false);
        ExBLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBLeftItemStateChanged(evt);
            }
        });

        ExBBottomLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/sw.png"))); // NOI18N
        buttonGroup2.add(ExBBottomLeft);
        ExBBottomLeft.setFocusable(false);
        ExBBottomLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBBottomLeftItemStateChanged(evt);
            }
        });

        ExBBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/s.png"))); // NOI18N
        buttonGroup2.add(ExBBottom);
        ExBBottom.setFocusable(false);
        ExBBottom.setSelected(true);
        ExBBottom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBBottomItemStateChanged(evt);
            }
        });

        ExBBottomRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/se.png"))); // NOI18N
        buttonGroup2.add(ExBBottomRight);
        ExBBottomRight.setFocusable(false);
        ExBBottomRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBBottomRightItemStateChanged(evt);
            }
        });

        ExBRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/setting/panel/icon/e.png"))); // NOI18N
        buttonGroup2.add(ExBRight);
        ExBRight.setFocusable(false);
        ExBRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExBRightItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(ExBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(ExBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(ExBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBTopRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ExBBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ExBBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));

        ExCBEffect.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"FLY", "ZOOM", "FADE"}));
        ExCBEffect.setFocusable(false);
        ExCBEffect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExCBEffectItemStateChanged(evt);
            }
        });

        jLabel8.setText(bundle.getString("OtherFeatures.jLabel8.text_1")); // NOI18N

        ExCBSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"VERY SLOW", "SLOW", "MEDIUM", "FAST", "VERY FAST"}));
        ExCBSpeed.setSelectedIndex(2);
        ExCBSpeed.setFocusable(false);
        ExCBSpeed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExCBSpeedItemStateChanged(evt);
            }
        });

        jLabel9.setText(bundle.getString("OtherFeatures.jLabel9.text_1")); // NOI18N

        ExCBSmooth.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"VERY SMOOTH", "SMOOTH", "MEDIUM", "ROUGH", "VERY ROUGH"}));
        ExCBSmooth.setSelectedIndex(2);
        ExCBSmooth.setFocusable(false);
        ExCBSmooth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExCBSmoothItemStateChanged(evt);
            }
        });

        jLabel10.setText(bundle.getString("OtherFeatures.jLabel10.text_1")); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ExCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ExCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(ExCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap()));
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel8)
                .addComponent(ExCBEffect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(ExCBSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel10)
                .addComponent(ExCBSmooth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel6)
                .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));

        jideTabbedPane1.addTab(bundle.getString("OtherFeatures.jPanel7.TabConstraints.tabTitle_1"), jPanel7); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jideTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(9, 9, 9)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jideTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        PanIPAddress.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.PanIPAddress.border.title"))); // NOI18N

        jLabel11.setText(bundle.getString("OtherFeatures.jLabel11.text")); // NOI18N

        txtIP.setEnabled(false);

        BChangeIP.setText(bundle.getString("OtherFeatures.BChangeIP.text")); // NOI18N
        BChangeIP.setButtonStyle(1);
        BChangeIP.setFocusable(false);
        BChangeIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BChangeIPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanIPAddressLayout = new javax.swing.GroupLayout(PanIPAddress);
        PanIPAddress.setLayout(PanIPAddressLayout);
        PanIPAddressLayout.setHorizontalGroup(
                PanIPAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanIPAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(BChangeIP, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        PanIPAddressLayout.setVerticalGroup(
                PanIPAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanIPAddressLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(PanIPAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel11)
                .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(BChangeIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OtherFeatures.jPanel11.border.title"))); // NOI18N

        ChKLayer.setSelected(true);
        ChKLayer.setText(bundle.getString("OtherFeatures.ChKLayer.text")); // NOI18N
        ChKLayer.setFocusable(false);
        ChKLayer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChKLayerItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(ChKLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ChKLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanIPAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }// </editor-fold>

    private void RBHostProviderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBHostProviderItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_RBHostProviderItemStateChanged

    private void RBWebsiteProviderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RBWebsiteProviderItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_RBWebsiteProviderItemStateChanged

    private void EBTopLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBTopLeftItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_EBTopLeftItemStateChanged

    private void EBTopRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBTopRightItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBTopRightItemStateChanged

    private void EBTopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBTopItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBTopItemStateChanged

    private void EBLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBLeftItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBLeftItemStateChanged

    private void EBBottomLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBBottomLeftItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBBottomLeftItemStateChanged

    private void EBBottomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBBottomItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBBottomItemStateChanged

    private void EBBottomRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBBottomRightItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBBottomRightItemStateChanged

    private void EBRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EBRightItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EBRightItemStateChanged

    private void EnBNorthWestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBNorthWestItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBNorthWestItemStateChanged

    private void EnBNorthEastItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBNorthEastItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBNorthEastItemStateChanged

    private void EnBNorthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBNorthItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBNorthItemStateChanged

    private void EnBWestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBWestItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBWestItemStateChanged

    private void EnBSouthWestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBSouthWestItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBSouthWestItemStateChanged

    private void EnBSouthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBSouthItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBSouthItemStateChanged

    private void EnBSouthEastItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBSouthEastItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBSouthEastItemStateChanged

    private void EnBEastItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBEastItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBEastItemStateChanged

    private void EnBCenterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnBCenterItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnBCenterItemStateChanged

    private void EnCBEffectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnCBEffectItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnCBEffectItemStateChanged

    private void EnCBSpeedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnCBSpeedItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnCBSpeedItemStateChanged

    private void EnCBSmoothItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnCBSmoothItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_EnCBSmoothItemStateChanged

    private void ExBNorthWestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBNorthWestItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBNorthWestItemStateChanged

    private void ExBNorthEastItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBNorthEastItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBNorthEastItemStateChanged

    private void ExBNorthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBNorthItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBNorthItemStateChanged

    private void ExBWestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBWestItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBWestItemStateChanged

    private void ExBSouthWestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBSouthWestItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBSouthWestItemStateChanged

    private void ExBSouthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBSouthItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBSouthItemStateChanged

    private void ExBSouthEastItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBSouthEastItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBSouthEastItemStateChanged

    private void ExBEastItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBEastItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBEastItemStateChanged

    private void ExBCenterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBCenterItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBCenterItemStateChanged

    private void ExBTopLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBTopLeftItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBTopLeftItemStateChanged

    private void ExBTopRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBTopRightItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBTopRightItemStateChanged

    private void ExBTopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBTopItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBTopItemStateChanged

    private void ExBLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBLeftItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBLeftItemStateChanged

    private void ExBBottomLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBBottomLeftItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBBottomLeftItemStateChanged

    private void ExBBottomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBBottomItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBBottomItemStateChanged

    private void ExBBottomRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBBottomRightItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExBBottomRightItemStateChanged

    private void ExBRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExBRightItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_ExBRightItemStateChanged

    private void ExCBEffectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExCBEffectItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExCBEffectItemStateChanged

    private void ExCBSpeedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExCBSpeedItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExCBSpeedItemStateChanged

    private void ExCBSmoothItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExCBSmoothItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);        // TODO add your handling code here:
    }//GEN-LAST:event_ExCBSmoothItemStateChanged

    private void BChangeIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BChangeIPActionPerformed
        String oldIPAddress = txtIP.getText();
        new ChangeIP(null, true).setVisible(true);
        if (!oldIPAddress.equals(Config.host)) {
            JOptionPane.showMessageDialog(this, I18N.get("Text.DMT-reboot")
                    + "", I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_BChangeIPActionPerformed

    private void ChKLayerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChKLayerItemStateChanged
        page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
    }//GEN-LAST:event_ChKLayerItemStateChanged

    private void initValues() {
        RBHostProvider.setSelected(Config.pref.getBoolean(SettingKeyFactory.OtherFeatures.RBHostProvider, true));
        RBWebsiteProvider.setSelected(Config.pref.getBoolean(SettingKeyFactory.OtherFeatures.RBWebsiteProvider, false));
        directionEntrance(Config.pref.getInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP_RIGHT));
        directionExit(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        locationEntrance(Config.pref.getInt(SettingKeyFactory.General.entranceLocation, SwingConstants.CENTER));
        setEffectEntrance(Config.pref.getInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_FLY));
        setEffecttExit(Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY));
        setSpeedEntrance(Config.pref.getInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_MEDIUM));
        setSpeedExit(Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        setSmoothEntrance(Config.pref.getInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM));
        setSmoothExit(Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM));
        txtIP.setText(Config.pref.get(SettingKeyFactory.Connection.HOST, Config.host));
        ChKLayer.setSelected(Config.pref.getBoolean(SettingKeyFactory.OtherFeatures.ChKLayer, true));
    }

    private void setSmoothEntrance(int smooth) {
        if (smooth == CustomAnimation.SMOOTHNESS_VERY_SMOOTH) {
            EnCBSmooth.setSelectedItem("VERY SMOOTH");
        } else if (smooth == CustomAnimation.SMOOTHNESS_SMOOTH) {
            EnCBSmooth.setSelectedItem("SMOOTH");
        } else if (smooth == CustomAnimation.SMOOTHNESS_MEDIUM) {
            EnCBSmooth.setSelectedItem("MEDIUM");
        } else if (smooth == CustomAnimation.SMOOTHNESS_ROUGH) {
            EnCBSmooth.setSelectedItem("ROUGH");
        } else if (smooth == CustomAnimation.SMOOTHNESS_VERY_ROUGH) {
            EnCBSmooth.setSelectedItem("VERY ROUGH");
        }
    }

    private void setSmoothExit(int smooth) {
        if (smooth == CustomAnimation.SMOOTHNESS_VERY_SMOOTH) {
            ExCBSmooth.setSelectedItem("VERY SMOOTH");
        } else if (smooth == CustomAnimation.SMOOTHNESS_SMOOTH) {
            ExCBSmooth.setSelectedItem("SMOOTH");
        } else if (smooth == CustomAnimation.SMOOTHNESS_MEDIUM) {
            ExCBSmooth.setSelectedItem("MEDIUM");
        } else if (smooth == CustomAnimation.SMOOTHNESS_ROUGH) {
            ExCBSmooth.setSelectedItem("ROUGH");
        } else if (smooth == CustomAnimation.SMOOTHNESS_VERY_ROUGH) {
            ExCBSmooth.setSelectedItem("VERY ROUGH");
        }
    }

    private void setSpeedEntrance(int speed) {
        if (speed == CustomAnimation.SPEED_VERY_SLOW) {
            EnCBSpeed.setSelectedItem("VERY SLOW");
        } else if (speed == CustomAnimation.SPEED_SLOW) {
            EnCBSpeed.setSelectedItem("SLOW");
        } else if (speed == CustomAnimation.SPEED_MEDIUM) {
            EnCBSpeed.setSelectedItem("MEDIUM");
        } else if (speed == CustomAnimation.SPEED_FAST) {
            EnCBSpeed.setSelectedItem("FAST");
        } else if (speed == CustomAnimation.SPEED_VERY_FAST) {
            EnCBSpeed.setSelectedItem("VERY FAST");
        }
    }

    private void setSpeedExit(int speed) {
        if (speed == CustomAnimation.SPEED_VERY_SLOW) {
            ExCBSpeed.setSelectedItem("VERY SLOW");
        } else if (speed == CustomAnimation.SPEED_SLOW) {
            ExCBSpeed.setSelectedItem("SLOW");
        } else if (speed == CustomAnimation.SPEED_MEDIUM) {
            ExCBSpeed.setSelectedItem("MEDIUM");
        } else if (speed == CustomAnimation.SPEED_FAST) {
            ExCBSpeed.setSelectedItem("FAST");
        } else if (speed == CustomAnimation.SPEED_VERY_FAST) {
            ExCBSpeed.setSelectedItem("VERY FAST");
        }
    }

    private void setEffecttExit(int effect) {
        if (effect == CustomAnimation.EFFECT_FLY) {
            ExCBEffect.setSelectedItem("FLY");
        } else if (effect == CustomAnimation.EFFECT_ZOOM) {
            ExCBEffect.setSelectedItem("ZOOM");
        } else if (effect == CustomAnimation.EFFECT_FADE) {
            ExCBEffect.setSelectedItem("FADE");
        }
    }

    private void setEffectEntrance(int effect) {
        if (effect == CustomAnimation.EFFECT_FLY) {
            EnCBEffect.setSelectedItem("FLY");
        } else if (effect == CustomAnimation.EFFECT_ZOOM) {
            EnCBEffect.setSelectedItem("ZOOM");
        } else if (effect == CustomAnimation.EFFECT_FADE) {
            EnCBEffect.setSelectedItem("FADE");
        }
    }

    private void directionEntrance(int direction) {
        if (direction == CustomAnimation.TOP) {
            EBTop.setSelected(true);
        } else if (direction == CustomAnimation.TOP_RIGHT) {
            EBTopRight.setSelected(true);
        } else if (direction == CustomAnimation.TOP_LEFT) {
            EBTopLeft.setSelected(true);
        } else if (direction == CustomAnimation.BOTTOM) {
            EBBottom.setSelected(true);
        } else if (direction == CustomAnimation.BOTTOM_LEFT) {
            EBBottomLeft.setSelected(true);
        } else if (direction == CustomAnimation.BOTTOM_RIGHT) {
            EBBottomRight.setSelected(true);
        } else if (direction == CustomAnimation.LEFT) {
            EBLeft.setSelected(true);
        } else if (direction == CustomAnimation.RIGHT) {
            EBRight.setSelected(true);
        }
    }

    private void locationEntrance(int location) {
        if (location == SwingConstants.CENTER) {
            EnBCenter.setSelected(true);
        } else if (location == SwingConstants.EAST) {
            EnBEast.setSelected(true);
        } else if (location == SwingConstants.NORTH) {
            EnBNorth.setSelected(true);
        } else if (location == SwingConstants.NORTH_EAST) {
            EnBNorthEast.setSelected(true);
        } else if (location == SwingConstants.NORTH_WEST) {
            EnBNorthWest.setSelected(true);
        } else if (location == SwingConstants.SOUTH) {
            EnBSouth.setSelected(true);
        } else if (location == SwingConstants.SOUTH_EAST) {
            EnBSouthEast.setSelected(true);
        } else if (location == SwingConstants.SOUTH_WEST) {
            EnBSouthWest.setSelected(true);
        } else if (location == SwingConstants.WEST) {
            EnBWest.setSelected(true);
        }
    }

    private void directionExit(int direction) {
        if (direction == CustomAnimation.TOP) {
            ExBTop.setSelected(true);
        } else if (direction == CustomAnimation.TOP_RIGHT) {
            ExBTopRight.setSelected(true);
        } else if (direction == CustomAnimation.TOP_LEFT) {
            ExBTopLeft.setSelected(true);
        } else if (direction == CustomAnimation.BOTTOM) {
            ExBBottom.setSelected(true);
        } else if (direction == CustomAnimation.BOTTOM_LEFT) {
            ExBBottomLeft.setSelected(true);
        } else if (direction == CustomAnimation.BOTTOM_RIGHT) {
            ExBBottomRight.setSelected(true);
        } else if (direction == CustomAnimation.LEFT) {
            ExBLeft.setSelected(true);
        } else if (direction == CustomAnimation.RIGHT) {
            ExBRight.setSelected(true);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static com.jidesoft.swing.JideButton BChangeIP;
    public static javax.swing.JCheckBox ChKLayer;
    public static com.jidesoft.swing.JideToggleButton EBBottom;
    public static com.jidesoft.swing.JideToggleButton EBBottomLeft;
    public static com.jidesoft.swing.JideToggleButton EBBottomRight;
    public static com.jidesoft.swing.JideToggleButton EBLeft;
    public static com.jidesoft.swing.JideToggleButton EBRight;
    public static com.jidesoft.swing.JideToggleButton EBTop;
    public static com.jidesoft.swing.JideToggleButton EBTopLeft;
    public static com.jidesoft.swing.JideToggleButton EBTopRight;
    public static com.jidesoft.swing.JideToggleButton EnBCenter;
    public static com.jidesoft.swing.JideToggleButton EnBEast;
    public static com.jidesoft.swing.JideToggleButton EnBNorth;
    public static com.jidesoft.swing.JideToggleButton EnBNorthEast;
    public static com.jidesoft.swing.JideToggleButton EnBNorthWest;
    public static com.jidesoft.swing.JideToggleButton EnBSouth;
    public static com.jidesoft.swing.JideToggleButton EnBSouthEast;
    public static com.jidesoft.swing.JideToggleButton EnBSouthWest;
    public static com.jidesoft.swing.JideToggleButton EnBWest;
    public static javax.swing.JComboBox EnCBEffect;
    public static javax.swing.JComboBox EnCBSmooth;
    public static javax.swing.JComboBox EnCBSpeed;
    public static com.jidesoft.swing.JideToggleButton ExBBottom;
    public static com.jidesoft.swing.JideToggleButton ExBBottomLeft;
    public static com.jidesoft.swing.JideToggleButton ExBBottomRight;
    public static com.jidesoft.swing.JideToggleButton ExBCenter;
    public static com.jidesoft.swing.JideToggleButton ExBEast;
    public static com.jidesoft.swing.JideToggleButton ExBLeft;
    public static com.jidesoft.swing.JideToggleButton ExBNorth;
    public static com.jidesoft.swing.JideToggleButton ExBNorthEast;
    public static com.jidesoft.swing.JideToggleButton ExBNorthWest;
    public static com.jidesoft.swing.JideToggleButton ExBRight;
    public static com.jidesoft.swing.JideToggleButton ExBSouth;
    public static com.jidesoft.swing.JideToggleButton ExBSouthEast;
    public static com.jidesoft.swing.JideToggleButton ExBSouthWest;
    public static com.jidesoft.swing.JideToggleButton ExBTop;
    public static com.jidesoft.swing.JideToggleButton ExBTopLeft;
    public static com.jidesoft.swing.JideToggleButton ExBTopRight;
    public static com.jidesoft.swing.JideToggleButton ExBWest;
    public static javax.swing.JComboBox ExCBEffect;
    public static javax.swing.JComboBox ExCBSmooth;
    public static javax.swing.JComboBox ExCBSpeed;
    private javax.swing.JPanel PanIPAddress;
    public static javax.swing.JRadioButton RBHostProvider;
    public static javax.swing.JRadioButton RBWebsiteProvider;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private com.jidesoft.swing.JideTabbedPane jideTabbedPane1;
    public static com.jidesoft.field.IPTextField txtIP;
    // End of variables declaration//GEN-END:variables
    AbstractDialogPage page;
}
