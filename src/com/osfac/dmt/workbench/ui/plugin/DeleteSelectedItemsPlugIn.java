package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.GeometryEditor;
import com.osfac.dmt.workbench.ui.SelectionManager;
import com.osfac.dmt.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import org.openjump.core.ui.images.IconLoader;

//Say "delete" for features but "remove" for layers; otherwise, "delete layers" may
//sound to a user that we're actually deleting the file from the disk. [Bob Boseko]
public class DeleteSelectedItemsPlugIn extends AbstractPlugIn {

    public static ImageIcon ICON = IconLoader.icon("item_delete.png");

    public DeleteSelectedItemsPlugIn() {
    }
    private GeometryEditor geometryEditor = new GeometryEditor();

    public boolean execute(final PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        ArrayList transactions = new ArrayList();
        final SelectionManager selectionManager =
                ((SelectionManagerProxy) context.getActiveInternalFrame()).getSelectionManager();

        for (final Layer layer : selectionManager.getLayersWithSelectedItems()) {
            transactions.add(EditTransaction.createTransactionOnSelection(
                    new EditTransaction.SelectionEditor() {
                        public Geometry edit(Geometry geometryWithSelectedItems,
                                Collection selectedItems) {
                            Geometry g = geometryWithSelectedItems;
                            for (Iterator i = selectedItems.iterator(); i.hasNext();) {
                                Geometry selectedItem = (Geometry) i.next();
                                g = geometryEditor.remove(g, selectedItem);
                            }
                            return g;
                        }
                    },
                    ((SelectionManagerProxy) context.getActiveInternalFrame()),
                    context.getWorkbenchFrame(),
                    getName(),
                    layer,
                    isRollingBackInvalidEdits(context),
                    true));
        }
        return EditTransaction.commit(transactions);
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1))
                .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck());
    }

    public void initialize(PlugInContext context) throws Exception {
        super.initialize(context);
        registerDeleteKey(context.getWorkbenchContext());
    }

    private void registerDeleteKey(final WorkbenchContext context) {
        context.getWorkbench().getFrame().addKeyboardShortcut(KeyEvent.VK_DELETE,
                0, this, createEnableCheck(context));
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
