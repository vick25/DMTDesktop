package de.latlon.deejump.plugin.manager;

import com.osfac.dmt.workbench.plugin.Extension;
import com.osfac.dmt.workbench.plugin.PlugInContext;

public class ExtensionManagerExtension extends Extension {
    
    public void configure(PlugInContext context) throws Exception {
        new ExtensionManagerPlugIn().initialize( context );
    }
    public String getName() {
        return "Extension Manager Extension";
    }
    
}
