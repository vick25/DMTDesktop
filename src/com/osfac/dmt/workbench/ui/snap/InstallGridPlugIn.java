package com.osfac.dmt.workbench.ui.snap;

import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.panel.GeneralPanel;
import com.osfac.dmt.workbench.DMTWorkbench;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.plugin.InstallRendererPlugIn;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.osfac.dmt.workbench.ui.renderer.Renderer;

public class InstallGridPlugIn extends InstallRendererPlugIn {

    public static SnapOptionsPanel snapOptionsPanel;

    public InstallGridPlugIn() {
        super(GridRenderer.CONTENT_ID, false);
    }

    protected Renderer.Factory createFactory(final TaskFrame frame) {
        return new Renderer.Factory() {
            public Renderer create() {
//                    return new GridRenderer(workbench.getBlackboard(), frame.getLayerViewPanel());
                return new GridRenderer(PersistentBlackboardPlugIn.get(workbench.getContext()), frame.getLayerViewPanel());
            }
        };
    }
    private DMTWorkbench workbench;

    public void initialize(PlugInContext context) throws Exception {
        workbench = context.getWorkbenchContext().getWorkbench();
        super.initialize(context);
        snapOptionsPanel = new SnapOptionsPanel(PersistentBlackboardPlugIn.get(context.getWorkbenchContext()));
        GeneralPanel.TabbedPaneAll.addTab(I18N.get("ui.snap.InstallGridPlugIn.snap-grid"), snapOptionsPanel);
        //[sstein: 29.10.2005] added the following lines to get sure that "snap-tab" will be on top
        int noTabs = GeneralPanel.TabbedPaneAll.getTabCount();
        GeneralPanel.TabbedPaneAll.setSelectedIndex(noTabs - 1);
    }
}
