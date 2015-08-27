package com.osfac.dmt;

import com.osfac.dmt.setting.SettingKeyFactory;
import com.osfac.dmt.workbench.DMTWorkbench;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Singleton for the Internationalization (I18N)
 *
 * <pre>
 * [1] HOWTO TRANSLATE JUMP IN MY OWN LANGUAGE
 *  Copy theses files and add the locales extension for your language and country instead of the *.
 *  - resources/jump_*.properties
 *  - com/vividsolutions/jump/workbench/ui/plugin/KeyboardPlugIn_*.html
 * [2] HOWTO TRANSLATE MY PLUGIN AND GIVE THE ABILITY TO TRANSLATE IT
 *  Use theses methods to use your own *.properties files :
 *  [Michael Michaud 2007-03-23] the 3 following methods have been deactivated
 *  com.osfac.dmt.I18N#setPlugInRessource(String, String)
 *  com.osfac.dmt.I18N#get(String, String)
 *  com.osfac.dmt.I18N#getMessage(String, String, Object[])
 *  you still can use plugInsResourceBundle (as in Pirol's plugin)
 *
 *  And use jump standard menus
 * </pre>
 *
 * Code example : [Michael Michaud 2007-03-23 : the following code example is no more valid and has
 * to be changed]
 *
 * <pre>
 * public class PrintPlugIn extends AbstractPlugIn
 *  {
 *    private String name = &quot;print&quot;;
 * public void initialize(PlugInContext context) throws Exception
 * {
 *   I18N.setPlugInRessource(name, &quot;org.agil.core.jump.plugin.print&quot;);
 *   context.getFeatureInstaller().addMainMenuItem(this,
 *                                                 new String[]
 *                                                 {MenuNames.TOOLS,I18N.get(name, &quot;print&quot;)},
 *                                                 I18N.get(name, &quot;print&quot;), false, null, null);
 * }
 * ...
 * </pre>
 *
 * <pre>
 * TODO :I18N (1) Improve translations
 * TODO :I18N (2) Separate config (customization) and I18N
 * TODO :I18N (3) Explore and discuss about I18N integration and Jakarta Common Ressources
 * (using it as a ressource interface)
 * </pre>
 *
 * @author Basile Chandesris - <chandesris@pt-consulting.lu>
 * @see com.osfac.dmt.workbench.ui.MenuNames
 *
 * @see com.osfac.dmt.workbench.ui.VTextIcon text rotation
 */
public final class I18N {

    private static final Logger LOG = Logger.getLogger(I18N.class);
    // [Michael Michaud 2007-03-23] removed SingletonHolder internal class
    // 1 - getInstance is enough to guarantee I18N instance unicity
    // 2 - I18N should not be instanciated as the class has only static methods
    private static final I18N instance = new I18N();
    // use 'jump<locale>.properties' i18n mapping file
    // STanner changed the place where are stored bundles. Now are in /language
    // public static ResourceBundle rb =
    // ResourceBundle.getBundle("com.osfac.dmt.jump");
    public static ResourceBundle DMTResourceBundle = ResourceBundle.getBundle(new StringBuilder("language/dmt_").
            append(Config.pref.get(SettingKeyFactory.Language.ABREV, "en")).toString());
//    public static ResourceBundle DMTResourceBundle = ResourceBundle.getBundle("language/dmt_en");
    // [Michael Michaud 2007-03-23] plugInsResourceBundle is deactivated because
    // all the methods
    // using it have been deactivated.
    // [sstein] activated again since Pirol does use it
    public static HashMap plugInsResourceBundle = new HashMap();
    /**
     * The map from category names to I18N instances.
     */
    private static Map<String, I18N> instances = new HashMap<>();
    private static ClassLoader classLoader;
    /**
     * The resource bundle for the I18N instance.
     */
    private ResourceBundle resourceBundle;

    /**
     * The core OpenJUMP I18N instance.
     */
    private I18N() {
        resourceBundle = DMTResourceBundle;
    }

    /**
     * Construct an I18N instance for the category.
     *
     * @param resourcePath The path to the language files.
     */
    private I18N(final String resourcePath) {
        resourceBundle = ResourceBundle.getBundle(resourcePath,
                Locale.getDefault(), classLoader);
    }

    /**
     * Set the class loader used to load resource bundles, must only be called by the plug-in
     * loader.
     *
     * @param classLoader the classLoader to set
     */
    public static void setClassLoader(ClassLoader classLoader) {
        I18N.classLoader = classLoader;
    }

    /**
     * Get the I18N text from the language file associated with this instance. If no label is
     * defined then a default string is created from the last part of the key.
     *
     * @param key The key of the text in the language file.
     * @return The I18Nized text.
     */
    public String getText(final String key) {
        try {
            return resourceBundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            String[] labelpath = key.split("\\.");
            LOG.debug(new StringBuilder("No resource bundle or no translation found for the key : ").
                    append(key).toString());
            return labelpath[labelpath.length - 1];
        }
    }

    /**
     * Get the I18N text from the language file associated with the specified category. If no label
     * is defined then a default string is created from the last part of the key.
     *
     * @param category The category.
     * @param key The key of the text in the language file.
     * @return The I18Nized text.
     */
    public static String getText(final String category, final String key) {
        I18N i18n = getInstance(category);
        return i18n.getText(key);
    }

    /**
     * Get the I18N instance for the category. A resource file must exist in the resource path for
     * language/jump for the category.
     *
     * @param category The category.
     * @return The instance.
     */
    public static I18N getInstance(final String category) {
        I18N instance = instances.get(category);
        if (instance == null) {
            String resourcePath = new StringBuilder(category.replace('.', '/')).append("/language/dmt").toString();
            instance = new I18N(resourcePath);
            instances.put(category, instance);
        }
        return instance;
    }

    public static I18N getInstance() {
        // [Michael Michaud 2007-03-04] guarantee I18N instance unicity without
        // creating a SingletonHolder inner class instance
        return (instance == null) ? new I18N() : instance;
        // return SingletonHolder._singleton;
    }

    // [ede] utility method as it is used in several places (loadFile,getLanguage...)
    public static Locale fromCode(final String localeCode) {
        // [Michael Michaud 2007-03-04] handle the case where lang is the only
        // variable instead of catching an ArrayIndexOutOfBoundsException
        String[] lc = localeCode.split("_");
        Locale locale = Locale.getDefault();
        if (lc.length > 1) {
            LOG.debug(new StringBuilder("lang:").append(lc[0]).append(" ").append("country:").append(lc[1]).toString());
            locale = new Locale(lc[0], lc[1]);
        } else if (lc.length > 0) {
            LOG.debug(new StringBuilder("lang:").append(lc[0]).toString());
            locale = new Locale(lc[0]);
        } else {
            LOG.debug(new StringBuilder(localeCode).
                    append(" is an illegal argument to define lang [and country]").toString());
        }

        return locale;
    }

    /**
     * Load file specified in command line (-i18n lang_country) (lang_country :language 2 letters +
     * "_" + country 2 letters) Tries first to extract lang and country, and if only lang is
     * specified, loads the corresponding resource bundle.
     *
     * @param langcountry
     */
    public static void loadFile(final String langcountry) {
        DMTResourceBundle = ResourceBundle.getBundle(new StringBuilder("language/dmt_").append(Config.pref.get(SettingKeyFactory.Language.ABREV, "en")).toString(),
                fromCode(langcountry));
    }

    public static void applyToRuntime() {
        Locale loc = DMTResourceBundle.getLocale();
        Locale.setDefault(loc);
        System.setProperty("user.language", loc.getLanguage());
        System.setProperty("user.country", loc.getCountry());
    }

    /**
     * Process text with the LOCALE 'jump_<locale>.properties' file
     *
     * @param label
     * @return i18n label [Michael Michaud 2007-03-23] If no resourcebundle is found, returns a
     * default string which is the last part of the label
     */
    public static String get(final String label) {
        try {
            return DMTResourceBundle.getString(label);
        } catch (java.util.MissingResourceException e) {
            String[] labelpath = label.split("\\.");
            LOG.debug(new StringBuilder("No resource bundle or no translation found for the key : ").append(label).toString());
            return labelpath[labelpath.length - 1];
        }
    }

    /**
     * Get the short signature for LOCALE (letters extension :language 2 letters + "_" + country 2
     * letters)
     *
     * @return string signature for LOCALE
     */
    public static String getLocale() {
        return new StringBuilder(DMTResourceBundle.getLocale().getLanguage()).
                append("_").append(DMTResourceBundle.getLocale().getCountry()).toString();
    }

    /**
     * Get the short signature for language (letters extension :language 2 letters)
     *
     * @return string signature for language
     */
    public static String getLanguage() {
        if ("".equals(DMTWorkbench.I18N_SETLOCALE)) {
            // No LOCALE has been specified at startup: choose default LOCALE
            return DMTResourceBundle.getLocale().getLanguage();
        } else {
            return fromCode(DMTWorkbench.I18N_SETLOCALE).getLanguage();
        }
    }

    /**
     * Get the short signature for country (2 letter code)
     *
     * @return string signature for country
     */
    public static String getCountry() {
        if ("".equals(DMTWorkbench.I18N_SETLOCALE)) {
            // No LOCALE has been specified at startup: choose default LOCALE
            return DMTResourceBundle.getLocale().getCountry();
        } else {
            return fromCode(DMTWorkbench.I18N_SETLOCALE).getCountry();
        }
    }

    /**
     * Process text with the LOCALE 'jump_<locale>.properties' file If no resourcebundle is found,
     * returns default string contained inside com.osfac.dmt.jump
     *
     * @param label with argument insertion : {0}
     * @param objects
     * @return i18n label
     */
    public static String getMessage(final String label, final Object[] objects) {
        try {
            final MessageFormat mformat = new MessageFormat(DMTResourceBundle.getString(label));
            return mformat.format(objects);
        } catch (java.util.MissingResourceException e) {
            final String[] labelpath = label.split("\\.");
            LOG.warn(new StringBuilder(e.getMessage()).append(" no default value, the resource key is used: ").
                    append(labelpath[labelpath.length - 1]).toString());
            final MessageFormat mformat = new MessageFormat(
                    labelpath[labelpath.length - 1]);
            return mformat.format(objects);
        }
    }

    /**
     * Get the I18N text from the language file associated with the specified category. If no label
     * is defined then a default string is created from the last part of the key.
     *
     * @param category The category.
     * @param label with argument insertion : {0}
     * @param objects
     * @return i18n label
     */
    public static String getMessage(final String category, final String label, final Object[] objects) {
        I18N i18n = getInstance(category);
        return i18n.getMessage(label, objects);
    }
}
