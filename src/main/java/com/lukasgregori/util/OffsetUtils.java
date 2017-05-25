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

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lukas Gregori
 */
public class OffsetUtils {

    private static RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();

    public static Coordinate addRandomOffset(Coordinate coordinate, String RAND_FACTOR_PROPERTY) {
        double x = coordinate.x * getRandomOffset(RAND_FACTOR_PROPERTY);
        double y = coordinate.y * getRandomOffset(RAND_FACTOR_PROPERTY);
        x = Math.max(0, Math.min(config.dimensionX, x));
        y = Math.max(0, Math.min(config.dimensionY, y));
        return new Coordinate(x, y);
    }

    public static double getRandomOffset(String RAND_FACTOR_PROPERTY) {
        double randFactor = ContextProvider.getDouble(RAND_FACTOR_PROPERTY);
        double max = 1 + randFactor;
        double min = 1 - randFactor;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
