package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.cursortool.editing.EditingPlugIn;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

public class EditablePlugIn extends AbstractPlugIn {

    private EditingPlugIn editingPlugIn;
    public static final ImageIcon ICON = IconLoader.icon("edit.gif");

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        boolean makeEditable = !context.getSelectedLayer(0).isEditable();
        for (Iterator i = Arrays.asList(context.getSelectedLayers()).iterator(); i.hasNext();) {
            Layer selectedLayer = (Layer) i.next();
            selectedLayer.setEditable(makeEditable);
        }
        if (makeEditable && !editingPlugIn.getToolbox(context.getWorkbenchContext()).isVisible()) {
            editingPlugIn.execute(context);
        }
        return true;
    }

    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        MultiEnableCheck mec = new MultiEnableCheck();

        mec.add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck());
        mec.add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));

        mec.add(new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component).setSelected(
                        workbenchContext.createPlugInContext().getSelectedLayer(0).isEditable());
                return null;
            }
        });

        mec.add(new EnableCheck() {
            public String check(JComponent component) {
                String errMsg = null;
                if (workbenchContext.createPlugInContext().getSelectedLayer(0).isReadonly()) {
                    errMsg = I18N.get("ui.plugin.EditablePlugIn.The-selected-layer-cannot-be-made-editable");
                }
                return errMsg;
            }
        });

        return mec;
    }

    public EditablePlugIn(EditingPlugIn editingPlugIn) {
        this.editingPlugIn = editingPlugIn;
    }
}
