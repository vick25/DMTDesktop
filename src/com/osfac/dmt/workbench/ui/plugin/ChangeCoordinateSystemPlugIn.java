package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.coordsys.CoordinateSystem;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.coordsys.Reprojector;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

/**
 * Implements a {@link com.osfac.dmt.workbench.plugin.PlugIn} that allows the
 * user to change coordinate systems.
 *
 */
public class ChangeCoordinateSystemPlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        //Don't make this plug-in undoable -- it's a lot of data to store in memory [Bob Boseko]
        context.getLayerManager().getUndoableEditReceiver()
                .reportIrreversibleChange();

        CoordinateSystem destination = (CoordinateSystem) JOptionPane
                .showInputDialog(context.getWorkbenchFrame(),
                "Coordinate system for task:", getName(),
                JOptionPane.PLAIN_MESSAGE, null, new ArrayList(
                CoordinateSystemRegistry
                .instance(context.getWorkbenchContext()
                .getBlackboard())
                .getCoordinateSystems())
                .toArray(),
                context.getLayerManager().getCoordinateSystem());

        if (destination == null) {
            return false;
        }

        if (context.getLayerManager().getCoordinateSystem() == destination) {
            return true;
        }

        if (Reprojector.instance().wouldChangeValues(context.getLayerManager()
                .getCoordinateSystem(),
                destination)) {
            //Two-phase commit [Bob Boseko]
            ArrayList transactions = new ArrayList();

            for (Iterator i = context.getLayerManager().iterator();
                    i.hasNext();) {
                Layer layer = (Layer) i.next();
                EditTransaction transaction = new EditTransaction(layer.getFeatureCollectionWrapper()
                        .getFeatures(),
                        getName(), layer, isRollingBackInvalidEdits(context),
                        false, context.getLayerViewPanel());

                //for (int j = 0; j < transaction.size(); j++) {
                for (Iterator<Feature> j = transaction.getFeatures().iterator(); j.hasNext();) {
                    Feature feature = j.next();
                    Reprojector.instance().reproject(transaction.getGeometry(feature),
                            context.getLayerManager().getCoordinateSystem(),
                            destination);
                }

                transactions.add(transaction);
            }

            EditTransaction.commit(transactions);
        }

        for (Iterator i = context.getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            layer.getFeatureCollectionWrapper().getFeatureSchema()
                    .setCoordinateSystem(destination);
        }

        context.getLayerManager().setCoordinateSystem(destination);
        if (context.getLayerViewPanel() != null) {
            context.getLayerViewPanel().getViewport().zoomToFullExtent();
        }

        return true;
    }
}
