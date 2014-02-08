package org.openjump.core.ui.plugin.edittoolbox;

import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.cursortool.editing.EditingPlugIn;
import com.osfac.dmt.workbench.ui.toolbox.ToolboxDialog;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.openjump.core.ui.plugin.edittoolbox.cursortools.RotateSelectedItemTool;

public class RotateSelectedItemPlugIn extends AbstractPlugIn {

    private boolean rotateItemButtonAdded = false;

    public void initialize(final PlugInContext context) throws Exception {
        //add a listener so that when the toolbox dialog opens the constrained tools will be added
        //we can't just add the tools directly at this point since the toolbox isn't ready yet

        context.getWorkbenchContext().getWorkbench().getFrame().addComponentListener(
                new ComponentAdapter() {
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
        return true;
    }

    public void addButton(final PlugInContext context) {
        if (!rotateItemButtonAdded) {
            final ToolboxDialog toolbox = ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext());
            toolbox.add(new RotateSelectedItemTool(new EnableCheckFactory(toolbox.getContext())));
            toolbox.finishAddingComponents();
            toolbox.validate();
            rotateItemButtonAdded = true;
        }
    }
}
