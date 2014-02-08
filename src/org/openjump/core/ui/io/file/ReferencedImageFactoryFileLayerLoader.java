package org.openjump.core.ui.io.file;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.imagery.ImageryLayerDataset;
import com.osfac.dmt.workbench.imagery.ReferencedImageFactory;
import com.osfac.dmt.workbench.imagery.ReferencedImageStyle;
import com.osfac.dmt.workbench.model.Category;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManager;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.openjump.core.ui.util.TaskUtil;
import org.openjump.util.UriUtil;

public class ReferencedImageFactoryFileLayerLoader extends AbstractFileLayerLoader {

    private WorkbenchContext workbenchContext;
    private ReferencedImageFactory imageFactory;

    // supportFileExtensions are added to the end of the list of the extension listed by
    // ReferencedImageFactory.getExtensions() double entries removed
    public ReferencedImageFactoryFileLayerLoader(
            WorkbenchContext workbenchContext, ReferencedImageFactory imageFactory,
            String[] supportFileExtensions) {
        // create with empty exts list set later
        super(imageFactory.getDescription(),
                new ArrayList());
        this.imageFactory = imageFactory;
        this.workbenchContext = workbenchContext;
        // remove double entries; factory exts first, delivered after
        HashSet exts = new HashSet(Arrays.asList(imageFactory.getExtensions()));
        if (supportFileExtensions instanceof String[]) {
            exts.addAll(Arrays.asList(supportFileExtensions));
        }
        this.addFileExtensions(new ArrayList(exts));
    }

    public boolean open(TaskMonitor monitor, URI uri, Map<String, Object> options) throws Exception {
        File file;
        if (uri.getScheme().equals("zip")) {
            try {
                File zipFileName = org.openjump.util.UriUtil.getZipFile(uri);
                String entryPath = UriUtil.getZipEntryFilePath(uri);
                String entryFileName = UriUtil.getFileName(uri);
                String entryBaseName = UriUtil.getFileNameWithoutExtension(uri);
                ZipFile zipFile = new ZipFile(zipFileName);
                try {
                    monitor.report("Decompressing: " + entryFileName);
                    file = unzip(zipFile, entryPath, entryFileName);
                    if (getFileExtensions() != null) {
                        for (String extension : getFileExtensions()) {
                            monitor.report("Decompressing: " + entryBaseName + "."
                                    + extension);
                            unzip(zipFile, entryPath, entryBaseName + "." + extension);
                        }
                    }
                } finally {
                    zipFile.close();
                }
            } catch (Exception e) {
                monitor.report(e);
                return false;
            }
        } else {
            file = new File(uri);
        }

        LayerManager layerManager = workbenchContext.getLayerManager();

        layerManager.setFiringEvents(false);
        Layer layer = createLayer(layerManager, file);
        layerManager.setFiringEvents(true);

        Feature feature;
        feature = createFeature(imageFactory, file,
                getImageryLayerDataset(layer));

        // only add layer if no exception occured
        Category category = TaskUtil.getSelectedCategoryName(workbenchContext);
        category.add(0, layer);

        layer.getFeatureCollectionWrapper().add(feature);
        // setFeatureCollectionModified(false) to solve BUG ID: 3424399
        layer.setFeatureCollectionModified(false);
        String imageFilePath = (String) feature.getAttribute(ImageryLayerDataset.ATTR_FILE);
        if (imageFactory.isEditableImage(imageFilePath)) {
            layer.setSelectable(true);
            layer.setEditable(true);
            layer.setReadonly(false);
        } else {
            layer.setSelectable(false);
            layer.setEditable(false);
            layer.setReadonly(true);
        }
        return true;
    }

    private File unzip(ZipFile zipFile, String path, String name)
            throws IOException {
        String entryName;
        if (path != null) {
            entryName = path + "/" + name;
        } else {
            entryName = name;
        }
        ZipEntry entry = zipFile.getEntry(entryName);
        if (entry != null) {
            File file = new File(System.getProperty("java.io.tmpdir"), name);
            file.deleteOnExit();
            InputStream in = zipFile.getInputStream(entry);

            ReadableByteChannel rc = null;
            FileOutputStream out = null;

            try {
                rc = Channels.newChannel(in);
                out = new FileOutputStream(file);
                FileChannel fc = out.getChannel();

                // read into the buffer
                long count = 0;
                int attempts = 0;
                long sz = entry.getSize();
                while (count < sz) {
                    long written = fc.transferFrom(rc, count, sz);
                    count += written;

                    if (written == 0) {
                        attempts++;
                        if (attempts > 100) {
                            throw new IOException("Error writing to file " + file);
                        }
                    } else {
                        attempts = 0;
                    }
                }

                out.close();
                out = null;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception ex) {
                    }
                }
            }
            return file;
        } else {
            return null;
        }

    }

    private ImageryLayerDataset getImageryLayerDataset(Layer layer) {
        ReferencedImageStyle irs = (ReferencedImageStyle) layer.getStyle(ReferencedImageStyle.class);
        return irs.getImageryLayerDataset();
    }

    private Feature createFeature(ReferencedImageFactory referencedImageFactory,
            File file, ImageryLayerDataset imageryLayerDataset) throws Exception {

        Feature feature = new BasicFeature(ImageryLayerDataset.getSchema());
        feature.setAttribute(ImageryLayerDataset.ATTR_FILE, file.getPath());
        feature.setAttribute(ImageryLayerDataset.ATTR_FORMAT,
                referencedImageFactory.getTypeName());
        feature.setAttribute(ImageryLayerDataset.ATTR_FACTORY,
                referencedImageFactory.getClass().getName());
        feature.setGeometry(new GeometryFactory().createPoint((Coordinate) null));
        imageryLayerDataset.createImage(feature);
        return feature;
    }

    private Layer createLayer(LayerManager layerManager, File file) {
        Layer layer = new Layer(file.getName(), Color.black, new FeatureDataset(
                ImageryLayerDataset.getSchema()), layerManager);
        layer.setEditable(true);
        layer.getBasicStyle().setEnabled(false);
        layer.getBasicStyle().setRenderingFill(false);
        layer.addStyle(new ReferencedImageStyle());
        return layer;
    }
}
