package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.util.Fmt;
import com.osfac.dmt.workbench.model.FenceLayerFinder;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.TextFrame;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.WKTWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class VerticesInFencePlugIn extends AbstractPlugIn {

    private WKTWriter wktWriter = new WKTWriter(3);
    private GeometryFactory factory = new GeometryFactory();

    public VerticesInFencePlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        TextFrame textFrame = new TextFrame(context.getWorkbenchFrame());
        textFrame.setTitle(I18N.get("ui.plugin.VerticesInFencePlugIn.vertices-in-fence"));
        textFrame.clear();
        textFrame.setText(description(context));
        textFrame.setSize(550, 300);
        context.getWorkbenchFrame().addInternalFrame(textFrame);
        return true;
    }

    private String description(PlugInContext context) {
        FenceLayerFinder fenceLayerFinder = new FenceLayerFinder(context);
        StringBuilder description = new StringBuilder();
        description.append("<html><body>");
        for (Iterator i = context.getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (!layer.isVisible()) {
                continue;
            }
            if (layer == fenceLayerFinder.getLayer()) {
                continue;
            }
            description.append(description(layer, context));
        }
        description.append("</body></html>");
        return description.toString();
    }

    public static Collection verticesInFence(
            Collection geometries,
            Geometry fence,
            boolean skipClosingVertex) {
        ArrayList verticesInFence = new ArrayList();
        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            verticesInFence.addAll(
                    verticesInFence(geometry, fence, skipClosingVertex).getCoordinates());
        }
        return verticesInFence;
    }

    /**
     * @param skipClosingVertex whether to ignore the duplicate point that
     * closes off a LinearRing or Polygon
     */
    public static VerticesInFence verticesInFence(
            Geometry geometry,
            final Geometry fence,
            final boolean skipClosingVertex) {
        final ArrayList coordinates = new ArrayList();
        final ArrayList indices = new ArrayList();
        //PointLocator is non-re-entrant. Therefore, create a new instance for each fence.
        //[Bob Boseko]
        final PointLocator pointLocator = new PointLocator();
        final IntWrapper index = new IntWrapper(-1);
        geometry.apply(new GeometryComponentFilter() {
            public void filter(Geometry geometry) {
                if (geometry instanceof GeometryCollection
                        || geometry instanceof Polygon) {
                    //Wait for the elements to be passed into the filter [Bob Boseko]
                    return;
                }
                Coordinate[] component = geometry.getCoordinates();
                for (int j = 0; j < component.length; j++) {
                    index.value++;
                    if (skipClosingVertex
                            && (component.length > 1)
                            && (j == (component.length - 1))
                            && component[j].equals(component[0])) {
                        continue;
                    }
                    if (pointLocator.locate(component[j], fence) == Location.EXTERIOR) {
                        continue;
                    }
                    coordinates.add(component[j]);
                    indices.add(new Integer(index.value));
                }
            }
        });
        return new VerticesInFence() {
            public List getCoordinates() {
                return coordinates;
            }

            public int getIndex(int i) {
                return ((Integer) indices.get(i)).intValue();
            }
        };
    }

    /**
     * @return an empty String if the layer has no coordinates in the fence
     */
    private String description(Layer layer, PlugInContext context) {
        boolean foundVertices = false;
        String description =
                "<Table width=100%><tr><td colspan=2 valign=top><i>" + I18N.get("ui.plugin.VerticesInFencePlugIn.layer") + " </i><font color='#3300cc'><b>"
                + layer.getName()
                + "</b></font></td></tr>";
        String bgcolor = "darkgrey";
        for (Iterator i =
                layer
                .getFeatureCollectionWrapper()
                .query(context.getLayerViewPanel().getFence().getEnvelopeInternal())
                .iterator();
                i.hasNext();) {
            Feature feature = (Feature) i.next();
            VerticesInFence verticesInFence =
                    verticesInFence(
                    feature.getGeometry(),
                    context.getLayerViewPanel().getFence(),
                    true);
            if (verticesInFence.getCoordinates().isEmpty()) {
                continue;
            }
            if (bgcolor.equals("#faebd7")) {
                bgcolor = "darkgrey";
            } else {
                bgcolor = "#faebd7";
            }
            foundVertices = true;
            //<<TODO:DEFECT>> Get platform-specific newline rather than "\n" [Bob Boseko]
            description += ("<tr bgcolor="
                    + bgcolor
                    + "><td width=10% valign=top><font size='-1'><i>" + I18N.get("ui.plugin.VerticesInFencePlugIn.feature-id") + " </i></font><font size='-1' color='#3300cc'><b>"
                    + feature.getID()
                    + "</b></font><td>");
            description += description(verticesInFence, feature.getGeometry());
            description += "</td></tr>";
        }
        description += "</table>";
        return foundVertices ? description : "";
    }
    private WKTDisplayHelper helper = new WKTDisplayHelper();

    private String description(VerticesInFence verticesInFence, Geometry geometry) {
        StringBuilder description = new StringBuilder();
        //<<TODO:FEATURE>> Perhaps we should change these \n's to the line separators
        //specific to the current platform. Then the user could copy the text and
        //paste it to any editor without any funny symbols potentially appearing. [Bob Boseko]
        description.append("<pre>");
        for (int i = 0; i < verticesInFence.getCoordinates().size(); i++) {
            description.append(
                    GUIUtil.escapeHTML(
                    "["
                    + Fmt.fmt(
                    helper.annotation(
                    geometry,
                    (Coordinate) verticesInFence.getCoordinates().get(i)),
                    10)
                    + "] "
                    + wktWriter.write(
                    factory.createPoint(
                    (Coordinate) verticesInFence.getCoordinates().get(i)))
                    + "\n",
                    false,
                    false));
        }
        description.append("</pre>");
        return description.toString();
    }

    public static interface VerticesInFence {

        public List getCoordinates();

        public int getIndex(int i);
    }

    private static class IntWrapper {

        public int value;

        public IntWrapper(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        WKTWriter wktWriter1 = new WKTWriter();
        ((Geometry) (new Object())).toString();
    }
}
