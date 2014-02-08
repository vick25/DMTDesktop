package com.osfac.wms;

/**
 * Represents a bounding box in a specific projection. A BoundingBox is
 * immutable, so you must create a new BoundingBox object, rather than modify
 * the the values of an existing BoundingBox.
 *
 * @author chodgson@refractions.net
 */
public class BoundingBox {

    private String srs;
    private double minx;
    private double miny;
    private double maxx;
    private double maxy;

    /**
     * Creates a new BoundingBox with the given SRS, minima and maxima.
     *
     * @param srs a WMS-style SRS string such as "EPSG:1234", or the special
     * string "LatLon" for a latitude/longitude box
     * @param minx the minimum x-value of the bounding box
     * @param miny the minimum y-value of the bounding box
     * @param maxx the maximum x-value of the bounding box
     * @param maxy the maximum y-value of the bounding box
     */
    public BoundingBox(String srs, double minx, double miny, double maxx, double maxy) {
        this.srs = srs;
        this.minx = minx;
        this.miny = miny;
        this.maxx = maxx;
        this.maxy = maxy;
    }

    /**
     * Gets the SRS string.
     *
     * @return the BoundingBox's SRS WMS-style string
     */
    public String getSRS() {
        return srs;
    }

    /**
     * Gets the BoundingBox's minimum x value.
     *
     * @return the BoundingBox's minimum x value
     */
    public double getMinX() {
        return minx;
    }

    /**
     * Gets the BoundingBox's minimum y value.
     *
     * @return the BoundingBox's minimum y value
     */
    public double getMinY() {
        return miny;
    }

    /**
     * Gets the BoundingBox's maximum x value.
     *
     * @return the BoundingBox's maximum x value
     */
    public double getMaxX() {
        return maxx;
    }

    /**
     * Gets the BoundingBox's maximum y value.
     *
     * @return the BoundingBox's maximum y value
     */
    public double getMaxY() {
        return maxy;
    }
}
