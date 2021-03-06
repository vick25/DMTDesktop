package com.osfac.dmt.workbench.ui.renderer.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.geom.Angle;
import com.osfac.dmt.geom.CoordUtil;
import com.osfac.dmt.geom.InteriorPointFinder;
import com.osfac.dmt.util.CoordinateArrays;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.util.Assert;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.openjump.core.ui.util.ScreenScale;

public class LabelStyle implements Style {

    public final static int FONT_BASE_SIZE = 12;
    public static final String FID_COLUMN = "$FID";
    public final static String ABOVE_LINE = "ABOVE_LINE";  //LDB: keep these for Project file
    public final static String ON_LINE = "ON_LINE";
    public final static String BELOW_LINE = "BELOW_LINE";
    public final static String DEFAULT = "DEFAULT";
    public final static String[] verticalAlignmentLookup = {ABOVE_LINE, ON_LINE, BELOW_LINE, DEFAULT};
    public final static String LEFT_SIDE = "LEFT_SIDE";
    public final static String CENTER = "CENTER";
    public final static String RIGHT_SIDE = "RIGHT_SIDE";
    public final static String[] horizontalPositionLookup = {LEFT_SIDE, CENTER, RIGHT_SIDE};
    // At the moment, internationalization is of no use as the UI display
    // an image in the vertical alignment ComboBox used [mmichaud 2007-06-02]
    // Disabled image in ComboBox and replaced with existing I18N text [LDB 2007-08-27]
    public static String DEFAULT_TEXT = I18N.get("ui.renderer.style.LabelStyle.default");
    public static String ABOVE_TEXT = I18N.get("ui.renderer.style.LabelStyle.above");
    public static String MIDDLE_TEXT = I18N.get("ui.renderer.style.LabelStyle.middle");
    public static String BELOW_TEXT = I18N.get("ui.renderer.style.LabelStyle.below");
    public final static String LEFT_SIDE_TEXT = I18N.get("ui.renderer.style.LabelStyle.left-side");
    public final static String CENTER_TEXT = I18N.get("ui.renderer.style.LabelStyle.center");
    public final static String RIGHT_SIDE_TEXT = I18N.get("ui.renderer.style.LabelStyle.right-side");
    public final static String JUSTIFY_CENTER_TEXT = I18N.get("ui.renderer.style.LabelStyle.centered");
    public final static String JUSTIFY_LEFT_TEXT = I18N.get("ui.renderer.style.LabelStyle.left-alignment");
    public final static String JUSTIFY_RIGHT_TEXT = I18N.get("ui.renderer.style.LabelStyle.right-alignment");
    public final static int JUSTIFY_CENTER = 0; // LDB: in retrospect, should have used text lookup as above
    public final static int JUSTIFY_LEFT = 1; // for readabilty of Project XML file
    public final static int JUSTIFY_RIGHT = 2;
    private GeometryFactory factory = new GeometryFactory();
    private Color originalColor;
    private AffineTransform originalTransform;
    private Layer layer;
    private Geometry viewportRectangle = null;
    private InteriorPointFinder interiorPointFinder = new InteriorPointFinder();
    private Quadtree labelsDrawn = null;
    private String attribute = LabelStyle.FID_COLUMN;
    private String angleAttribute = ""; //"" means no angle attribute [Bob Boseko]
    private String heightAttribute = ""; //"" means no height attribute [Bob Boseko]
    private boolean enabled = false;
    private Color color = Color.black;
    private Font font = new Font("Dialog", Font.PLAIN, FONT_BASE_SIZE);
    private boolean scaling = false;
    private double height = 12;
    private boolean hidingOverlappingLabels = true;
    public String verticalAlignment = DEFAULT;
    public String horizontalPosition = CENTER;
    private int horizontalAlignment = JUSTIFY_CENTER;
    private boolean outlineShowing = false;
    private Color outlineColor = new Color(230, 230, 230, 192);
    private double outlineWidth = 4d;
    private Stroke outlineStroke = new BasicStroke(4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private boolean hideAtScale = false;
    private double scaleToHideAt = 20000d;

    public LabelStyle() {
    }

    public void initialize(Layer layer) {
        labelsDrawn = new Quadtree();
        viewportRectangle = null;
        this.layer = layer;
    }

    public void paint(Feature f, Graphics2D g, Viewport viewport)
            throws NoninvertibleTransformException {
        Object attributeValue = getAttributeValue(f);
        String attributeStringValue;
        if ((attributeValue == null)) {
            return;
        } else if (attributeValue instanceof String) {
            // added .trim() 2007-07-13 [mmichaud]
            attributeStringValue = ((String) attributeValue).trim();
            if (attributeStringValue.length() == 0) {
                return;
            }
        } else if (attributeValue instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
            attributeStringValue = dateFormat.format((Date) attributeValue);
        } else if (attributeValue instanceof Double) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            attributeStringValue = numberFormat.format(((Double) attributeValue).doubleValue());
        } else if (attributeValue instanceof Integer) {
            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
            attributeStringValue = numberFormat.format(((Integer) attributeValue).intValue());
        } else {
            attributeStringValue = attributeValue.toString();
        }

        if (isHidingAtScale()) {
            double scale = height / getFont().getSize2D();
            if (isScaling()) {
                scale *= viewport.getScale();
            }
            double realScale = ScreenScale.getHorizontalMapScale(viewport);
            if (realScale > scaleToHideAt) {
                return;
            }
        }
        Geometry viewportIntersection = intersection(f.getGeometry(), viewport);
        if (viewportIntersection == null) {
            return;
        }
        ModelSpaceLabelSpec spec = modelSpaceLabelSpec(viewportIntersection);
        Point2D labelCentreInViewSpace =
                viewport.toViewPoint(new Point2D.Double(spec.location.x, spec.location.y));
        paint(
                g,
                attributeStringValue,
                viewport,//.getScale(),
                labelCentreInViewSpace,
                angle(f, getAngleAttribute(), spec.angle),
                height(f, getHeightAttribute(), getHeight()),
                spec.dim);
    }

