package irepport.view.save;

import irepport.view.JRSaveContributor;
import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRSaver;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPrintSaveContributor.java 3035 2009-08-27 12:05:03Z teodord $
 */
public class JRPrintSaveContributor extends JRSaveContributor {

    /**
     *
     */
    private static final String EXTENSION_JRPRINT = ".jrprint";

    /**
     *
     */
    public JRPrintSaveContributor(Locale locale, ResourceBundle resBundle) {
        super(locale, resBundle);
    }

    /**
     *
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().toLowerCase().endsWith(EXTENSION_JRPRINT);
    }

    /**
     *
     */
    public String getDescription() {
        return getBundleString("file.desc.jrprint");
    }

    /**
     *
     */
    public void save(JasperPrint jasperPrint, File file) throws JRException {
        if (!file.getName().toLowerCase().endsWith(EXTENSION_JRPRINT)) {
            file = new File(file.getAbsolutePath() + EXTENSION_JRPRINT);
        }

        if (!file.exists() || JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                null,
                MessageFormat.format(
                getBundleString("file.exists"),
                new Object[]{file.getName()}),
                getBundleString("save"),
                JOptionPane.OK_CANCEL_OPTION)) {
            JRSaver.saveObject(jasperPrint, file);
        }
    }
}
