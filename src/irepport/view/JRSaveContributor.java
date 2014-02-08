package irepport.view;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRSaveContributor.java 3678 2010-04-02 12:13:06Z shertage $
 */
public abstract class JRSaveContributor extends FileFilter {

    private Locale locale = null;
    private ResourceBundle resourceBundle = null;

    /**
     *
     */
    public JRSaveContributor() {
        this(null, null);
    }

    /**
     *
     */
    public JRSaveContributor(Locale locale, ResourceBundle resBundle) {
        if (locale != null) {
            this.locale = locale;
        } else {
            this.locale = Locale.getDefault();
        }

        if (resBundle == null) {
            this.resourceBundle = ResourceBundle.getBundle("irepport/view/viewer", this.locale);
        } else {
            this.resourceBundle = resBundle;
        }
    }

    /**
     *
     */
    protected String getBundleString(String key) {
        return resourceBundle.getString(key);
    }

    /**
     *
     */
    public abstract void save(JasperPrint jasperPrint, File file) throws JRException;
}
