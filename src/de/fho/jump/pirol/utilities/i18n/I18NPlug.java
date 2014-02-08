package de.fho.jump.pirol.utilities.i18n;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.DMTWorkbench;
import de.fho.jump.pirol.utilities.debugOutput.DebugUserIds;
import de.fho.jump.pirol.utilities.debugOutput.PersonalLogger;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * Class taken from the RasterImage-i18N PlugIn of Jan Ruzicka
 * (jan.ruzicka@vsb.cz) and modified for PIROL.
 *
 */
public final class I18NPlug {

    protected static PersonalLogger logger = new de.fho.jump.pirol.utilities.debugOutput.PersonalLogger(
            DebugUserIds.ALL);
    public static boolean jumpi18n = true;
    private static HashMap plugInResourceBundle = new HashMap();

    /**
     * *
     * Set plugin I18N resource file
     *
     * Tries to use locale set in command line (if set)
     *
     * @param pluginName (path + name)
     *
     * @param bundle reference of the bundle file
     *
     */
    public static void setPlugInRessource(String pluginName, String bundle) {

        try {

            String local = DMTWorkbench.I18N_SETLOCALE;

        } catch (java.lang.NoSuchFieldError s) {

            jumpi18n = false;

        }

        if (jumpi18n == true) {

            if ("".equals(DMTWorkbench.I18N_SETLOCALE)) {

                // No locale has been specified at startup: choose default locale

                I18N.plugInsResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle));

                //logger.printDebug(I18N.plugInsResourceBundle.get(pluginName)+" "+bundle);

            } else {

                String lang = DMTWorkbench.I18N_SETLOCALE.split("_")[0];

                try {

                    String country = DMTWorkbench.I18N_SETLOCALE.split("_")[1];

                    Locale locale = new Locale(lang, country);

                    I18N.plugInsResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle, locale));

                    //logger.printDebug(I18N.plugInsResourceBundle.get(pluginName)+" "+bundle+" "+locale);

                } catch (java.lang.ArrayIndexOutOfBoundsException e) {

                    Locale locale = new Locale(lang);

                    I18N.plugInsResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle, locale));

                    //LOG.debug(I18N.plugInsResourceBundle.get(pluginName)+" "+bundle+" "+locale);

                }

            }

        } else {

            // in this case we use the default .properties file (en)

            I18NPlug.plugInResourceBundle.put(pluginName, ResourceBundle
                    .getBundle(bundle));

            //logger.printDebug(I18NPlug.plugInResourceBundle.get(pluginName)+" cz.vsb.gisak.jump.rasterimage");					

        }

    }

    /**
     *
     * Process text with the locale 'pluginName_<locale>.properties' file
     *
     *
     *
     * @param pluginName (path + name)
     *
     * @param label
     *
     * @return i18n label
     *
     */
    public static String get(String pluginName, String label) {

        if (jumpi18n == true) {

            /*
			 
             logger.printDebug(I18N.plugInsResourceBundle.get(pluginName)+" "+label
			 
             + ((ResourceBundle)I18N.plugInsResourceBundle
			 
             .get(pluginName))
			 
             .getString(label));
			 
             */

            return ((ResourceBundle) I18N.plugInsResourceBundle.get(pluginName)).getString(label);

        }

        return ((ResourceBundle) I18NPlug.plugInResourceBundle
                .get(pluginName))
                .getString(label);

    }

    /**
     *
     * Process text with the locale 'pluginName_<locale>.properties' file
     *
     *
     *
     * @param pluginName (path + name)
     *
     * @param label with argument insertion : {0}
     *
     * @param objects
     *
     * @return i18n label
     *
     */
    public static String getMessage(String pluginName, String label,
            Object[] objects) {

        if (jumpi18n == true) {

            MessageFormat mf = new MessageFormat(
                    ((ResourceBundle) I18N.plugInsResourceBundle.get(pluginName)).getString(label));

            return mf.format(objects);

        }

        MessageFormat mf = new MessageFormat(
                ((ResourceBundle) I18NPlug.plugInResourceBundle
                .get(pluginName))
                .getString(label));

        return mf.format(objects);

    }
}