    /**
     * Gets the appropriate attribute value, if one exists. If for some reason
     * the attribute column does not exist, return null
     *
     * @param f
     * @return the value of the attribute
     * @return null if the attribute column does not exist
     */
    private Object getAttributeValue(Feature f) {
        if (getAttribute().equals(LabelStyle.FID_COLUMN)) {
            return f.getID() + "";
        }
        if (!f.getSchema().hasAttribute(getAttribute())) {
            return null;
        }
        return f.getAttribute(getAttribute());
    }

    public static double angle(Feature feature,
            String angleAttributeName,
            double defaultAngle) {
        if (angleAttributeName.equals("")) {
            return defaultAngle;
        }
        Object angleAttribute = feature.getAttribute(angleAttributeName);
        if (angleAttribute == null) {
            return defaultAngle;
        }
        try {
            return Angle.toRadians(Double.parseDouble(angleAttribute.toString().trim()));
        } catch (NumberFormatException e) {
            return defaultAngle;
        }
    }

    private ModelSpaceLabelSpec modelSpaceLabelSpec(Geometry geometry)
            throws NoninvertibleTransformException {
        if (geometry.getDimension() == 1) {
            return modelSpaceLabelSpec1D(geometry);
        }
        if (geometry.getDimension() == 0) { //LDB: treat points as linear to justify them
            return new ModelSpaceLabelSpec(geometry.getCoordinate(), 0d, 0);
        }
        if (verticalAlignment.equals(ON_LINE) || verticalAlignment.equals(DEFAULT)) {
            return new ModelSpaceLabelSpec(interiorPointFinder.findPoint(geometry), 0, 2);
        }
        return new ModelSpaceLabelSpec(findPoint(geometry), 0, 2);
    }

    /**
     * Find a point at upper-left, upper-center, upper-right, center-left,
     * center, center-right, lower-left, lower-center or lower-right of the
     * geometry envelope.
     */
    public Coordinate findPoint(Geometry geometry) {
        if (geometry.isEmpty()) {
            return new Coordinate(0, 0);
        }
        Envelope envelope = geometry.getEnvelopeInternal();
        double x = (envelope.getMinX() + envelope.getMaxX()) / 2d;
        double y = (envelope.getMinY() + envelope.getMaxY()) / 2d;
        if (verticalAlignment.equals(DEFAULT) && geometry.getDimension() != 2) {
            y = envelope.getMaxY();
        } else if (verticalAlignment.equals(ABOVE_LINE)) {
            y = envelope.getMaxY();
        } else if (verticalAlignment.equals(BELOW_LINE)) {
            y = envelope.getMinY();
        }
        switch (horizontalPosition) {
            case LEFT_SIDE:
                x = envelope.getMinX();
                break;
            case RIGHT_SIDE:
                x = envelope.getMaxX();
                break;
        }
        return new Coordinate(x, y);
    }

