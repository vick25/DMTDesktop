package com.osfac.dmt.form;

import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.grid.AbstractTableCellEditorRenderer;
import com.jidesoft.grid.CellRolloverSupport;
import com.jidesoft.grid.CellSpanTable;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.RolloverTableUtils;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.hints.ListDataIntelliHints;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PaintPanel;
import com.jidesoft.utils.PortingUtils;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.download.DownloadData;
import com.osfac.dmt.mail.MailMessage;
import com.osfac.dmt.mail.MailSender;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.tools.geosearch.GeoResult;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class DataRequestForm extends javax.swing.JDialog {

    public DataRequestForm(java.awt.Frame parent, boolean modal, ArrayList vID,
            String size, ArrayList vPathRow) {
        this(parent, modal, vID, size, vPathRow, null);
        BBack3.setVisible(false);
    }

    public DataRequestForm(java.awt.Frame parent, boolean modal, ArrayList vID,
            String size, ArrayList vPathRow, GeoResult geoResult) {
        super(parent, modal);
        this.parent = (JFrame) parent;
        this.vID = vID;
        this.vPathRow = vPathRow;
        this.geoResult = geoResult;
        createTable(vID);
        initComponents(I18N.DMTResourceBundle);
        initialize(size);
        this.setIconImage(new ImageIcon(getClass().getResource(""
                + "/com/osfac/dmt/images/user-info(3).png")).getImage());
        txtFirstName.requestFocus();
        CBNationality.setModel(new DefaultComboBoxModel(Config.getAllCountry()));
        CBNationality.setSelectedIndex(57);
        autoCompleteTxt();
        checkCategories();
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtFirstName.getText().equals("") && !txtFamilyName.getText().equals("") && checkOutUsage()
                        && !txtAdress.getText().equals("") && !CBProfession.getSelectedItem().toString().equals("")
                        && !CBInstitution.getSelectedItem().toString().equals("") && !CBNationality.getSelectedItem().toString().equals("")
                        && !txtNote.getText().equals("") && !txtInterstedArea.getText().equals("") && !txtComment.getText().equals("")) {
                    BSave.setEnabled(true);
                } else {
                    BSave.setEnabled(false);
                }
                if (!txtFirstName.getText().equals("") && !txtFamilyName.getText().equals("")) {
                    BSearch.setEnabled(true);
                } else {
                    BSearch.setEnabled(false);
                }
            }
        });
        timer.start();
        this.setLocationRelativeTo(parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BGSex = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panOnglet = new com.jidesoft.swing.JideTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        jLabel4 = new javax.swing.JLabel();
        labelnom = new javax.swing.JLabel();
        txtOtherName = new javax.swing.JTextField();
        RBFemale = new javax.swing.JRadioButton();
        txtFirstName = new javax.swing.JTextField();
        RBMale = new javax.swing.JRadioButton();
        labelPrenom = new javax.swing.JLabel();
        txtFamilyName = new javax.swing.JTextField();
        labeladresse = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAdress = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        BSearch = new com.jidesoft.swing.JideButton();
        jXPanel2 = new org.jdesktop.swingx.JXPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEMail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        labelprofession = new javax.swing.JLabel();
        labInstitut = new javax.swing.JLabel();
        labNationalite = new javax.swing.JLabel();
        CBNationality = new com.jidesoft.combobox.ListComboBox();
        CBProfession = new com.jidesoft.swing.AutoCompletionComboBox();
        CBInstitution = new com.jidesoft.swing.AutoCompletionComboBox();
        BNext1 = new com.jidesoft.swing.JideButton();
        BBack3 = new com.jidesoft.swing.JideButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        labelusages = new javax.swing.JLabel();
        ChBLandsat = new javax.swing.JCheckBox();
        ChBASTER = new javax.swing.JCheckBox();
        ChBSRTM = new javax.swing.JCheckBox();
        labelNImage = new javax.swing.JLabel();
        labelVImage = new javax.swing.JLabel();
        ChBOthers = new javax.swing.JCheckBox();
        jScrollPane5 = new javax.swing.JScrollPane(table);
        txtSize = new javax.swing.JTextField();
        txtNumber = new javax.swing.JTextField();
        ChBOtherUsage = new javax.swing.JCheckBox();
        ChBProfessionel = new javax.swing.JCheckBox();
        ChBAcademique = new javax.swing.JCheckBox();
        BBack1 = new com.jidesoft.swing.JideButton();
        BNext2 = new com.jidesoft.swing.JideButton();
        jPanel4 = new javax.swing.JPanel();
        labelnotes = new javax.swing.JLabel();
        labelzi = new javax.swing.JLabel();
        labeldistribution = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtInterstedArea = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtComment = new javax.swing.JTextPane();
        BCancel = new com.jidesoft.swing.JideButton();
        BSave = new com.jidesoft.swing.JideButton();
        BBack2 = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        setTitle(bundle.getString("DataRequestForm.title")); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText(bundle.getString("DataRequestForm.jLabel2.text")); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/ident.png"))); // NOI18N

        panOnglet.setBoldActiveTab(true);
        panOnglet.setColorTheme(3);
        panOnglet.setFocusable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jXPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jXPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DataRequestForm.jXPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        jLabel4.setText(bundle.getString("DataRequestForm.jLabel4.text")); // NOI18N

        labelnom.setText(bundle.getString("DataRequestForm.labelnom.text")); // NOI18N

        BGSex.add(RBFemale);
        RBFemale.setText(bundle.getString("DataRequestForm.RBFemale.text")); // NOI18N
        RBFemale.setFocusable(false);
        RBFemale.setOpaque(false);

        BGSex.add(RBMale);
        RBMale.setSelected(true);
        RBMale.setText(bundle.getString("DataRequestForm.RBMale.text")); // NOI18N
        RBMale.setFocusable(false);
        RBMale.setOpaque(false);

        labelPrenom.setText(bundle.getString("DataRequestForm.labelPrenom.text")); // NOI18N

        labeladresse.setBackground(new java.awt.Color(241, 235, 235));
        labeladresse.setText(bundle.getString("DataRequestForm.labeladresse.text")); // NOI18N
        labeladresse.setOpaque(true);

        jScrollPane1.setViewportView(txtAdress);

        jLabel5.setText(bundle.getString("DataRequestForm.jLabel5.text")); // NOI18N

        BSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/document_preview.png"))); // NOI18N
        BSearch.setEnabled(false);
        BSearch.setFocusable(false);
        BSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jXPanel1Layout = new javax.swing.GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jXPanel1Layout.createSequentialGroup()
                        .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelPrenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelnom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtOtherName)
                            .addComponent(txtFamilyName)
                            .addComponent(txtFirstName)
                            .addGroup(jXPanel1Layout.createSequentialGroup()
                                .addComponent(RBMale, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(RBFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 126, Short.MAX_VALUE))))
                    .addComponent(labeladresse)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jXPanel1Layout.setVerticalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelnom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFamilyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(txtOtherName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(RBFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RBMale, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(labeladresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jXPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DataRequestForm.jXPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        jLabel8.setText(bundle.getString("DataRequestForm.jLabel8.text")); // NOI18N

        jLabel7.setText(bundle.getString("DataRequestForm.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jXPanel2Layout = new javax.swing.GroupLayout(jXPanel2);
        jXPanel2.setLayout(jXPanel2Layout);
        jXPanel2Layout.setHorizontalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEMail, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jXPanel2Layout.setVerticalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jXPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DataRequestForm.jPanel5.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        labelprofession.setText(bundle.getString("DataRequestForm.labelprofession.text")); // NOI18N

        labInstitut.setText(bundle.getString("DataRequestForm.labInstitut.text")); // NOI18N

        labNationalite.setText(bundle.getString("DataRequestForm.labNationalite.text")); // NOI18N

        CBNationality.setEditable(false);
        CBNationality.setFocusable(false);

        CBProfession.setStrict(false);

        CBInstitution.setStrict(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labInstitut, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labNationalite, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(labelprofession))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBInstitution, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CBProfession, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBProfession, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelprofession, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CBInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labInstitut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labNationalite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBNationality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {CBInstitution, CBNationality, CBProfession});

        BNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_next.png"))); // NOI18N
        BNext1.setText(bundle.getString("DataRequestForm.BNext1.text")); // NOI18N
        BNext1.setFocusable(false);
        BNext1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BNext1ActionPerformed(evt);
            }
        });

        BBack3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_previous.png"))); // NOI18N
        BBack3.setText(bundle.getString("DataRequestForm.BBack3.text")); // NOI18N
        BBack3.setFocusable(false);
        BBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBack3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jXPanel1, 457, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(BBack3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel5, jXPanel1, jXPanel2});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BNext1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BBack3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        panOnglet.addTab(bundle.getString("DataRequestForm.jPanel2.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/s_lang.png")), jPanel2); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText(bundle.getString("DataRequestForm.jLabel12.text")); // NOI18N

        labelusages.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelusages.setText(bundle.getString("DataRequestForm.labelusages.text")); // NOI18N

        ChBLandsat.setText(bundle.getString("DataRequestForm.ChBLandsat.text")); // NOI18N
        ChBLandsat.setEnabled(false);
        ChBLandsat.setFocusable(false);
        ChBLandsat.setOpaque(false);

        ChBASTER.setText(bundle.getString("DataRequestForm.ChBASTER.text")); // NOI18N
        ChBASTER.setEnabled(false);
        ChBASTER.setFocusable(false);
        ChBASTER.setOpaque(false);

        ChBSRTM.setText(bundle.getString("DataRequestForm.ChBSRTM.text")); // NOI18N
        ChBSRTM.setEnabled(false);
        ChBSRTM.setFocusable(false);
        ChBSRTM.setOpaque(false);

        labelNImage.setText(bundle.getString("DataRequestForm.labelNImage.text")); // NOI18N

        labelVImage.setText(bundle.getString("DataRequestForm.labelVImage.text")); // NOI18N

        ChBOthers.setText(bundle.getString("DataRequestForm.ChBOthers.text")); // NOI18N
        ChBOthers.setEnabled(false);
        ChBOthers.setFocusable(false);
        ChBOthers.setOpaque(false);

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));

        txtSize.setEditable(false);
        txtSize.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        txtNumber.setEditable(false);
        txtNumber.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        ChBOtherUsage.setText(bundle.getString("DataRequestForm.ChBOtherUsage.text")); // NOI18N
        ChBOtherUsage.setFocusable(false);
        ChBOtherUsage.setOpaque(false);

        ChBProfessionel.setText(bundle.getString("DataRequestForm.ChBProfessionel.text")); // NOI18N
        ChBProfessionel.setFocusable(false);
        ChBProfessionel.setOpaque(false);

        ChBAcademique.setText(bundle.getString("DataRequestForm.ChBAcademique.text")); // NOI18N
        ChBAcademique.setFocusable(false);
        ChBAcademique.setOpaque(false);

        BBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_previous.png"))); // NOI18N
        BBack1.setText(bundle.getString("DataRequestForm.BBack1.text")); // NOI18N
        BBack1.setFocusable(false);
        BBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBack1ActionPerformed(evt);
            }
        });

        BNext2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_next.png"))); // NOI18N
        BNext2.setText(bundle.getString("DataRequestForm.BNext2.text")); // NOI18N
        BNext2.setFocusable(false);
        BNext2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BNext2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ChBLandsat)
                            .addComponent(ChBSRTM)
                            .addComponent(ChBASTER)
                            .addComponent(ChBOthers))
                        .addGap(76, 76, 76)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ChBAcademique)
                            .addComponent(ChBProfessionel)
                            .addComponent(ChBOtherUsage)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(BBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BNext2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(labelNImage, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelVImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(261, 261, 261)
                        .addComponent(labelusages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelusages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(ChBAcademique)
                                .addGap(23, 23, 23))
                            .addComponent(ChBProfessionel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChBOtherUsage))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ChBLandsat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChBSRTM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChBASTER)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChBOthers)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelNImage)
                    .addComponent(labelVImage)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BBack1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BNext2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panOnglet.addTab(bundle.getString("DataRequestForm.jPanel3.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/preview.png")), jPanel3); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        labelnotes.setBackground(new java.awt.Color(241, 235, 235));
        labelnotes.setText(bundle.getString("DataRequestForm.labelnotes.text")); // NOI18N
        labelnotes.setOpaque(true);

        labelzi.setBackground(new java.awt.Color(241, 235, 235));
        labelzi.setText(bundle.getString("DataRequestForm.labelzi.text")); // NOI18N
        labelzi.setOpaque(true);

        labeldistribution.setBackground(new java.awt.Color(241, 235, 235));
        labeldistribution.setText(bundle.getString("DataRequestForm.labeldistribution.text")); // NOI18N
        labeldistribution.setOpaque(true);

        txtNote.setVerifyInputWhenFocusTarget(false);
        jScrollPane2.setViewportView(txtNote);

        txtInterstedArea.setVerifyInputWhenFocusTarget(false);
        jScrollPane3.setViewportView(txtInterstedArea);

        txtComment.setVerifyInputWhenFocusTarget(false);
        jScrollPane4.setViewportView(txtComment);

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("DataRequestForm.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png"))); // NOI18N
        BSave.setText(bundle.getString("DataRequestForm.BSave.text")); // NOI18N
        BSave.setFocusable(false);
        BSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSaveActionPerformed(evt);
            }
        });

        BBack2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_previous.png"))); // NOI18N
        BBack2.setText(bundle.getString("DataRequestForm.BBack2.text")); // NOI18N
        BBack2.setFocusable(false);
        BBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBack2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(BBack2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelnotes, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labeldistribution, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelzi, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BBack2, BCancel, BSave});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(labelnotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelzi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labeldistribution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BBack2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panOnglet.addTab(bundle.getString("DataRequestForm.jPanel4.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/pyramid2.png")), jPanel4); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1))
                    .addComponent(panOnglet, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(panOnglet, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        BGSex = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panOnglet = new com.jidesoft.swing.JideTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        jLabel4 = new javax.swing.JLabel();
        labelnom = new javax.swing.JLabel();
        txtOtherName = new javax.swing.JTextField();
        RBFemale = new javax.swing.JRadioButton();
        txtFirstName = new javax.swing.JTextField();
        RBMale = new javax.swing.JRadioButton();
        labelPrenom = new javax.swing.JLabel();
        txtFamilyName = new javax.swing.JTextField();
        labeladresse = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAdress = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        BSearch = new com.jidesoft.swing.JideButton();
        jXPanel2 = new org.jdesktop.swingx.JXPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEMail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        labelprofession = new javax.swing.JLabel();
        labInstitut = new javax.swing.JLabel();
        labNationalite = new javax.swing.JLabel();
        CBNationality = new com.jidesoft.combobox.ListComboBox();
        CBProfession = new com.jidesoft.swing.AutoCompletionComboBox();
        CBInstitution = new com.jidesoft.swing.AutoCompletionComboBox();
        BNext1 = new com.jidesoft.swing.JideButton();
        BBack3 = new com.jidesoft.swing.JideButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        labelusages = new javax.swing.JLabel();
        ChBLandsat = new javax.swing.JCheckBox();
        ChBASTER = new javax.swing.JCheckBox();
        ChBSRTM = new javax.swing.JCheckBox();
        labelNImage = new javax.swing.JLabel();
        labelVImage = new javax.swing.JLabel();
        ChBOthers = new javax.swing.JCheckBox();
        jScrollPane5 = new javax.swing.JScrollPane(table);
        txtSize = new javax.swing.JTextField();
        txtNumber = new javax.swing.JTextField();
        ChBOtherUsage = new javax.swing.JCheckBox();
        ChBProfessionel = new javax.swing.JCheckBox();
        ChBAcademique = new javax.swing.JCheckBox();
        BBack1 = new com.jidesoft.swing.JideButton();
        BNext2 = new com.jidesoft.swing.JideButton();
        jPanel4 = new javax.swing.JPanel();
        labelnotes = new javax.swing.JLabel();
        labelzi = new javax.swing.JLabel();
        labeldistribution = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtInterstedArea = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtComment = new javax.swing.JTextPane();
        BCancel = new com.jidesoft.swing.JideButton();
        BSave = new com.jidesoft.swing.JideButton();
        BBack2 = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("DataRequestForm.title")); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText(bundle.getString("DataRequestForm.jLabel2.text")); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/ident.png"))); // NOI18N

        panOnglet.setBoldActiveTab(true);
        panOnglet.setColorTheme(3);
        panOnglet.setFocusable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jXPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jXPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DataRequestForm.jXPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        jLabel4.setText(bundle.getString("DataRequestForm.jLabel4.text")); // NOI18N

        labelnom.setText(bundle.getString("DataRequestForm.labelnom.text")); // NOI18N

        BGSex.add(RBFemale);
        RBFemale.setText(bundle.getString("DataRequestForm.RBFemale.text")); // NOI18N
        RBFemale.setFocusable(false);
        RBFemale.setOpaque(false);

        BGSex.add(RBMale);
        RBMale.setSelected(true);
        RBMale.setText(bundle.getString("DataRequestForm.RBMale.text")); // NOI18N
        RBMale.setFocusable(false);
        RBMale.setOpaque(false);

        labelPrenom.setText(bundle.getString("DataRequestForm.labelPrenom.text")); // NOI18N

        labeladresse.setBackground(new java.awt.Color(241, 235, 235));
        labeladresse.setText(bundle.getString("DataRequestForm.labeladresse.text")); // NOI18N
        labeladresse.setOpaque(true);

        jScrollPane1.setViewportView(txtAdress);

        jLabel5.setText(bundle.getString("DataRequestForm.jLabel5.text")); // NOI18N

        BSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/document_preview.png"))); // NOI18N
        BSearch.setEnabled(false);
        BSearch.setFocusable(false);
        BSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jXPanel1Layout = new javax.swing.GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(
                jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jXPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jXPanel1Layout.createSequentialGroup()
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(labelPrenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labelnom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(txtOtherName)
                .addComponent(txtFamilyName)
                .addComponent(txtFirstName)
                .addGroup(jXPanel1Layout.createSequentialGroup()
                .addComponent(RBMale, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RBFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 126, Short.MAX_VALUE))))
                .addComponent(labeladresse)
                .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        jXPanel1Layout.setVerticalGroup(
                jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jXPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(BSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labelPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(labelnom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtFamilyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel4)
                .addComponent(txtOtherName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(RBFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(RBMale, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(labeladresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jXPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jXPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DataRequestForm.jXPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        jLabel8.setText(bundle.getString("DataRequestForm.jLabel8.text")); // NOI18N

        jLabel7.setText(bundle.getString("DataRequestForm.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jXPanel2Layout = new javax.swing.GroupLayout(jXPanel2);
        jXPanel2.setLayout(jXPanel2Layout);
        jXPanel2Layout.setHorizontalGroup(
                jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jXPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(txtEMail, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE)));
        jXPanel2Layout.setVerticalGroup(
                jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jXPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtEMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel7)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DataRequestForm.jPanel5.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        labelprofession.setText(bundle.getString("DataRequestForm.labelprofession.text")); // NOI18N

        labInstitut.setText(bundle.getString("DataRequestForm.labInstitut.text")); // NOI18N

        labNationalite.setText(bundle.getString("DataRequestForm.labNationalite.text")); // NOI18N

        CBNationality.setEditable(false);
        CBNationality.setFocusable(false);

        CBProfession.setStrict(false);

        CBInstitution.setStrict(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(labInstitut, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(labNationalite, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                .addComponent(labelprofession))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(CBNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(CBInstitution, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CBProfession, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(CBProfession, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labelprofession, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(CBInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labInstitut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(labNationalite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(CBNationality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{CBInstitution, CBNationality, CBProfession});

        BNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_next.png"))); // NOI18N
        BNext1.setText(bundle.getString("DataRequestForm.BNext1.text")); // NOI18N
        BNext1.setFocusable(false);
        BNext1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BNext1ActionPerformed(evt);
            }
        });

        BBack3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_previous.png"))); // NOI18N
        BBack3.setText(bundle.getString("DataRequestForm.BBack3.text")); // NOI18N
        BBack3.setFocusable(false);
        BBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBack3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jXPanel1, 457, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(BBack3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap()));

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{jPanel5, jXPanel1, jXPanel2});

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(BNext1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(BBack3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE)));

        panOnglet.addTab(bundle.getString("DataRequestForm.jPanel2.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/s_lang.png")), jPanel2); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText(bundle.getString("DataRequestForm.jLabel12.text")); // NOI18N

        labelusages.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelusages.setText(bundle.getString("DataRequestForm.labelusages.text")); // NOI18N

        ChBLandsat.setText(bundle.getString("DataRequestForm.ChBLandsat.text")); // NOI18N
        ChBLandsat.setEnabled(false);
        ChBLandsat.setFocusable(false);
        ChBLandsat.setOpaque(false);

        ChBASTER.setText(bundle.getString("DataRequestForm.ChBASTER.text")); // NOI18N
        ChBASTER.setEnabled(false);
        ChBASTER.setFocusable(false);
        ChBASTER.setOpaque(false);

        ChBSRTM.setText(bundle.getString("DataRequestForm.ChBSRTM.text")); // NOI18N
        ChBSRTM.setEnabled(false);
        ChBSRTM.setFocusable(false);
        ChBSRTM.setOpaque(false);

        labelNImage.setText(bundle.getString("DataRequestForm.labelNImage.text")); // NOI18N

        labelVImage.setText(bundle.getString("DataRequestForm.labelVImage.text")); // NOI18N

        ChBOthers.setText(bundle.getString("DataRequestForm.ChBOthers.text")); // NOI18N
        ChBOthers.setEnabled(false);
        ChBOthers.setFocusable(false);
        ChBOthers.setOpaque(false);

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));

        txtSize.setEditable(false);
        txtSize.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        txtNumber.setEditable(false);
        txtNumber.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        ChBOtherUsage.setText(bundle.getString("DataRequestForm.ChBOtherUsage.text")); // NOI18N
        ChBOtherUsage.setFocusable(false);
        ChBOtherUsage.setOpaque(false);

        ChBProfessionel.setText(bundle.getString("DataRequestForm.ChBProfessionel.text")); // NOI18N
        ChBProfessionel.setFocusable(false);
        ChBProfessionel.setOpaque(false);

        ChBAcademique.setText(bundle.getString("DataRequestForm.ChBAcademique.text")); // NOI18N
        ChBAcademique.setFocusable(false);
        ChBAcademique.setOpaque(false);

        BBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_previous.png"))); // NOI18N
        BBack1.setText(bundle.getString("DataRequestForm.BBack1.text")); // NOI18N
        BBack1.setFocusable(false);
        BBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBack1ActionPerformed(evt);
            }
        });

        BNext2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_next.png"))); // NOI18N
        BNext2.setText(bundle.getString("DataRequestForm.BNext2.text")); // NOI18N
        BNext2.setFocusable(false);
        BNext2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BNext2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ChBLandsat)
                .addComponent(ChBSRTM)
                .addComponent(ChBASTER)
                .addComponent(ChBOthers))
                .addGap(76, 76, 76)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ChBAcademique)
                .addComponent(ChBProfessionel)
                .addComponent(ChBOtherUsage)))
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(BBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BNext2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(labelNImage, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelVImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(labelusages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelusages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(ChBAcademique)
                .addGap(23, 23, 23))
                .addComponent(ChBProfessionel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChBOtherUsage))
                .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ChBLandsat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChBSRTM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChBASTER)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChBOthers)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(labelNImage)
                .addComponent(labelVImage)
                .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(BBack1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(BNext2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));

        panOnglet.addTab(bundle.getString("DataRequestForm.jPanel3.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/preview.png")), jPanel3); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        labelnotes.setBackground(new java.awt.Color(241, 235, 235));
        labelnotes.setText(bundle.getString("DataRequestForm.labelnotes.text")); // NOI18N
        labelnotes.setOpaque(true);

        labelzi.setBackground(new java.awt.Color(241, 235, 235));
        labelzi.setText(bundle.getString("DataRequestForm.labelzi.text")); // NOI18N
        labelzi.setOpaque(true);

        labeldistribution.setBackground(new java.awt.Color(241, 235, 235));
        labeldistribution.setText(bundle.getString("DataRequestForm.labeldistribution.text")); // NOI18N
        labeldistribution.setOpaque(true);

        txtNote.setVerifyInputWhenFocusTarget(false);
        jScrollPane2.setViewportView(txtNote);

        txtInterstedArea.setVerifyInputWhenFocusTarget(false);
        jScrollPane3.setViewportView(txtInterstedArea);

        txtComment.setVerifyInputWhenFocusTarget(false);
        jScrollPane4.setViewportView(txtComment);

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BCancel.setText(bundle.getString("DataRequestForm.BCancel.text")); // NOI18N
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        BSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png"))); // NOI18N
        BSave.setText(bundle.getString("DataRequestForm.BSave.text")); // NOI18N
        BSave.setFocusable(false);
        BSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSaveActionPerformed(evt);
            }
        });

        BBack2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/go_previous.png"))); // NOI18N
        BBack2.setText(bundle.getString("DataRequestForm.BBack2.text")); // NOI18N
        BBack2.setFocusable(false);
        BBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBack2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(BBack2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(labelnotes, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labeldistribution, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labelzi, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap()));

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{BBack2, BCancel, BSave});

        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(labelnotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelzi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labeldistribution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(BSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(BBack2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));

        panOnglet.addTab(bundle.getString("DataRequestForm.jPanel4.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/pyramid2.png")), jPanel4); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel1))
                .addComponent(panOnglet, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(panOnglet, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }// </editor-fold>

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_BCancelActionPerformed

    private void BSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSaveActionPerformed
        if (Config.valideEmail(txtEMail)) {
            WorkbenchFrame.progress.setProgressStatus(I18N.get("DataRequestForm.saving-data-request"));
            WorkbenchFrame.progress.setIndeterminate(true);
            final int idRequester = insertRequester();
            if (idRequester != -1) {
                this.dispose();
                final int idDelivery = insertDelivery(idRequester);
                WorkbenchFrame.progress.setProgress(100);
                Thread th = new Thread() {
                    @Override
                    public void run() {
                        try {
                            WorkbenchFrame.progress.setProgressStatus(I18N.get("DataRequestForm.creating-pdf-file"));
                            WorkbenchFrame.progress.setIndeterminate(true);
                            HashMap idReq = new HashMap();
                            idReq.put("id_requester", idRequester);
                            idReq.put("usage", getUsage(idDelivery));
                            idReq.put("country", getCountry(idDelivery));
                            idReq.put("number", getNumber(idDelivery));
                            idReq.put("pathrow", getPathRow());
                            idReq.put("categories", getCategories(idDelivery));

                            idReq.put("logo", getClass().getResourceAsStream("/com/osfac/dmt/form/jasper/001.png"));
                            InputStream inForm = getClass().getResourceAsStream("/com/osfac/dmt/form/jasper/form.jasper");
                            JasperPrint jp = JasperFillManager.fillReport(inForm, idReq, Config.con);
                            File directory = new File(new JFileChooser().getCurrentDirectory() + File.separator + "OSFAC-DMT"
                                    + "" + File.separator + Config.correctText(new SimpleDateFormat("yyyy-MM-dd"
                                    + "", new DateFormatSymbols()).format(new Date())));
                            directory.mkdirs();
                            String pathPDF = verification(requestPDF(directory));
                            JasperExportManager.exportReportToPdfFile(jp, pathPDF);
                            WorkbenchFrame.progress.setProgress(100);
                            WorkbenchFrame.timer.start();
                            confirmFileCreated(idDelivery, pathPDF);
                            createAlert(pathPDF);
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
                            sendEmail(pathPDF, Config.capitalFirstLetter(txtFirstName.getText()) + " " + txtFamilyName.getText().toUpperCase() + " "
                                    + "" + txtOtherName.getText().toUpperCase(), idDelivery);
                        } catch (JRException ex) {
                            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
                        }
                    }
                };
                th.start();
                if (Config.isFullVersion()) {
                    if (WorkbenchFrame.BSConnect.isEnabled()) {
                        JOptionPane.showMessageDialog(DMTWorkbench.frame, I18N.get("GeoResult.message-server-connection-error"
                        + ""), I18N.get("Text.Warning"), JOptionPane.WARNING_MESSAGE);
                    } else {
                        new DownloadData(vID, idDelivery).setVisible(true);
                    }
                }
            }
        }
    }//GEN-LAST:event_BSaveActionPerformed

    private void BBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BBack2ActionPerformed
        panOnglet.setSelectedIndex(1);
    }//GEN-LAST:event_BBack2ActionPerformed

    private void BBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BBack1ActionPerformed
        panOnglet.setSelectedIndex(0);
    }//GEN-LAST:event_BBack1ActionPerformed

    private void BNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BNext2ActionPerformed
        panOnglet.setSelectedIndex(2);
    }//GEN-LAST:event_BNext2ActionPerformed

    private void BNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BNext1ActionPerformed
        panOnglet.setSelectedIndex(1);
    }//GEN-LAST:event_BNext1ActionPerformed

    private void BBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BBack3ActionPerformed
        this.dispose();
        geoResult.setVisible(true);
    }//GEN-LAST:event_BBack3ActionPerformed

    private void BSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSearchActionPerformed
        if (doCount(txtFirstName.getText(), txtFamilyName.getText()) == 0) {
            JOptionPane.showMessageDialog(this, ""
                    + I18N.get("DataRequestForm.No-matching-requester"), I18N.get("DataRequestForm.Warning-message"), JOptionPane.WARNING_MESSAGE);
        } else {
            findNamesDataBase(txtFirstName.getText(), txtFamilyName.getText());
        }
    }//GEN-LAST:event_BSearchActionPerformed

    private JComponent createSampleAlertTable(ActionListener closeAction, String firstName, String familyName) {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        JPanel leftPanel = new JPanel(new BorderLayout(6, 6));
        leftPanel.add(new JLabel(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/1_folder_2.png"))));
        leftPanel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);
        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        rightPanel.add(createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/option.png"))));
        JideButton closeButton = createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/close.png")));
        closeButton.addActionListener(closeAction);
        rightPanel.add(closeButton);
        String text = "<HTML><CENTER><H4><U>OSFAC - Data Management Tool</U></H4></CENTER>";
        if (!firstName.isEmpty() && !familyName.isEmpty()) {
            text += "\"<font color=blue>" + firstName + " " + familyName + "</font>\" " + I18N.get("DataRequestForm.found-in-database") + "<br>";
        }
        text += "</HTML>";
        final JLabel LabelMessage = new JLabel(text);
        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
        panel.add(LabelMessage, BorderLayout.CENTER);
        alertTable = new CellSpanTable(getRequester(firstName, familyName), new String[]{I18N.get("Text.ID"), I18N.get("Text.FIRSTNAME"), I18N.get("Text.FAMILYNAME"), I18N.get("Text.OTHERNAME"), ""}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        alertTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alertTable.setRowSelectionAllowed(false);
        alertTable.getColumnModel().getColumn(4).setCellEditor(new ButtonsCellEditorRenderer());
        alertTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonsCellEditorRenderer());
        alertTable.getColumnModel().getColumn(4).setMaxWidth(20);
        alertTable.getColumnModel().getColumn(4).setMinWidth(20);

        alertTable.getColumnModel().getColumn(0).setMaxWidth(50);
        alertTable.getColumnModel().getColumn(0).setMinWidth(50);
        alertTable.setRowHeight(19);
        alertTable.setShowGrid(false);
        RolloverTableUtils.install(alertTable);
        panel.add(alertTable, BorderLayout.SOUTH);
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

    private String[][] getRequester(String firstName, String familyName) {
        String[][] value = new String[doCount(firstName, familyName)][5];
        try {
            PreparedStatement ps;
            int i = 0;
            if (!firstName.isEmpty() && !familyName.isEmpty()) {
                ps = Config.con.prepareStatement("select distinct * from dmt_requester where firstname = ? and "
                        + "familyname = ? and id_requester in (select max(id_requester) "
                        + "from dmt_requester where (firstname = ? or familyname = ?))");
                ps.setString(1, firstName);
                ps.setString(2, familyName);
                ps.setString(3, firstName);
                ps.setString(4, familyName);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    if (!checkOutID(value, res.getString("id_requester"))) {
                        value[i][0] = res.getString("id_requester");
                        value[i][1] = res.getString("firstname");
                        value[i][2] = res.getString("familyname");
                        value[i][3] = res.getString("othername");
                        i++;
                    }
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return value;
    }

    private boolean checkOutID(String data[][], String id) {
        ArrayList<String> tab = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            tab.add(data[i][0]);
        }
        for (int i = 0; i < tab.size(); i++) {
            if (tab.get(i) != null) {
                if (tab.get(i).equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int doCount(String firstname, String familyName) {
        try {
            PreparedStatement ps;
            int sum = 0;
            if (!firstname.isEmpty() && !familyName.isEmpty()) {
                ps = Config.con.prepareStatement("select count(id_requester) from dmt_requester "
                        + "where firstname = ? and familyname = ? and id_requester in (select max(id_requester) "
                        + "from dmt_requester where (firstname = ? or familyname = ?))");
                ps.setString(1, firstname);
                ps.setString(2, familyName);
                ps.setString(3, firstname);
                ps.setString(4, familyName);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    sum = res.getInt(1);
                }
            }
            return sum;
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return 0;
    }

    private void createAlertTable(String familyName, String otherName) {
        alertRequester = new Alert();
        alertRequester.setAlwaysOnTop(true);
        alertRequester.getContentPane().setLayout(new BorderLayout());
        hideAnimationT = new CustomAnimation(CustomAnimation.TYPE_EXIT, Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        hideAnimationT.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideAnimationT.setDirection(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        alertRequester.setHideAnimation(hideAnimationT);
        alertRequester.getContentPane().add(createSampleAlertTable(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alertRequester.hidePopup();
            }
        }, familyName, otherName));
        alertRequester.setResizable(true);
        alertRequester.setMovable(true);
        alertRequester.setTimeout(0);
        alertRequester.setTransient(false);
        alertRequester.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));
        CustomAnimation showAnimation = new CustomAnimation(CustomAnimation.TYPE_ENTRANCE, Config.pref.getInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_MEDIUM));
        showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        showAnimation.setDirection(Config.pref.getInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP_RIGHT));
        alertRequester.setShowAnimation(showAnimation);
        CustomAnimation hideA = new CustomAnimation(CustomAnimation.TYPE_EXIT, Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        hideA.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideA.setDirection(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        alertRequester.setHideAnimation(hideA);
        _alertGroup.add(alertRequester);
        alertRequester.showPopup(Config.pref.getInt(SettingKeyFactory.General.entranceLocation, SwingConstants.CENTER), this);
    }

    private void findNamesDataBase(String firstName, String familyName) {
        if (!firstName.isEmpty() || !familyName.isEmpty()) {
            createAlertTable(firstName, familyName);
        }
    }

    private void createAlert(String formPath) {
        alert = new Alert();
        alert.setAlwaysOnTop(true);
        alert.getContentPane().setLayout(new BorderLayout());
        hideAnimation = new CustomAnimation(CustomAnimation.TYPE_EXIT, Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideAnimation.setDirection(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        alert.setHideAnimation(hideAnimation);
        alert.getContentPane().add(createSampleAlert(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alert.hidePopup();
            }
        }, formPath));
        alert.setResizable(true);
        alert.setMovable(true);
        alert.setTimeout(4000);
        alert.setTransient(false);
        alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));
        CustomAnimation showAnimation = new CustomAnimation(CustomAnimation.TYPE_ENTRANCE, Config.pref.getInt(SettingKeyFactory.General.entranceEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.entranceSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.entranceSpeed, CustomAnimation.SPEED_MEDIUM));
        showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        showAnimation.setDirection(Config.pref.getInt(SettingKeyFactory.General.entranceDirection, CustomAnimation.TOP_RIGHT));
        alert.setShowAnimation(showAnimation);
        CustomAnimation hideAnim = new CustomAnimation(CustomAnimation.TYPE_EXIT, Config.pref.getInt(SettingKeyFactory.General.exitEffect, CustomAnimation.EFFECT_FLY), Config.pref.getInt(SettingKeyFactory.General.exitSmoothness, CustomAnimation.SMOOTHNESS_MEDIUM), Config.pref.getInt(SettingKeyFactory.General.exitSpeed, CustomAnimation.SPEED_MEDIUM));
        hideAnim.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideAnim.setDirection(Config.pref.getInt(SettingKeyFactory.General.exitDirection, CustomAnimation.BOTTOM));
        alert.setHideAnimation(hideAnim);
        _alertGroup.add(alert);
        alert.showPopup(Config.pref.getInt(SettingKeyFactory.General.entranceLocation, SwingConstants.CENTER), parent);
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
        alert2.showPopup(Config.pref.getInt(SettingKeyFactory.General.entranceLocation, SwingConstants.CENTER), parent);
    }

    private JComponent createSampleAlert(ActionListener closeAction, final String formPath) {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        JPanel leftPanel = new JPanel(new BorderLayout(6, 6));
        leftPanel.add(new JLabel(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/1_folder_2.png"))));
        leftPanel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);
        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        rightPanel.add(createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/option.png"))));
        JideButton closeButton = createButton(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/close.png")));
        closeButton.addActionListener(closeAction);
        rightPanel.add(closeButton);
        final String text = "<HTML><CENTER><H4><U>OSFAC-Data Management Tool</U></H4></CENTER>"
                + "<H3><font color=green>" + I18N.get("DataRequestForm.a-pdf-file-created") + "</font></H3>"
                + "</HTML>";
        final JLabel LabelMessage = new JLabel(text);
        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
        panel.add(LabelMessage, BorderLayout.CENTER);
        JideButton button = new JideButton(formPath);
        button.setFont(new Font("Tahoma", Font.PLAIN, 10));
        button.setButtonStyle(JideButton.HYPERLINK_STYLE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((new File(formPath)).exists()) {
                    try {
                        Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + formPath);
                        p.waitFor();
                        alert.hidePopup();
                    } catch (InterruptedException | IOException ex) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
                    }
                } else {
                    System.out.println("File is not exists");
                }
            }
        });
        panel.add(button, BorderLayout.SOUTH);
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

    private void sendEmail(String filePath, String names, int idDelivery) {
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
            msg.setAttachmentURL(filePath);
            //Envoyer le message
            mail.sendMessage(msg);
            WorkbenchFrame.progress.setProgress(100);
            WorkbenchFrame.progress.setProgressStatus(I18N.get("ReviewDataRequestForm.Sending-confirmation-Email-to")
                    + " " + txtEMail.getText() + " ......");
            WorkbenchFrame.progress.setIndeterminate(true);
            //send email to the requester : Source de message
            msg.setFrom(new InternetAddress("dmt@osfac.net", "OSFAC-DMT"));
            //Destination
            msg.setTo(txtEMail.getText());
            //Sujet de message
            msg.setSubject(I18N.get("ReviewDataRequestForm.Email-subject-to-requester"));
            String emailContent, title;
            if (RBMale.isSelected()) {
                title = "Mr";
            } else {
                title = "Ms";
            }
            if (getCategories(idDelivery).contains("ASTER") || getCategories(idDelivery).contains("SPOT")) {
                emailContent = title + " <b>" + names + I18N.get("ReviewDataRequestForm.Email-content-to-other");
            } else {
                emailContent = title + " <b>" + names + I18N.get("ReviewDataRequestForm.Email-content-to-other-aster-spot");
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

    private String getUsage(int idDelivery) {
        String usageForm = "";
        try {
            PreparedStatement ps = Config.con.prepareStatement("select distinct usage_name from dmt_usage inner join dmt_choose on "
                    + "dmt_choose.id_usage = dmt_usage.id_usage where id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                usageForm += res.getString(1) + ",";
            }
            usageForm = usageForm.substring(0, usageForm.length() - 1);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return usageForm;
    }

    private int getNumber(int idDelivery) {
        int number = 0;
        try {
            PreparedStatement ps = Config.con.prepareStatement("select count(id_image) from dmt_deliver where id_delivery = ?");
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

    private String getCategories(int idDelivery) {
        String category = "";
        try {
            PreparedStatement ps = Config.con.prepareStatement("select distinct category_name from dmt_category inner join dmt_image on "
                    + "dmt_image.id_category = dmt_category.id_category inner join dmt_deliver on "
                    + "dmt_image.id_image = dmt_deliver.id_image where id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                category += res.getString(1) + ",";
            }
            category = category.substring(0, category.length() - 1);
            return category;
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return category;
    }

    private String getPathRow() {
        String pathrow = "";
        for (int i = 0; i < vPathRow.size(); i++) {
            pathrow += "P" + vPathRow.get(i).toString().substring(0, 3) + "-R0"
                    + "" + vPathRow.get(i).toString().substring(3) + "; ";
        }
        pathrow = pathrow.substring(0, pathrow.length() - 2);
        return pathrow;
    }

    private String getCountry(int idDelivery) {
        String country = "";
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT distinct country_name FROM `dmt_country` dmt_country INNER JOIN `dmt_include` "
                    + "dmt_include ON dmt_country.`id_country` = dmt_include.`id_country` INNER JOIN `dmt_pathrow` dmt_pathrow ON "
                    + "dmt_include.`path_row` = dmt_pathrow.`path_row` INNER JOIN `dmt_concern` dmt_concern ON dmt_pathrow.`path_row` = "
                    + "dmt_concern.`path_row` INNER JOIN `dmt_image` dmt_image ON dmt_concern.`id_image` = dmt_image.`id_image` "
                    + "INNER JOIN `dmt_deliver` dmt_deliver ON dmt_image.`id_image` = dmt_deliver.`id_image` "
                    + "where id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                country += res.getString(1) + ",";
            }
            country = country.substring(0, country.length() - 1);
            return country;
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return country;
    }

    private int insertRequester() {
        try {
            PreparedStatement ps = Config.con.prepareStatement("insert into dmt_requester values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                    + "", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setString(2, Config.capitalFirstLetter(txtFirstName.getText()));
            ps.setString(3, Config.capitalFirstLetter(txtFamilyName.getText()));
            ps.setString(4, Config.capitalFirstLetter(txtOtherName.getText()));
            if (RBMale.isSelected()) {
                ps.setString(5, "Male");
            } else {
                ps.setString(5, "Female");
            }
            ps.setString(6, Config.capitalFirstLetter(txtAdress.getText()));
            ps.setString(7, Config.capitalFirstLetter(txtPhone.getText()));
            ps.setString(8, txtEMail.getText().toLowerCase());
            ps.setString(9, Config.capitalFirstLetter(CBProfession.getSelectedItem().toString()));
            ps.setString(10, Config.capitalFirstLetter(CBInstitution.getSelectedItem().toString()));
            ps.setString(11, Config.capitalFirstLetter(CBNationality.getSelectedItem().toString()));
            ps.setString(12, Config.capitalFirstLetter(txtInterstedArea.getText()));
            ps.setString(13, Config.capitalFirstLetter(txtNote.getText()));
            ps.setString(14, Config.capitalFirstLetter(txtComment.getText()));
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    return res.getInt(1);
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return -1;
    }

    private int insertRequester(Connection con) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into dmt_requester values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                    + "", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setString(2, Config.capitalFirstLetter(txtFirstName.getText()));
            ps.setString(3, Config.capitalFirstLetter(txtFamilyName.getText()));
            ps.setString(4, Config.capitalFirstLetter(txtOtherName.getText()));
            if (RBMale.isSelected()) {
                ps.setString(5, "Male");
            } else {
                ps.setString(5, "Female");
            }
            ps.setString(6, Config.capitalFirstLetter(txtAdress.getText()));
            ps.setString(7, Config.capitalFirstLetter(txtPhone.getText()));
            ps.setString(8, txtEMail.getText().toLowerCase());
            ps.setString(9, Config.capitalFirstLetter(CBProfession.getSelectedItem().toString()));
            ps.setString(10, Config.capitalFirstLetter(CBInstitution.getSelectedItem().toString()));
            ps.setString(11, Config.capitalFirstLetter(CBNationality.getSelectedItem().toString()));
            ps.setString(12, Config.capitalFirstLetter(txtInterstedArea.getText()));
            ps.setString(13, Config.capitalFirstLetter(txtNote.getText()));
            ps.setString(14, Config.capitalFirstLetter(txtComment.getText()));
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    return res.getInt(1);
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return -1;
    }

    private int insertDelivery(int idRequester) {
        int idDelivery = -1;
        try {
            PreparedStatement ps = Config.con.prepareStatement("insert into dmt_delivery values (?,?,?,?,?,?,?,?,?)"
                    + "", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setInt(2, idRequester);
            ps.setInt(3, 4);
            ps.setString(4, txtSize.getText());
            ps.setString(5, Config.dateFormatDB.format(new Date()));
            ps.setString(6, getPathRow());
            ps.setString(7, "No");
            ps.setString(8, "No");
            ps.setString(9, "");
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    idDelivery = res.getInt(1);
                    ArrayList<Integer> list = getIDUsage();
                    for (int i = 0; i < list.size(); i++) {
                        ps = Config.con.prepareStatement("insert into dmt_choose values (?,?)");
                        ps.setInt(1, idDelivery);
                        ps.setInt(2, list.get(i));
                        int result2 = ps.executeUpdate();
                    }
                    for (int i = 0; i < vID.size(); i++) {
                        ps = Config.con.prepareStatement("insert into dmt_deliver values (?,?)");
                        ps.setString(1, vID.get(i));
                        ps.setInt(2, idDelivery);
                        int result3 = ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return idDelivery;
    }

    private int insertDelivery(int idRequester, Connection con) {
        int idDelivery = -1;
        try {
            PreparedStatement ps = con.prepareStatement("insert into dmt_delivery values (?,?,?,?,?,?,?,?,?)"
                    + "", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setInt(2, idRequester);
            ps.setInt(3, 4);
            ps.setString(4, txtSize.getText());
            ps.setString(5, Config.dateFormatDB.format(new Date()));
            ps.setString(6, getPathRow());
            ps.setString(7, "Yes");
            ps.setString(8, "No");
            ps.setString(9, "");
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    idDelivery = res.getInt(1);
                    ArrayList<Integer> list = getIDUsage();
                    for (int i = 0; i < list.size(); i++) {
                        ps = con.prepareStatement("insert into dmt_choose values (?,?)");
                        ps.setInt(1, idDelivery);
                        ps.setInt(2, list.get(i));
                        int result2 = ps.executeUpdate();
                    }
                    for (int i = 0; i < vID.size(); i++) {
                        ps = con.prepareStatement("insert into dmt_deliver values (?,?)");
                        ps.setString(1, vID.get(i));
                        ps.setInt(2, idDelivery);
                        int result3 = ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return idDelivery;
    }

    private void confirmSentEmail(int idDelivery) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("update dmt_delivery set confirm_email_sent = ? where id_delivery = ?");
            ps.setString(1, "Yes");
            ps.setInt(2, idDelivery);
            int result = ps.executeUpdate();
            if (result == 1) {
                WorkbenchFrame.timer.start();
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private void confirmFileCreated(int idDelivery, String pathFile) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("update dmt_delivery set form_path = ? "
                    + "where id_delivery = ?");
            ps.setString(1, pathFile);
            ps.setInt(2, idDelivery);
            int result = ps.executeUpdate();
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private ArrayList<Integer> getIDUsage() {
        ArrayList<Integer> list = new ArrayList<>();
        if (ChBAcademique.isSelected()) {
            list.add(1);
        }
        if (ChBProfessionel.isSelected()) {
            list.add(2);
        }
        if (ChBOtherUsage.isSelected()) {
            list.add(3);
        }
        return list;
    }

    private void checkCategories() {
        for (int i = 0; i < vID.size(); i++) {
            if (getCategory(vID.get(i)).equalsIgnoreCase("LANDSAT")) {
                ChBLandsat.setSelected(true);
            } else if (getCategory(vID.get(i)).equalsIgnoreCase("ASTER")) {
                ChBASTER.setSelected(true);
            } else if (getCategory(vID.get(i)).equalsIgnoreCase("SRTM")) {
                ChBSRTM.setSelected(true);
            } else {
                ChBOthers.setSelected(true);
            }
        }
    }

    private String getCategory(String idImage) {
        String category = "";
        try {
            PreparedStatement ps = Config.con.prepareStatement("select category_name from dmt_category join dmt_image on "
                    + "dmt_image.id_category = dmt_category.id_category where id_image = ?");
            ps.setString(1, idImage);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return category;
    }

    private String manyCriteria(ArrayList list) {
        String values = "";
        for (int i = 0; i < list.size(); i++) {
            values += "\'" + list.get(i) + "\',";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private void createTable(ArrayList vID) {
        tableModel = new MyTableModel();
        table = new SortableTable(tableModel);
//        table.setSortable(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{getColorFromKey(Config.pref.get(
                    SettingKeyFactory.FontColor.RStripe21Color1, "253, 253, 244")), getColorFromKey(Config.pref.get(
                    SettingKeyFactory.FontColor.RStripe21Color2, "230, 230, 255"))}));
        table.setColumnResizable(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMinWidth(70);
        table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMaxWidth(70);
        try {
            Statement stat = Config.con.createStatement();
            ResultSet res = stat.executeQuery("select * from dmt_image where id_image in (" + manyCriteria(vID) + ")");
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
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private Color getColorFromKey(String value) {
        String tab[] = value.split(", ");
        return new Color(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]));
    }

    public class MyTableModel extends AbstractTableModel {

        private String[] columnNames = {I18N.get("Text.ID"), I18N.get("Text.IMAGES"), I18N.get("Text.DATE"), I18N.get("Text.SIZE-IN-MO")};
        private ArrayList[] Data;

        public MyTableModel() {
            Data = new ArrayList[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                Data[i] = new ArrayList();
            }
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return Data[0].size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return Data[col].get(row);
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
            Data[col].set(row, value);
            fireTableCellUpdated(row, col);
        }

        public void addNewRow() {
            for (int i = 0; i < columnNames.length; i++) {
                if (i == 0) {
                    Data[i].add(false);
                } else {
                    Data[i].add("");
                }
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

    private void autoCompleteTxt() {
        ListDataIntelliHints intellihints;
        PreparedStatement ps;
        ResultSet res;
        ArrayList<String> list = new ArrayList();
        try {
            ps = Config.con.prepareStatement("select distinct firstname from dmt_requester order by firstname");
            res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getString(1));
            }
            intellihints = new ListDataIntelliHints(txtFirstName, list);
            intellihints.setCaseSensitive(false);
            ArrayList<String> list2 = new ArrayList();
            ps = Config.con.prepareStatement("select distinct familyname from dmt_requester order by familyname");
            res = ps.executeQuery();
            while (res.next()) {
                list2.add(res.getString(1));
            }
            intellihints = new ListDataIntelliHints(txtFamilyName, list2);
            intellihints.setCaseSensitive(false);
            ArrayList<String> list3 = new ArrayList();
            ps = Config.con.prepareStatement("select distinct othername from dmt_requester where othername <> ? order by othername");
            ps.setString(1, "");
            res = ps.executeQuery();
            while (res.next()) {
                list3.add(res.getString(1));
            }
            intellihints = new ListDataIntelliHints(txtOtherName, list3);
            intellihints.setCaseSensitive(false);
            ArrayList<String> list4 = new ArrayList();
            ps = Config.con.prepareStatement("select distinct email from dmt_requester where email <> ? order by email");
            ps.setString(1, "");
            res = ps.executeQuery();
            while (res.next()) {
                list4.add(res.getString(1));
            }
            intellihints = new ListDataIntelliHints(txtEMail, list4);
            intellihints.setCaseSensitive(false);
            ArrayList<String> list5 = new ArrayList();
            ps = Config.con.prepareStatement("select distinct phone from dmt_requester where phone <> ? order by phone");
            ps.setString(1, "");
            res = ps.executeQuery();
            while (res.next()) {
                list5.add(res.getString(1));
            }
            intellihints = new ListDataIntelliHints(txtPhone, list5);
            intellihints.setCaseSensitive(false);
            ps = Config.con.prepareStatement("select distinct profession from dmt_requester where profession <> ? order by profession");
            ps.setString(1, "");
            res = ps.executeQuery();
            while (res.next()) {
                CBProfession.addItem(res.getString(1));
            }
            CBProfession.setSelectedIndex(-1);
            ps = Config.con.prepareStatement("select distinct institution from dmt_requester where institution <> ? order by institution");
            ps.setString(1, "");
            res = ps.executeQuery();
            while (res.next()) {
                CBInstitution.addItem(res.getString(1));
            }
            CBInstitution.setSelectedIndex(-1);
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }

    private boolean checkOutUsage() {
        if (ChBAcademique.isSelected() || ChBProfessionel.isSelected() || ChBOtherUsage.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    private File requestPDF(File path) {
        File f = new File(path.getAbsolutePath() + File.separator + "Application(1).pdf");
        return f;
    }

    private String verification(File f) {
        String retour;
        if (f.exists()) {
            counter++;
            File fichier = new File(f.getParent() + File.separator + "Application" + "(" + counter + ")" + ".pdf");
            retour = verification(fichier);
        } else {
            retour = f.getAbsolutePath();
        }
        return retour;
    }

    private JButton createButton(Icon icon, Icon rolloverIcon) {
        JButton button = new JideButton(icon);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(9, 9));
        button.setMaximumSize(new Dimension(9, 9));
        button.setMinimumSize(new Dimension(9, 9));
        button.setContentAreaFilled(false);
        button.setRolloverIcon(rolloverIcon);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    class ButtonsCellEditorRenderer extends AbstractTableCellEditorRenderer implements CellRolloverSupport {

        @Override
        public Component createTableCellEditorRendererComponent(JTable table, int row, int column) {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.setOpaque(true);
            panel.add(createButton(applyIcon, applyRolloverIcon));
            return panel;
        }

        @Override
        public void configureTableCellEditorRendererComponent(final JTable table, Component editorRendererComponent,
                boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, final int row, int column) {
            if (!forRenderer) {
                JButton editButton = (JButton) (((JPanel) editorRendererComponent).getComponent(0));
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setDataFromAlertToFields(row);
                    }
                });
            }
            editorRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            editorRendererComponent.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean isRollover(JTable table, MouseEvent e, int row, int column) {
            return true;
        }
    }

    private void setDataFromAlertToFields(int row) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("select * from dmt_requester "
                    + "where id_requester = ?");
            ps.setInt(1, Integer.parseInt(alertTable.getValueAt(row, 0).toString()));
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                txtFirstName.setText(res.getString("firstname"));
                txtFamilyName.setText(res.getString("familyname"));
                txtOtherName.setText(res.getString("othername"));
                if (res.getString("sex").equalsIgnoreCase("Male")) {
                    RBMale.setSelected(true);
                } else {
                    RBFemale.setSelected(true);
                }
                txtAdress.setText(res.getString("adress"));
                txtPhone.setText(res.getString("phone"));
                txtEMail.setText(res.getString("email"));
                CBProfession.setSelectedItem(res.getString("profession"));
                CBInstitution.setSelectedItem(res.getString("institution"));
                CBNationality.setSelectedItem(res.getString("nationality"));
            }
            alertRequester.hidePopup();
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
        }
    }

    private void initialize(String size) {
        txtNumber.setText(vID.size() + " image(s)");
        txtSize.setText(size);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BBack1;
    private com.jidesoft.swing.JideButton BBack2;
    private com.jidesoft.swing.JideButton BBack3;
    private com.jidesoft.swing.JideButton BCancel;
    private javax.swing.ButtonGroup BGSex;
    private com.jidesoft.swing.JideButton BNext1;
    private com.jidesoft.swing.JideButton BNext2;
    private com.jidesoft.swing.JideButton BSave;
    private com.jidesoft.swing.JideButton BSearch;
    private com.jidesoft.swing.AutoCompletionComboBox CBInstitution;
    private com.jidesoft.combobox.ListComboBox CBNationality;
    private com.jidesoft.swing.AutoCompletionComboBox CBProfession;
    private javax.swing.JCheckBox ChBASTER;
    private javax.swing.JCheckBox ChBAcademique;
    private javax.swing.JCheckBox ChBLandsat;
    private javax.swing.JCheckBox ChBOtherUsage;
    private javax.swing.JCheckBox ChBOthers;
    private javax.swing.JCheckBox ChBProfessionel;
    private javax.swing.JCheckBox ChBSRTM;
    private javax.swing.JRadioButton RBFemale;
    private javax.swing.JRadioButton RBMale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private org.jdesktop.swingx.JXPanel jXPanel1;
    private org.jdesktop.swingx.JXPanel jXPanel2;
    private javax.swing.JLabel labInstitut;
    private javax.swing.JLabel labNationalite;
    private javax.swing.JLabel labelNImage;
    private javax.swing.JLabel labelPrenom;
    private javax.swing.JLabel labelVImage;
    private javax.swing.JLabel labeladresse;
    private javax.swing.JLabel labeldistribution;
    private javax.swing.JLabel labelnom;
    private javax.swing.JLabel labelnotes;
    private javax.swing.JLabel labelprofession;
    private javax.swing.JLabel labelusages;
    private javax.swing.JLabel labelzi;
    private com.jidesoft.swing.JideTabbedPane panOnglet;
    private javax.swing.JTextPane txtAdress;
    private javax.swing.JTextPane txtComment;
    private javax.swing.JTextField txtEMail;
    private javax.swing.JTextField txtFamilyName;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextPane txtInterstedArea;
    private javax.swing.JTextPane txtNote;
    private javax.swing.JTextField txtNumber;
    private javax.swing.JTextField txtOtherName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSize;
    // End of variables declaration//GEN-END:variables
    private SortableTable table;
    private JideTable alertTable;
    private MyTableModel tableModel;
    private ArrayList<String> vID = new ArrayList<>(), vPathRow = new ArrayList<>();
    private ImageIcon applyIcon = new ImageIcon(getClass().getResource("/com/osfac/dmt/images/gnome-logout(18).png"));
    private ImageIcon applyRolloverIcon = new ImageIcon(getClass().getResource("/com/osfac/dmt/images/gnome-logout(1).png"));
    private int counter = 1;
    private JFrame parent;
    private Alert alert, alert2;
    private GeoResult geoResult;
    private AlertGroup _alertGroup = new AlertGroup(), _alertGroup2 = new AlertGroup();
    private CustomAnimation hideAnimation, hideAnimation2, hideAnimationT;
    Alert alertRequester;
}
