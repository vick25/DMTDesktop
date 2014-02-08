package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.workbench.model.FenceLayerFinder;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.InfoFrame;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.Color;
import java.awt.Cursor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FeatureInfoTool extends SpecifyFeaturesTool {

    //public static final ImageIcon ICON = IconLoaderFamFam.icon("information.png");
    public static final ImageIcon ICON = IconLoader.icon("Info.gif");

    public FeatureInfoTool() {
        setColor(Color.magenta);
    }

    public Icon getIcon() {
        return ICON;
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("InfoCursor.gif").getImage());
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();
        InfoFrame infoFrame = getTaskFrame().getInfoFrame();
        if (!wasShiftPressed()) {
            infoFrame.getModel().clear();
        }
        Map map = layerToSpecifiedFeaturesMap();
        Iterator i = map.keySet().iterator();
        while (i.hasNext()) {
            Layer layer = (Layer) i.next();
            if (layer.getName().equals(FenceLayerFinder.LAYER_NAME)) {
                continue;
            }
            Collection features = (Collection) map.get(layer);
            infoFrame.getModel().add(layer, features);
        }
        infoFrame.surface();
    }
}
