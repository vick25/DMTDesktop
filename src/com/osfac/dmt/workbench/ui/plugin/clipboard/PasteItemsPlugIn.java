package com.osfac.dmt.workbench.ui.plugin.clipboard;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.Reprojector;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.io.WKTReader;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.openjump.core.ui.images.IconLoader;

/**
 * Lets user paste items from the clipboard.
 */
public class PasteItemsPlugIn extends AbstractPlugIn {

    public static ImageIcon ICON = IconLoader.icon("items_paste.png");
    public static final String MUST_NOT_BE_EMPTY = I18N.get("ui.plugin.PasteItemsPlugIn.clipboard-must-not-be-empty");
    public static final String MUST_CONTAIN_GEOMETRY = I18N.get("ui.plugin.PasteItemsPlugIn.clipboard-must-contain-geometries-or-wkt");
    private static final String DECIMAL_PATTERN = "\\d+(?:\\.\\d+)?";
    private static final String WHITESPACE_OR_COMMA = "(?:\\s+|(?:\\s*,\\s*))";
    private static final Pattern pointCoordsPattern = Pattern.compile("\\s*\\(?\\s*("
            + DECIMAL_PATTERN
            + ")"
            + WHITESPACE_OR_COMMA
            + "("
            + DECIMAL_PATTERN
            + ")(?:" + WHITESPACE_OR_COMMA + "(" + DECIMAL_PATTERN + "))?\\s*\\)?\\s*");
    private WKTReader reader = new WKTReader();

    // Note: Need to copy the data twice: once when the user hits Copy, so she is
    // free to modify the original afterwards, and again when the user hits Paste,
    // so she is free to modify the first copy then hit Paste again. [Bob Boseko]
    public PasteItemsPlugIn() {
    }

    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "P", "&P", false);
    }

    public boolean execute(final PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);

        Collection features;
        Transferable transferable = GUIUtil.getContents(Toolkit.getDefaultToolkit()
                .getSystemClipboard());

        if (transferable.isDataFlavorSupported(CollectionOfFeaturesTransferable.COLLECTION_OF_FEATURES_FLAVOR)) {
            features = (Collection) GUIUtil.getContents(
                    Toolkit.getDefaultToolkit().getSystemClipboard()).getTransferData(
                    CollectionOfFeaturesTransferable.COLLECTION_OF_FEATURES_FLAVOR);
        } else {
            // Allow the user to paste features using WKT. [Bob Boseko]
            String value = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            features = processCoordinates(value);
            if (features.isEmpty()) {
                features = reader.read(new StringReader(value)).getFeatures();
            }
        }

        final Layer layer = context.getSelectedLayer(0);
        final Collection featureCopies = conform(features,
                layer.getFeatureCollectionWrapper().getFeatureSchema());
        execute(new UndoableCommand(getName()) {
            public void execute() {
                layer.getFeatureCollectionWrapper().addAll(featureCopies);
            }

            public void unexecute() {
                layer.getFeatureCollectionWrapper().removeAll(featureCopies);
            }
        }, context);

        return true;
    }

    private Collection<Feature> processCoordinates(String value) {
        Matcher matcher = pointCoordsPattern.matcher(value);
        Collection<Feature> features = new ArrayList<>();
        if (matcher.find() && matcher.start() == 0) {
            do {
                double x = Double.parseDouble(matcher.group(1));
                double y = Double.parseDouble(matcher.group(2));
                Coordinate coordinate = new Coordinate(x, y);
                String zString = matcher.group(3);
                if (zString != null) {
                    coordinate.z = Double.parseDouble(zString);
                }
                FeatureSchema featureSchema = new FeatureSchema();
                featureSchema.addAttribute("Geometry", AttributeType.GEOMETRY);

                Feature feature = new BasicFeature(featureSchema);
                Point point = new GeometryFactory().createPoint(coordinate);
                feature.setGeometry(point);
                features.add(feature);
            } while (matcher.find());
        }
        return features;
    }

    public static Collection conform(Collection features,
            FeatureSchema targetFeatureSchema) {
        final ArrayList featureCopies = new ArrayList();

        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            featureCopies.add(conform(feature, targetFeatureSchema));
        }

        return featureCopies;
    }

    private static Feature conform(Feature original,
            FeatureSchema targetFeatureSchema) {
        // Transfer as many attributes as possible, matching on name. [Bob Boseko]
        Feature copy = new BasicFeature(targetFeatureSchema);
        copy.setGeometry((Geometry) original.getGeometry().clone());

        for (int i = 0; i < original.getSchema().getAttributeCount(); i++) {
            if (i == original.getSchema().getGeometryIndex()) {
                continue;
            }

            String attributeName = original.getSchema().getAttributeName(i);

            if (!copy.getSchema().hasAttribute(attributeName)) {
                continue;
            }

            if (copy.getSchema().getAttributeType(attributeName) != original.getSchema()
                    .getAttributeType(attributeName)) {
                continue;
            }

            copy.setAttribute(attributeName, original.getAttribute(attributeName));
        }

        Reprojector.instance().reproject(copy.getGeometry(),
                original.getSchema().getCoordinateSystem(),
                copy.getSchema().getCoordinateSystem());

        return copy;
    }

    public static MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()).add(
                checkFactory.createExactlyNLayersMustBeSelectedCheck(1)).add(
                checkFactory.createSelectedLayersMustBeEditableCheck()).add(
                new EnableCheck() {
                    public String check(JComponent component) {
                        Transferable transferable = GUIUtil.getContents(Toolkit.getDefaultToolkit()
                                .getSystemClipboard());

                        if (transferable == null) {
                            return MUST_NOT_BE_EMPTY;
                        }

                        if (transferable.isDataFlavorSupported(CollectionOfFeaturesTransferable.COLLECTION_OF_FEATURES_FLAVOR)) {
                            return null;
                        }

                        try {
                            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                                String value = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                                if (isWKT(value) || isCoordinates(value)) {
                                    return null;
                                }
                            }
                        } catch (UnsupportedFlavorException | IOException e) {
                            workbenchContext.getErrorHandler().handleThrowable(e);
                        }

                        return MUST_CONTAIN_GEOMETRY;
                    }

                    private boolean isCoordinates(String value) {
                        return pointCoordsPattern.matcher(value).find();
                    }

                    private boolean isWKT(String s) {
                        try {
                            new WKTReader().read(new StringReader(s));

                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                });
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}
