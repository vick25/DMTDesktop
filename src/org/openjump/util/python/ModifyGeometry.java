package org.openjump.util.python;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.UndoableEditReceiver;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.GeometryEditor;
import com.vividsolutions.jts.geom.Geometry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ModifyGeometry extends java.lang.Object {

    private static WorkbenchContext workbenchContext = null;
    private String name;
    private ArrayList transactions = new ArrayList();

    public ModifyGeometry(String name) {
        this.name = name;
    }

    public static void setWorkbenchContext(WorkbenchContext workContext) {
        workbenchContext = workContext;
    }

    public void commitTransactions() {
        LayerManager lm = workbenchContext.getLayerManager();
        if (lm != null) {
            UndoableEditReceiver uer = lm.getUndoableEditReceiver();
            if (uer != null) {
                uer.startReceiving();
                uer.reportNothingToUndoYet();
                EditTransaction.commit(transactions);
                uer.stopReceiving();
            }
        } else {
            EditTransaction.commit(transactions);
        }
    }

    public void addTransactionOnSelection(Layer layer) {
        EditTransaction transaction =
                EditTransaction.createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {
                for (Iterator j = selectedItems.iterator(); j.hasNext();) {
                    Geometry item = (Geometry) j.next();
                    modify(item); //define this in jython class
                }

                return geometryWithSelectedItems;
            }
        }, workbenchContext.getLayerViewPanel(), workbenchContext.getLayerViewPanel().getContext(), name, layer, false, false);
        transactions.add(transaction);
    }

    public void modify(Geometry geometry) {
        //define this in jython class
    }

    public void addRemoveFeaturesTransaction(Layer layer, Collection features) {
        GeometryEditor geometryEditor = new GeometryEditor();
        EditTransaction transaction = new EditTransaction(features, name, layer, false, true, workbenchContext.getLayerViewPanel().getContext());
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            Geometry g = transaction.getGeometry(feature);
            g = geometryEditor.remove(g, g);
            transaction.setGeometry(feature, g);
        }
        transactions.add(transaction);
    }

    public void addChangeGeometryTransaction(Layer layer, Feature feature, Geometry geometry) {
        ArrayList features = new ArrayList();
        features.add(feature);
        EditTransaction transaction = new EditTransaction(features, name, layer, false, true, workbenchContext.getLayerViewPanel().getContext());
        transaction.setGeometry(feature, geometry);
        transactions.add(transaction);
    }
}
