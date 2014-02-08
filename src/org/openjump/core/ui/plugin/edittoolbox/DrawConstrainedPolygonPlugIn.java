package org.openjump.core.ui.plugin.edittoolbox;

import com.osfac.dmt.I18N;
import com.osfac.dmt.setting.panel.GeneralPanel;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.cursortool.QuasimodeTool;
import com.osfac.dmt.workbench.ui.cursortool.editing.EditingPlugIn;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import com.osfac.dmt.workbench.ui.zoom.PanTool;
import com.osfac.dmt.workbench.ui.zoom.ZoomTool;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.openjump.core.ui.plugin.edittoolbox.cursortools.DrawConstrainedPolygonTool;
import org.openjump.core.ui.plugin.edittoolbox.tab.ConstraintsOptionsPanel;

public class DrawConstrainedPolygonPlugIn extends AbstractPlugIn {

    public static ConstraintsOptionsPanel constraintsOPanel;
    private boolean polygonButtonAdded = false;
    final static String constraints = I18N.get("org.openjump.core.ui.plugin.edittoolbox.DrawConstrainedPolygonPlugIn.Constraints");
    final static String errorSeeOutputWindow = I18N.get("org.openjump.core.ui.plugin.edittoolbox.DrawConstrainedPolygonPlugIn.Error-See-Output-Window");

    public void initialize(final PlugInContext context) throws Exception {
        constraintsOPanel = new ConstraintsOptionsPanel(context.getWorkbenchContext());
        GeneralPanel.TabbedPaneAll.addTab(constraints, constraintsOPanel);
        //add a listener so that when the toolbox dialog opens the constrained tools will be added
        //we can't just add the tools directly at this point since the toolbox isn't ready yet
        context.getWorkbenchContext().getWorkbench().getFrame().addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                final ToolboxDialog toolBox = ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext());
                toolBox.addComponentListener(new ComponentAdapter() {
                    public void componentShown(ComponentEvent e) {
                        addButton(context);
                    }

                    public void componentHidden(ComponentEvent e) {
                    }
                });
            }
        });
    }

    public boolean execute(PlugInContext context) throws Exception {
        try {
            CursorTool polyTool = DrawConstrainedPolygonTool.create((LayerNamePanelProxy) context.getActiveInternalFrame());
            context.getLayerViewPanel().setCurrentCursorTool(polyTool);
            return true;
        } catch (Exception e) {
            context.getWorkbenchFrame().warnUser(errorSeeOutputWindow);
            context.getWorkbenchFrame().getOutputFrame().createNewDocument();
            context.getWorkbenchFrame().getOutputFrame().addText("DrawConstrainedPolygonPlugIn Exception:" + e.toString());
            return false;
        }
    }

    public void addButton(final PlugInContext context) {
        if (!polygonButtonAdded) {
            final ToolboxDialog toolbox = ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext());
            toolbox.addToolBar();
            QuasimodeTool quasimodeTool = new QuasimodeTool(DrawConstrainedPolygonTool.create(toolbox.getContext()));
            quasimodeTool.add(new QuasimodeTool.ModifierKeySpec(true, false, false), null);
            quasimodeTool.add(new QuasimodeTool.ModifierKeySpec(true, true, false), null);
            quasimodeTool.add(new QuasimodeTool.ModifierKeySpec(false, false, true), new ZoomTool());
            quasimodeTool.add(new QuasimodeTool.ModifierKeySpec(false, true, true), new PanTool());
            toolbox.add(quasimodeTool, null);
            toolbox.finishAddingComponents();
            toolbox.validate();
            polygonButtonAdded = true;
        }
    }
}
