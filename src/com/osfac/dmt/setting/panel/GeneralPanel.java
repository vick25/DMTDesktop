package com.osfac.dmt.setting.panel;

import com.jidesoft.dialog.AbstractDialogPage;

public class GeneralPanel extends javax.swing.JPanel {

    public GeneralPanel(AbstractDialogPage page) {
        this.page = page;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TabbedPaneAll = new com.jidesoft.swing.JideTabbedPane();

        TabbedPaneAll.setBoldActiveTab(true);
        TabbedPaneAll.setColorTheme(3);
        TabbedPaneAll.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabbedPaneAll, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabbedPaneAll, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static com.jidesoft.swing.JideTabbedPane TabbedPaneAll;
    // End of variables declaration//GEN-END:variables
    AbstractDialogPage page;
}
