package com.osfac.dmt.workbench.ui.plugin.generate;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.AttributeType;
import com.osfac.dmt.feature.BasicFeature;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.List;

public class BoundaryMatchDataEngine {

    private Coordinate southwestCornerOfLeftLayer = new Coordinate(0, 0);
    private int layerHeightInCells = 4;
    private int layerWidthInCells = 1;
    private double cellSideLength = 100;
    private int verticesPerCellSide = 4;
    private double boundaryAmplitude = 20;
    private double boundaryPeriod = 150;
    private int verticesPerBoundarySide = 6;
    private double maxBoundaryPerturbation = 1.0;
    private double perturbationProbability = 0.5;
    private GeometryFactory factory = new GeometryFactory();

    public BoundaryMatchDataEngine() {
    }

    public void setSouthwestCornerOfLeftLayer(
            Coordinate newSouthwestCornerOfLeftLayer) {
        southwestCornerOfLeftLayer = newSouthwestCornerOfLeftLayer;
    }

    public void setLayerHeightInCells(int newLayerHeightInCells) {
        layerHeightInCells = newLayerHeightInCells;
    }

    public void setLayerWidthInCells(int newLayerWidthInCells) {
        layerWidthInCells = newLayerWidthInCells;
    }

    public void setCellSideLength(double newCellSideLength) {
        cellSideLength = newCellSideLength;
    }

    public void setVerticesPerCellSide(int newVerticesPerCellSide) {
        verticesPerCellSide = newVerticesPerCellSide;
    }

    public void setBoundaryAmplitude(double newBoundaryAmplitude) {
        boundaryAmplitude = newBoundaryAmplitude;
    }

    public void setBoundaryPeriod(double newBoundaryPeriod) {
        boundaryPeriod = newBoundaryPeriod;
    }

    public void setVerticesPerBoundarySide(int newVerticesPerBoundarySide) {
        verticesPerBoundarySide = newVerticesPerBoundarySide;
    }

    public void setMaxBoundaryPerturbation(double newMaxBoundaryPerturbation) {
        maxBoundaryPerturbation = newMaxBoundaryPerturbation;
    }

    public void setPerturbationProbability(double newPerturbationProbability) {
        perturbationProbability = newPerturbationProbability;
    }

    public Coordinate getSouthwestCornerOfLeftLayer() {
        return southwestCornerOfLeftLayer;
    }

    public int getLayerHeightInCells() {
        return layerHeightInCells;
    }

    public int getLayerWidthInCells() {
        return layerWidthInCells;
    }

    public double getCellSideLength() {
        return cellSideLength;
    }

    public int getVerticesPerCellSide() {
        return verticesPerCellSide;
    }

    public double getBoundaryAmplitude() {
        return boundaryAmplitude;
    }

    public double getBoundaryPeriod() {
        return boundaryPeriod;
    }

    public int getVerticesPerBoundarySide() {
        return verticesPerBoundarySide;
    }

    public double getMaxBoundaryPerturbation() {
        return maxBoundaryPerturbation;
    }

    public double getPerturbationProbability() {
        return perturbationProbability;
    }

    public void execute(PlugInContext context) {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);

