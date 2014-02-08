package com.osfac.dmt.workbench.ui.plugin.generate;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.task.DummyTaskMonitor;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.warp.Triangle;
import com.osfac.dmt.warp.Triangulator;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.warp.WarpingPanel;
import com.osfac.dmt.workbench.ui.warp.WarpingVectorLayerFinder;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;

/**
 * See White, Marvin S., Jr. and Griffin, Patricia. 1985. Piecewise linear
 * rubber-sheet map transformation. "The American Cartographer" 12:2, 123-31.
 */
public class ShowTriangulationPlugIn extends AbstractPlugIn {

    private final static Color GOLD = new Color(255, 192, 0, 150);
    private Triangulator triangulator = new Triangulator();

    public ShowTriangulationPlugIn(WarpingPanel warpingPanel) {
        this.warpingPanel = warpingPanel;
    }
    private WarpingPanel warpingPanel;

    public void initialize(PlugInContext context) throws Exception {
    }

    public EnableCheck createEnableCheck(WorkbenchContext context) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(context);
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }
    public final static String SOURCE_LAYER_NAME = I18N.get("ui.plugin.generate.ShowTriangulationPlugIn.initial-triangulation");
    public final static String DESTINATION_LAYER_NAME = I18N.get("ui.plugin.generate.ShowTriangulationPlugIn.final-triangulation");

    private Layer sourceLayer(LayerManagerProxy layerManagerProxy) {
        return layerManagerProxy.getLayerManager().getLayer(SOURCE_LAYER_NAME);
    }

    private Layer destinationLayer(LayerManagerProxy layerManagerProxy) {
        return layerManagerProxy.getLayerManager().getLayer(DESTINATION_LAYER_NAME);
    }

    private WarpingVectorLayerFinder warpingVectorLayerFinder(LayerManagerProxy proxy) {
        return new WarpingVectorLayerFinder(proxy);
    }

    private Envelope envelopeOfTails(Collection vectors) {
        Envelope envelope = new Envelope();
        for (Iterator i = vectors.iterator(); i.hasNext();) {
            LineString vector = (LineString) i.next();
            envelope.expandToInclude(vector.getCoordinateN(0));
        }
        return envelope;
    }

    public boolean execute(final PlugInContext context) throws Exception {
        context.getLayerManager().getUndoableEditReceiver().reportNothingToUndoYet();
        execute(createCommand(context.getWorkbenchContext(), true), context);
        return true;
    }

    private UndoableCommand createCommand(
            final WorkbenchContext context,
            final boolean createLayersIfNonExistent) {
        Envelope datasetEnvelope = new Envelope();
        if (warpingPanel.currentSourceLayer() != null) {
            datasetEnvelope =
                    warpingPanel.currentSourceLayer().getFeatureCollectionWrapper().getEnvelope();
        }
        if (datasetEnvelope.isNull()) {
            datasetEnvelope = envelopeOfTails(warpingVectorLayerFinder(context).getVectors());
        }
        if (datasetEnvelope.isNull()) {
            return UndoableCommand.DUMMY;
        }
        if (datasetEnvelope.getWidth() == 0) {
            //Otherwise we could end up with zero-area quadrilaterals [Bob Boseko]
            datasetEnvelope.expandToInclude(
                    new Coordinate(datasetEnvelope.getMinX() + 1, datasetEnvelope.getMinY()));
            datasetEnvelope.expandToInclude(
                    new Coordinate(datasetEnvelope.getMinX() - 1, datasetEnvelope.getMinY()));
        }
        if (datasetEnvelope.getHeight() == 0) {
            datasetEnvelope.expandToInclude(
                    new Coordinate(datasetEnvelope.getMinX(), datasetEnvelope.getMinY() + 1));
            datasetEnvelope.expandToInclude(
                    new Coordinate(datasetEnvelope.getMinX(), datasetEnvelope.getMinY() - 1));
        }
        Map triangleMap =
                triangulator.triangleMap(
                datasetEnvelope,
                warpingVectorLayerFinder(context).getVectors(),
                new DummyTaskMonitor());
        List[] sourceAndDestinationTriangles =
                CollectionUtil.keysAndCorrespondingValues(triangleMap);
        final FeatureCollection sourceFeatureCollection =
                toFeatureCollection(sourceAndDestinationTriangles[0]);
        final FeatureCollection destinationFeatureCollection =
                toFeatureCollection(sourceAndDestinationTriangles[1]);
        return addUndo(new UndoableCommand(getName()) {
            public void execute() {
                if (sourceLayer(context) != null) {
                    sourceLayer(context).setFeatureCollection(sourceFeatureCollection);
                    sourceLayer(context).setVisible(true);
                }
                if (sourceLayer(context) == null && createLayersIfNonExistent) {
                    Layer sourceLayer =
                            context.getLayerManager().addLayer(
                            StandardCategoryNames.WORKING,
                            SOURCE_LAYER_NAME,
                            sourceFeatureCollection);
                    init(sourceLayer, Color.gray, 150, 1);
                }
                if (destinationLayer(context) != null) {
                    destinationLayer(context).setFeatureCollection(destinationFeatureCollection);
                    destinationLayer(context).setVisible(true);
                }
                if (destinationLayer(context) == null && createLayersIfNonExistent) {
                    Layer destinationLayer =
                            context.getLayerManager().addLayer(
                            StandardCategoryNames.WORKING,
                            DESTINATION_LAYER_NAME,
                            destinationFeatureCollection);
                    init(destinationLayer, GOLD, 255, 1);
                }
            }

            public void unexecute() {
                //Undo is handled by #addUndo. [Bob Boseko]
            }
        }, context);
    }

    public UndoableCommand addLayerGeneration(
            final UndoableCommand wrappeeCommand,
            final WorkbenchContext context,
            final boolean createLayersIfNonExistent) {
        return new UndoableCommand(wrappeeCommand.getName()) {
            private UndoableCommand layerGenerationCommand = null;

            private UndoableCommand layerGenerationCommand() {
                if (layerGenerationCommand == null) {
                    layerGenerationCommand = createCommand(context, createLayersIfNonExistent);
                }
                return layerGenerationCommand;
            }

            public void execute() {
                wrappeeCommand.execute();
                layerGenerationCommand().execute();
            }

            public void unexecute() {
                layerGenerationCommand().unexecute();
                wrappeeCommand.unexecute();
            }
        };
    }

    public static UndoableCommand addUndo(
            final UndoableCommand wrappeeCommand,
            final LayerManagerProxy proxy) {
        return Layer.addUndo(
                DESTINATION_LAYER_NAME,
                proxy,
                Layer.addUndo(SOURCE_LAYER_NAME, proxy, wrappeeCommand));
    }
    private final static String WARP_ID_NAME = "WARP_ID";

    private FeatureCollection toFeatureCollection(Collection triangles) {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        featureSchema.addAttribute(WARP_ID_NAME, AttributeType.INTEGER);

        FeatureCollection featureCollection = new FeatureDataset(featureSchema);

        int j = 0;
        for (Iterator i = triangles.iterator(); i.hasNext();) {
            Triangle t = (Triangle) i.next();
            j++;
            Feature feature = new BasicFeature(featureSchema);
            feature.setGeometry(factory.createPolygon(t.toLinearRing(), null));
            feature.setAttribute(WARP_ID_NAME, new Integer(j));
            featureCollection.add(feature);
        }

        return featureCollection;
    }
    private GeometryFactory factory = new GeometryFactory();

    private void init(Layer layer, Color color, int alpha, int lineWidth) {
        boolean firingEvents = layer.getLayerManager().isFiringEvents();
        layer.getLayerManager().setFiringEvents(false);
        try {
            layer.getBasicStyle().setLineColor(color);
            layer.getBasicStyle().setFillColor(color);
            layer.getBasicStyle().setAlpha(alpha);
            layer.getBasicStyle().setLineWidth(lineWidth);
            layer.getBasicStyle().setRenderingFill(false);
            layer.getVertexStyle().setEnabled(true);
            layer.getVertexStyle().setSize(4);
            layer.getLabelStyle().setEnabled(true);
            layer.getLabelStyle().setColor(color);
            layer.getLabelStyle().setFont(layer.getLabelStyle().getFont().deriveFont(Font.PLAIN, 12));
            layer.getLabelStyle().setAttribute(WARP_ID_NAME);
            layer.getLabelStyle().setHeight(12);
            layer.getLabelStyle().setScaling(false);
            layer.getLabelStyle().setHidingOverlappingLabels(false);
        } finally {
            layer.getLayerManager().setFiringEvents(firingEvents);
        }
        layer.fireAppearanceChanged();
    }

    public Icon getIcon() {
        return IconLoader.icon("Triangle.gif");
    }
}
