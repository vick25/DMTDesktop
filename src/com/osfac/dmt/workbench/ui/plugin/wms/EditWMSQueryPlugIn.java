package com.osfac.dmt.workbench.ui.plugin.wms;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.model.WMSLayer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.wms.MapLayer;
import java.awt.Dimension;
import java.util.Iterator;
import javax.swing.JLabel;

public class EditWMSQueryPlugIn extends AbstractPlugIn {

    public MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayerablesMustBeSelectedCheck(
                1, Layerable.class));
    }

    public boolean execute(PlugInContext context) throws Exception {
        WMSLayer layer = (WMSLayer) context.getLayerNamePanel()
                .selectedNodes(WMSLayer.class)
                .iterator().next();
        MultiInputDialog dialog = new MultiInputDialog(context.getWorkbenchFrame(),
                I18N.get("ui.plugin.wms.EditWMSQueryPlugIn.edit-wms-query"), true);
        dialog.setInset(0);
        dialog.setSideBarImage(IconLoader.icon("EditWMSLayer.jpg"));
        dialog.setSideBarDescription(
                I18N.get("ui.plugin.wms.EditWMSQueryPlugIn.this-dialog-enables-you-to-change-the-layers-being-retrieved-from-a-web-map-server"));

        EditWMSQueryPanel panel = new EditWMSQueryPanel(layer.getService(),
                layer.getLayerNames(), layer.getSRS(), layer.getAlpha(), layer.getFormat());
        panel.setPreferredSize(new Dimension(600, 450));

        //The field name "Chosen Layers" will appear on validation error messages
        //e.g. if the user doesn't pick any layers. [Bob Boseko]
        dialog.addRow(I18N.get("ui.plugin.wms.EditWMSQueryPlugIn.chosen-layers"), new JLabel(""), panel, panel.getEnableChecks(), null);
        dialog.setVisible(true);

        if (dialog.wasOKPressed()) {
            layer.removeAllLayerNames();

            for (Iterator i = panel.getChosenMapLayers().iterator();
                    i.hasNext();) {
                MapLayer mapLayer = (MapLayer) i.next();
                layer.addLayerName(mapLayer.getName());
            }

            layer.setSRS(panel.getSRS());
            layer.setAlpha(panel.getAlpha());
            layer.setFormat(panel.getFormat());
            layer.fireAppearanceChanged();

            return true;
        }

        return false;
    }
}