    private ModelSpaceLabelSpec modelSpaceLabelSpec1D(Geometry geometry) {
        LineSegment segment;
        switch (horizontalPosition) {
            case LEFT_SIDE:
                segment = endSegment(geometry);
                return new ModelSpaceLabelSpec(
                        factory.createPoint(segment.p0).getCoordinate(), angle(segment), 1);
            case RIGHT_SIDE:
                segment = endSegment(geometry);
                return new ModelSpaceLabelSpec(
                        factory.createPoint(segment.p1).getCoordinate(), angle(segment), 1);
            default:
                segment = longestSegment(geometry);
                break;
        }
        //LineSegment longestSegment = longestSegment(geometry);
        return new ModelSpaceLabelSpec(
                (horizontalAlignment == JUSTIFY_CENTER)
                ? CoordUtil.average(segment.p0, segment.p1)
                : (horizontalAlignment == JUSTIFY_LEFT)
                ? segment.p0 : segment.p1,
                angle(segment),
                1);
    }

    private double angle(LineSegment segment) {
        double angle = Angle.angle(segment.p0, segment.p1);
        //Don't want upside-down labels! [Bob Boseko]
        if (angle < (-Math.PI / 2d)) {
            angle += Math.PI;
        }
        if (angle > (Math.PI / 2d)) {
            angle -= Math.PI;
        }
        return angle;
    }

    private LineSegment longestSegment(Geometry geometry) {
        double maxSegmentLength = -1;
        Coordinate c0 = null;
        Coordinate c1 = null;
        List arrays = CoordinateArrays.toCoordinateArrays(geometry, false);
        for (Iterator i = arrays.iterator(); i.hasNext();) {
            Coordinate[] coordinates = (Coordinate[]) i.next();
            for (int j = 1; j < coordinates.length; j++) { //start 1
                double length = coordinates[j - 1].distance(coordinates[j]);
                if (length > maxSegmentLength) {
                    maxSegmentLength = length;
                    c0 = coordinates[j - 1];
                    c1 = coordinates[j];
                }
            }
        }
        return new LineSegment(c0, c1);
    }

    private LineSegment endSegment(Geometry geometry) {
        Coordinate c0 = geometry.getCoordinates()[0];
        Coordinate c1 = geometry.getCoordinates()[geometry.getNumPoints() - 1];
        //if (geometry.getNumPoints() < 3) return new LineSegment(c0, c1);
        if (c0.x <= c1.x && horizontalPosition.equals(LEFT_SIDE)) {
            if (horizontalAlignment == JUSTIFY_RIGHT) {
                return new LineSegment(c0, new Coordinate(c1.x, c0.y));
            } else {
                return new LineSegment(c0, geometry.getCoordinates()[1]);
            }
        } else if (c0.x <= c1.x && horizontalPosition.equals(RIGHT_SIDE)) {
            if (horizontalAlignment == JUSTIFY_LEFT) {
                return new LineSegment(c1, new Coordinate(c1.x + 1.0, c1.y));
            } else {
                return new LineSegment(geometry.getCoordinates()[geometry.getNumPoints() - 2], c1);
            }
        } else if (c0.x > c1.x && horizontalPosition.equals(LEFT_SIDE)) {
            if (horizontalAlignment == JUSTIFY_RIGHT) {
                return new LineSegment(new Coordinate(c1.x - 1.0, c1.y), c1);
            } else {
                return new LineSegment(c1, geometry.getCoordinates()[geometry.getNumPoints() - 2]);
            }
        } else if (c0.x > c1.x && horizontalPosition.equals(RIGHT_SIDE)) {
            if (horizontalAlignment == JUSTIFY_LEFT) {
                return new LineSegment(new Coordinate(c0.x - 1, c0.y), c0);
            } else {
                return new LineSegment(geometry.getCoordinates()[1], c0);
            }
        } else {
            return longestSegment(geometry);
        }
    }

    public static double height(
            Feature feature,
            String heightAttributeName,
            double defaultHeight) {
        if (heightAttributeName.equals("")) {
            return defaultHeight;
        }
        Object heightAttribute = feature.getAttribute(heightAttributeName);
        if (heightAttribute == null) {
            return defaultHeight;
        }
        try {
            return Double.parseDouble(heightAttribute.toString().trim());
        } catch (NumberFormatException e) {
            return defaultHeight;
        }
    }

