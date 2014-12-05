package com.cadplan.jump;

import com.osfac.dmt.workbench.plugin.Extension;
import com.osfac.dmt.workbench.plugin.PlugInContext;

public class VertexSymbolsExtension extends Extension
{
    public void configure(PlugInContext context) throws Exception
    {
        new VertexSymbolsPlugIn().initialize(context);
    }
}
