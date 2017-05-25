/*********************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Lukas Gregori
 * lukas.gregori@student.tugraz.at
 * www.lukasgregori.com
 *
 * (c) 2017 by Lukas Gregori
 *********************************************************************/

package com.lukasgregori.containers.segments;

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.util.ContextProvider;
import com.lukasgregori.util.Segment;
import com.sun.istack.internal.Nullable;
import com.vividsolutions.jts.geom.Coordinate;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Lukas Gregori
 */
public class SegmentGrid implements SegmentContainer {

    private SegmentGridCell[][] grid;

    private int rowCount;

    private int colCount;

    public SegmentGrid(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;

        grid = new SegmentGridCell[rowCount][colCount];
        IntStream.range(0, rowCount).forEach(
                i -> IntStream.range(0, colCount).forEach(
                        j -> grid[i][j] = new SegmentGridCell()));
    }

    @Override
    public void addSegment(Segment segment) {
        Point p0 = getCellPosition(segment.p0);
        Point p1 = getCellPosition(segment.p1);

        grid[p0.y][p0.x].addSegment(segment);
        grid[p1.y][p1.x].addSegment(segment);

        // Edge case if line crosses three segments
        if (p0.x != p1.x && p0.y != p1.y) {
            grid[p0.y][p0.x - (p0.x - p1.x)].addSegment(segment);
            grid[p0.y - (p0.y - p1.y)][p0.x].addSegment(segment);
        }
    }

    @Override
    @Nullable
    public Coordinate getClosestIntersection(Segment segment) {
        Stream<Coordinate> intersections = getIntersections(segment).stream()
                .sorted(Comparator.comparingDouble(i -> i.distance(segment.p0)));
        return intersections.findFirst().orElse(null);
    }

    private ArrayList<Coordinate> getIntersections(Segment segment) {
        Point p0 = getCellPosition(segment.p0);
        Point p1 = getCellPosition(segment.p1);

        ArrayList<Coordinate> intersections = grid[p0.y][p0.x].getIntersections(segment);

        if (!p0.equals(p1)) {
            intersections.addAll(grid[p1.y][p1.x].getIntersections(segment));
        }

        return intersections;
    }

    private Point getCellPosition(Coordinate coordinate) {
        RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();
        int x = (int) (coordinate.x * colCount / config.dimensionX);
        int y = (int) (coordinate.y * rowCount / config.dimensionY);

        if (x >= colCount || x < 0 || y >= rowCount || y < 0) {
            throw new InvalidParameterException();
        }

        return new Point(x, y);
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return colCount;
    }
}
