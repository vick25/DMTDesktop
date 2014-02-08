package com.osfac.dmt.workbench.ui.plugin.imagery;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.imagery.ImageryLayerDataset;
import com.osfac.dmt.workbench.imagery.ReferencedImageStyle;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.images.famfam.IconLoaderFamFam;
import java.awt.Color;
import java.util.Collection;
import javax.swing.Icon;
import org.openjump.core.ui.plugin.AbstractUiPlugIn;

public class AddImageLayerPlugIn extends AbstractUiPlugIn {

    private static int nameCounter = 1;

    @Override
    public void initialize(final PlugInContext context) throws Exception {
        super.initialize(context);
        context.getFeatureInstaller()
                .addMainMenuItem(new String[]{MenuNames.FILE}, this, createEnableCheck(workbenchContext), 4);
    }

    @Override
    public String getName() {
        return I18N.get("ui.plugin.imagery.AddImageLayerPlugIn.Add-Image-Layer");
    }

    @Override
    public Icon getIcon() {
        return IconLoaderFamFam.icon("image_add.png");
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        LayerManager lm = context.getLayerManager();
        ImageFeatureCreator ifc = new ImageFeatureCreator();

        lm.setFiringEvents(false);
        Layer layer = createLayer(lm);
        lm.setFiringEvents(true);

        Collection features = ifc.getImages(context, layer);

        if (features != null) {
            lm.addLayer(chooseCategory(context), layer);
            layer.getFeatureCollectionWrapper().addAll(features);
            ifc.setLayerSelectability(layer);
        }

        return false;
    }

    private String chooseCategory(PlugInContext context) {
        return context.getLayerNamePanel() == null ? StandardCategoryNames.WORKING
                : context.getLayerNamePanel().getSelectedCategories().isEmpty() ? StandardCategoryNames.WORKING
                : context.getLayerNamePanel().getSelectedCategories().iterator().next().toString();
    }

    private Layer createLayer(LayerManager lm) {
        String newLayerName = I18N.get("ui.plugin.imagery.AddImageLayerPlugIn.Image") + "_" + nameCounter++;
        Layer layer = new Layer(newLayerName,
                Color.black,
                new FeatureDataset(ImageryLayerDataset.getSchema()),
                lm);
        layer.setEditable(true);
        // Set fill just for the icon beside the layer name [Bob Boseko
        // 2005-04-11]
        layer.getBasicStyle().setRenderingFill(false);
        layer.getBasicStyle().setEnabled(false);
        layer.addStyle(new ReferencedImageStyle());
        return layer;
    }

    public MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck());
    }
}