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

package com.lukasgregori.util;

import com.lukasgregori.containers.segments.SegmentGrid;
import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.output.OutputWriterStrategy;
import com.lukasgregori.output.SVGOutputWriter;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Lukas Gregori
 */
public class EntityContainer {

    private static final String MAX_SEGMENT_LENGTH = "max.segment.length";

    private static EntityContainer instance;

    private ArrayList<Segment> allHighwaySegments;

    private ArrayList<Segment> allStreetSegments;

    private CountDownLatch highwayLatch;

    private SegmentGrid segmentGrid;

    private EntityContainer() {
        RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();
        highwayLatch = new CountDownLatch(CollectionUtils.size(config.highways));
        allHighwaySegments = new ArrayList<>();
        allStreetSegments = new ArrayList<>();
        initSegmentGrid(config);
    }

    private void initSegmentGrid(RoadNetworkConfiguration config) {
        double maxLength = Double.parseDouble(ContextProvider.getString(MAX_SEGMENT_LENGTH));
        int rowCount = (int) (config.dimensionY / (1.5f * maxLength));
        int colCount = (int) (config.dimensionX / (1.5f * maxLength));
        segmentGrid = new SegmentGrid(rowCount, colCount);
    }

    public static synchronized EntityContainer getInstance() {
        if (instance == null) {
            instance = new EntityContainer();
        }
        return instance;
    }

    public synchronized void addHighwaySegment(Segment newHighwaySegment) {
        segmentGrid.addSegment(newHighwaySegment);
        allHighwaySegments.add(newHighwaySegment);
    }

    public synchronized void addStreetSegment(Segment newStreetSegment) {
        segmentGrid.addSegment(newStreetSegment);
        allStreetSegments.add(newStreetSegment);
    }

    public synchronized void decreaseHighwayCount() {
        highwayLatch.countDown();
    }

    public void waitOnHighwaysToFinish() {
        try {
            highwayLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long getRemainingHighwayCount() {
        return highwayLatch.getCount();
    }

    public Coordinate getClosestIntersection(Segment segment) {
        return segmentGrid.getClosestIntersection(segment);
    }

    public SegmentGrid getSegmentGrid() {
        return segmentGrid;
    }

    public ArrayList<Segment> getAllHighwaySegments() {
        return allHighwaySegments;
    }

    public ArrayList<Segment> getAllStreetSegments() {
        return allStreetSegments;
    }
}