package com.osfac.dmt.workbench.ui.plugin.imagery;

import com.osfac.dmt.DMTException;
import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.util.Block;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.FileUtil;
import com.osfac.dmt.util.OrderedMap;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.imagery.ImageryLayerDataset;
import com.osfac.dmt.workbench.imagery.ReferencedImage;
import com.osfac.dmt.workbench.imagery.ReferencedImageFactory;
import com.osfac.dmt.workbench.imagery.ReferencedImageStyle;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.util.Assert;
import java.io.File;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ImageFeatureCreator {

    public Collection getImages(PlugInContext context, Layer layer) {
        final String LAST_DIRECTORY_KEY = getClass().getName() + " - LAST DIRECTORY";
        final String LAST_FILE_FILTER_DESCRIPTION_KEY = getClass().getName() + " - LAST FILE FILTER DESCRIPTION";

        Blackboard blackboard = PersistentBlackboardPlugIn.get(context.getWorkbenchContext());
        JFileChooser fileChooser = GUIUtil.createJFileChooserWithExistenceChecking();

        fileChooser.setCurrentDirectory(new File(
                (String) blackboard.get(LAST_DIRECTORY_KEY, fileChooser.getCurrentDirectory().getPath())));
        GUIUtil.removeChoosableFileFilters(fileChooser);

        OrderedMap fileFilterToReferencedImageFactoryMap = getFileFilterToReferencedImageFactoryMap(insertCompositeReferencedImageFactory(context.getWorkbenchContext().getRegistry().getEntries(
                ReferencedImageFactory.REGISTRY_CLASSIFICATION)));

        for (Iterator i = fileFilterToReferencedImageFactoryMap.keyList().iterator(); i.hasNext();) {
            FileFilter fileFilter = (FileFilter) i.next();
            fileChooser.addChoosableFileFilter(fileFilter);
        }
        fileChooser.setFileFilter((FileFilter) fileFilterToReferencedImageFactoryMap.keyList().get(0));

        for (Iterator i = Arrays.asList(fileChooser.getChoosableFileFilters()).iterator(); i.hasNext();) {
            FileFilter fileFilter = (FileFilter) i.next();
            if (fileFilter.getDescription().equals(
                    blackboard.get(
                    LAST_FILE_FILTER_DESCRIPTION_KEY, ""))) {
                fileChooser.setFileFilter(fileFilter);
                break;
            }
        }
        fileChooser.setMultiSelectionEnabled(true);

        Collection selectedFeatures = null;

        // Show modal here
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(context.getWorkbenchFrame())) {
            blackboard.put(LAST_DIRECTORY_KEY, fileChooser.getCurrentDirectory().getPath());
            blackboard.put(LAST_FILE_FILTER_DESCRIPTION_KEY, fileChooser.getFileFilter().getDescription());

            File[] selectedFiles = fileChooser.getSelectedFiles();

            if (selectedFiles != null && selectedFiles.length > 0) {
                ReferencedImageFactory rif = (ReferencedImageFactory) fileFilterToReferencedImageFactoryMap.get(fileChooser.getFileFilter());

                if (rif.isAvailable(context.getWorkbenchContext())) {
                    selectedFeatures = createFeatures(
                            rif,
                            selectedFiles,
                            getImageryLayerDataset(layer));
                    // check for errors and tell
                    for (Iterator iterator = selectedFeatures.iterator(); iterator
                            .hasNext();) {
                        Feature feat = (Feature) iterator.next();
                        if (feat.getAttribute(ImageryLayerDataset.ATTR_ERROR) != null) {
                            context.getErrorHandler().handleThrowable(new DMTException(I18N.get("com.osfac.dmt.workbench.ui.plugin.imagery.ImageFeatureCreator.there-were-errors")));
                            break;
                        }
                    }
                } else {
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, I18N.get("ui.plugin.imagery.ImageFeatureCreator.The-driver-for") + " " + rif.getTypeName() + " " + I18N.get("ui.plugin.imagery.ImageFeatureCreator.is-not-available-Please-check-your-configuration"),
                            I18N.get("ui.plugin.imagery.ImageFeatureCreator.Warning"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                }
            }
        }

        return selectedFeatures;
    }

    /**
     * Sets whether an imagery layer is selectable, editable, and read-only. The
     * policy used is that a imagery layer is selectable/editable iff: <ul>
     * <li>There is a single image in the layer <li>The image type is editable
     * </ul>
     *
     * @param layer the layer to update
     */
    public void setLayerSelectability(Layer layer) {
        boolean selectable = false;
        Collection features = layer.getFeatureCollectionWrapper().getFeatures();

        if (features.size() == 1) {
            Feature f = (Feature) features.iterator().next();
            String imageFilePath = (String) f.getAttribute(ImageryLayerDataset.ATTR_FILE);
            String factoryClassPath = (String) f.getAttribute(ImageryLayerDataset.ATTR_FACTORY);

            if (imageFilePath != null && factoryClassPath != null) {
                try {
                    ReferencedImageFactory imageFactory = (ReferencedImageFactory) Class.forName(factoryClassPath).newInstance();
                    selectable = imageFactory.isEditableImage(imageFilePath);
                } catch (Exception ex) {
                    // do nothing
                }
            }
        }

        layer.setSelectable(selectable);
        layer.setEditable(selectable);
        layer.setReadonly(!selectable);
    }

    private ReferencedImageFactory getReferencedImageFactoryProper(
            ReferencedImageFactory factory, File file) {
        return factory instanceof CompositeReferencedImageFactory ? ((CompositeReferencedImageFactory) factory)
                .referencedImageFactory(FileUtil.getExtension(file))
                : factory;
    }

    private ImageryLayerDataset getImageryLayerDataset(Layer layer) {
        ReferencedImageStyle irs = (ReferencedImageStyle) layer.getStyle(ReferencedImageStyle.class);
        return irs.getImageryLayerDataset();
    }

    private Collection createFeatures(
            final ReferencedImageFactory referencedImageFactory,
            File[] files,
            final ImageryLayerDataset imageryLayerDataset) {
        return CollectionUtil.collect(Arrays.asList(files),
                new Block() {
                    public Object yield(Object file) {
                        return createFeature(referencedImageFactory,
                                (File) file,
                                imageryLayerDataset);
                    }
                });
    }

    private Feature createFeature(
            ReferencedImageFactory referencedImageFactory,
            File file,
            ImageryLayerDataset imageryLayerDataset) {

        Feature feature = new BasicFeature(ImageryLayerDataset.getSchema());
        feature.setAttribute(ImageryLayerDataset.ATTR_FILE, file.getPath());
        feature.setAttribute(ImageryLayerDataset.ATTR_FORMAT,
                getReferencedImageFactoryProper(referencedImageFactory, file).getTypeName());
        feature.setAttribute(ImageryLayerDataset.ATTR_FACTORY,
                getReferencedImageFactoryProper(referencedImageFactory, file).getClass().getName());
        // Set Geometry to an empty Geometry, in case an exception occurs before
        // the Geometry is actually set. [Bob Boseko 2005-04-12]
        feature.setGeometry(new GeometryFactory().createPoint((Coordinate) null));
        // Set the Geometry. [Bob Boseko 2005-04-12]
        try {
            imageryLayerDataset.createImage(feature);

            // or set error message as feat attrib
        } catch (Exception e) {
            feature.setAttribute(ImageryLayerDataset.ATTR_ERROR,
                    WorkbenchFrame.toMessage(e) + "\n\n" + StringUtil.stackTrace(e));
            e.printStackTrace();
        }

        return feature;
    }

    private OrderedMap getFileFilterToReferencedImageFactoryMap(
            Collection referencedImageFactories) {
        OrderedMap fileFilterToReferencedImageFactoryMap = new OrderedMap();
        for (Iterator i = referencedImageFactories.iterator(); i.hasNext();) {
            ReferencedImageFactory referencedImageFactory = (ReferencedImageFactory) i.next();
//            if(referencedImageFactory.isAvailable()){
            fileFilterToReferencedImageFactoryMap.put(
                    createFileFilter(referencedImageFactory), referencedImageFactory);
//            }
        }
        return fileFilterToReferencedImageFactoryMap;
    }

    private FileFilter createFileFilter(ReferencedImageFactory referencedImageFactory) {
        return GUIUtil.createFileFilter((referencedImageFactory.getTypeName()
                .equals(referencedImageFactory.getDescription()) ? ""
                : referencedImageFactory.getTypeName() + " - ")
                + referencedImageFactory.getDescription(),
                referencedImageFactory.getExtensions());
    }

    private Collection insertCompositeReferencedImageFactory(
            List referencedImageFactories) {
        return CollectionUtil.concatenate(Arrays.asList(new Collection[]{
                    Collections.singleton(new CompositeReferencedImageFactory(
                    referencedImageFactories)),
                    referencedImageFactories}));
    }

    private static class CompositeReferencedImageFactory implements
            ReferencedImageFactory {

        private List referencedImageFactories;

        public CompositeReferencedImageFactory(List referencedImageFactories) {
            this.referencedImageFactories = new ArrayList();
            for (Iterator i = referencedImageFactories.iterator(); i.hasNext();) {
                ReferencedImageFactory referencedImageFactory = (ReferencedImageFactory) i.next();
                if (referencedImageFactory.isAvailable(null)) {
                    this.referencedImageFactories.add(referencedImageFactory);
                }
            }
        }

        public String getTypeName() {
            return I18N.get("ui.plugin.imagery.ImageFeatureCreator.All-Formats");
        }

        public boolean isEditableImage(String location) {
            return false;
        }

        public String getDescription() {
            return getTypeName();
        }

        public String[] getExtensions() {
            return (String[]) CollectionUtil.concatenate(
                    CollectionUtil.collect(referencedImageFactories,
                    new Block() {
                        public Object yield(
                                Object referencedImageFactory) {
                            return Arrays.asList(((ReferencedImageFactory) referencedImageFactory)
                                    .getExtensions());
                        }
                    })).toArray(new String[]{});
        }

        public ReferencedImageFactory referencedImageFactory(String extension) {
            for (Iterator i = referencedImageFactories.iterator(); i.hasNext();) {
                ReferencedImageFactory referencedImageFactory = (ReferencedImageFactory) i.next();
                if (Arrays.asList(referencedImageFactory.getExtensions())
                        .contains(extension.toLowerCase())) {
                    return referencedImageFactory;
                }
            }
            Assert.shouldNeverReachHere();
            return null;
        }

        public ReferencedImage createImage(String location) {
            throw new UnsupportedOperationException();
        }

        public boolean isAvailable(WorkbenchContext context) {
            return true;
        }
    }
}
