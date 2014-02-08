package com.osfac.dmt.tools.update;

public class UpdateDB extends javax.swing.JPanel {

    public UpdateDB() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jideTabbedPane1 = new com.jidesoft.swing.JideTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        filterField = new com.jidesoft.grid.QuickTableFilterField();
        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        BRefresh = new com.jidesoft.swing.JideButton();
        jPanel1 = new javax.swing.JPanel();

        jideTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jideTabbedPane1.setBoldActiveTab(true);
        jideTabbedPane1.setColorTheme(3);
        jideTabbedPane1.setFocusable(false);

        filterField.setHintText("Type here to filter");
        filterField.setShowMismatchColor(true);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setRollover(true);

        BRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-48-clear.png"))); // NOI18N
        BRefresh.setText("Refresh");
        BRefresh.setFocusable(false);
        BRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(BRefresh);

        jToolBar2.add(jToolBar1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
        );

        jideTabbedPane1.addTab("Synchronise Database", new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/icon-16-clear.png")), jPanel2); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        jideTabbedPane1.addTab("Update Database", new javax.swing.ImageIcon(getClass().getResource("/com/osfac/dmt/images/database.png")), jPanel1); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jideTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jideTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRefreshActionPerformed
        
    }//GEN-LAST:event_BRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jidesoft.swing.JideButton BRefresh;
    private com.jidesoft.grid.QuickTableFilterField filterField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private com.jidesoft.swing.JideTabbedPane jideTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
