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
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPdfSaveContributor.java 3035 2009-08-27 12:05:03Z teodord $
 */
public class JRPdfSaveContributor extends JRSaveContributor {

    /**
     *
     */
    private static final String EXTENSION_PDF = ".pdf";

    /**
     *
     */
    public JRPdfSaveContributor(Locale locale, ResourceBundle resBundle) {
        super(locale, resBundle);
    }

    /**
     *
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().toLowerCase().endsWith(EXTENSION_PDF);
    }

    /**
     *
     */
    public String getDescription() {
        return getBundleString("file.desc.pdf");
    }

    /**
     *
     */
    public void save(JasperPrint jasperPrint, File file) throws JRException {
        if (!file.getName().toLowerCase().endsWith(EXTENSION_PDF)) {
            file = new File(file.getAbsolutePath() + EXTENSION_PDF);
        }

        if (!file.exists()
                || JOptionPane.OK_OPTION
                == JOptionPane.showConfirmDialog(
                null,
                MessageFormat.format(
                getBundleString("file.exists"),
                new Object[]{file.getName()}),
                getBundleString("save"),
                JOptionPane.OK_CANCEL_OPTION)) {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
            exporter.exportReport();
        }
    }
}
