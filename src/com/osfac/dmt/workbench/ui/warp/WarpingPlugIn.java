package com.osfac.dmt.workbench.ui.warp;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxPlugIn;
import java.awt.BorderLayout;
import javax.swing.JComponent;

public class WarpingPlugIn extends ToolboxPlugIn {

    public void initialize(PlugInContext context) throws Exception {
        createMainMenuItem(
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_WARP},
                GUIUtil.toSmallIcon(IconLoader.icon("GoalFlag.gif")),
                context.getWorkbenchContext());
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        WarpingPanel warpingPanel = new WarpingPanel(toolbox);
        toolbox.getCenterPanel().add(warpingPanel, BorderLayout.CENTER);
        add(new DrawWarpingVectorTool(), false, toolbox, warpingPanel);
        add(new DeleteWarpingVectorTool(), false, toolbox, warpingPanel);
        toolbox.getToolBar().addSeparator();
        add(new DrawIncrementalWarpingVectorTool(warpingPanel), true, toolbox, warpingPanel);
        add(new DeleteIncrementalWarpingVectorTool(warpingPanel), true, toolbox, warpingPanel);
        //Set y so it is positioned below Editing toolbox. [Bob Boseko]
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 175, false));
    }

    private void add(
            CursorTool tool,
            final boolean incremental,
            ToolboxDialog toolbox,
            final WarpingPanel warpingPanel) {
        //Logic for enabling either the incremental-warping-vector tools or the warping-vector
        //tools, depending on whether the Warp Incrementally checkbox is selected or not. [Bob Boseko]
        toolbox.add(tool, new EnableCheck() {
            public String check(JComponent component) {
                if (incremental && warpingPanel.isWarpingIncrementally()) {
                    return null;
                }
                if (!incremental && !warpingPanel.isWarpingIncrementally()) {
                    return null;
                }
                return I18N.get("ui.warp.WarpingPlugIn.incremental-warping-must-be") + " " + (incremental ? I18N.get("ui.warp.WarpingPlugIn.enabled") : I18N.get("ui.warp.WarpingPlugIn.disabled"));
            }
        });
    }
}
