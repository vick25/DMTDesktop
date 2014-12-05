package com.cadplan.jump;

import com.osfac.dmt.workbench.DMTWorkbench;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

//import org.apache.log4j.Logger;
public final class I18NPlug {

    boolean debug = false;
    private String language = "en";
    private String country = "AU";
    private Locale locale = null;
    ResourceBundle rb;
    String pluginName;

    public I18NPlug(String pluginName, String bundle) {
        this.pluginName = pluginName;

        if ("".equals(DMTWorkbench.I18N_SETLOCALE)) {
            locale = null;
        } else {
            StringTokenizer st = new StringTokenizer(DMTWorkbench.I18N_SETLOCALE, "_");
            language = st.nextToken();
            country = "";
            if (st.hasMoreTokens()) {
                country = st.nextToken();
                locale = new Locale(language, country);
            } else {
                locale = new Locale(language);
            }

        }

        //System.out.println("plugin name: "+pluginName+"  bundle:"+bundle);
        if (locale == null) {
            rb = ResourceBundle.getBundle(bundle);
        } else {
            rb = ResourceBundle.getBundle(bundle, locale);
        }

    }

    public String get(String label) {
        try {
            String text = rb.getString(label);
            return text;
        } catch (Exception ex) {
            System.out.println("ERROR Get - Missing language resource: " + label);
            return "<" + label + ">";
        }

    }
}
