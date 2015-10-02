package com.osfac.dmt.tools.request;

import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.DMTIconsFactory;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class DataRequestSync extends javax.swing.JDialog {

    public DataRequestSync(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents(I18N.DMTResourceBundle);
        syncDataBases();
        this.setLocationRelativeTo(parent);
        this.setIconImage(DMTIconsFactory.getImageIcon(DMTIconsFactory.Standard.SYNC).getImage());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PBar = new javax.swing.JProgressBar();
        BCancel = new com.jidesoft.swing.JideButton();
        labTask = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        setTitle(bundle.getString("DataRequestSync.title")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        PBar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/background2.png"))); // NOI18N
        BCancel.setToolTipText(bundle.getString("DataRequestSync.BCancel.toolTipText")); // NOI18N
        BCancel.setButtonStyle(1);
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        labTask.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PBar, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labTask, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(PBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTask, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
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

        jPanel1 = new javax.swing.JPanel();
        PBar = new javax.swing.JProgressBar();
        BCancel = new com.jidesoft.swing.JideButton();
        labTask = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        setTitle(bundle.getString("DataRequestSync.title")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        PBar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        BCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/background2.png"))); // NOI18N
        BCancel.setToolTipText(bundle.getString("DataRequestSync.BCancel.toolTipText")); // NOI18N
        BCancel.setButtonStyle(1);
        BCancel.setFocusable(false);
        BCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCancelActionPerformed(evt);
            }
        });

        labTask.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(PBar, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(labTask, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(21, Short.MAX_VALUE)
                        .addComponent(PBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(BCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labTask, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)));

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

    private void BCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCancelActionPerformed
        this.hideFrame();
    }//GEN-LAST:event_BCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        if (JOptionPane.showConfirmDialog(DMTWorkbench.frame, "Do you really want to abort database synchronizing?",
//                I18N.get("Text.Confirm"), 0) == 0) {
//            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//            thread.interrupt();
//            this.dispose();
//        }
        this.hideFrame();
    }//GEN-LAST:event_formWindowClosing

    private void hideFrame() {
        WorkbenchFrame.DataRequestFound.setVisible(true);
        this.setVisible(false);
    }

    private void syncDataBases() {
        thread = new Thread() {
            @Override
            public void run() {
                PBar.setIndeterminate(true);
                labTask.setText(I18N.get("DataRequestSync.text-connecting-to-remote-database"));
                WorkbenchFrame.DataRequestFound.setToolTipText(labTask.getText());
                Connection con = WorkbenchFrame.remoteDBConnecting();
                labTask.setText(I18N.get("DataRequestSync.text-connecting-to-remote-database-done"));
                if (con != null) {
                    try {
                        number = doCount("SELECT COUNT(*) FROM dmt_delivery WHERE confirm_request_treated = ?",
                                con, "No");
                        PBar.setMaximum(number);
                        labTask.setText(new StringBuilder(I18N.get("DataRequestSync.Number-of-items-to-be-synchronized"))
                                .append(" ").append(number).toString());
                        WorkbenchFrame.DataRequestFound.setToolTipText(labTask.getText());
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM dmt_delivery "
                                + "WHERE confirm_request_treated = ?");
                        ps.setString(1, "No");
                        ResultSet res = ps.executeQuery();
                        int i = 0;
                        while (res.next()) {
                            int idRequesterNew = insertRequester(findRequesterData(res.getInt(1), con));
                            PBar.setIndeterminate(false);
                            PBar.setStringPainted(true);
                            if (idRequesterNew != -1) {
                                insertDelivery(idRequesterNew, findDeliveryData(res.getInt(1), con), con, res.getInt(1));
                                PBar.setValue(++i);
                                WorkbenchFrame.DataRequestFound.setToolTipText(new StringBuilder(I18N.get("DataRequestSync.Synchronizing-database"))
                                        .append(PBar.getString()).toString());
                                confirmSync(res.getInt(1), con);
                            }
                        }
                        WorkbenchFrame.dataRequestSync = null;
                        dispose();
                        WorkbenchFrame.DataRequestFound.setVisible(false);
                        if (DataRequestManager.BRefresh != null && i > 0) {
                            DataRequestManager.BRefresh.doClick();
                        }
                    } catch (SQLException e) {
                        JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                                e.getMessage(), null, null, e, Level.SEVERE, null));
                    }
                }
            }
        };
        thread.start();
    }

    private ArrayList<String> findDeliveryData(int idDelivery, Connection con) {
        ArrayList<String> data = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_user,"
                    + "image_size,request_date,pathrow,confirm_email_sent,confirm_request_treated,"
                    + "form_path FROM dmt_delivery WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            ResultSetMetaData rsmd = res.getMetaData();
            int nbCols = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= nbCols; i++) {
                    data.add(res.getString(i));
                }
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return data;
    }

    private int insertDelivery(int idRequester, ArrayList<String> data, Connection con, int idDeliveryOld) {
        int idDelivery = -1;
        try {
            PreparedStatement ps = Config.con.prepareStatement("INSERT INTO dmt_delivery VALUES (?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            ps.setInt(2, idRequester);
            for (int i = 0; i < data.size(); i++) {
                ps.setString((i + 3), data.get(i));
            }
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    idDelivery = res.getInt(1);
                    ArrayList<Integer> list = getIDUsage(idDeliveryOld, con);
                    for (int i = 0; i < list.size(); i++) {
                        ps = Config.con.prepareStatement("INSERT INTO dmt_choose VALUES (?,?)");
                        ps.setInt(1, idDelivery);
                        ps.setInt(2, list.get(i));
                        int result2 = ps.executeUpdate();
                    }
                    ArrayList<Integer> IDImages = getIDImages(idDeliveryOld, con);
                    for (int i = 0; i < IDImages.size(); i++) {
                        ps = Config.con.prepareStatement("INSERT INTO dmt_deliver VALUES (?,?)");
                        ps.setInt(1, IDImages.get(i));
                        ps.setInt(2, idDelivery);
                        int result3 = ps.executeUpdate();
                    }
                }
            }
            labTask.setText(new StringBuilder(NewRequester).append(" ").append(I18N.get("DataRequestSync.added-successfully")).toString());
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return idDelivery;
    }

    private ArrayList<Integer> getIDImages(int idDelivery, Connection con) {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_image FROM dmt_deliver WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getInt(1));
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return list;
    }

    private ArrayList<Integer> getIDUsage(int idDelivery, Connection con) {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_usage FROM dmt_choose WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getInt(1));
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return list;
    }

    private ArrayList<String> findRequesterData(int idDelivery, Connection con) {
        ArrayList<String> RequesterData = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT firstname,"
                    + "familyname,othername,sex,adress,phone,email,profession,institution,nationality,"
                    + "interest_area,usefulness,comment FROM dmt_requester INNER JOIN dmt_delivery "
                    + "ON dmt_requester.id_requester = dmt_delivery.id_requester WHERE id_delivery = ?");
            ps.setInt(1, idDelivery);
            ResultSet res = ps.executeQuery();
            ResultSetMetaData rsmd = res.getMetaData();
            int nbCols = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= nbCols; i++) {
                    RequesterData.add(res.getString(i));
                }
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return RequesterData;
    }

    private int insertRequester(ArrayList<String> data) {
        try {
            PreparedStatement ps = Config.con.prepareStatement("INSERT INTO dmt_requester VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, null);
            for (int i = 0; i < data.size(); i++) {
                ps.setString((i + 2), data.get(i));
            }
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    NewRequester = new StringBuilder().append(data.get(0)).append(" ").append(data.get(1))
                            .append(" ").append(data.get(2)).toString();
                    labTask.setText(new StringBuilder(NewRequester).append(" ")
                            .append(I18N.get("DataRequestSync.has-been-found")).toString());
                    return res.getInt(1);
                }
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
        return -1;
    }

    private int doCount(String sql, Connection con, String value) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, value);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        return 0;
    }

    private void confirmSync(int idDeliveryOld, Connection con) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE dmt_delivery SET confirm_request_treated = ? "
                    + "WHERE id_delivery = ?");
            ps.setString(1, "Yes");
            ps.setInt(2, idDeliveryOld);
            int result = ps.executeUpdate();
            if (result == 1) {
                WorkbenchFrame.timer.start();
            }
        } catch (SQLException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                    ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BCancel;
    public static javax.swing.JProgressBar PBar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labTask;
    // End of variables declaration//GEN-END:variables
    private String NewRequester;
    private int number;
    private Thread thread;
}
