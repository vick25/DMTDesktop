package com.osfac.dmt.workbench.plugin;

import com.osfac.dmt.util.StringUtil;

/**
 * The "entry point" into a JAR file containing PlugIns. The Workbench searches
 * the JARs in its lib/ext directory for Extensions. It instantiates each
 * Extension and calls its #configure method.
 */
public abstract class Extension implements Configuration {

    public String getName() {
        //Package is null if default package. [Bob Boseko]
        return StringUtil.toFriendlyName(getClass().getName(), "Extension")
                + (getClass().getPackage() == null
                ? ""
                : " (" + getClass().getPackage().getName() + ")");
    }

    public String getVersion() {
        return "";
    }
}
