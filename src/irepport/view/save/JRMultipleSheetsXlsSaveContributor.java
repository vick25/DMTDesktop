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
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRMultipleSheetsXlsSaveContributor.java 3659 2010-03-31
 * 10:20:49Z shertage $
 */
public class JRMultipleSheetsXlsSaveContributor extends JRSaveContributor {

    /**
     *
     */
    private static final String EXTENSION_XLS = ".xls";

    /**
     *
     */
    public JRMultipleSheetsXlsSaveContributor(Locale locale, ResourceBundle resBundle) {
        super(locale, resBundle);
    }

    /**
     *
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().toLowerCase().endsWith(EXTENSION_XLS);
    }

    /**
     *
     */
    public String getDescription() {
        return getBundleString("file.desc.xls.multiple.sheets");
    }

    /**
     *
     */
    public void save(JasperPrint jasperPrint, File file) throws JRException {
        if (!file.getName().toLowerCase().endsWith(EXTENSION_XLS)) {
            file = new File(file.getAbsolutePath() + EXTENSION_XLS);
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
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            exporter.exportReport();
        }
    }
}
