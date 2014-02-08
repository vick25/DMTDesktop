package com.osfac.dmt.workbench.ui.plugin.datastore;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.datastore.ConnectionManager;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxPlugIn;
import java.awt.BorderLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

public class ConnectionManagerToolboxPlugIn extends ToolboxPlugIn {

    private static final String INSTANCE_KEY = ConnectionManagerToolboxPlugIn.class
            .getName() + " - INSTANCE";

    private ConnectionManagerToolboxPlugIn() {
    }

    public String getName() {
        // Specify name explicitly, as auto-name-generator
        // says "Connection Manager Toolbox" [Bob Boseko 2005-03-14]
        return I18N.get("jump.workbench.ui.plugin.datastore.ConnectionManagerToolboxPlugIn.Connection-Manager");
    }

    public static final ConnectionManagerToolboxPlugIn instance(
            Blackboard blackboard) {
        if (blackboard.get(INSTANCE_KEY) == null) {
            blackboard.put(INSTANCE_KEY, new ConnectionManagerToolboxPlugIn());
        }
        return (ConnectionManagerToolboxPlugIn) blackboard.get(INSTANCE_KEY);
    }

    public void initialize(final PlugInContext context) throws Exception {
        if (1 == 1) {
            throw new UnsupportedOperationException(
                    "To do: fix: ConnectionManagerToolbox does not stay in sync with ConnectionManager object. Implement eventing. [Bob Boseko 2005-03-24]");
        }
        new FeatureInstaller(context.getWorkbenchContext()).addMainMenuItem(
                this, (new String[]{MenuNames.VIEW}), getName() + "...{pos:1}",
                true, null, new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component).setSelected(getToolbox(
                        context.getWorkbenchContext()).isVisible());
                return null;
            }
        });
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        ConnectionManagerPanel connectionManagerPanel = new ConnectionManagerPanel(
                ConnectionManager
                .instance(toolbox.getContext()),
                toolbox.getContext().getRegistry(), toolbox.getContext()
                .getErrorHandler(), toolbox.getContext());
        toolbox.getCenterPanel().add(connectionManagerPanel,
                BorderLayout.CENTER);
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 20, false));
    }
}