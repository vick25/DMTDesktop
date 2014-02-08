package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.renderer.FeatureSelectionRenderer;
import javax.swing.Icon;

public class SelectFeaturesTool extends SelectTool {

    public SelectFeaturesTool() {
        super(FeatureSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
        return IconLoader.icon("Select.gif");
        //return IconLoaderFamFam.icon("cursor.png");
    }

    public void activate(LayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        selection = layerViewPanel.getSelectionManager().getFeatureSelection();
    }
}
