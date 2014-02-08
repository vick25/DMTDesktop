package com.osfac.dmt.workbench.ui.toolbox;

import com.jidesoft.swing.JideToggleButton;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchToolBar;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.*;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * An always-on-top modeless dialog with a WorkbenchToolBar (for CursorTools,
 * PlugIns, and other buttons). Takes care of unpressing the CursorTools (if
 * necessary) when the dialog is closed (and pressing the first CursorTool on
 * the main toolbar).
 */
//Must use modeless dialog rather than JInternalFrame; otherwise, if we
// implement
//it as an internal frame, when we click it, the original TaskFrame will
// deactivate,
//thus deactivating the current CursorTool. [Bob Boseko]
public class ToolboxDialog extends JDialog {

    public AbstractButton getButton(Class cursorToolClass) {
        for (Iterator i = toolBars.iterator(); i.hasNext();) {
            WorkbenchToolBar toolBar = (WorkbenchToolBar) i.next();
            AbstractButton button = toolBar.getButton(cursorToolClass);
            if (button != null) {
                return button;
            }
        }
        return null;
    }

    public WorkbenchToolBar getToolBar() {
        if (toolBars.isEmpty()) {
            addToolBar();
        }
        return (WorkbenchToolBar) toolBars.get(toolBars.size() - 1);
    }

    public WorkbenchContext getContext() {
        return context;
    }

    public WorkbenchToolBar.ToolConfig add(CursorTool tool) {
        return add(tool, null);
    }

    /**
     * @param enableCheck null to leave unspecified
     */
    public WorkbenchToolBar.ToolConfig add(CursorTool tool,
            EnableCheck enableCheck) {
        WorkbenchToolBar.ToolConfig config = getToolBar().addCursorTool(tool);
        JideToggleButton button = config.getButton();
        getToolBar().setEnableCheck(button,
                enableCheck != null ? enableCheck : new MultiEnableCheck());
        registerButton(button, enableCheck);
        return config;
    }

    public void addPlugIn(PlugIn plugIn, EnableCheck enableCheck, Icon icon) {
        registerButton(getToolBar().addPlugIn(icon, plugIn, enableCheck,
                context), enableCheck);
    }

    private void registerButton(AbstractButton button, EnableCheck enableCheck) {
        buttons.add(button);
    }
    private ArrayList buttons = new ArrayList();
    private ArrayList toolBars = new ArrayList();

    public void addToolBar() {
        toolBars.add(new WorkbenchToolBar(context, context.getWorkbench()
                .getFrame().getToolBar().getButtonGroup()));
        getToolBar().setBorder(null);
        getToolBar().setFloatable(false);
        gridLayout1.setRows(toolBars.size());
        toolbarsPanel.add(getToolBar());
    }

    public ToolboxDialog(final WorkbenchContext context) {
        super(context.getWorkbench().getFrame(), "", false);
        try {
            jbInit();
        } catch (Exception e) {
            JXErrorPane.showDialog(null, new ErrorInfo(I18N.get("com.osfac.dmt.Config.Error")
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        this.context = context;
        setResizable(true);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                if (buttons.contains(context.getWorkbench().getFrame()
                        .getToolBar().getSelectedCursorToolButton())) {
                    ((AbstractButton) context.getWorkbench().getFrame()
                            .getToolBar().getButtonGroup().getElements()
                            .nextElement()).doClick();
                }
            }
        });
    }

    /**
     * Call this method after all the CursorTools have been added.
     */
    public void finishAddingComponents() {
        pack();
        // #initializeLocation will be called again in #setVisible, but
        // call it here just in case the window is realized by some other
        // means than #setVisible (unlikely). [Bob Boseko 2005-03-14]
        initializeLocation();
    }

    public void setVisible(boolean visible) {
        if (visible && !locationInitializedBeforeMakingDialogVisible) {
            // #initializeLocation was called in #finishAddingComponents,
            // but the Workbench may have moved since then, so call
            // #initializeLocation again just before making the dialog
            // visible. [Bob Boseko 2005-03-14]
            initializeLocation();
            locationInitializedBeforeMakingDialogVisible = true;
        }
        super.setVisible(visible);
    }
    private boolean locationInitializedBeforeMakingDialogVisible = false;

    private void initializeLocation() {
        GUIUtil.setLocation(this, initialLocation, context.getWorkbench()
                .getFrame().getDesktopPane());
    }
    private GUIUtil.Location initialLocation = new GUIUtil.Location(0, false, 0, false);
    private WorkbenchContext context;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel centerPanel = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel toolbarsPanel = new JPanel();
    private GridLayout gridLayout1 = new GridLayout();

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        centerPanel.setLayout(borderLayout2);
        toolbarsPanel.setLayout(gridLayout1);
        gridLayout1.setColumns(1);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(toolbarsPanel, BorderLayout.NORTH);
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void updateEnabledState() {
        for (Iterator i = toolBars.iterator(); i.hasNext();) {
            WorkbenchToolBar toolBar = (WorkbenchToolBar) i.next();
            toolBar.updateEnabledState();
        }
    }

    public void setInitialLocation(GUIUtil.Location location) {
        initialLocation = location;
    }
}