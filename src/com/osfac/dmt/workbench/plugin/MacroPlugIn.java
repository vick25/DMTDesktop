package com.osfac.dmt.workbench.plugin;

/**
 * A sequence of plug-ins treated as one.
 */
public class MacroPlugIn extends AbstractPlugIn {

    protected PlugIn[] plugIns;

    public MacroPlugIn(PlugIn[] plugIns) {
        this.plugIns = (PlugIn[]) plugIns.clone();
    }

    public void initialize(PlugInContext context) throws Exception {
        for (int i = 0; i < plugIns.length; i++) {
            plugIns[i].initialize(context);
        }
    }

    public boolean execute(PlugInContext context) throws Exception {
        for (int i = 0; i < plugIns.length; i++) {
            if (!plugIns[i].execute(context)) {
                return false;
            }
        }

        return true;
    }

    public String getName() {
        String name = "";

        for (int i = 0; i < plugIns.length; i++) {
            if (i > 0) {
                name += " + ";
            }

            name += plugIns[i].getName();
        }

        return name;
    }
}
