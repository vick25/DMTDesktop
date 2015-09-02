package com.osfac.dmt.authen;

import com.jidesoft.hints.ListDataIntelliHints;
import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.setting.panel.PrivacyPanel;
import com.osfac.dmt.user.CreateUser;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class PanAuthen extends javax.swing.JPanel {

    public PanAuthen(FadingPanel glassPane, JFrame parent) {
        this.glassPane = glassPane;
        this.parent = parent;
        initComponents(I18N.DMTResourceBundle);
        BServerIP.setText(new StringBuilder(I18N.get("com.osfac.dmt.Config.Server-Text")).append(" ").
                append(Config.pref.get(SettingKeyFactory.Connection.HOST, Config.host)).toString());

        //initialize value and log in automatically
        initialValues();
        if (txtEmail.getText().equals("")) {
            txtEmail.requestFocus();
        } else {
            txtPassword.requestFocus();
        }
        ListDataIntelliHints intellihints = new ListDataIntelliHints(txtEmail, getUserEmail());
        intellihints.setCaseSensitive(false);
        Timer time = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtEmail.getText().equals("") && !String.valueOf(txtPassword.getPassword()).equals("")) {
                    BLogIn.setEnabled(true);
                } else {
                    BLogIn.setEnabled(false);
                }
            }
        });
        time.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        BExit = new javax.swing.JButton();
        BLogIn = new javax.swing.JButton();
        BPasswordForgot = new com.jidesoft.swing.JideButton();
        BServerIP = new com.jidesoft.swing.JideButton();
        BSignUp = new com.jidesoft.swing.JideButton();

        setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        jLabel1.setText(bundle.getString("PanAuthen.jLabel1.text")); // NOI18N

        txtEmail.setColumns(20);
        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(bundle.getString("PanAuthen.jLabel2.text")); // NOI18N

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        BExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit(5).png"))); // NOI18N
        BExit.setText(bundle.getString("PanAuthen.BExit.text")); // NOI18N
        BExit.setFocusable(false);
        BExit.setOpaque(false);
        BExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BExitActionPerformed(evt);
            }
        });

        BLogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/b_usrcheck.png"))); // NOI18N
        BLogIn.setText(bundle.getString("PanAuthen.BLogIn.text")); // NOI18N
        BLogIn.setEnabled(false);
        BLogIn.setFocusable(false);
        BLogIn.setOpaque(false);
        BLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BLogInActionPerformed(evt);
            }
        });

        BPasswordForgot.setForeground(new java.awt.Color(255, 255, 153));
        BPasswordForgot.setText(bundle.getString("PanAuthen.BPasswordForgot.text")); // NOI18N
        BPasswordForgot.setButtonStyle(3);
        BPasswordForgot.setFocusable(false);
        BPasswordForgot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BPasswordForgot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPasswordForgotActionPerformed(evt);
            }
        });

        BServerIP.setForeground(new java.awt.Color(255, 255, 153));
        BServerIP.setText(bundle.getString("PanAuthen.BServerIP.text")); // NOI18N
        BServerIP.setToolTipText(bundle.getString("PanAuthen.BServerIP.toolTipText")); // NOI18N
        BServerIP.setButtonStyle(3);
        BServerIP.setFocusable(false);
        BServerIP.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        BServerIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BServerIPActionPerformed(evt);
            }
        });

        BSignUp.setForeground(new java.awt.Color(255, 255, 153));
        BSignUp.setText(bundle.getString("PanAuthen.BSignUp.text")); // NOI18N
        BSignUp.setToolTipText(bundle.getString("PanAuthen.BSignUp.toolTipText")); // NOI18N
        BSignUp.setButtonStyle(3);
        BSignUp.setFocusable(false);
        BSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSignUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BServerIP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BSignUp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPassword)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(BPasswordForgot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(BExit)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BExit, BLogIn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BPasswordForgot, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BExit)
                    .addComponent(BLogIn))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        BExit = new javax.swing.JButton();
        BLogIn = new javax.swing.JButton();
        BPasswordForgot = new com.jidesoft.swing.JideButton();
        BServerIP = new com.jidesoft.swing.JideButton();
        BSignUp = new com.jidesoft.swing.JideButton();

        setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(bundle.getString("PanAuthen.jLabel1.text")); // NOI18N

        txtEmail.setColumns(20);
        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(bundle.getString("PanAuthen.jLabel2.text")); // NOI18N

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        BExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit(5).png"))); // NOI18N
        BExit.setText(bundle.getString("PanAuthen.BExit.text")); // NOI18N
        BExit.setFocusable(false);
        BExit.setOpaque(false);
        BExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BExitActionPerformed(evt);
            }
        });

        BLogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/b_usrcheck.png"))); // NOI18N
        BLogIn.setText(bundle.getString("PanAuthen.BLogIn.text")); // NOI18N
        BLogIn.setEnabled(false);
        BLogIn.setFocusable(false);
        BLogIn.setOpaque(false);
        BLogIn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BLogInActionPerformed(evt);
            }
        });

        BPasswordForgot.setForeground(new java.awt.Color(255, 255, 153));
        BPasswordForgot.setText(bundle.getString("PanAuthen.BPasswordForgot.text")); // NOI18N
        BPasswordForgot.setButtonStyle(3);
        BPasswordForgot.setFocusable(false);
        BPasswordForgot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BPasswordForgot.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPasswordForgotActionPerformed(evt);
            }
        });

        BServerIP.setForeground(new java.awt.Color(255, 255, 153));
        BServerIP.setText(bundle.getString("PanAuthen.BServerIP.text")); // NOI18N
        BServerIP.setToolTipText(bundle.getString("PanAuthen.BServerIP.toolTipText")); // NOI18N
        BServerIP.setButtonStyle(3);
        BServerIP.setFocusable(false);
        BServerIP.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        BServerIP.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BServerIPActionPerformed(evt);
            }
        });

        BSignUp.setForeground(new java.awt.Color(255, 255, 153));
        BSignUp.setText(bundle.getString("PanAuthen.BSignUp.text")); // NOI18N
        BSignUp.setToolTipText(bundle.getString("PanAuthen.BSignUp.toolTipText")); // NOI18N
        BSignUp.setButtonStyle(3);
        BSignUp.setFocusable(false);
        BSignUp.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSignUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BServerIP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BSignUp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtPassword)
                                                .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(128, 128, 128)
                                        .addComponent(BPasswordForgot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(BLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(BExit)))
                        .addContainerGap()));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{BExit, BLogIn});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(BPasswordForgot, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel1)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(BSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BExit)
                                .addComponent(BLogIn))
                        .addContainerGap()));
    }// </editor-fold>

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        BLogInActionPerformed(evt);
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void BExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_BExitActionPerformed

    private void BLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BLogInActionPerformed
        try {
            boolean found = false;
            String username = txtEmail.getText();
            String password2 = String.copyValueOf(txtPassword.getPassword());
            PreparedStatement ps = Config.con.prepareStatement("SELECT * FROM dmt_user");
            ResultSet res = ps.executeQuery();
            ResultSetMetaData rsmd = res.getMetaData();
            int nbCols = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= nbCols; i++) {
                    if ((username.equalsIgnoreCase(res.getString(i)))
                            && (password2.equalsIgnoreCase(Config.decrypt(res.getString(i + 1))))) {
                        found = true;
                        if (PrivacyPanel.ChBParameter.isSelected()) {
                            if (PrivacyPanel.RBEmail.isSelected()) {
                                saveUserNameOnly();
                            } else {
                                saveUserNameAndPassword();
                            }
                        } else {
                            UnsaveUserNameAndPassword();
                        }
                        Config.pref.putBoolean(SettingKeyFactory.Privacy.rememberEmailOnly, PrivacyPanel.RBEmail.isSelected());
                        Config.pref.putBoolean(SettingKeyFactory.Privacy.rememberLoginInfo, PrivacyPanel.ChBParameter.isSelected());
                        Config.pref.putBoolean(SettingKeyFactory.Privacy.loginInAutomatically, PrivacyPanel.chkLogAuto.isSelected());
                        WorkbenchFrame.idUser = res.getInt("id_user");
                        glassPane.setVisible(true);
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, I18N.get("com.osfac.dmt.authen.PanAuthen-Error-connection-setting"),
                        I18N.get("com.osfac.dmt.Config.Error"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }//GEN-LAST:event_BLogInActionPerformed

private void BPasswordForgotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPasswordForgotActionPerformed
        new PasswordForgot(parent, true).setVisible(true);// TODO add your handling code here:
}//GEN-LAST:event_BPasswordForgotActionPerformed

private void BServerIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BServerIPActionPerformed
        new ChangeIP(parent, true).setVisible(true);
}//GEN-LAST:event_BServerIPActionPerformed

    private void BSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSignUpActionPerformed
        new CreateUser(parent, true, false).setVisible(true);
    }//GEN-LAST:event_BSignUpActionPerformed

    private ArrayList getUserEmail() {
        ArrayList<String> list = new ArrayList();
        try {
            PreparedStatement ps = Config.con.prepareStatement("SELECT DISTINCT email FROM dmt_user "
                    + "WHERE id_user NOT IN (?, ?) ORDER BY email");
            ps.setInt(1, 3);
            ps.setInt(2, 4);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getString(1));
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return list;
    }

    private void initialValues() {
        txtEmail.setText(Config.pref.get(SettingKeyFactory.Privacy.rememberEmail, ""));
        txtPassword.setText(Config.pref.get(SettingKeyFactory.Privacy.rememberPassword, ""));
        if (PrivacyPanel.ChBParameter.isSelected() && !PrivacyPanel.RBEmail.isSelected()) {
            if (!txtEmail.getText().isEmpty() && !String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
                if (PrivacyPanel.chkLogAuto.isSelected()) {
                    BLogInActionPerformed(null);
                }
            }
        }
    }

    private void saveUserNameOnly() {
        Config.pref.put(SettingKeyFactory.Privacy.rememberEmail, txtEmail.getText());
        Config.pref.put(SettingKeyFactory.Privacy.rememberPassword, "");
    }

    private void saveUserNameAndPassword() {
        Config.pref.put(SettingKeyFactory.Privacy.rememberEmail, txtEmail.getText());
        Config.pref.put(SettingKeyFactory.Privacy.rememberPassword, String.copyValueOf(txtPassword.getPassword()));
    }

    private void UnsaveUserNameAndPassword() {
        Config.pref.put(SettingKeyFactory.Privacy.rememberEmail, "");
        Config.pref.put(SettingKeyFactory.Privacy.rememberPassword, "");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BExit;
    private javax.swing.JButton BLogIn;
    private com.jidesoft.swing.JideButton BPasswordForgot;
    public static com.jidesoft.swing.JideButton BServerIP;
    private com.jidesoft.swing.JideButton BSignUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    public static javax.swing.JTextField txtEmail;
    public static javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
    FadingPanel glassPane;
    JFrame parent;
}
