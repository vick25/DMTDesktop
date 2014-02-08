package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.clipboard.AbstractTransferable;
import com.osfac.dmt.workbench.ui.plugin.clipboard.DummyClipboardOwner;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

/**
 * Copy a Feature Schema in the clipboard.
 */
public class CopySchemaPlugIn extends AbstractPlugIn {

    public static ImageIcon ICON = IconLoader.icon("schema_copy.png");

    public void initialize(PlugInContext context) throws Exception {

        WorkbenchContext workbenchContext = context.getWorkbenchContext();
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);

        JPopupMenu layerNamePopupMenu =
                context
                .getWorkbenchContext()
                .getWorkbench()
                .getFrame()
                .getLayerNamePopupMenu();

        featureInstaller.addPopupMenuItem(layerNamePopupMenu, this,
                new String[]{I18N.get("ui.MenuNames.SCHEMA")},
                getName(),
                false,
                getIcon(),
                CopySchemaPlugIn.createEnableCheck(workbenchContext));
    }

    public boolean execute(PlugInContext context) throws Exception {
        String schemaString = "";
        Collection layerCollection = (Collection) context.getWorkbenchContext().getLayerNamePanel().selectedNodes(Layer.class);
        Layer layer = (Layer) layerCollection.iterator().next();
        FeatureSchema featureSchema = layer.getFeatureCollectionWrapper().getFeatureSchema();
        int numAttributes = featureSchema.getAttributeCount();

        for (int index = 0; index < numAttributes; index++) {
            String name = featureSchema.getAttributeName(index);
            AttributeType type = featureSchema.getAttributeType(index);
            schemaString = schemaString + name + "\t" + type + "\n";
        }

        final String clipString = schemaString;

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new AbstractTransferable(
                new DataFlavor[]{DataFlavor.stringFlavor}) {
                    public Object getTransferData(DataFlavor flavor)
                            throws UnsupportedFlavorException, IOException {
                        return clipString;
                    }
                }, new DummyClipboardOwner());

        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
