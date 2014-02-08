package com.osfac.dmt.workbench.ui.renderer.java2D;

import com.osfac.dmt.I18N;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * TODO : I18N to finish
 */
public class GeometryCollectionShape implements Shape {

    private ArrayList shapes = new ArrayList();

    public GeometryCollectionShape() {
    }

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public Rectangle getBounds() {
        Rectangle rectangle = null;

        for (Iterator i = shapes.iterator(); i.hasNext();) {
            Shape shape = (Shape) i.next();

            if (rectangle == null) {
                rectangle = shape.getBounds();
            } else {
                rectangle.add(shape.getBounds());
            }
        }
        return rectangle;
    }

    public Rectangle2D getBounds2D() {
        //  LDB: Implemented for printing interface
        Rectangle2D rectangle = null;

        for (Iterator i = shapes.iterator(); i.hasNext();) {
            Shape shape = (Shape) i.next();

            if (rectangle == null) {
                rectangle = shape.getBounds2D();
            } else {
                rectangle.add(shape.getBounds2D());
            }
        }

        return rectangle;
    }

    public boolean contains(double x, double y) {
        /**
         * @todo Implement this java.awt.Shape method
         */
        throw new java.lang.UnsupportedOperationException(
                I18N.get("ui.renderer.GeometryCollectionShape.method-contains-not-yet-implemented"));
    }

    public boolean contains(Point2D p) {
        /**
         * @todo Implement this java.awt.Shape method
         */
        throw new java.lang.UnsupportedOperationException(
                I18N.get("ui.renderer.GeometryCollectionShape.method-contains-not-yet-implemented"));
    }

    public boolean intersects(double x, double y, double w, double h) {
        /**
         * @todo Implement this java.awt.Shape method
         */
        throw new java.lang.UnsupportedOperationException(
                "Method intersects() not yet implemented.");
    }

    public boolean intersects(Rectangle2D r) {
        /**
         * @todo Implement this java.awt.Shape method
         */
        throw new java.lang.UnsupportedOperationException(
                "Method intersects() not yet implemented.");
    }

    public boolean contains(double x, double y, double w, double h) {
        /**
         * @todo Implement this java.awt.Shape method
         */
        throw new java.lang.UnsupportedOperationException(
                "Method contains() not yet implemented.");
    }

    public boolean contains(Rectangle2D r) {
        /**
         * @todo Implement this java.awt.Shape method
         */
        throw new java.lang.UnsupportedOperationException(
                "Method contains() not yet implemented.");
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return new ShapeCollectionPathIterator(shapes, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        // since we don't support curved geometries, can simply delegate to the simple method
        return getPathIterator(at);
    }
}
