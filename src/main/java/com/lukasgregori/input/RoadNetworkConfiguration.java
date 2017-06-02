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

package com.lukasgregori.input;

import com.vividsolutions.jts.geom.Coordinate;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author Lukas Gregori
 */
public class RoadNetworkConfiguration {

    public final int dimensionX;

    public final int dimensionY;

    public ArrayList<Coordinate> startPoints;

    public ArrayList<Coordinate> endPoints;

    public RoadNetworkConfiguration(int dimensionX, int dimensionY, ArrayList<Coordinate> startPoints, ArrayList<Coordinate> endPoints) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.startPoints = new ArrayList<>(startPoints);
        this.endPoints = new ArrayList<>(endPoints);
    }

    public RoadNetworkConfiguration(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        this(InputParser.parseInput(fileName));
    }

    public RoadNetworkConfiguration(RoadNetworkConfiguration src) {
        this(src.dimensionX, src.dimensionY, src.startPoints, src.endPoints);
    }

    @Override
    public String toString() {
        return "Road Network Configuration:\n" +
                "DIM(" + dimensionX + "," + dimensionY + "), " +
                "SP[" + startPoints.size() + "], " +
                "EP[" + endPoints.size() + "]";
    }
}
