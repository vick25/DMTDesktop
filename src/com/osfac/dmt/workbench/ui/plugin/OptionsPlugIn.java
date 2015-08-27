package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.panel.GeneralPanel;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditOptionsPanel;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.SnapVerticesToolsOptionsPanel;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import org.openjump.core.ui.DatasetOptionsPanel;
import org.openjump.core.ui.SelectionStyllingOptionsPanel;

public class OptionsPlugIn extends AbstractPlugIn {

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        WorkbenchFrame.actionShowOptionDialog();
        return true;
    }

    @Override
    public void initialize(PlugInContext context) throws Exception {
        datasetOptionsPanel = new DatasetOptionsPanel(context.getWorkbenchContext());
        selectionStyllingOptionsPanel = new SelectionStyllingOptionsPanel(context.getWorkbenchContext());
        snapVerticesToolsOptionsPanel = new SnapVerticesToolsOptionsPanel(context.getWorkbenchContext().getWorkbench().getBlackboard());
        editOptionsPanel = new EditOptionsPanel(context
                .getWorkbenchContext().getWorkbench().getBlackboard(), context
                .getWorkbenchContext().getWorkbench().getFrame()
                .getDesktopPane());
        GeneralPanel.TabbedPaneAll.addTab(I18N.get("ui.plugin.OptionsPlugIn.view-edit"), editOptionsPanel);
        GeneralPanel.TabbedPaneAll.addTab(I18N.get("ui.plugin.OptionsPlugIn.snap-vertices-tools"), GUIUtil.resize(
                IconLoader.icon("QuickSnap.gif"), 12), snapVerticesToolsOptionsPanel);
        // [Matthias Scholz 3. Sept 2010] SelectionStyllingOptionsPanel added
        GeneralPanel.TabbedPaneAll.addTab(I18N.get("ui.plugin.OptionsPlugIn.selection-style"), selectionStyllingOptionsPanel);
        // [Matthias Scholz 15. Sept 2010] DatasetOptionsPanel added
        GeneralPanel.TabbedPaneAll.addTab(I18N.get("ui.DatasetOptionsPanel.datasetOptions"), datasetOptionsPanel);
    }
    public static DatasetOptionsPanel datasetOptionsPanel;
    public static SelectionStyllingOptionsPanel selectionStyllingOptionsPanel;
    public static SnapVerticesToolsOptionsPanel snapVerticesToolsOptionsPanel;
    public static EditOptionsPanel editOptionsPanel;
}
