package com.osfac.dmt.form;

import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PaintPanel;
import com.jidesoft.utils.PortingUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.mail.MailMessage;
import com.osfac.dmt.mail.MailSender;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class ReviewDataRequestForm extends javax.swing.JDialog {

    public ReviewDataRequestForm(java.awt.Frame parent, boolean modal, int idDelivery) {
        super(parent, modal);
        this.idDelivery = idDelivery;
        this.con = Config.con;
        initComponents(I18N.DMTResourceBundle);
        createTable();
        getRequestInfo(idDelivery);
        this.setLocationRelativeTo(parent);
    }

    private void getRequestInfo(int idDelivery) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM dmt_requester INNER JOIN dmt_delivery ON "
                    + "dmt_delivery.id_requester = dmt_requester.id_requester WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                labRequester.setText(": " + res.getString("firstname") + " "
                        + res.getString("familyname").toUpperCase() + " "
                        + res.getString("othername").toUpperCase());
                firstname = res.getString("firstname");
                familyname = res.getString("familyname");
                othername = res.getString("othername");
                sex = res.getString("sex");
                labProfession.setText(": " + res.getString("profession"));
                labAdress.setText(": " + res.getString("adress"));
                labEmail.setText(": " + res.getString("email"));
                labPhone.setText(": " + res.getString("phone"));
                labInstitution.setText(": " + res.getString("institution"));
                labNationality.setText(": " + res.getString("nationality"));

                size = res.getString("image_size");
                filepath = res.getString("form_path");
                txtArea.setText(res.getString("interest_area"));
                txtDescribe.setText(res.getString("usefulness"));
                txtAboutOSFAC.setText(res.getString("comment"));
                pathrow = res.getString("pathrow");
                labDate.setText(": " + Config.dateFormat.format(res.getDate("request_date")));
            }
            txtApplication.setText(getUsageInfo(idDelivery));
            txtCategory.setText(getCategoriesInfo(idDelivery));
            txtVolume.setText(getNumber(idDelivery) + " image(s)" + "\n" + size);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
            this.dispose();
        }
    }

    private int getNumber(int idDelivery) {
        int number = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(id_image) FROM dmt_deliver WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                number = res.getInt(1);
                return number;
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return number;
    }

    private String getCategoriesInfo(int idDelivery) {
        String category = "";
        try {
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT category_name FROM dmt_category INNER JOIN dmt_image ON "
                    + "dmt_image.id_category = dmt_category.id_category INNER JOIN dmt_deliver ON "
                    + "dmt_image.id_image = dmt_deliver.id_image WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                category += res.getString(1) + "\n";
            }
            return category;
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return category;
    }

    private String getUsageInfo(int idDelivery) {
        String usageForm = "";
        try {
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT usage_name FROM dmt_usage INNER JOIN dmt_choose ON "
                    + "dmt_choose.id_usage = dmt_usage.id_usage WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                usageForm += res.getString(1) + "\n";
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return usageForm;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labRequester = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labProfession = new javax.swing.JLabel();
        labAdress = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labPhone = new javax.swing.JLabel();
        labInstitution = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labEmail = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        labNationality = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescribe = new javax.swing.JTextPane();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextPane();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAboutOSFAC = new javax.swing.JTextPane();
        jLabel36 = new javax.swing.JLabel();
        labDate = new javax.swing.JLabel();
        BCancel = new com.jidesoft.swing.JideButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtVolume = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCategory = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtApplication = new javax.swing.JTextPane();
        BValidate = new com.jidesoft.swing.JideButton();
        BEdit = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        setTitle(bundle.getString("ReviewDataRequestForm.title")); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText(bundle.getString("ReviewDataRequestForm.jLabel1.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText(bundle.getString("ReviewDataRequestForm.jLabel3.text")); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText(bundle.getString("ReviewDataRequestForm.jLabel6.text")); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText(bundle.getString("ReviewDataRequestForm.jLabel7.text")); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText(bundle.getString("ReviewDataRequestForm.jLabel10.text")); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText(bundle.getString("ReviewDataRequestForm.jLabel11.text")); // NOI18N

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel25.setText(bundle.getString("ReviewDataRequestForm.jLabel25.text")); // NOI18N

        jLabel27.setText(bundle.getString("ReviewDataRequestForm.jLabel27.text")); // NOI18N

        jLabel28.setText(bundle.getString("ReviewDataRequestForm.jLabel28.text")); // NOI18N

        jLabel29.setText(bundle.getString("ReviewDataRequestForm.jLabel29.text")); // NOI18N

        jLabel33.setBackground(new java.awt.Color(241, 235, 235));
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setText(bundle.getString("ReviewDataRequestForm.jLabel33.text")); // NOI18N
        jLabel33.setOpaque(true);

        txtDescribe.setEditable(false);
        jScrollPane1.setViewportView(txtDescribe);

        jLabel34.setBackground(new java.awt.Color(241, 235, 235));
        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel34.setText(bundle.getString("ReviewDataRequestForm.jLabel34.text")); // NOI18N
        jLabel34.setOpaque(true);

        txtArea.setEditable(false);
        jScrollPane2.setViewportView(txtArea);

        jLabel35.setBackground(new java.awt.Color(241, 235, 235));
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel35.setText(bundle.getString("ReviewDataRequestForm.jLabel35.text")); // NOI18N
        jLabel35.setOpaque(true);

        txtAboutOSFAC.setEditable(false);
        jScrollPane3.setViewportView(txtAboutOSFAC);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel36.setText(bundle.getString("ReviewDataRequestForm.jLabel36.text")); // NOI18N

        labDate.setText(bundle.getString("ReviewDataRequestForm.labDate.text")); // NOI18N

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("ReviewDataRequestForm.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        txtVolume.setEditable(false);
        jScrollPane4.setViewportView(txtVolume);

        txtCategory.setEditable(false);
        jScrollPane5.setViewportView(txtCategory);

        txtApplication.setEditable(false);
        jScrollPane6.setViewportView(txtApplication);

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"))); // NOI18N
        BValidate.setText(bundle.getString("ReviewDataRequestForm.BValidate.text")); // NOI18N
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        BEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download(5).png"))); // NOI18N
        BEdit.setText(bundle.getString("ReviewDataRequestForm.BEdit.text")); // NOI18N
        BEdit.setFocusable(false);
        BEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(133, 133, 133))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(labInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(labNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labDate, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labRequester, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labProfession, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(130, 130, 130))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labRequester, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labProfession, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(labEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(labInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(labNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel36)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDate)
                    .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labRequester = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labProfession = new javax.swing.JLabel();
        labAdress = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labPhone = new javax.swing.JLabel();
        labInstitution = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labEmail = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        labNationality = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescribe = new javax.swing.JTextPane();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextPane();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAboutOSFAC = new javax.swing.JTextPane();
        jLabel36 = new javax.swing.JLabel();
        labDate = new javax.swing.JLabel();
        BCancel = new com.jidesoft.swing.JideButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtVolume = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCategory = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtApplication = new javax.swing.JTextPane();
        BValidate = new com.jidesoft.swing.JideButton();
        BEdit = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("ReviewDataRequestForm.title")); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText(bundle.getString("ReviewDataRequestForm.jLabel1.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText(bundle.getString("ReviewDataRequestForm.jLabel3.text")); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText(bundle.getString("ReviewDataRequestForm.jLabel6.text")); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText(bundle.getString("ReviewDataRequestForm.jLabel7.text")); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText(bundle.getString("ReviewDataRequestForm.jLabel10.text")); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText(bundle.getString("ReviewDataRequestForm.jLabel11.text")); // NOI18N

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel25.setText(bundle.getString("ReviewDataRequestForm.jLabel25.text")); // NOI18N

        jLabel27.setText(bundle.getString("ReviewDataRequestForm.jLabel27.text")); // NOI18N

        jLabel28.setText(bundle.getString("ReviewDataRequestForm.jLabel28.text")); // NOI18N

        jLabel29.setText(bundle.getString("ReviewDataRequestForm.jLabel29.text")); // NOI18N

        jLabel33.setBackground(new java.awt.Color(241, 235, 235));
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setText(bundle.getString("ReviewDataRequestForm.jLabel33.text")); // NOI18N
        jLabel33.setOpaque(true);

        txtDescribe.setEditable(false);
        jScrollPane1.setViewportView(txtDescribe);

        jLabel34.setBackground(new java.awt.Color(241, 235, 235));
        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel34.setText(bundle.getString("ReviewDataRequestForm.jLabel34.text")); // NOI18N
        jLabel34.setOpaque(true);

        txtArea.setEditable(false);
        jScrollPane2.setViewportView(txtArea);

        jLabel35.setBackground(new java.awt.Color(241, 235, 235));
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel35.setText(bundle.getString("ReviewDataRequestForm.jLabel35.text")); // NOI18N
        jLabel35.setOpaque(true);

        txtAboutOSFAC.setEditable(false);
        jScrollPane3.setViewportView(txtAboutOSFAC);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel36.setText(bundle.getString("ReviewDataRequestForm.jLabel36.text")); // NOI18N

        labDate.setText(bundle.getString("ReviewDataRequestForm.labDate.text")); // NOI18N

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("ReviewDataRequestForm.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        txtVolume.setEditable(false);
        jScrollPane4.setViewportView(txtVolume);

        txtCategory.setEditable(false);
        jScrollPane5.setViewportView(txtCategory);

        txtApplication.setEditable(false);
        jScrollPane6.setViewportView(txtApplication);

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply16.png"))); // NOI18N
        BValidate.setText(bundle.getString("ReviewDataRequestForm.BValidate.text")); // NOI18N
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        BEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/download(5).png"))); // NOI18N
        BEdit.setText(bundle.getString("ReviewDataRequestForm.BEdit.text")); // NOI18N
        BEdit.setFocusable(false);
        BEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(133, 133, 133))
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel3)
                                                                .addComponent(jLabel7)
                                                                .addComponent(jLabel6)
                                                                .addComponent(jLabel1)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(jLabel10)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(labInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(jLabel11)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(jLabel25)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(labNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(labDate, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(BEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(11, 11, 11)
                                                        .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(11, 11, 11)
                                                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(labRequester, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labProfession, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(130, 130, 130))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(labRequester, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(labProfession, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(labAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(labEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(labPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(labInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel25)
                                .addComponent(labNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel36)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labDate)
                                .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_BCancelActionPerformed

    private void BValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValidateActionPerformed
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    if (Config.isLiteVersion()) {
                        new Thread() {
                            @Override
                            public void run() {
                                Connection conN = WorkbenchFrame.remoteDBConnecting();
                                if (conN != null) {
                                    int idRequester = insertRequester(conN);
                                    if (idRequester != -1) {
                                        int idDelivery = insertDelivery(idRequester, conN);
                                    }
                                }
                            }
                        }.start();
                    }
                    WorkbenchFrame.progress.setProgressStatus(I18N.get("ReviewDataRequestForm.Sending-Email-to") + " dmt@osfac.net ......");
                    WorkbenchFrame.progress.setIndeterminate(true);
                    sendEmail(labRequester.getText().substring(2), idDelivery);
                } catch (Exception e) {
                    JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                            e.getMessage(), null, null, e, Level.SEVERE, null));
                }
            }
        };
        th.start();
        this.dispose();
    }//GEN-LAST:event_BValidateActionPerformed

    private void BEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEditActionPerformed
        this.dispose();
        new DataRequestFormedit(DMTWorkbench.frame, true, idDelivery).setVisible(true);
    }//GEN-LAST:event_BEditActionPerformed

    private int insertRequester(Connection con) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO dmt_requester VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setString(2, Config.capitalFirstLetter(firstname));
            ps.setString(3, Config.capitalFirstLetter(familyname));
            ps.setString(4, Config.capitalFirstLetter(othername));
            ps.setString(5, sex);
            ps.setString(6, Config.capitalFirstLetter(labAdress.getText().substring(2)));
            ps.setString(7, labPhone.getText().substring(2));
            ps.setString(8, labEmail.getText().substring(2).toLowerCase());
            ps.setString(9, Config.capitalFirstLetter(labProfession.getText().substring(2)));
            ps.setString(10, Config.capitalFirstLetter(labInstitution.getText().substring(2)));
            ps.setString(11, Config.capitalFirstLetter(labNationality.getText().substring(2)));
            ps.setString(12, Config.capitalFirstLetter(txtArea.getText()));
            ps.setString(13, Config.capitalFirstLetter(txtDescribe.getText()));
            ps.setString(14, Config.capitalFirstLetter(txtAboutOSFAC.getText()));
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    return res.getInt(1);
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return -1;
    }

    private int insertDelivery(int idRequester, Connection con) {
        int IDDelivery = -1;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO dmt_delivery VALUES (?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setInt(2, idRequester);
            ps.setInt(3, 4);
            ps.setString(4, size);
            ps.setString(5, Config.dateFormatDB.format(new Date()));
            ps.setString(6, pathrow);
            ps.setString(7, "No");
            ps.setString(8, "No");
            ps.setString(9, filepath);
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    IDDelivery = res.getInt(1);
                    ArrayList<Integer> list = getUsageIDs();
                    for (int i = 0; i < list.size(); i++) {
                        ps = con.prepareStatement("INSERT INTO dmt_choose VALUES (?,?)");
                        ps.setInt(1, IDDelivery);
                        ps.setInt(2, list.get(i));
                        int result2 = ps.executeUpdate();
                    }
                    ArrayList<Integer> vID = getIDImages();
                    for (int i = 0; i < vID.size(); i++) {
                        ps = con.prepareStatement("INSERT INTO dmt_deliver VALUES (?,?)");
                        ps.setInt(1, vID.get(i));
                        ps.setInt(2, IDDelivery);
                        int result3 = ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return IDDelivery;
    }

    private ArrayList<Integer> getIDImages() {
        ArrayList<Integer> list = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_image FROM dmt_deliver\n"
                    + "WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getInt(1));
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return list;
    }

    private ArrayList getUsageIDs() {
        ArrayList list = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_usage FROM dmt_choose\n"
                    + "WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getInt(1));
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return list;
    }

    private void sendEmail(String names, int idDelivery) {
        try {
            MailSender mail = new MailSender("smtp.gmail.com", 465, "dmt@osfac.net", "osfaclab01", true);
            MailMessage msg = new MailMessage();
            //Source de message
            msg.setFrom(new InternetAddress("dmt@osfac.net", "OSFAC-DMT"));
            //Destination
            msg.setTo("dmt@osfac.net");
            //Sujet de message
            msg.setSubject(I18N.get("ReviewDataRequestForm.Email-subject-to-dmt"));
            //Contenu de message
            msg.setContent(I18N.get("ReviewDataRequestForm.Email-content-to-dmt-part1") + names
                    + I18N.get("ReviewDataRequestForm.Email-content-to-dmt-part2"), true);
            //Piece jointe s'il y a lieu
//            msg.setAttachmentURL(filePath);
            //Envoyer le message
            mail.sendMessage(msg);
            WorkbenchFrame.progress.setProgress(100);
            WorkbenchFrame.progress.setProgressStatus(I18N.get("ReviewDataRequestForm.Sending-confirmation-Email-to")
                    + " " + labEmail.getText().substring(2) + " ......");
            WorkbenchFrame.progress.setIndeterminate(true);
            //send email to the requester : Source de message
            msg.setFrom(new InternetAddress("dmt@osfac.net", "OSFAC-DMT"));
            //Destination
            msg.setTo(labEmail.getText().substring(2));
            //Sujet de message
            msg.setSubject(I18N.get("ReviewDataRequestForm.Email-subject-to-requester"));
            String emailContent;
            if (getCategories(idDelivery).contains("ASTER") || getCategories(idDelivery).contains("SPOT")) {
                emailContent = "Mr/Ms <b>" + names + I18N.get("ReviewDataRequestForm.Email-content-to-other");
            } else {
                emailContent = "Mr/Ms <b>" + names + I18N.get("ReviewDataRequestForm.Email-content-to-other-aster-spot");
            }
            //Contenu de message
            msg.setContent(emailContent, true);
            //Envoyer le message
            mail.sendMessage(msg);
            confirmSentEmail(idDelivery);
            WorkbenchFrame.progress.setProgress(100);
            createAlert();
        } catch (UnsupportedEncodingException | MessagingException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
//            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), "Internet connection has not been found in this computer.\n"
//                    + "Please verify your internet connection and try again later ....", null, null, ex, Level.SEVERE, null));
            WorkbenchFrame.progress.setProgress(100);
        }
    }

    private void createAlert() {
        alert2 = new Alert();
        alert2.setAlwaysOnTop(true);
        alert2.getContentPane().setLayout(new BorderLayout());
        hideAnimation2 = new CustomAnimation(CustomAnimation.TYPE_EXIT, Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        hideAnimation2.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideAnimation2.setDirection(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        alert2.setHideAnimation(hideAnimation2);
        alert2.getContentPane().add(createSampleAlert(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alert2.hidePopup();
            }
        }));
        alert2.setResizable(true);
        alert2.setMovable(true);
        alert2.setTimeout(4000);
        alert2.setTransient(false);
        alert2.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));
        CustomAnimation showAnimation = new CustomAnimation(CustomAnimation.TYPE_ENTRANCE, Config.pref.getInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_MEDIUM));
        showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        showAnimation.setDirection(Config.pref.getInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP_RIGHT));
        alert2.setShowAnimation(showAnimation);
        CustomAnimation hideAnim = new CustomAnimation(CustomAnimation.TYPE_EXIT, Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        hideAnim.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideAnim.setDirection(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        alert2.setHideAnimation(hideAnim);
        _alertGroup2.add(alert2);
        alert2.showPopup(Config.pref.getInt(SettingKeyFactory.General.entranceLocation, SwingConstants.CENTER), DMTWorkbench.frame);
    }

    private JComponent createSampleAlert(ActionListener closeAction) {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        bottomPanel.add(createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/flag.png"))));
        JideButton deleteButton = createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/delete.png")));
        deleteButton.addActionListener(closeAction);
        bottomPanel.add(deleteButton);

        JPanel leftPanel = new JPanel(new BorderLayout(6, 6));
        leftPanel.add(new JLabel(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/mail2.png"))));
        leftPanel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);

        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        rightPanel.add(createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/option.png"))));
        JideButton closeButton = createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/close.png")));
        closeButton.addActionListener(closeAction);
        rightPanel.add(closeButton);
        final String text = I18N.get("ReviewDataRequestForm.Confirmation-message-Email-Sent");
        final String highlightText = I18N.get("ReviewDataRequestForm.Confirmation-message-Email-Sent-highlight");
        final JLabel message = new JLabel(text);
        message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                message.setText(highlightText);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                message.setText(text);
            }
        });
        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
        panel.add(message, BorderLayout.CENTER);
        JPanel topPanel = JideSwingUtilities.createTopPanel(rightPanel);
        panel.add(topPanel, BorderLayout.AFTER_LINE_ENDS);
        panel.add(leftPanel, BorderLayout.BEFORE_LINE_BEGINS);
        for (int i = 0; i < panel.getComponentCount(); i++) {
            JideSwingUtilities.setOpaqueRecursively(panel.getComponent(i), false);
        }
        panel.setOpaque(true);
        panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(231, 229, 224), 0, panel.getPreferredSize().height, new Color(212, 208, 200)));
        return panel;
    }

    private JideButton createButton(Icon icon) {
        return new JideButton(icon);
    }

    private void confirmSentEmail(int idDelivery) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE dmt_delivery SET confirm_email_sent = ? "
                    + "WHERE id_delivery = ?");
            ps.setString(1, "Yes");
            ps.setInt(2, idDelivery);
            int result = ps.executeUpdate();
            WorkbenchFrame.timer.start();
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private String getCategories(int idDelivery) {
        String category = "";
        try {
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT category_name FROM dmt_category\n"
                    + "INNER JOIN dmt_image ON\n"
                    + "dmt_category.id_category = dmt_image.id_category\n"
                    + "INNER JOIN dmt_deliver ON\n"
                    + "dmt_image.id_image = dmt_deliver.id_image WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                category += res.getString(1) + ",";
            }
            category = category.substring(0, category.length() - 1);
            return category;
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return category;
    }

    private void createTable() {
        tableModel = new MyTableModel();
        table = new SortableTable(tableModel);
        table.setSortable(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnResizable(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMinWidth(70);
        table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMaxWidth(70);
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM dmt_image WHERE id_image IN "
                    + "(SELECT id_image FROM dmt_deliver WHERE id_delivery=?)");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                if (table.getRowCount() >= i) {
                    tableModel.addNewRow();
                }
                table.setValueAt(res.getString("id_image"), i, 0);
                table.setValueAt(res.getString("image_name"), i, 1);
                table.setValueAt(res.getDate("date"), i, 2);
                table.setValueAt(res.getDouble("size"), i, 3);
                i++;
            }
            TableUtils.autoResizeAllColumns(table);
            TableUtils.autoResizeAllRows(table);
            Config.centerTableHeadAndBold(table);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    public class MyTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES = {I18N.get("Text.ID"), I18N.get("Text.IMAGES"), I18N.get("Text.DATE"), I18N.get("Text.SIZE-IN-MO")};
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
                if (i == 0) {
                    DATA[i].add(false);
                } else {
                    DATA[i].add("");
                }
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
    private com.jidesoft.swing.JideButton BCancel;
    private com.jidesoft.swing.JideButton BEdit;
    private com.jidesoft.swing.JideButton BValidate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel labAdress;
    private javax.swing.JLabel labDate;
    private javax.swing.JLabel labEmail;
    private javax.swing.JLabel labInstitution;
    private javax.swing.JLabel labNationality;
    private javax.swing.JLabel labPhone;
    private javax.swing.JLabel labProfession;
    private javax.swing.JLabel labRequester;
    private javax.swing.JTextPane txtAboutOSFAC;
    private javax.swing.JTextPane txtApplication;
    private javax.swing.JTextPane txtArea;
    private javax.swing.JTextPane txtCategory;
    private javax.swing.JTextPane txtDescribe;
    private javax.swing.JTextPane txtVolume;
    // End of variables declaration//GEN-END:variables
    int idDelivery;
    private SortableTable table;
    private MyTableModel tableModel;
    private Alert alert2;
    private String sex, firstname, familyname, othername, size, filepath, pathrow;
    private AlertGroup _alertGroup2 = new AlertGroup();
    private CustomAnimation hideAnimation2;
    private Connection con = null;
}
