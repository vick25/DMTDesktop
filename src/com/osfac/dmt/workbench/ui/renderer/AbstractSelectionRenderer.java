package com.osfac.dmt.workbench.ui.renderer;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.Viewport;
import com.osfac.dmt.workbench.ui.renderer.style.Style;
import com.osfac.dmt.workbench.ui.renderer.style.StyleUtil;
import com.osfac.dmt.workbench.ui.renderer.style.VertexStyle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;
import de.latlon.deejump.plugin.style.VertexStylesFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.Icon;

public abstract class AbstractSelectionRenderer extends FeatureCollectionRenderer implements Style {

    public final static int HANDLE_WIDTH = 5;
    private boolean enabled = true;
    private Stroke handleStroke = new BasicStroke(1);
    private Color handleFillColor;
    private Color handleLineColor = Color.black;
    private Stroke lineStroke = new BasicStroke(2);
    private Color lineColor;
    private Stroke fillStroke = new BasicStroke(1);
    private Color fillColor;
    private boolean filling = true;
    private int selectionPointSize = 5;
    private String selectionPointForm = VertexStylesFactory.SQUARE_STYLE;
    VertexStyle vertexStyle = null;
    protected LayerViewPanel panel;

    public AbstractSelectionRenderer(Object contentID,
            LayerViewPanel panel,
            Color color,
            boolean paintingHandles,
            boolean filling) {
        super(contentID, panel);
        this.panel = panel;
        handleFillColor = color;
        lineColor = color;
        fillColor = GUIUtil.alphaColor(Color.white, 75);
        this.paintingHandles = paintingHandles;
        this.filling = filling;
        vertexStyle = VertexStylesFactory.createVertexStyle(selectionPointForm);
        vertexStyle.setSize(selectionPointSize);
        vertexStyle.setAlpha(255);
        vertexStyle.setFillColor(handleFillColor);
        vertexStyle.setLineColor(handleLineColor);
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public Icon getIcon() {
        throw new UnsupportedOperationException();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Object clone() {
        Assert.shouldNeverReachHere();
        return null;
    }

    public void initialize(Layer layer) {
    }
    private Map<Feature, Collection<Geometry>> featureToSelectedItemsMap;
    private boolean paintingHandles;

    public void paint(Feature f, Graphics2D g, Viewport viewport)
            throws NoninvertibleTransformException {
        for (Iterator i = featureToSelectedItemsMap.get(f).iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            paint(geometry, g, viewport);
        }
    }
    Rectangle2D.Double handle = new Rectangle2D.Double(0, 0, HANDLE_WIDTH, HANDLE_WIDTH);

    public void paint(Geometry geometry, Graphics2D g, Viewport viewport)
            throws NoninvertibleTransformException {
        if (!viewport.getEnvelopeInModelCoordinates().intersects(geometry.getEnvelopeInternal())) {
            return;
        }
        Coordinate[] modelCoordinates = geometry.getCoordinates();
        if ((geometry.getDimension() > 0) || (!paintingHandles)) {  //points will be obscurred by handles anyway      
            StyleUtil.paint(
                    geometry,
                    g,
                    viewport,
                    filling,
                    fillStroke,
                    fillColor,
                    true,
                    lineStroke,
                    lineColor);
        }
        if (paintingHandles) {
            // paintHandles(g, coordinates, handleStroke, handleFillColor, handleLineColor, panel.getViewport());
            // LDB: the above method is very slow.  The following code is aproximately equivalent
            //      although it draws a different style of handle (overlapping vs. hollow)
            Coordinate[] viewCoordinates = viewport.getJava2DConverter().toViewCoordinates(modelCoordinates);
            g.setStroke(handleStroke);
            Rectangle2D viewRectangle = viewport.toViewRectangle(
                    viewport.getEnvelopeInModelCoordinates());
            for (int i = 0; i < viewCoordinates.length; i++) {
                Coordinate p = viewCoordinates[i];
                double x = p.x;
                double y = p.y;
                if (!viewRectangle.contains(x, y)) {  //<<JOKE:handle with care>>
                    //Otherwise get "sun.dc.pr.PRException: endPath: bad path" exception [Bob Boseko 10/22/2003]
                    continue;
                }
                vertexStyle.paint(g, new Point2D.Double(x, y)); // [Matthias Scholz 3. Sept. 2010]
            }
        }
    }

    protected Collection styles() {
        return Collections.singleton(this);
    }

    protected Map layerToFeaturesMap() {
        featureToSelectedItemsMap = new HashMap<Feature, Collection<Geometry>>();
        Map<Layer, List<Feature>> layerToFeaturesMap = new HashMap();
        for (Iterator i = panel.getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            Map<Feature, List<Geometry>> featureToSelectedItemsMapForLayer = featureToSelectedItemsMap(layer);
            // For each feature of layer, put selected items into a general
            // featureToSelectedItemsMap
            for (Feature feature : featureToSelectedItemsMapForLayer.keySet()) {
                List<Geometry> list = new ArrayList(1);
                List<Geometry> selectedItems = featureToSelectedItemsMapForLayer.get(feature);
                if (selectedItems != null) {
                    for (Geometry g : selectedItems) {
                        list.add(g);
                    }
                }
                featureToSelectedItemsMap.put(feature, list);
            }
            List list = new ArrayList();
            // put each feature with selected items in a layerToFeaturesMap
            for (Feature feature : featureToSelectedItemsMapForLayer.keySet()) {
                list.add(feature);
            }
            layerToFeaturesMap.put(layer, list);
        }
        return layerToFeaturesMap;
    }

    protected abstract Map<Feature, List<Geometry>> featureToSelectedItemsMap(Layer layer);

    private static Shape handle(Point2D point) {
        Rectangle2D.Double handle = new Rectangle2D.Double(0.0, 0.0, HANDLE_WIDTH, HANDLE_WIDTH);
        handle.x = point.getX() - (HANDLE_WIDTH / 2);
        handle.y = point.getY() - (HANDLE_WIDTH / 2);

        return handle;
    }

    public static void paintHandles(Graphics2D g,
            Coordinate[] coordinates,
            Stroke stroke,
            Color fillColor,
            Color lineColor,
            Viewport viewport) throws NoninvertibleTransformException {
        g.setStroke(stroke);
        g.setColor(fillColor);

        for (int i = 0; i < coordinates.length; i++) {
            if (!viewport.getEnvelopeInModelCoordinates().contains(coordinates[i])) {
                //Otherwise get "sun.dc.pr.PRException: endPath: bad path" exception [Bob Boseko 10/22/2003]
                continue;
            }
            g.fill(handle(viewport.toViewPoint(
                    new Point2D.Double(coordinates[i].x, coordinates[i].y))));
        }

        g.setColor(lineColor);

        for (int i = 0; i < coordinates.length; i++) {
            if (!viewport.getEnvelopeInModelCoordinates().contains(coordinates[i])) {
                //Otherwise get "sun.dc.pr.PRException: endPath: bad path" exception [Bob Boseko 10/22/2003]
                continue;
            }
            g.draw(handle(viewport.toViewPoint(
                    new Point2D.Double(coordinates[i].x, coordinates[i].y))));
        }
    }

    protected boolean useImageCaching(Map layerToFeaturesMap) {
        return true;
    }

    /**
     * Sets the Color for the Selection rendering.
     *
     * @param color
     */
    public void setSelectionLineColor(Color color) {
        lineColor = color;
        handleFillColor = color;
        vertexStyle.setFillColor(handleFillColor);
    }

    /**
     * Sets the pointsize for selected features.
     *
     * @param selectionPointSize
     */
    public void setSelectionPointSize(int selectionPointSize) {
        this.selectionPointSize = selectionPointSize;
        vertexStyle.setSize(selectionPointSize);
    }

    /**
     * Sets the point form. For possible forms please see
     * {@linkplain de.latlon.deejump.plugin.style.VertexStylesFactory VertexStylesFactory}
     * constants.
     *
     * @param selectionPointForm
     */
    public void setSelectionPointForm(String selectionPointForm) {
        this.selectionPointForm = selectionPointForm;
        vertexStyle = VertexStylesFactory.createVertexStyle(selectionPointForm);
        vertexStyle.setSize(selectionPointSize);
        vertexStyle.setAlpha(255);
        vertexStyle.setFillColor(handleFillColor);
        vertexStyle.setLineColor(handleLineColor);
    }
}
