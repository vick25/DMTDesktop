package com.osfac.dmt.workbench.ui;

import com.osfac.dmt.I18N;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class panTutorial extends javax.swing.JPanel {

    public panTutorial() {
        initComponents(I18N.DMTResourceBundle);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BOsfac = new com.jidesoft.swing.JideButton();
        BAster = new com.jidesoft.swing.JideButton();
        BSpot = new com.jidesoft.swing.JideButton();

        setOpaque(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        BOsfac.setText(bundle.getString("panTutorial.BOsfac.text")); // NOI18N
        BOsfac.setButtonStyle(3);
        BOsfac.setFocusable(false);
        BOsfac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOsfacActionPerformed(evt);
            }
        });

        BAster.setText(bundle.getString("panTutorial.BAster.text")); // NOI18N
        BAster.setButtonStyle(3);
        BAster.setFocusable(false);
        BAster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAsterActionPerformed(evt);
            }
        });

        BSpot.setText(bundle.getString("panTutorial.BSpot.text")); // NOI18N
        BSpot.setButtonStyle(3);
        BSpot.setFocusable(false);
        BSpot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSpotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BOsfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BAster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BSpot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BOsfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BAster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BSpot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        BOsfac = new com.jidesoft.swing.JideButton();
        BAster = new com.jidesoft.swing.JideButton();
        BSpot = new com.jidesoft.swing.JideButton();

        setOpaque(false);

        BOsfac.setText(bundle.getString("panTutorial.BOsfac.text")); // NOI18N
        BOsfac.setButtonStyle(3);
        BOsfac.setFocusable(false);
        BOsfac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOsfacActionPerformed(evt);
            }
        });

        BAster.setText(bundle.getString("panTutorial.BAster.text")); // NOI18N
        BAster.setButtonStyle(3);
        BAster.setFocusable(false);
        BAster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAsterActionPerformed(evt);
            }
        });

        BSpot.setText(bundle.getString("panTutorial.BSpot.text")); // NOI18N
        BSpot.setButtonStyle(3);
        BSpot.setFocusable(false);
        BSpot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSpotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BOsfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BAster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BSpot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(35, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BOsfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BAster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BSpot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)));
    }

    private void BOsfacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOsfacActionPerformed
//        try {
//            Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
//                    + "" + new File("osfac.pdf"));
//            p.waitFor();
//        } catch (IOException | InterruptedException ex) {
//            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"), ex.getMessage(), null, null, ex, Level.SEVERE, null));
//        }
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://www.osfac.net/index.php?option=com_content&view=article&id=6&Itemid=162&lang=en"));
            } catch (URISyntaxException | IOException e) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        e.getMessage(), null, null, e, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BOsfacActionPerformed

    private void BAsterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAsterActionPerformed
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://carpe.umd.edu/forest_monitoring/satellite_data_clearinghouse.php#ASTER"));
            } catch (URISyntaxException | IOException e) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        e.getMessage(), null, null, e, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BAsterActionPerformed

    private void BSpotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSpotActionPerformed
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://carpe.umd.edu/forest_monitoring/satellite_data_clearinghouse.php#SPOT"));
            } catch (URISyntaxException | IOException e) {
                JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error"),
                        e.getMessage(), null, null, e, Level.SEVERE, null));
            }
        }
    }//GEN-LAST:event_BSpotActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BAster;
    private com.jidesoft.swing.JideButton BOsfac;
    private com.jidesoft.swing.JideButton BSpot;
    // End of variables declaration//GEN-END:variables
}
