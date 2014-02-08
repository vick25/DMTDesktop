package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import org.openjump.core.ui.SelectionStyllingOptionsPanel;

//<<TODO:REFACTORING>> Refactor code common to SelectionHandleRenderer and
//VertexRenderer [Bob Boseko]
public class FeatureSelectionRenderer extends AbstractSelectionRenderer {

    public final static String CONTENT_ID = "SELECTED_FEATURES";

    public FeatureSelectionRenderer(LayerViewPanel panel) {
        super(CONTENT_ID, panel, Color.yellow, true, true);
        // get the persistent Blackboard for set the selectionstyle values [Matthias Scholz 3. Sept. 2010]
        if (panel.getContext() instanceof WorkbenchFrame) {
            Blackboard blackboard = ((WorkbenchFrame) panel.getContext()).getContext().getBlackboard();
            blackboard = PersistentBlackboardPlugIn.get(blackboard);
            // the Color
            Object color = blackboard.get(SelectionStyllingOptionsPanel.BB_SELECTION_STYLE_COLOR, SelectionStyllingOptionsPanel.DEFAULT_SELECTION_STYLE_COLOR);
            if (color instanceof Color) {
                setSelectionLineColor((Color) color);
            }
            // the size
            Object size = blackboard.get(SelectionStyllingOptionsPanel.BB_SELECTION_STYLE_POINT_SIZE, SelectionStyllingOptionsPanel.DEFAULT_SELECTION_STYLE_POINT_SIZE);
            if (size instanceof Integer) {
                setSelectionPointSize(((Integer) blackboard.get(SelectionStyllingOptionsPanel.BB_SELECTION_STYLE_POINT_SIZE, SelectionStyllingOptionsPanel.DEFAULT_SELECTION_STYLE_POINT_SIZE)).intValue());
            }
            // and the form
            setSelectionPointForm((String) blackboard.get(SelectionStyllingOptionsPanel.BB_SELECTION_STYLE_POINT_FORM, SelectionStyllingOptionsPanel.DEFAULT_SELECTION_STYLE_POINT_FORM));
        }
    }

    protected Map<Feature, List<Geometry>> featureToSelectedItemsMap(Layer layer) {
        return panel.getSelectionManager()
                .getFeatureSelection()
                .getFeatureToSelectedItemCollectionMap(layer);
    }
}
