package com.osfac.dmt.tools.update.category;

import com.osfac.dmt.Config;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class AddCategory extends javax.swing.JDialog {

    public AddCategory(JFrame parent, boolean modal) {
        this.parent = parent;
        initComponents();
        Timer time = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtCategory.getText().equals("")) {
                    BValidate.setEnabled(true);
                } else {
                    BValidate.setEnabled(false);
                }
            }
        });
        time.start();
        this.setIconImage(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/view_multicolumn(10).png")).getImage());
        this.setLocationRelativeTo(this.parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        BValidate = new com.jidesoft.swing.JideButton();
        BClose = new com.jidesoft.swing.JideButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Category");
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setResizable(false);

        jLabel1.setText("Category");

        BValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/apply(5).png"))); // NOI18N
        BValidate.setText("OK");
        BValidate.setEnabled(false);
        BValidate.setFocusable(false);
        BValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BValidateActionPerformed(evt);
            }
        });

        BClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/exit16x16.png"))); // NOI18N
        BClose.setText("Close");
        BClose.setFocusable(false);
        BClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BClose, BValidate});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BValidate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

    private void BValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValidateActionPerformed
        if (uniqueness(txtCategory.getText())) {
            try {
                PreparedStatement ps = Config.con.prepareStatement("insert into dmt_category (category_name)"
                        + " values (?)");
                ps.setString(1, txtCategory.getText().toUpperCase());
                int result = ps.executeUpdate();
                if (result == 1) {
                    this.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_BValidateActionPerformed

    private void BCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_BCloseActionPerformed

    private boolean uniqueness(String category) {
        boolean unique = true;
        try {
            PreparedStatement ps = Config.con.prepareStatement("select * from dmt_category where "
                    + "category_name = ?");
            ps.setString(1, category);
            Config.res = ps.executeQuery();
            while (Config.res.next()) {
                JOptionPane.showMessageDialog(this, "The cathegory \""
                        + "" + category + "\" already exists in the database"
                        + "", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unique;
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Config dbConnection = new Config();
                AddCategory dialog = new AddCategory(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BClose;
    private com.jidesoft.swing.JideButton BValidate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCategory;
    // End of variables declaration//GEN-END:variables
    JFrame parent;
}
