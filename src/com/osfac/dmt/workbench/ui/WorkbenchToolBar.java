package com.osfac.dmt.workbench.ui;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.DMTCommandBarFactory;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.cursortool.QuasimodeTool;
import com.osfac.dmt.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jts.util.Assert;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Makes it easy to add CursorTools and PlugIns as toolbar buttons. An "EnableCheck" is used to
 * specify whether to enable or disable the CursorTool buttons. Moreover, CursorTools are added as
 * mutually exclusive toggle buttons (that is, JToggleButtons in a ButtonGroup). When a CursorTool
 * button is pressed, the current CursorTool is unregistered and the new one is registered with the
 * LayerViewPanel.
 * <P>
 * Set the cursor-tool-enable-check to use context-sensitive enabling of toolbar buttons.
 * <P>
 * Set the task-monitor-manager to report the progress of threaded plug-ins.
 */
public class WorkbenchToolBar extends EnableableToolBar {

    private HashMap cursorToolClassToButtonMap = new HashMap();
    private LayerViewPanelProxy layerViewPanelProxy;
    private TaskMonitorManager taskMonitorManager = null;

    public AbstractButton getButton(Class cursorToolClass) {
        Assert.isTrue(CursorTool.class.isAssignableFrom(cursorToolClass));
        return (AbstractButton) cursorToolClassToButtonMap.get(cursorToolClass);
    }
    // By default, CursorTool buttons are always enabled. [Bob Boseko]
    private EnableCheck cursorToolEnableCheck = new EnableCheck() {
        @Override
        public String check(JComponent component) {
            return null;
        }
    };
    private ButtonGroup cursorToolButtonGroup;

    public WorkbenchToolBar(LayerViewPanelProxy layerViewPanelProxy) {
        this(layerViewPanelProxy, new ButtonGroup());
    }

    public WorkbenchToolBar(LayerViewPanelProxy layerViewPanelProxy, ButtonGroup cursorToolButtonGroup) {
        this.cursorToolButtonGroup = cursorToolButtonGroup;
        this.layerViewPanelProxy = layerViewPanelProxy;
    }

    public void setCursorToolEnableCheck(EnableCheck cursorToolEnableCheck) {
        this.cursorToolEnableCheck = cursorToolEnableCheck;
    }

    public void setTaskMonitorManager(TaskMonitorManager taskMonitorManager) {
        this.taskMonitorManager = taskMonitorManager;
    }

    public ToolConfig addCursorTool(final CursorTool cursorTool) {
        return addCursorTool(cursorTool.getName(), cursorTool,
                new JideToggleButton() {
                    @Override
                    public String getToolTipText(MouseEvent event) {
                        //Get tooltip text dynamically [Bob Boseko 11/13/2003]
                        return cursorTool.getName();
                    }
                });
    }

    /**
     * Add's a CursorTool with an own JToggleButton. This is useful, if you want to add CursorTool
     * with an own JToggleButton implementation, such a DropDownToggleButton.
     *
     * @return
     */
    public ToolConfig addCursorTool(final CursorTool cursorTool, JideToggleButton button) {
        button.setToolTipText(cursorTool.getName());
        return addCursorTool(cursorTool.getName(), cursorTool, button);
    }

    public static class ToolConfig {

        private JideToggleButton button;
        private QuasimodeTool quasimodeTool;

        public ToolConfig(JideToggleButton button, QuasimodeTool quasimodeTool) {
            this.button = button;
            this.quasimodeTool = quasimodeTool;
        }

        public JideToggleButton getButton() {
            return button;
        }

        public QuasimodeTool getQuasimodeTool() {
            return quasimodeTool;
        }
    }

    public ToolConfig addCursorTool(String tooltip, final CursorTool cursorTool) {
        JideToggleButton button = new JideToggleButton();
        return addCursorTool(tooltip, cursorTool, button);
    }

