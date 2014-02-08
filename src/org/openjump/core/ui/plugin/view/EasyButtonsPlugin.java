package org.openjump.core.ui.plugin.view;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxPlugIn;
import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

public class EasyButtonsPlugin extends ToolboxPlugIn {

    private String TOOLBOX_NAME = I18N.get("org.openjump.core.ui.plugin.view.EasyButtonsPlugin.EZ-Buttons");
    private JPopupMenu popup = new JPopupMenu();

    public void initialize(PlugInContext context) throws Exception {
        createMainMenuItem(
                new String[]{MenuNames.CUSTOMIZE},
                getIcon(),
                context.getWorkbenchContext());
    }

    public String getName() {
        return TOOLBOX_NAME;
    }

    public Icon getIcon() {
        return new ImageIcon(getClass().getResource("ez.png"));
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        EasyPanel buttonPanel = new EasyPanel(toolbox);
        toolbox.getCenterPanel().add(buttonPanel, BorderLayout.CENTER);
        toolbox.setInitialLocation(new GUIUtil.Location(10, true, 270, false));
//		toolbox.setIconImage(((ImageIcon)getIcon()).getImage());
    }

    private void add(
            CursorTool tool,
            final boolean incremental,
            ToolboxDialog toolbox,
            final EasyPanel warpingPanel) {
        toolbox.add(tool, new EnableCheck() {
            public String check(JComponent component) {
                return null;
            }
        });
    }
}
