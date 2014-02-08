package irepport.view;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.util.Locale;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class JasperViewer extends javax.swing.JDialog {

    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
    protected JRViewer viewer = null;
    private boolean isExitOnClose = true;

    public JasperViewer(java.awt.Frame parent, boolean modal, InputStream is, boolean isXMLFile) throws JRException {
        this(parent, modal, is, isXMLFile, true);
    }

    public JasperViewer(java.awt.Frame parent, boolean modal, InputStream is, boolean isXMLFile, boolean isExitOnClose) throws JRException {
        this(parent, modal, is, isXMLFile, isExitOnClose, null);
    }

    public JasperViewer(java.awt.Frame parent, boolean modal, InputStream is, boolean isXMLFile, boolean isExitOnClose, Locale locale) throws JRException {
        super(parent, modal);
        if (locale != null) {
            setLocale(locale);
        }
        this.isExitOnClose = isExitOnClose;
        initComponents();
        this.viewer = new JRViewer(is, isXMLFile, locale);
        this.pnlMain.add(this.viewer, BorderLayout.CENTER);
    }

    public JasperViewer(java.awt.Frame parent, boolean modal, JasperPrint jasperPrint) {
        this(parent, modal, jasperPrint, true);
    }

    public JasperViewer(java.awt.Frame parent, boolean modal, JasperPrint jasperPrint, boolean isExitOnClose) {
        this(parent, modal, jasperPrint, isExitOnClose, null);
    }

    public JasperViewer(java.awt.Frame parent, boolean modal, JasperPrint jasperPrint, boolean isExitOnClose, Locale locale) {
        super(parent, modal);
        if (locale != null) {
            setLocale(locale);
        }
        this.isExitOnClose = isExitOnClose;

        initComponents();

        this.viewer = new JRViewer(jasperPrint, locale);
        this.pnlMain.add(this.viewer, BorderLayout.CENTER);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();

        setTitle(":::  Aperçu et impression  :::    OSFAC - DMT");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/osfacdmt/images/osfac1.png")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlMain.setLayout(new java.awt.BorderLayout());
        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-777)/2, (screenSize.height-611)/2, 777, 611);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (this.isExitOnClose) {
            System.exit(0);
        } else {
            this.setVisible(false);
            this.viewer.clear();
            this.viewer = null;
            this.getContentPane().removeAll();
            this.dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    public void setZoomRatio(float zoomRatio) {
        viewer.setZoomRatio(zoomRatio);
    }

    public void setFitWidthZoomRatio() {
        viewer.setFitWidthZoomRatio();
    }

    public void setFitPageZoomRatio() {
        viewer.setFitPageZoomRatio();
    }

    public static void viewReport(java.awt.Frame parent, boolean modal, InputStream is, boolean isXMLFile) throws JRException {
        viewReport(parent, modal, is, isXMLFile, true, null);
    }

    public static void viewReport(java.awt.Frame parent, boolean modal, InputStream is, boolean isXMLFile, boolean isExitOnClose) throws JRException {
        viewReport(parent, modal, is, isXMLFile, isExitOnClose, null);
    }

    public static void viewReport(java.awt.Frame parent, boolean modal, InputStream is, boolean isXMLFile, boolean isExitOnClose, Locale locale) throws JRException {
        JasperViewer jasperViewer = new JasperViewer(parent, modal, is, isXMLFile, isExitOnClose, locale);
        jasperViewer.setVisible(true);
    }

    public static void viewReport(java.awt.Frame parent, boolean modal, JasperPrint jasperPrint) {
        viewReport(parent, modal, jasperPrint, true, null);
    }

    public static void viewReport(java.awt.Frame parent, boolean modal, JasperPrint jasperPrint, boolean isExitOnClose) {
        viewReport(parent, modal, jasperPrint, isExitOnClose, null);
    }

    public static void viewReport(java.awt.Frame parent, boolean modal, JasperPrint jasperPrint, boolean isExitOnClose, Locale locale) {
        JasperViewer jasperViewer =
                new JasperViewer(parent, modal,
                jasperPrint,
                isExitOnClose,
                locale);
        jasperViewer.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
