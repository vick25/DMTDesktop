package com.osfac.dmt.workbench.ui.cursortool.editing;

import com.osfac.dmt.I18N;
import com.osfac.dmt.geom.EnvelopeUtil;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.GeometryEditor;
import com.osfac.dmt.workbench.ui.cursortool.Animations;
import com.osfac.dmt.workbench.ui.cursortool.SpecifyFeaturesTool;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.VerticesInFencePlugIn;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
//Override SpecifyFeaturesTool rather than just DragTool so that clicks are
//handled. [Bob Boseko]
public class DeleteVertexTool extends SpecifyFeaturesTool {

    private EnableCheckFactory checkFactory;
    private GeometryEditor geometryEditor = new GeometryEditor();

    public DeleteVertexTool(EnableCheckFactory checkFactory) {
        this.checkFactory = checkFactory;
        setViewClickBuffer(5);
    }

    protected void gestureFinished() throws java.lang.Exception {
        reportNothingToUndoYet();
        if (!check(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1))) {
            return;
        }
        if (!check(checkFactory.createSelectedItemsLayersMustBeEditableCheck())) {
            return;
        }
        final ArrayList verticesDeleted = new ArrayList();
        ArrayList transactions = new ArrayList();
        for (Iterator i =
                getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
                i.hasNext();) {
            Layer layer = (Layer) i.next();
            transactions.add(createTransaction(layer, verticesDeleted));
        }
        int emptyGeometryCount = EditTransaction.emptyGeometryCount(transactions);
        if (emptyGeometryCount > 0) {
            getPanel().getContext().warnUser(
                    I18N.get("ui.cursortool.editing.DeleteVertexTool.cancelled-deletion-would-result-in-empty-geometry"));
            return;
        }
        if (verticesDeleted.isEmpty()) {
            getPanel().getContext().warnUser(I18N.get("ui.cursortool.editing.DeleteVertexTool.no-selection-handles-here"));
            return;
        }
        EditTransaction.commit(transactions, new EditTransaction.SuccessAction() {
            public void run() {
                try {
                    Animations.drawExpandingRings(
                            getPanel().getViewport().toViewPoints(verticesDeleted),
                            true,
                            Color.red,
                            getPanel(),
                            new float[]{15, 15});
                } catch (Throwable t) {
                    getPanel().getContext().warnUser(t.toString());
                }
            }
        });
    }

    protected EditTransaction createTransaction(
            Layer layer,
            final ArrayList verticesDeleted)
            throws NoninvertibleTransformException {
        final Geometry box = EnvelopeUtil.toGeometry(getBoxInModelCoordinates());
        EditTransaction transaction =
                EditTransaction
                .createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(
                    Geometry geometryWithSelectedItems,
                    Collection selectedItems) {
                if (wasClick() && !verticesDeleted.isEmpty()) {
                    Assert.isTrue(verticesDeleted.size() == 1);
                    return geometryWithSelectedItems;
                }
                if (!box
                        .getEnvelopeInternal()
                        .intersects(geometryWithSelectedItems.getEnvelopeInternal())) {
                    return geometryWithSelectedItems;
                }
                Collection verticesInBox =
                        VerticesInFencePlugIn.verticesInFence(selectedItems, box, true);
                if (wasClick() && !verticesInBox.isEmpty()) {
                    verticesDeleted.add(verticesInBox.iterator().next());
                } else {
                    verticesDeleted.addAll(verticesInBox);
                }
                return geometryEditor.deleteVertices(
                        geometryWithSelectedItems,
                        verticesInBox);
            }
        },
                getPanel(),
                getPanel().getContext(),
                getName(),
                layer,
                isRollingBackInvalidEdits(),
                false);
        return transaction;
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("DeleteCursor.gif").getImage());
    }

    public Icon getIcon() {
        return IconLoader.icon("DeleteVertex.gif");
    }
}
