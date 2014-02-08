package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

public class SelectablePlugIn extends AbstractPlugIn {

    public static final ImageIcon ICON = IconLoader.icon("SmallSelect.gif");

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        boolean makeSelectable = !context.getSelectedLayer(0).isSelectable();
        for (Iterator i = Arrays.asList(context.getSelectedLayers()).iterator(); i.hasNext();) {
            Layer selectedLayer = (Layer) i.next();
            selectedLayer.setSelectable(makeSelectable);
        }
        return true;
    }

    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1))
                .add(new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component).setSelected(
                        workbenchContext.createPlugInContext().getSelectedLayer(0).isSelectable());
                return null;
            }
        });
    }
}
