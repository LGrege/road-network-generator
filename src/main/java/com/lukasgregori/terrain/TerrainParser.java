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

package com.lukasgregori.terrain;

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.util.ContextProvider;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Lukas Gregori
 */
public class TerrainParser {

    private static RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();

    private static int terrainAngle = ContextProvider.getInt("terrain.rays.angle");

    private static int rayCount = ContextProvider.getInt("terrain.rays.amount");

    private static BufferedImage heightMap;

    public TerrainParser(String fileName) throws FileNotFoundException {
        try {
            ClassLoader classLoader = TerrainParser.class.getClassLoader();
            Optional<URL> opt = Optional.ofNullable(classLoader.getResource(fileName));
            String path = opt.orElseThrow(FileNotFoundException::new).getFile();
            heightMap = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Error, heightmap file not found");
        }
    }

    public static Segment adaptRouteToTerrain(Segment segment) {
        try {
            ArrayList<Segment> rays = generateRays(segment);
            rays.sort(Comparator.comparingInt(r -> heightMap.getRGB((int) r.p1.x, (int) r.p1.y) & 0xFF));
            return rays.get(0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return segment;
        }
    }

    private static ArrayList<Segment> generateRays(Segment startSegment) {
        ArrayList<Segment> rays = new ArrayList<>();
        rays.add(startSegment);

        IntStream.rangeClosed(-rayCount / 2, rayCount / 2).forEach(i -> {
            double angle = startSegment.angle() + i * terrainAngle;
            Coordinate newEndPoint = getEndPoint(startSegment.p0, angle, startSegment.getLength());
            Segment newSegment = new Segment(startSegment.p0, newEndPoint);
            rays.add(newSegment);
        });

        return rays;
    }

    private static Coordinate getEndPoint(Coordinate start, double angle, double distance) {
        double newX = start.x + Math.cos(Math.toRadians(angle)) * distance;
        double newY = start.y + Math.sin(Math.toRadians(angle)) * distance;
        newX = Math.max(0, Math.min(config.dimensionX, newX));
        newY = Math.max(0, Math.min(config.dimensionY, newY));
        return new Coordinate(newX, newY);
    }

    public static BufferedImage getHeightMap() {
        return heightMap;
    }
}
