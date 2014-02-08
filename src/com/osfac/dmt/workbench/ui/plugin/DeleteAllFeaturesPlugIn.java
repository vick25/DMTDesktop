package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class DeleteAllFeaturesPlugIn extends AbstractPlugIn {

    public DeleteAllFeaturesPlugIn() {
    }

    public boolean execute(final PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        ArrayList transactions = new ArrayList();
        for (Iterator i = Arrays.asList(context.getLayerNamePanel().getSelectedLayers()).iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            transactions.add(createTransaction(layer, context));
        }
        return EditTransaction.commit(transactions);
    }

    private EditTransaction createTransaction(Layer layer, PlugInContext context) {
        EditTransaction transaction = new EditTransaction(new ArrayList(),
                getName(), layer, isRollingBackInvalidEdits(context), true, context.getWorkbenchFrame());
        for (Iterator i = layer.getFeatureCollectionWrapper().getFeatures().iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            transaction.deleteFeature(feature);
        }
        return transaction;
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1))
                .add(checkFactory.createSelectedLayersMustBeEditableCheck());
    }
    public static final ImageIcon ICON = IconLoader.icon("delete.png");
}
