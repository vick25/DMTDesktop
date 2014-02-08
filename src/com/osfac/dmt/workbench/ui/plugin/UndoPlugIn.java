package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.undo.UndoManager;

public class UndoPlugIn extends AbstractPlugIn {

    public UndoPlugIn() {
    }
    private String sName = "undo";

    public void initialize(PlugInContext context) throws Exception {
        sName = I18N.get("com.osfac.dmt.workbench.ui.plugin.UndoPlugIn");
    }

    public boolean execute(PlugInContext context) throws Exception {
        ((LayerManagerProxy) context.getWorkbenchContext()
                .getWorkbench()
                .getFrame()
                .getActiveInternalFrame())
                .getLayerManager().getUndoableEditReceiver().getUndoManager().undo();
        //Exclude the plug-in's activity from the undo history [Bob Boseko]
        reportNothingToUndoYet(context);
        context.getWorkbenchFrame().getToolBar().updateEnabledState();
        return true;
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(new EnableCheck() {
            public String check(JComponent component) {
                UndoManager undoManager =
                        ((LayerManagerProxy) workbenchContext
                        .getWorkbench()
                        .getFrame()
                        .getActiveInternalFrame())
                        .getLayerManager()
                        .getUndoableEditReceiver()
                        .getUndoManager();
                component.setToolTipText(undoManager.getUndoPresentationName());
                return (!undoManager.canUndo()) ? "X" : null;
            }
        });
    }

    public ImageIcon getIcon() {
        //return IconLoaderFamFam.icon("arrow_undo.png");
        return IconLoader.icon("Undo.gif");
    }

    @Override
    public String getName() {
        return sName;
    }
}
