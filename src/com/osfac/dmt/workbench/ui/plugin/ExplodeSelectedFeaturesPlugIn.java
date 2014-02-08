package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.vividsolutions.jts.geom.GeometryCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.openjump.core.ui.images.IconLoader;

/**
 * Explodes features based on a GeometryCollection into several features.
 * Resulting feature's geometries are the component of the GeometryCollection.
 * Other attributes are the same as the original one.
 */
public class ExplodeSelectedFeaturesPlugIn extends AbstractPlugIn {

    public static ImageIcon ICON = IconLoader.icon("features_explode.png");

    public boolean execute(final PlugInContext context) throws Exception {
        final ArrayList transactions = new ArrayList();
        for (Iterator i = context.getLayerViewPanel()
                .getSelectionManager()
                .getLayersWithSelectedItems()
                .iterator(); i.hasNext();) {
            Layer layerWithSelectedItems = (Layer) i.next();
            transactions.add(createTransaction(layerWithSelectedItems, context));
        }
        return EditTransaction.commit(transactions, new EditTransaction.SuccessAction() {
            public void run() {
                //SuccessActions don't run after redos, which is the behaviour we want here. [Bob Boseko]
                for (Iterator i = transactions.iterator(); i.hasNext();) {
                    EditTransaction transaction = (EditTransaction) i.next();
                    context
                            .getLayerViewPanel()
                            .getSelectionManager()
                            .getFeatureSelection()
                            .selectItems(transaction.getLayer(), newFeatures(transaction));
                }
            }
        });
    }

    private Collection newFeatures(EditTransaction transaction) {
        ArrayList newFeatures = new ArrayList();
        for (java.util.Iterator<Feature> i = transaction.getFeatures().iterator(); i.hasNext();) {
            Feature f = i.next();
            if (!transaction.getGeometry(f).isEmpty()) {
                newFeatures.add(f);
            }
        }
        //for (int i = 0; i < transaction.size(); i++) {
        //    if (!transaction.getGeometry(i).isEmpty()) {
        //        newFeatures.add(transaction.getFeature(i));
        //    }
        //}
        return newFeatures;
    }

    private EditTransaction createTransaction(Layer layer, PlugInContext context) {
        Collection intactFeatures =
                context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layer);
        EditTransaction transaction =
                new EditTransaction(
                new ArrayList(),
                getName(),
                layer,
                isRollingBackInvalidEdits(context),
                true,
                context.getLayerViewPanel());
        for (Iterator i = intactFeatures.iterator(); i.hasNext();) {
            Feature intactFeature = (Feature) i.next();
            transaction.deleteFeature(intactFeature);
        }
        for (Iterator i = explode(intactFeatures).iterator(); i.hasNext();) {
            Feature explodedFeature = (Feature) i.next();
            transaction.createFeature(explodedFeature);
        }
        return transaction;
    }

    private List explode(Collection features) {
        ArrayList explodedFeatures = new ArrayList();
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            GeometryCollection collection = (GeometryCollection) feature.getGeometry();
            // clone the feature and nullify its geometry before 
            //feature = (Object)feature.clone();
            //feature.setGeometry(collection.getFactory().createGeometryCollection(new Geometry[0]));
            //feature.setGeometry(null);
            for (int j = 0; j < collection.getNumGeometries(); j++) {
                Feature explodedFeature = (Feature) feature.clone(false);
                explodedFeature.setGeometry(collection.getGeometryN(j));
                explodedFeatures.add(explodedFeature);
            }
        }
        return explodedFeatures;
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNFeaturesMustHaveSelectedItemsCheck(1))
                .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck())
                .add(new EnableCheck() {
            public String check(JComponent component) {
                Collection featuresWithSelectedItems =
                        workbenchContext
                        .getLayerViewPanel()
                        .getSelectionManager()
                        .getFeaturesWithSelectedItems();
                for (Iterator i = featuresWithSelectedItems.iterator(); i.hasNext();) {
                    Feature feature = (Feature) i.next();
                    if (!(feature.getGeometry() instanceof GeometryCollection)) {
                        return "Selected feature"
                                + StringUtil.s(featuresWithSelectedItems.size())
                                + " must be geometry collection"
                                + StringUtil.s(featuresWithSelectedItems.size());
                    }
                }
                return null;
            }
        });
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