    public void paint(Graphics2D g,
            String text,
            Viewport viewport,
            Point2D viewCentre,
            double angle,
            double height,
            int dim) {
        setup(g);
        try {
            double viewportScale = viewport.getScale();
            double scale = height / getFont().getSize2D();
            if (isScaling()) {
                scale *= viewportScale;
            }
            TextLayout layout = new TextLayout(text, getFont(), g.getFontRenderContext());
            AffineTransform transform = g.getTransform();
            configureTransform(transform, viewCentre, scale, layout, angle, dim);
            g.setTransform(transform);
            if (isHidingOverlappingLabels()) {
                Area transformedLabelBounds =
                        new Area(layout.getBounds()).createTransformedArea(transform);
                Envelope transformedLabelBoundsEnvelope =
                        envelope(transformedLabelBounds);
                if (collidesWithExistingLabel(transformedLabelBounds,
                        transformedLabelBoundsEnvelope)) {
                    return;
                }
                labelsDrawn.insert(
                        transformedLabelBoundsEnvelope,
                        transformedLabelBounds);
            }
            if (outlineShowing) {
                g.setColor(outlineColor);
                g.setStroke(outlineStroke);
                g.draw(layout.getOutline(null));
            }
            g.setColor(getColor());
            layout.draw(g, 0, 0);
        } finally {
            cleanup(g);
        }
    }

    private Envelope envelope(Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();
        return new Envelope(bounds.getMinX(), bounds.getMaxX(),
                bounds.getMinY(), bounds.getMaxY());
    }

