package com.osfac.dmt.workbench.ui.plugin;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.util.JConsole;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxPlugIn;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BeanShellPlugIn extends ToolboxPlugIn {

    private static final String sName = I18N.get("com.osfac.dmt.workbench.ui.plugin.BeanShellPlugIn.BeanShell-Console");

    public void initialize(PlugInContext context) throws Exception {
        // [Michael Michaud 2007-03-23]
        // Moves MenuNames.TOOLS/MenuNames.TOOLS_PROGRAMMING to MenuNames.CUSTOMIZE
        createMainMenuItem(new String[]{MenuNames.CUSTOMIZE}, null, context
                .getWorkbenchContext());
    }

    public String getName() {
        // [Michael Michaud 2007-03-23] Rename BeanShell to BeanShell Console to differentiate
        // from BeanShell scripts menus
        return sName;
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        try {
            final JConsole console = new JConsole();
            console.setPreferredSize(new Dimension(430, 240));
            console.print(I18N.get("ui.plugin.BeanShellPlugIn.the-workbenchcontext-may-be-referred-to-as-wc"));
            console.print(I18N.get("ui.plugin.BeanShellPlugIn.warning-pasting-in-multiple-statements-may-cause-the-application-to-freeze"));
            toolbox.getCenterPanel().add(console, BorderLayout.CENTER);
            Interpreter interpreter = new Interpreter(console);
            interpreter.setClassLoader(toolbox.getContext().getWorkbench()
                    .getPlugInManager().getClassLoader());
            interpreter.set("wc", toolbox.getContext());
            interpreter.eval("setAccessibility(true)");
            interpreter.eval("import com.vividsolutions.jts.geom.*");
            interpreter.eval("import com.osfac.dmt.feature.*");
            new Thread(interpreter).start();
        } catch (EvalError e) {
            toolbox.getContext().getErrorHandler().handleThrowable(e);
        }
    }
}