        FeatureCollection leftFeatureCollection = new FeatureDataset(featureSchema);
        FeatureCollection rightFeatureCollection = new FeatureDataset(featureSchema);
        addLeftSquareCells(leftFeatureCollection);
        addRightSquareCells(rightFeatureCollection);
        addBoundaryCells(leftFeatureCollection, rightFeatureCollection);
        context.addLayer(StandardCategoryNames.WORKING, I18N.get("ui.plugin.generate.BoundaryMatchDataEngine.left"),
                leftFeatureCollection);
        context.addLayer(StandardCategoryNames.WORKING, I18N.get("ui.plugin.generate.BoundaryMatchDataEngine.right"),
                rightFeatureCollection);
    }

    private double segmentLength() {
        return cellSideLength / (verticesPerCellSide - 1);
    }

    private void addBoundaryCells(FeatureCollection leftFeatureCollection,
            FeatureCollection rightFeatureCollection) {
        Coordinate southwestCornerOfBoundary = new Coordinate(southwestCornerOfLeftLayer.x
                + (layerWidthInCells * cellSideLength),
                southwestCornerOfLeftLayer.y);
        Coordinate topLeftBoundaryCoordinate = null;
        Coordinate topRightBoundaryCoordinate = null;
        double boundaryX = southwestCornerOfBoundary.x + (cellSideLength / 2);

        for (int j = 0; j < layerHeightInCells; j++) {
            topLeftBoundaryCoordinate = addBoundaryCell(leftFeatureCollection,
                    boundaryX, southwestCornerOfBoundary.x,
                    southwestCornerOfBoundary.y + (j * cellSideLength),
                    topLeftBoundaryCoordinate);
            topRightBoundaryCoordinate = addBoundaryCell(rightFeatureCollection,
                    boundaryX, southwestCornerOfBoundary.x + cellSideLength,
                    southwestCornerOfBoundary.y + (j * cellSideLength),
                    topRightBoundaryCoordinate);
        }
    }

    /**
     * @return the northernmost boundary coordinate
     */
    private Coordinate addBoundaryCell(FeatureCollection featureCollection,
            double boundaryX, double flatX, double south,
            Coordinate prevCellsTopBoundaryCoordinate) {
        List boundaryCoordinates = boundaryCoordinates(boundaryX, south,
                prevCellsTopBoundaryCoordinate);
        add(boundaryCell(flatX, south, boundaryCoordinates), featureCollection);

        return (Coordinate) boundaryCoordinates.get(0);
    }

    /**
     * @param x position of the flat wall (as opposed to the sinusoidal wall)
     */
    private Polygon boundaryCell(double x, double south,
            List boundaryCoordinates) {
        ArrayList coordinates = new ArrayList();

        for (int i = 0; i < verticesPerCellSide; i++) {
            coordinates.add(round(new Coordinate(x,
                    south + (i * segmentLength()))));
        }

        coordinates.addAll(boundaryCoordinates);
        coordinates.add(coordinates.get(0));

        return polygon(coordinates);
    }

    /**
     * @param west x coordinate of the boundary cell
     * @param south y coordinate of the boundary cell
     * @return coordinates of the boundary for the given cell, from the north
     * vertex to the south vertex
     */
    private List boundaryCoordinates(double boundaryX, double south,
            Coordinate prevCellsTopBoundaryCoordinate) {
        ArrayList boundaryCoordinates = new ArrayList();
        double segmentLength = cellSideLength / (verticesPerBoundarySide - 1);

        for (int i = verticesPerBoundarySide - 1; i >= 0; i--) {
            if ((i == 0) && (prevCellsTopBoundaryCoordinate != null)) {
                //Ensure continuity of the boundary despite perturbations [Bob Boseko]
                boundaryCoordinates.add(prevCellsTopBoundaryCoordinate);

                continue;
            }

            double y = south + (i * segmentLength);
            double x = boundaryX
                    + (boundaryAmplitude * Math.sin((2 * Math.PI * y) / boundaryPeriod));

            //To ensure perturbations are negative half the time, multiply by two
            //then subtract the max perturbation. [Bob Boseko]
            if (Math.random() < perturbationProbability) {
                x += ((2 * Math.random() * maxBoundaryPerturbation)
                        - maxBoundaryPerturbation);
                y += ((2 * Math.random() * maxBoundaryPerturbation)
                        - maxBoundaryPerturbation);
            }

            // round the coordinates to be integers [MD]
            boundaryCoordinates.add(round(new Coordinate(x, y)));
        }

        return boundaryCoordinates;
    }

    private void addLeftSquareCells(FeatureCollection leftFeatureCollection) {
        addSquareCells(leftFeatureCollection, southwestCornerOfLeftLayer);
    }

    private void addRightSquareCells(FeatureCollection rightFeatureCollection) {
        Coordinate southwestCornerOfRightLayer = new Coordinate(
                //Divide by 2 because layer width is half of layer height
                //Add 1 for boundary column
                southwestCornerOfLeftLayer.x
                + ((layerWidthInCells + 1) * cellSideLength),
                southwestCornerOfLeftLayer.y);
        addSquareCells(rightFeatureCollection, southwestCornerOfRightLayer);
    }

    private void addSquareCells(FeatureCollection featureCollection,
            Coordinate southwestCornerOfLayer) {
        for (int i = 0; i < layerWidthInCells; i++) {
            for (int j = 0; j < layerHeightInCells; j++) {
                add(squareCell(i, j, southwestCornerOfLayer), featureCollection);
            }
        }
    }

    private void add(Polygon polygon, FeatureCollection featureCollection) {
        Feature feature = new BasicFeature(featureCollection.getFeatureSchema());
        feature.setGeometry(polygon);
        featureCollection.add(feature);
    }

    private Polygon squareCell(int i, int j, Coordinate southwestCornerOfLayer) {
        return squareCell(southwestCornerOfLayer.x + (i * cellSideLength),
                southwestCornerOfLayer.y + (j * cellSideLength));
    }

    private Polygon squareCell(double west, double south) {
        ArrayList coordinates = new ArrayList();

        for (int i = 0; i < (verticesPerCellSide - 1); i++) {
            coordinates.add(round(new Coordinate(west,
                    south + (i * segmentLength()))));
        }

        for (int i = 0; i < (verticesPerCellSide - 1); i++) {
            coordinates.add(round(new Coordinate(west + (i * segmentLength()),
                    south + cellSideLength)));
        }

        for (int i = verticesPerCellSide - 1; i > 0; i--) {
            coordinates.add(round(new Coordinate(west + cellSideLength,
                    south + (i * segmentLength()))));
        }

        for (int i = verticesPerCellSide - 1; i > 0; i--) {
            coordinates.add(round(new Coordinate(west + (i * segmentLength()),
                    south)));
        }

        coordinates.add(coordinates.get(0));

        return polygon(coordinates);
    }

    private Polygon polygon(List coordinates) {
        Coordinate[] coordinateArray = (Coordinate[]) coordinates.toArray(new Coordinate[]{});

        return factory.createPolygon(factory.createLinearRing(coordinateArray),
                null);
    }

    private Coordinate round(Coordinate coord) {
        coord.x = Math.floor(coord.x);
        coord.y = Math.floor(coord.y);

        return coord;
    }
}