    private boolean collidesWithExistingLabel(
            Area transformedLabelBounds,
            Envelope transformedLabelBoundsEnvelope) {
        List potentialCollisions = labelsDrawn.query(transformedLabelBoundsEnvelope);
        for (Iterator i = potentialCollisions.iterator(); i.hasNext();) {
            Area potentialCollision = (Area) i.next();
            Area intersection = new Area(potentialCollision);
            intersection.intersect(transformedLabelBounds);
            if (!intersection.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void setup(Graphics2D g) {
        originalTransform = g.getTransform();
        originalColor = g.getColor();
    }

    private void cleanup(Graphics2D g) {
        g.setTransform(originalTransform);
        g.setColor(originalColor);
    }

    /**
     * Even though a feature's envelope intersects the viewport, the feature
     * itself may not intersect the viewport. In this case, this method returns
     * null.
     */
    private Geometry intersection(Geometry geometry, Viewport viewport) {
        Geometry geo;
        try {
            //LDB: need to catch the NoninvertibleTransformException instead of just throwing it
            geo = geometry.intersection(viewportRectangle(viewport));
        } catch (NoninvertibleTransformException e) {
            return null;
        }
        if (geo.getNumGeometries() == 0) {
            return null;
        }
        return geo;
    }

    private Geometry viewportRectangle(Viewport viewport)
            throws NoninvertibleTransformException {
        if (viewportRectangle == null) {
            Envelope e =
                    viewport.toModelEnvelope(
                    0,
                    viewport.getPanel().getWidth(),
                    0,
                    viewport.getPanel().getHeight());
            viewportRectangle =
                    factory.createPolygon(
                    factory.createLinearRing(
                    new Coordinate[]{
                        new Coordinate(e.getMinX(), e.getMinY()),
                        new Coordinate(e.getMinX(), e.getMaxY()),
                        new Coordinate(e.getMaxX(), e.getMaxY()),
                        new Coordinate(e.getMaxX(), e.getMinY()),
                        new Coordinate(e.getMinX(), e.getMinY())}),
                    null);
        }
        return viewportRectangle;
    }

    private void configureTransform(AffineTransform transform,
            Point2D viewCentre,
            double scale,
            TextLayout layout,
            double angle,
            int dim) {

        double xTranslation = viewCentre.getX();
        double yTranslation = viewCentre.getY() + ((scale * GUIUtil.trueAscent(layout)) / 2d);
        if (dim == 1) {
            xTranslation -= horizontalAlignmentOffset(scale * layout.getBounds().getWidth());
            yTranslation -= verticalAlignmentOffset(scale * layout.getBounds().getHeight(), dim);
        } else if (dim == 0) {
            xTranslation -= horizontalPositionOffset(scale * layout.getBounds().getWidth());
            yTranslation -= verticalAlignmentOffset(scale * layout.getBounds().getHeight(), dim);
        } else {
            xTranslation -= horizontalAlignmentOffset(scale * layout.getBounds().getWidth());
            yTranslation -= verticalAlignmentOffset(scale * layout.getBounds().getHeight(), dim);
        }
        //Negate the angle because the positive y-axis points downwards.
        //See the #rotate JavaDoc. [Bob Boseko]
        transform.rotate(-angle, viewCentre.getX(), viewCentre.getY());
        transform.translate(xTranslation, yTranslation);
        transform.scale(scale, scale);
    }

    private double verticalAlignmentOffset(double scaledLabelHeight, int dim) {
        if (getVerticalAlignment().equals(ON_LINE)
                || (getVerticalAlignment().equals(DEFAULT) && dim == 2)) {
            return 0;
        }
        double buffer = 3;
        double offset = buffer
                + (layer.getBasicStyle().getLineWidth() / 2d)
                + (scaledLabelHeight / 2d);
        if (getVerticalAlignment().equals(ABOVE_LINE)
                || (getVerticalAlignment().equals(DEFAULT) && dim != 2)) {
            return offset;
        }
        if (getVerticalAlignment().equals(BELOW_LINE)) {
            return -offset;
        }
        Assert.shouldNeverReachHere();
        return 0;
    }

    private double horizontalPositionOffset(double width) {
        if (getHorizontalPosition().equals(LEFT_SIDE)) {
            return width;
        }
        if (getHorizontalPosition().equals(CENTER)) {
            return width / 2d;
        }
        if (getHorizontalPosition().equals(RIGHT_SIDE)) {
            return 0;
        }
        Assert.shouldNeverReachHere();
        return 0;
    }

    private double horizontalAlignmentOffset(double width) {
        if (getHorizontalAlignment() == JUSTIFY_CENTER) {
            return width / 2d;
        }
        if (getHorizontalAlignment() == JUSTIFY_LEFT) {
            return 0;
        }
        if (getHorizontalAlignment() == JUSTIFY_RIGHT) {
            return width;  //LDB: see hack in modelSpaceLabelSpec1D
        }
        Assert.shouldNeverReachHere();
        return 0;
    }

    /**
     * @return approximate alignment offset for estimation
     */
    public double getVerticalAlignmentOffset(int dim) {
        return verticalAlignmentOffset(getHeight(), dim) - getHeight() / 2;
    }

    /**
     * @return approximate alignment offset for estimation
     */
    public double getHorizontalAlignmentOffset(String text) {
        return horizontalAlignmentOffset(text.length() * getHeight() * 0.6);
    }

    public String getAttribute() {
        return attribute;
    }

    public String getAngleAttribute() {
        return angleAttribute;
    }

    public String getHeightAttribute() {
        return heightAttribute;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public boolean isScaling() {
        return scaling;
    }

    public double getHeight() {
        return height;
    }

    public boolean isHidingOverlappingLabels() {
        return hidingOverlappingLabels;
    }

    public boolean isHidingAtScale() {
        return hideAtScale;
    }

    public boolean getHideAtScale() {
        return hideAtScale;
    }

    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    public String getHorizontalPosition() {
        return horizontalPosition;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public boolean getHidingOverlappingLabels() {
        return hidingOverlappingLabels;
    }

    public boolean getOutlineShowing() {
        return outlineShowing;
    }

    public double getOutlineWidth() {
        return outlineWidth;
    }

    public double getScaleToHideAt() {
        return scaleToHideAt;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public void setHorizontalPosition(String horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setAngleAttribute(String angleAttribute) {
        this.angleAttribute = angleAttribute;
    }

    public void setHeightAttribute(String heightAttribute) {
        this.heightAttribute = heightAttribute;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setScaling(boolean scaling) {
        this.scaling = scaling;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setHidingOverlappingLabels(boolean hidingOverlappingLabels) {
        this.hidingOverlappingLabels = hidingOverlappingLabels;
    }

    public void setOutlineShowing(boolean outlineShowing) {
        this.outlineShowing = outlineShowing;
    }

    public void setOutlineWidth(double outlineWidth) {
        this.outlineWidth = outlineWidth;
        this.outlineStroke = new BasicStroke((float) outlineWidth,
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    }

    public void setScaleToHideAt(double scaleToHideAt) {
        this.scaleToHideAt = scaleToHideAt;
    }

    public void setOutlineColor(Color outlineColor, int alpha) {
        this.outlineColor = new Color(outlineColor.getRed(),
                outlineColor.getGreen(),
                outlineColor.getBlue(),
                alpha);
    }

    public void setOutlineColor(Color outlineColor) {
        if (outlineColor != null) {
            int alpha = this.outlineColor.getAlpha();
            setOutlineColor(outlineColor, alpha);
        }
    }

    public void setHideAtScale(boolean hideAtScale) {
        this.hideAtScale = hideAtScale;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            Assert.shouldNeverReachHere();
            return null;
        }
    }

    private class ModelSpaceLabelSpec {

        public double angle;
        public Coordinate location;
        public int dim;

        public ModelSpaceLabelSpec(Coordinate location, double angle, int dim) {
            this.location = location;
            this.angle = angle;
            this.dim = dim;
        }
    }
}
