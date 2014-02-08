package com.osfac.dmt.workbench.ui;

import com.jidesoft.swing.JideSwingUtilities;
import com.osfac.dmt.I18N;
import javax.swing.ImageIcon;

public class ShortCut extends javax.swing.JPanel {

    public ShortCut() {
        initComponents(I18N.DMTResourceBundle);
        CPTutorial.setContentPane(JideSwingUtilities.createTopPanel(new panTutorial()));
        CPRequest.setContentPane(JideSwingUtilities.createTopPanel(new panRequest()));
        CPTutorial.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/bookcase22x22.png")));
        CPRequest.setIcon(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/1_folder2.png")));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        collapsiblePanes2 = new com.jidesoft.pane.CollapsiblePanes();
        CPTutorial = new com.jidesoft.pane.CollapsiblePane();
        CPRequest = new com.jidesoft.pane.CollapsiblePane();

        CPTutorial.setFocusable(false);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("language/dmt_en"); // NOI18N
        CPTutorial.setTitle(bundle.getString("ShortCut.CPTutorial.title")); // NOI18N

        CPRequest.setFocusable(false);
        CPRequest.setTitle(bundle.getString("ShortCut.CPRequest.title")); // NOI18N

        javax.swing.GroupLayout collapsiblePanes2Layout = new javax.swing.GroupLayout(collapsiblePanes2);
        collapsiblePanes2.setLayout(collapsiblePanes2Layout);
        collapsiblePanes2Layout.setHorizontalGroup(
            collapsiblePanes2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(CPTutorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(CPRequest, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );
        collapsiblePanes2Layout.setVerticalGroup(
            collapsiblePanes2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(collapsiblePanes2Layout.createSequentialGroup()
                .addComponent(CPTutorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CPRequest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(collapsiblePanes2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collapsiblePanes2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents(java.util.ResourceBundle bundle) {

        collapsiblePanes2 = new com.jidesoft.pane.CollapsiblePanes();
        CPTutorial = new com.jidesoft.pane.CollapsiblePane();
        CPRequest = new com.jidesoft.pane.CollapsiblePane();

        CPTutorial.setFocusable(false);
        CPTutorial.setTitle(bundle.getString("ShortCut.CPTutorial.title")); // NOI18N

        CPRequest.setFocusable(false);
        CPRequest.setTitle(bundle.getString("ShortCut.CPRequest.title")); // NOI18N

        javax.swing.GroupLayout collapsiblePanes2Layout = new javax.swing.GroupLayout(collapsiblePanes2);
        collapsiblePanes2.setLayout(collapsiblePanes2Layout);
        collapsiblePanes2Layout.setHorizontalGroup(
                collapsiblePanes2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(CPTutorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CPRequest, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE));
        collapsiblePanes2Layout.setVerticalGroup(
                collapsiblePanes2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(collapsiblePanes2Layout.createSequentialGroup()
                .addComponent(CPTutorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CPRequest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(collapsiblePanes2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(collapsiblePanes2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.pane.CollapsiblePane CPRequest;
    private com.jidesoft.pane.CollapsiblePane CPTutorial;
    private com.jidesoft.pane.CollapsiblePanes collapsiblePanes2;
    // End of variables declaration//GEN-END:variables
}
