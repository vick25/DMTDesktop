package com.osfac.dmt.setting;

import com.jidesoft.icons.IconsFactory;
import javax.swing.ImageIcon;

public class SettingIconsFactory {

    public static class Options {

        public static final String GENERAL = "/com/osfac/dmt/setting/images/general.png";
        public static final String PRIVACY = "/com/osfac/dmt/setting/images/privacy.png";
        public static final String WEB = "/com/osfac/dmt/setting/images/web.png";
        public static final String THEMES = "/com/osfac/dmt/setting/images/themes.png";
        public static final String LANGUAGE = "/com/osfac/dmt/setting/images/language.png";
        public static final String FONTSCOLOR = "/com/osfac/dmt/setting/images/fontscolor.png";
        public static final String CHARTFEATURE = "/com/osfac/dmt/setting/images/kchart22x22.png";
        public static final String DIALOGICON = "/com/osfac/dmt/images/settings.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null) {
            return IconsFactory.getImageIcon(SettingIconsFactory.class, name);
        } else {
            return null;
        }
    }
//    public static void main(String[] argv) {
//        IconsFactory.generateHTML(SettingIconsFactory.class);
//    }
}
