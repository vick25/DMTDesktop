package com.osfac.dmt.workbench.driver;

import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.CompressedFile;
import com.osfac.dmt.io.DriverProperties;
import com.osfac.dmt.io.ShapefileReader;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.ui.AbstractDriverPanel;
import com.osfac.dmt.workbench.ui.BasicFileDriverPanel;
import com.osfac.dmt.workbench.ui.ErrorHandler;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFileFilter;
import java.io.File;

public class ShapeFileInputDriver extends AbstractInputDriver {

    private ShapefileReader reader = new ShapefileReader();
    private BasicFileDriverPanel panel;

    public ShapeFileInputDriver() {
    }

    public String toString() {
        return GUIUtil.shpDesc;
    }

    public AbstractDriverPanel getPanel() {
        return panel;
    }

    public void input(LayerManager layerManager, String categoryName) throws Exception {
        String extension;
        File selectedFile = panel.getSelectedFile();
        String name = GUIUtil.nameWithoutExtension(selectedFile);
        String fname = selectedFile.getAbsolutePath();

        extension = fname.substring(fname.length() - 3);

        DriverProperties dp = new DriverProperties();

        if (extension.equalsIgnoreCase("zip")) {
            String internalName;

            dp.set("CompressedFile", fname);
            internalName = CompressedFile.getInternalZipFnameByExtension(".shp",
                    fname);

            if (internalName == null) {
                throw new Exception(
                        "Couldnt find a .shp file inside the .zip file: " + fname);
            }

            dp.set("File", internalName);
        } else {
            dp.set("File", fname);
        }

        FeatureCollection featureCollection = reader.read(dp);
        Layer layer = layerManager.addLayer(categoryName, name,
                featureCollection);
    }

    public void initialize(DriverManager driverManager,
            ErrorHandler errorHandler) {
        super.initialize(driverManager, errorHandler);
        panel = new BasicFileDriverPanel(errorHandler);
        panel.setFileDescription(GUIUtil.shpDesc);
        panel.setFileFilter(new WorkbenchFileFilter(GUIUtil.shpDesc));
        panel.setFileMustExist(true);
    }
}
