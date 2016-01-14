package com.osfac.dmt.tools.search;

import com.jidesoft.swing.CheckBoxList;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class MainCriteria extends javax.swing.JPanel {

    public MainCriteria() {
        initComponents(I18N.DMTResourceBundle);
        fillComboboxes();
    }

    private void fillComboboxes() {
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    WorkbenchFrame.progress.setIndeterminate(true);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Connecting-to-database"));
//                    stat = Config.con.createStatement();
                    int index;
                    ResultSet res;
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-category-images"));
                    PreparedStatement ps = Config.con.prepareStatement("SELECT DISTINCT category_name FROM dmt_category\n"
                            + "ORDER BY category_name ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelCategory = new DefaultComboBoxModel();
                    modelCategory.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelCategory.insertElementAt(res.getString(1), index);
                        index++;
                    }
                    CBCategory.setModel(modelCategory);
                    CBCategory.setSelectedIndex(-1);

                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-path"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT path FROM dmt_pathrow WHERE path_row IN\n"
                            + "(SELECT path_row FROM dmt_concern WHERE id_image IN (SELECT id_image FROM dmt_image WHERE id_category=2)) ORDER BY path ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelPath = new DefaultComboBoxModel();
                    modelPath.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelPath.insertElementAt(res.getString(1), index);
                        index++;
                    }
                    CBPath.setModel(modelPath);
                    CBPath.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-row"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT row FROM dmt_pathrow INNER JOIN dmt_concern "
                            + "ON dmt_pathrow.path_row=dmt_concern.path_row INNER JOIN dmt_image ON "
                            + "dmt_image.id_image=dmt_concern.id_image INNER JOIN dmt_category ON "
                            + "dmt_category.id_category=dmt_image.id_category WHERE category_name = 'Landsat' ORDER BY row ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelRow = new DefaultComboBoxModel();
                    modelRow.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelRow.insertElementAt(res.getString(1), index);
                        index++;
                    }
                    CBRow.setModel(modelRow);
                    CBRow.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-year"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT YEAR(date) FROM dmt_image ORDER BY YEAR(date) ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelYear = new DefaultComboBoxModel();
                    modelYear.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelYear.insertElementAt(res.getString(1), index);
                        index++;
                    }
                    CBYear.setModel(modelYear);
                    CBYear.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-country"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT country_name FROM dmt_country");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelCountry = new DefaultComboBoxModel();
                    modelCountry.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelCountry.insertElementAt(res.getString(1), index);
                        index++;
                    }
                    CBCountry.setModel(modelCountry);
                    CBCountry.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-ortho-rectified"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT ortho FROM dmt_image WHERE ortho <> '' ORDER BY ortho ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelOrtho = new DefaultComboBoxModel();
                    modelOrtho.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelOrtho.insertElementAt(Config.capitalFirstLetter(res.getString(1)), index);
                        index++;
                    }
                    CBOrtho.setModel(modelOrtho);
                    CBOrtho.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-SLC"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT slc FROM dmt_image WHERE slc <> '' ORDER BY slc ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelSLC = new DefaultComboBoxModel();
                    modelSLC.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelSLC.insertElementAt(Config.capitalFirstLetter(res.getString(1)), index);
                        index++;
                    }
                    CBSLC.setModel(modelSLC);
                    CBSLC.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Filling-mission"));
                    ps = Config.con.prepareStatement("SELECT DISTINCT mission FROM dmt_image INNER JOIN "
                            + "dmt_category ON dmt_category.id_category=dmt_image.id_category "
                            + "WHERE category_name = 'Landsat' and mission <> '' ORDER BY mission ASC");
                    res = ps.executeQuery();
                    DefaultComboBoxModel modelmission = new DefaultComboBoxModel();
                    modelmission.insertElementAt(CheckBoxList.ALL, 0);
                    index = 1;
                    while (res.next()) {
                        modelmission.insertElementAt(res.getString(1), index);
                        index++;
                    }
                    CBMission.setModel(modelmission);
                    CBMission.setSelectedIndex(-1);
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("Search.Finalizing"));
                    WorkbenchFrame.progress.setProgress(100);
                } catch (SQLException ex) {
                    JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
                }
            }
        };
        th.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        labCategory = new javax.swing.JLabel();
        CBCategory = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBPath = new com.jidesoft.combobox.CheckBoxListComboBox();
        labPath = new javax.swing.JLabel();
        CBRow = new com.jidesoft.combobox.CheckBoxListComboBox();
        labRow = new javax.swing.JLabel();
        CBYear = new com.jidesoft.combobox.CheckBoxListComboBox();
        labYear = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labCountry = new javax.swing.JLabel();
        CBCountry = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBMission = new com.jidesoft.combobox.CheckBoxListComboBox();
        labMission = new javax.swing.JLabel();
        CBOrtho = new com.jidesoft.combobox.CheckBoxListComboBox();
        labOrtho = new javax.swing.JLabel();
        CBSLC = new com.jidesoft.combobox.CheckBoxListComboBox();
        labelSLC = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        labCategory.setText(bundle.getString("MainCriteria.labCategory.text")); // NOI18N

        CBCategory.setEditable(false);
        CBCategory.setFocusable(false);
        CBCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBCategoryItemStateChanged(evt);
            }
        });

        CBPath.setEditable(false);
        CBPath.setEnabled(false);
        CBPath.setFocusable(false);

        labPath.setText(bundle.getString("MainCriteria.labPath.text")); // NOI18N
        labPath.setEnabled(false);

        CBRow.setEditable(false);
        CBRow.setEnabled(false);
        CBRow.setFocusable(false);

        labRow.setText(bundle.getString("MainCriteria.labRow.text")); // NOI18N
        labRow.setEnabled(false);

        CBYear.setEditable(false);
        CBYear.setEnabled(false);
        CBYear.setFocusable(false);

        labYear.setText(bundle.getString("MainCriteria.labYear.text")); // NOI18N
        labYear.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labCategory)
                    .addComponent(labPath)
                    .addComponent(labRow)
                    .addComponent(labYear))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(CBYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CBPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CBRow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labCategory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBRow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labRow))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labYear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        labCountry.setText(bundle.getString("MainCriteria.labCountry.text")); // NOI18N
        labCountry.setEnabled(false);

        CBCountry.setEditable(false);
        CBCountry.setEnabled(false);
        CBCountry.setFocusable(false);

        CBMission.setEditable(false);
        CBMission.setEnabled(false);
        CBMission.setFocusable(false);

        labMission.setText(bundle.getString("MainCriteria.labMission.text")); // NOI18N
        labMission.setEnabled(false);

        CBOrtho.setEditable(false);
        CBOrtho.setEnabled(false);
        CBOrtho.setFocusable(false);

        labOrtho.setText(bundle.getString("MainCriteria.labOrtho.text")); // NOI18N
        labOrtho.setEnabled(false);

        CBSLC.setEditable(false);
        CBSLC.setEnabled(false);
        CBSLC.setFocusable(false);

        labelSLC.setText(bundle.getString("MainCriteria.labelSLC.text")); // NOI18N
        labelSLC.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labCountry)
                    .addComponent(labOrtho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelSLC)
                    .addComponent(labMission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBSLC, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(CBMission, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(CBOrtho, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(CBCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labCountry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labMission))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBOrtho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labOrtho))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBSLC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSLC))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        jPanel1 = new javax.swing.JPanel();
        labCategory = new javax.swing.JLabel();
        CBCategory = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBPath = new com.jidesoft.combobox.CheckBoxListComboBox();
        labPath = new javax.swing.JLabel();
        CBRow = new com.jidesoft.combobox.CheckBoxListComboBox();
        labRow = new javax.swing.JLabel();
        CBYear = new com.jidesoft.combobox.CheckBoxListComboBox();
        labYear = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labCountry = new javax.swing.JLabel();
        CBCountry = new com.jidesoft.combobox.CheckBoxListComboBox();
        CBMission = new com.jidesoft.combobox.CheckBoxListComboBox();
        labMission = new javax.swing.JLabel();
        CBOrtho = new com.jidesoft.combobox.CheckBoxListComboBox();
        labOrtho = new javax.swing.JLabel();
        CBSLC = new com.jidesoft.combobox.CheckBoxListComboBox();
        labelSLC = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        labCategory.setText(bundle.getString("MainCriteria.labCategory.text")); // NOI18N

        CBCategory.setEditable(false);
        CBCategory.setFocusable(false);
        CBCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBCategoryItemStateChanged(evt);
            }
        });

        CBPath.setEditable(false);
        CBPath.setEnabled(false);
        CBPath.setFocusable(false);

        labPath.setText(bundle.getString("MainCriteria.labPath.text")); // NOI18N
        labPath.setEnabled(false);

        CBRow.setEditable(false);
        CBRow.setEnabled(false);
        CBRow.setFocusable(false);

        labRow.setText(bundle.getString("MainCriteria.labRow.text")); // NOI18N
        labRow.setEnabled(false);

        CBYear.setEditable(false);
        CBYear.setEnabled(false);
        CBYear.setFocusable(false);

        labYear.setText(bundle.getString("MainCriteria.labYear.text")); // NOI18N
        labYear.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labCategory)
                                .addComponent(labPath)
                                .addComponent(labRow)
                                .addComponent(labYear))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(CBCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addComponent(CBYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CBPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CBRow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labCategory))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labPath))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBRow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labRow))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labYear))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        labCountry.setText(bundle.getString("MainCriteria.labCountry.text")); // NOI18N
        labCountry.setEnabled(false);

        CBCountry.setEditable(false);
        CBCountry.setEnabled(false);
        CBCountry.setFocusable(false);

        CBMission.setEditable(false);
        CBMission.setEnabled(false);
        CBMission.setFocusable(false);

        labMission.setText(bundle.getString("MainCriteria.labMission.text")); // NOI18N
        labMission.setEnabled(false);

        CBOrtho.setEditable(false);
        CBOrtho.setEnabled(false);
        CBOrtho.setFocusable(false);

        labOrtho.setText(bundle.getString("MainCriteria.labOrtho.text")); // NOI18N
        labOrtho.setEnabled(false);

        CBSLC.setEditable(false);
        CBSLC.setEnabled(false);
        CBSLC.setFocusable(false);

        labelSLC.setText(bundle.getString("MainCriteria.labelSLC.text")); // NOI18N
        labelSLC.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labCountry)
                                .addComponent(labOrtho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelSLC)
                                .addComponent(labMission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(CBSLC, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addComponent(CBMission, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(CBOrtho, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(CBCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap()));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labCountry))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labMission))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBOrtho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labOrtho))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(CBSLC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelSLC))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
    }// </editor-fold>

    private void CBCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBCategoryItemStateChanged
        boolean beEnabled = CBCategory.getSelectedObjects().length == 1
                && CBCategory.getSelectedObjects()[0].equals("LANDSAT");
        labelSLC.setEnabled(beEnabled);
        labCountry.setEnabled(beEnabled);
        labMission.setEnabled(beEnabled);
        labOrtho.setEnabled(beEnabled);
        labPath.setEnabled(beEnabled);
        labRow.setEnabled(beEnabled);

        CBCountry.setEnabled(beEnabled);
        CBMission.setEnabled(beEnabled);
        CBOrtho.setEnabled(beEnabled);
        CBPath.setEnabled(beEnabled);
        CBRow.setEnabled(beEnabled);
        CBSLC.setEnabled(beEnabled);

        boolean catSelected = CBCategory.getSelectedObjects().length > 0;
        labYear.setEnabled(catSelected);
        CBYear.setEnabled(catSelected);
        categorySelected(catSelected);
    }//GEN-LAST:event_CBCategoryItemStateChanged

    private void categorySelected(boolean catSelected) {
        try {
            if (catSelected) {
                int index;
                ResultSet res = Config.con.createStatement().executeQuery("SELECT DISTINCT YEAR(date)\n"
                        + "FROM dmt_image INNER JOIN dmt_category ON dmt_category.id_category = dmt_image.id_category\n"
                        + "WHERE category_name IN (" + manyCriteria(CBCategory.getSelectedObjects()) + ")\n"
                        + "ORDER BY YEAR(date) ASC");
                DefaultComboBoxModel modelYear = new DefaultComboBoxModel();
                modelYear.insertElementAt(CheckBoxList.ALL, 0);
                index = 1;
                while (res.next()) {
                    modelYear.insertElementAt(res.getString(1), index);
                    index++;
                }
                CBYear.setModel(modelYear);
                CBYear.setSelectedIndex(-1);

//                res = Config.con.createStatement().executeQuery("SELECT DISTINCT path FROM dmt_pathrow "
//                        + "INNER JOIN dmt_concern ON dmt_concern.path_row = dmt_pathrow.path_row INNER JOIN "
//                        + "dmt_image ON dmt_image.id_image = dmt_concern.id_image INNER JOIN dmt_category ON "
//                        + "dmt_category.id_category = dmt_image.id_category WHERE "
//                        + "category_name IN (" + manyCriteria(CBCategory.getSelectedObjects()) + ") "
//                        + "ORDER BY path ASC");
//                DefaultComboBoxModel modelPath = new DefaultComboBoxModel();
//                modelPath.insertElementAt(CheckBoxList.ALL, 0);
//                index = 1;
//                while (res.next()) {
//                    modelPath.insertElementAt(res.getString(1), index);
//                    index++;
//                }
//                CBPath.setModel(modelPath);
//                CBPath.setSelectedIndex(-1);
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private String manyCriteria(Object[] list) {
        String values = "";
        for (int i = 0; i < list.length; i++) {
            values += "\'" + list[i] + "\',";
        }
        return values.substring(0, values.length() - 1);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static com.jidesoft.combobox.CheckBoxListComboBox CBCategory;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBCountry;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBMission;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBOrtho;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBPath;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBRow;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBSLC;
    public static com.jidesoft.combobox.CheckBoxListComboBox CBYear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labCategory;
    private javax.swing.JLabel labCountry;
    private javax.swing.JLabel labMission;
    private javax.swing.JLabel labOrtho;
    private javax.swing.JLabel labPath;
    private javax.swing.JLabel labRow;
    private javax.swing.JLabel labYear;
    private javax.swing.JLabel labelSLC;
    // End of variables declaration//GEN-END:variables
}
