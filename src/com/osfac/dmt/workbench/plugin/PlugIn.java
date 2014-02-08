package com.osfac.dmt.workbench.plugin;

/**
 * <p> Plug-ins are code modules that can be easily added to or removed from
 * JUMP Workbench. For example, each menu item in the JUMP Workbench is a
 * PlugIn. Typically plug-ins are executed with a menu item -- FeatureInstaller
 * has methods for adding plug-ins as menu items. Alternatively, a plug-in need
 * not be associated with a menu-item; it might, for example, simply run some
 * code when the Workbench starts up. </p>
 *
 * <p> "Built-in" plug-ins are configured in a Setup class. Third-party plug-ins
 * reside in a JAR file that also contains an Extension class that configures
 * them. During development, third-party plug-ins may be specified in the
 * workbench-properties.xml file, to avoid having to build a JAR file. </p>
 *
 * @see com.osfac.dmt.workbench.Setup
 * @see Extension
 * @see PlugInManager
 */
public interface PlugIn {

    /**
     * Called when Workbench starts up to allow plugins to initialize
     * themselves.
     */
    public void initialize(PlugInContext context) throws Exception;

    /**
     * Performs the action for this plugin. For threaded plugins with dialogs,
     * this method contains the code to invoke the dialog. If the user cancels
     * the dialog, this method should return
     * <code>false</code> to prevent the run method from being called.
     *
     * @return true if the action completed, false if it was aborted. Used by
     * ThreadedPlugIns to indicate that their #run method needn't be called
     * next.
     * @throws Exception if a problem occurs during plug-in execution
     * @see ThreadedPlugIn
     */
    public boolean execute(PlugInContext context) throws Exception;

    /**
     * Returns a very brief description of this PlugIn e.g. for display as a
     * menu item
     *
     * @return the name of this PlugIn
     */
    public String getName();
}
