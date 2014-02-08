package com.osfac.dmt.workbench.ui.plugin.imagery;

import com.osfac.dmt.workbench.imagery.ReferencedImageFactory;
import com.osfac.dmt.workbench.imagery.ecw.ECWImageFactory;
import com.osfac.dmt.workbench.imagery.geotiff.GeoTIFFImageFactory;
import com.osfac.dmt.workbench.imagery.graphic.GraphicImageFactory;
import com.osfac.dmt.workbench.imagery.mrsid.MrSIDImageFactory;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.registry.Registry;

public class InstallReferencedImageFactoriesPlugin extends AbstractPlugIn {

    public void initialize(final PlugInContext context) throws Exception {
        Registry registry = context.getWorkbenchContext().getRegistry();

        registry.createEntry(
                ReferencedImageFactory.REGISTRY_CLASSIFICATION,
                new GraphicImageFactory());
        registry.createEntry(
                ReferencedImageFactory.REGISTRY_CLASSIFICATION,
                new ECWImageFactory());
        registry.createEntry(
                ReferencedImageFactory.REGISTRY_CLASSIFICATION,
                new GeoTIFFImageFactory());
        registry.createEntry(
                ReferencedImageFactory.REGISTRY_CLASSIFICATION,
                new MrSIDImageFactory());
    }
}