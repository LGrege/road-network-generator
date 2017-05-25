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
import com.lukasgregori.terrain.TerrainParser;
import com.lukasgregori.util.ContextProvider;
import com.lukasgregori.util.EntityContainer;
import com.lukasgregori.util.OffsetUtils;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.Random;

/**
 * @author Lukas Gregori
 */
public class NTStreetSegment implements Replaceable {

    private static final String RAND_ANGLE_FACTOR = "angle.rand.factor";

    private static final String SEGMENT_LENGTH = "max.segment.length";

    private Segment segment;

    NTStreetSegment(Segment segment) {
        this.segment = segment;
    }

    @Override
    public void replace() {
        waitOnHighwaysToFinish();
        Coordinate closestIntersection = EntityContainer.getInstance().getClosestIntersection(segment);

        if (closestIntersection != null) {
            Segment terminalSegment = new Segment(segment.p0, closestIntersection);
            EntityContainer.getInstance().addStreetSegment(terminalSegment);
            return;
        }

        continueStreet();
        branchSideStreet();
    }

    private void continueStreet() {
        EntityContainer.getInstance().addStreetSegment(segment);
        double angle = segment.angle() + OffsetUtils.getRandomOffset(RAND_ANGLE_FACTOR);
        createNewStreetSegment(angle);
    }

    private void branchSideStreet() {
        double angle = getBranchAngle();
        createNewStreetSegment(angle);
    }

    private double getBranchAngle() {
        double angle = segment.angle() + OffsetUtils.getRandomOffset(RAND_ANGLE_FACTOR);
        return new Random().nextBoolean() ? angle + 90 : angle - 90;
    }

    private void createNewStreetSegment(double angle) {
        double length = ContextProvider.getDouble(SEGMENT_LENGTH);
        double xNew = length * Math.cos(Math.toRadians(angle)) + segment.p1.x;
        double yNew = length * Math.sin(Math.toRadians(angle)) + segment.p1.y;

        Segment newSegment = new Segment(segment.p1, new Coordinate(xNew, yNew));
        newSegment = TerrainParser.adaptRouteToTerrain(newSegment);

        NTStreetSegment streetSegment = new NTStreetSegment(newSegment);
        LTaskScheduler.getInstance().addTask(new LTask(streetSegment));
    }

    private void waitOnHighwaysToFinish() {
        try {
            EntityContainer.getInstance().highwayLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}