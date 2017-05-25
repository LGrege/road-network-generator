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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * @author Lukas Gregori
 */
public class Segment extends LineSegment {

    public Segment(Coordinate c1, Coordinate c2) {
        super(c1, c2);
    }

    @Override
    public double angle() {
        return Math.toDegrees(super.angle());
    }

    @Override
    public Coordinate intersection(LineSegment segment) {
        Coordinate intersection = super.intersection(segment);
        return isValidIntersection(intersection, segment) ? intersection : null;
    }

    private boolean isValidIntersection(Coordinate intersection, LineSegment segment) {
        return intersection != null && !(intersection.equals2D(p1) | intersection.equals2D(p1)
                | intersection.equals2D(segment.p0) | intersection.equals2D(segment.p1));
    }
}
