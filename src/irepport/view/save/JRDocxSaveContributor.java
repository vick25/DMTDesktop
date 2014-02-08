package irepport.view.save;

import irepport.view.JRSaveContributor;
import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

public class JRDocxSaveContributor extends JRSaveContributor {

    private static final String EXTENSION_DOCX = ".docx";

    public JRDocxSaveContributor(Locale locale, ResourceBundle resBundle) {
        super(locale, resBundle);
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().toLowerCase().endsWith(EXTENSION_DOCX);
    }

    public String getDescription() {
        return getBundleString("file.desc.docx");
    }

    public void save(JasperPrint jasperPrint, File file) throws JRException {
        if (!file.getName().toLowerCase().endsWith(EXTENSION_DOCX)) {
            file = new File(file.getAbsolutePath() + EXTENSION_DOCX);
        }

        if (!file.exists() || JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                null, MessageFormat.format(getBundleString("file.exists"),
                new Object[]{file.getName()}), getBundleString("save"), JOptionPane.OK_CANCEL_OPTION)) {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
            exporter.exportReport();
        }
    }
}
