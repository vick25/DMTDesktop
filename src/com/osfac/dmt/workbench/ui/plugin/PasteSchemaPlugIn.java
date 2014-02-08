package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEventType;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

/**
 * Apply a Feature Schema to a FeatureCollection. Attributes already in the
 * FeatureCollection schema are ignored.
 */
public class PasteSchemaPlugIn extends AbstractPlugIn {

    public static ImageIcon ICON = IconLoader.icon("schema_paste.png");

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

        Transferable transferable = GUIUtil.getContents(Toolkit.getDefaultToolkit().getSystemClipboard());

        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String schemaString = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            if (!schemaString.endsWith("\n")) {
                schemaString = schemaString + "\n";
            }
            FeatureSchema cbFeatureSchema = new FeatureSchema();
            boolean isSchema = (schemaString.length() > 0);

            if (isSchema) {
                int tabIndex = schemaString.indexOf("\t");
                int crIndex = schemaString.indexOf("\n");
                boolean endOfString = ((tabIndex < 0) || (crIndex < 0));

                while (!endOfString) {
                    String name = schemaString.substring(0, tabIndex);
                    String typeStr = schemaString.substring(tabIndex + 1, crIndex);
                    AttributeType type = AttributeType.STRING;

                    if (typeStr.compareToIgnoreCase("STRING") == 0) {
                        type = AttributeType.STRING;
                    } else if (typeStr.compareToIgnoreCase("DOUBLE") == 0) {
                        type = AttributeType.DOUBLE;
                    } else if (typeStr.compareToIgnoreCase("INTEGER") == 0) {
                        type = AttributeType.INTEGER;
                    } else if (typeStr.compareToIgnoreCase("DATE") == 0) {
                        type = AttributeType.DATE;
                    } else if (typeStr.compareToIgnoreCase("GEOMETRY") == 0) {
                        type = AttributeType.GEOMETRY;
                    } else if (typeStr.compareToIgnoreCase("OBJECT") == 0) {
                        type = AttributeType.OBJECT;
                    } else {
                        isSchema = false;
                        break;
                    }

                    cbFeatureSchema.addAttribute(name, type);
                    schemaString = schemaString.substring(crIndex + 1);
                    tabIndex = schemaString.indexOf("\t");
                    crIndex = schemaString.indexOf("\n");
                    endOfString = ((tabIndex < 0) || (crIndex < 0));
                }

                isSchema = (cbFeatureSchema.getAttributeCount() > 0);
            }

            if (isSchema) {
                Collection layerCollection = (Collection) context.getWorkbenchContext().getLayerNamePanel().selectedNodes(Layer.class);

                for (Iterator i = layerCollection.iterator(); i.hasNext();) {
                    Layer layer = (Layer) i.next();
                    FeatureSchema layerSchema = layer.getFeatureCollectionWrapper().getFeatureSchema();
                    int numAttributes = cbFeatureSchema.getAttributeCount();
                    boolean changedSchema = false;

                    for (int index = 0; index < numAttributes; index++) {
                        String name = cbFeatureSchema.getAttributeName(index);
                        AttributeType type = cbFeatureSchema.getAttributeType(index);

                        if (!layerSchema.hasAttribute(name)) {
                            if ((type == AttributeType.STRING)
                                    || (type == AttributeType.DOUBLE)
                                    || (type == AttributeType.INTEGER)
                                    || (type == AttributeType.DATE)
                                    || (type == AttributeType.OBJECT)) {
                                layerSchema.addAttribute(name, type);
                                changedSchema = true;
                            }
                        }
                    }

                    if (changedSchema) {
                        List layerFeatures = layer.getFeatureCollectionWrapper().getFeatures();

                        for (int j = 0; j < layerFeatures.size(); j++) {
                            Feature newFeature = new BasicFeature(layerSchema);
                            Feature origFeature = (Feature) layerFeatures.get(j);
                            int numAttribs = origFeature.getAttributes().length;

                            for (int k = 0; k < numAttribs; k++) {
                                newFeature.setAttribute(k, origFeature.getAttribute(k));
                            }

                            origFeature.setSchema(newFeature.getSchema());
                            origFeature.setAttributes(newFeature.getAttributes());
                        }

                        layer.setFeatureCollectionModified(true);
                        layer.fireLayerChanged(LayerEventType.METADATA_CHANGED);
                    }
                }
            }
        }
        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1))
                .add(checkFactory.createSelectedLayersMustBeEditableCheck());
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
