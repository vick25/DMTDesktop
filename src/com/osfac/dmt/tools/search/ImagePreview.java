package com.osfac.dmt.tools.search;

import com.osfac.dmt.Config;
import com.osfac.dmt.I18N;
import com.osfac.dmt.JImagePanel;
import com.osfac.dmt.setting.SettingKeyFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class ImagePreview extends javax.swing.JDialog {

    public ImagePreview(java.awt.Frame parent, boolean modal, String pathImage, String imageName) {
        super(parent, modal);
        this.pathImage = pathImage;
        panImage = new JImagePanel();
        panImage.setLayout(new BorderLayout());
        panImage.setAutoSize(true);
        panImage.setOpaque(true);
        panImage.setBackground(Color.white);
        String pathimg = Config.pref.get(SettingKeyFactory.OtherFeatures.HOST, "http://www.osfac.net") + pathImage;
        try {
            panImage.setImage(new ImageIcon(new URL(pathimg)).getImage());
        } catch (MalformedURLException e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        panImage.repaint();
        initComponents();
        this.setTitle(I18N.get("ImagePreview.Quick-look-of") + imageName);
        this.setIconImage(new ImageIcon(getClass().getResource("/com/osfac/dmt/images/arrow_out.png")).getImage());
        this.setLocationRelativeTo(parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane(panImage);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    JImagePanel panImage;
    String pathImage;
}