    private ToolConfig addCursorTool(String tooltip, final CursorTool cursorTool, JideToggleButton button) {
        cursorToolButtonGroup.add(button);
        cursorToolClassToButtonMap.put(cursorTool.getClass(), button);
        final QuasimodeTool quasimodeTool = QuasimodeTool.addStandardQuasimodes(cursorTool);
        add(button, tooltip, cursorTool.getIcon(),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //It's null when the Workbench starts up. [Bob Boseko]
                        //Or the active frame may not have a LayerViewPanel. [Bob Boseko]
                        if (layerViewPanelProxy.getLayerViewPanel() != null) {
                            layerViewPanelProxy.getLayerViewPanel().setCurrentCursorTool(quasimodeTool);
                        }

                        //<<TODO:DESIGN>> We really shouldn't create a new LeftClickFilter on each
                        //click of the tool button. Not a big deal though. [Bob Boseko]
                    }
                }, cursorToolEnableCheck);
        if (cursorToolButtonGroup.getButtonCount() == 1) {
            cursorToolButtonGroup.setSelected(button.getModel(), true);
            reClickSelectedCursorToolButton();
        }
        return new ToolConfig(button, quasimodeTool);
    }

    public ButtonGroup getButtonGroup() {
        return cursorToolButtonGroup;
    }

    public JideToggleButton getSelectedCursorToolButton() {
        for (Enumeration e = cursorToolButtonGroup.getElements();
                e.hasMoreElements();) {
            JideToggleButton button = (JideToggleButton) e.nextElement();
            button.setFocusable(false);
            if (button.getModel() == cursorToolButtonGroup.getSelection()) {
                return button;
            }
        }
        Assert.shouldNeverReachHere();
        return null;
    }

    public void reClickSelectedCursorToolButton() {
        if (cursorToolButtonGroup.getButtonCount() == 0) {
            return;
        }
        getSelectedCursorToolButton().doClick();
    }

    //<<TODO:REFACTOR>> This method duplicates code in FeatureInstaller, with the
    //result that when the latter was updated (to handle ThreadedPlugIns), the
    //changes were left out from the former.
    public JButton addPlugIn(Icon icon, final PlugIn plugIn, EnableCheck enableCheck, WorkbenchContext workbenchContext) {
        final JideButton button = new JideButton();
        ActionListener listener = AbstractPlugIn.toActionListener(plugIn, workbenchContext, taskMonitorManager);
        if (plugIn.getName().equalsIgnoreCase(I18N.get("com.osfac.dmt.workbench.ui.plugin.NewTaskPlugIn"))) {
            button.setVisible(false);
            DMTCommandBarFactory.newProject.addActionListener(listener);
        } else if (plugIn.getName().equalsIgnoreCase("undo")) {//Don't translate this text
            button.setVisible(false);
            DMTCommandBarFactory.undo.addActionListener(listener);
            new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DMTCommandBarFactory.undo.setEnabled(button.isEnabled());
                }
            }).start();
        } else if (plugIn.getName().equalsIgnoreCase("redo")) {//Don't translate this text
            button.setVisible(false);
            DMTCommandBarFactory.redo.addActionListener(listener);
            new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DMTCommandBarFactory.redo.setEnabled(button.isEnabled());
                }
            }).start();
        }
        add(button, plugIn.getName(), icon, listener, enableCheck);
        button.setFocusable(false);
        return button;
    }

    public JButton addPlugIn(final int index, final PlugIn plugIn,
            final Icon icon, final EnableCheck enableCheck,
            final WorkbenchContext workbenchContext) {
        final JideButton button = new JideButton();
        button.setFocusable(false);
        ActionListener listener = AbstractPlugIn.toActionListener(plugIn, workbenchContext, taskMonitorManager);
        if (plugIn.getName().equalsIgnoreCase(I18N.get("org.openjump.core.ui.plugin.file.OpenWizardPlugIn"))) {
            button.setVisible(false);
            DMTCommandBarFactory.open.addActionListener(listener);
        } else if (plugIn.getName().equalsIgnoreCase(I18N.get("org.openjump.core.ui.plugin.mousemenu.SaveDatasetsPlugIn"))) {//Don't translate this text
            button.setVisible(false);
            DMTCommandBarFactory.saveDataset.addActionListener(listener);
            new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DMTCommandBarFactory.saveDataset.setEnabled(button.isEnabled());
                }
            }).start();
        }
        add(index, button, plugIn.getName(), icon, listener, enableCheck);
        return button;
    }
}
