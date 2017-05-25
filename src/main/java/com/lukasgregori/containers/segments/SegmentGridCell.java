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

import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Lukas Gregori
 */
public class SegmentGridCell {

    private HashSet<LineSegment> segments;

    private ReadWriteLock segmentsLock;

    public SegmentGridCell() {
        segments = new HashSet<>();
        segmentsLock = new ReentrantReadWriteLock();
    }

    public ArrayList<Coordinate> getIntersections(Segment newSegment) {
        ArrayList<Coordinate> intersections = new ArrayList<>();
        segmentsLock.readLock().lock();

        segments.forEach(segment -> {
            Optional<Coordinate> intersection = Optional.ofNullable(segment.intersection(newSegment));
            intersection.ifPresent(intersections::add);
        });

        segmentsLock.readLock().unlock();
        return intersections;
    }

    public void addSegment(Segment newSegment) {
        segmentsLock.writeLock().lock();
        segments.add(newSegment);
        segmentsLock.writeLock().unlock();
    }
}
