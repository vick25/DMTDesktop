package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.I18N;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layerable;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComponent;

/**
 *
 * Lets user paste layers from the clipboard.
 * 
*/
public class PasteLayersPlugIn extends LayerableClipboardPlugIn {

    public static final String MUST_NOT_BE_EMPTY = I18N.get("ui.plugin.PasteLayersPlugIn.clipboard-must-not-be-empty");
    public static final String MUST_BE_LAYERS = I18N.get("ui.plugin.PasteLayersPlugIn.clipboard-must-contain-layers");

    //Note: Need to copy the data twice: once when the user hits Copy, so she is
    //free to modify the original afterwards, and again when the user hits Paste,
    //so she is free to modify the first copy then hit Paste again. [Bob Boseko]
    public PasteLayersPlugIn() {
    }

    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "P", "&P", false);
    }

    public boolean execute(PlugInContext context) throws Exception {
        Transferable transferable =
                GUIUtil.getContents(Toolkit.getDefaultToolkit().getSystemClipboard());

        if (!transferable
                .isDataFlavorSupported(
                CollectionOfLayerablesTransferable.COLLECTION_OF_LAYERABLES_FLAVOR)) {
            return false;
        }

        Collection layerables =
                (Collection) transferable.getTransferData(
                CollectionOfLayerablesTransferable.COLLECTION_OF_LAYERABLES_FLAVOR);
        //Cache selected category because selection will change (to layer) after adding first layer
        //if no other layers exist. [Bob Boseko]
        Category selectedCategory =
                ((Category) context.getLayerNamePanel().getSelectedCategories().iterator().next());
        for (Iterator i = layerables.iterator(); i.hasNext();) {
            Layerable layerable = (Layerable) i.next();
            Layerable clone = cloneLayerable(layerable);
            clone.setLayerManager(context.getLayerManager());
            context.getLayerManager().addLayerable(selectedCategory.getName(), clone);
            clone.setName(context.getLayerManager().uniqueLayerName(clone.getName()));
        }

        return true;
    }

    public MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNCategoriesMustBeSelectedCheck(1))
                .add(new EnableCheck() {
            public String check(JComponent component) {
                Transferable transferable = GUIUtil.getContents(
                        Toolkit.getDefaultToolkit().getSystemClipboard());
                if (transferable == null) {
                    return MUST_NOT_BE_EMPTY;
                }
                if (!transferable.isDataFlavorSupported(
                        CollectionOfLayerablesTransferable.COLLECTION_OF_LAYERABLES_FLAVOR)) {
                    return MUST_BE_LAYERS;
                }
                return null;
            }
        });
    }
}
