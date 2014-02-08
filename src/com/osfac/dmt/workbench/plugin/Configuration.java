package com.osfac.dmt.workbench.plugin;

/**
 * The Workbench searches the JARs in its lib/ext directory for Configurations.
 * It instantiates each Configuration and calls its #configure method. <p> Note:
 * It is preferable to use Extensions rather than Configurations, as Extensions
 * specify metadata like name and version.
 *
 * @see Extension
 */
public interface Configuration {

    public void configure(PlugInContext context) throws Exception;
}
