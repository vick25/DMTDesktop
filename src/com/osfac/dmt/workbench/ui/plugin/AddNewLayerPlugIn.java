package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.cursortool.editing.EditingPlugIn;
import java.util.Collection;

public class AddNewLayerPlugIn extends AbstractPlugIn {

    public AddNewLayerPlugIn() {
    }

    public static FeatureCollection createBlankFeatureCollection() {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        return new FeatureDataset(featureSchema);
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        Collection selectedCategories = context.getLayerNamePanel().getSelectedCategories();
        context.addLayer(selectedCategories.isEmpty()
                ? StandardCategoryNames.WORKING
                : selectedCategories.iterator().next().toString(), I18N.get("ui.plugin.AddNewLayerPlugIn.new"),
                createBlankFeatureCollection()).setFeatureCollectionModified(false)
                .setEditable(true);
        ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext())
                .setVisible(true);
        return true;
    }
}
