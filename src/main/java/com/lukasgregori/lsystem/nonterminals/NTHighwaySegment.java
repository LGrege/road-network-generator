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

package com.lukasgregori.lsystem.nonterminals;

import com.lukasgregori.lsystem.LTask;
import com.lukasgregori.lsystem.LTaskScheduler;
import com.lukasgregori.terrain.HeightMapParser;
import com.lukasgregori.terrain.TerrainParser;
import com.lukasgregori.util.ContextProvider;
import com.lukasgregori.util.OffsetUtils;
import com.lukasgregori.util.EntityContainer;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.math.Vector2D;

/**
 * @author Lukas Gregori
 */
public class NTHighwaySegment implements Replaceable {

    private static final String SNAP_DISTANCE = "highway.snap.distance";

    private static final String SEGMENT_LENGTH = "max.segment.length";

    private static final String RAND_FACTOR = "highway.rand.factor";

    private Segment lastSegment;

    private Coordinate target;

    private double distance;

    NTHighwaySegment(Segment lastSegment, Coordinate target) {
        this.lastSegment = lastSegment;
        this.target = target;
        distance = target.distance(lastSegment.p1);
    }

    @Override
    public void replace() {
        if (distance < ContextProvider.getInt(SNAP_DISTANCE)) {
            mergeHighwaySegmentToTarget();
            EntityContainer.getInstance().decreaseHighwayCount();
        } else {
            addNewHighwaySegment();
        }
    }

    private void mergeHighwaySegmentToTarget() {
        Segment newHighwaySegment = new Segment(lastSegment.p1, target);
        EntityContainer.getInstance().addHighwaySegment(newHighwaySegment);
    }

    private void addNewHighwaySegment() {
        Segment newSegment = new Segment(lastSegment.p1, getNextTarget());
        newSegment = HeightMapParser.adaptRouteToTerrain(newSegment);

        Coordinate closestIntersection = EntityContainer.getInstance().getClosestIntersection(newSegment);

        if (closestIntersection != null) {
            Segment terminalSegment = new Segment(newSegment.p0, closestIntersection);
            EntityContainer.getInstance().addHighwaySegment(terminalSegment);
            EntityContainer.getInstance().decreaseHighwayCount();
            return;
        }

        continueHighway(newSegment);
        branchSideStreet();
    }

    private void continueHighway(Segment segment) {
        EntityContainer.getInstance().addHighwaySegment(segment);
        NTHighwaySegment ntHighwaySegment = new NTHighwaySegment(segment, target);
        LTaskScheduler.getInstance().addTask(new LTask(ntHighwaySegment));
    }

    private void branchSideStreet() {
        Coordinate target = OffsetUtils.addRandomOffset(lastSegment.p1, RAND_FACTOR);
        NTStreetSegment streetSegment = new NTStreetSegment(new Segment(lastSegment.p1, target));
        LTaskScheduler.getInstance().addTask(new LTask(streetSegment));
    }

    private Coordinate getNextTarget() {
        Vector2D direction = new Vector2D(lastSegment.p1, target);
        Vector2D offset = direction.divide(distance).multiply(ContextProvider.getInt(SEGMENT_LENGTH));
        Coordinate nextPoint = offset.add(new Vector2D(lastSegment.p1)).toCoordinate();
        return OffsetUtils.addRandomOffset(nextPoint, RAND_FACTOR);
    }
}