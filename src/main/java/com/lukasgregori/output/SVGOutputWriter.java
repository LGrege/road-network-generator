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

package com.lukasgregori.output;

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.terrain.TerrainParser;
import com.lukasgregori.util.ContextProvider;
import com.lukasgregori.util.EntityContainer;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * @author Lukas Gregori
 */
public class SVGOutputWriter implements OutputWriterStrategy {

    private static final String HEIGHT_MAP_ENABLED = "height.map.enabled";

    private static final String GRID_ENABLED = "grid.enabled";

    private SVGGraphics2D svgGenerator;

    private File file;

    private RoadNetworkConfiguration config;

    @Override
    public void openFile(File file) {
        this.file = file;
        this.config = ContextProvider.getNetworkConfig();
        initSVGGenerator();
    }

    @Override
    public void closeFile() {
        try {
            Writer out = new FileWriter(file);
            svgGenerator.stream(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleHighways(ArrayList<Segment> highwaySegments) {
        highwaySegments.forEach(segment -> drawSegment(segment, Color.red, 1.0f));
        config.highways.forEach(highway -> {
            drawCoordinate(highway.start, Color.green, 10);
            drawCoordinate(highway.target, Color.blue, 10);
        });
    }

    @Override
    public void handleStreets(ArrayList<Segment> streetSegments) {
        streetSegments.forEach(segment -> drawSegment(segment, Color.red, 0.2f));
    }

    private void drawSegment(Segment segment, Color color, float lineWidth) {
        svgGenerator.setColor(color);
        svgGenerator.setStroke(new BasicStroke(lineWidth));
        svgGenerator.draw(new Line2D.Double(segment.p0.x, segment.p0.y, segment.p1.x, segment.p1.y));
    }

    private void drawCoordinate(Coordinate coordinate, Color color, int width) {
        svgGenerator.setColor(color);
        svgGenerator.fill(new Ellipse2D.Double(coordinate.x - width / 2.0f, coordinate.y - width / 2.0f, width, width));
    }

    private void drawGrid(int rowCount, int columnCount) {
        svgGenerator.setColor(Color.lightGray);

        double stepWidthX = (double) config.dimensionX / columnCount;
        double stepWidthY = (double) config.dimensionY / rowCount;

        IntStream.range(0, columnCount).forEach(i -> svgGenerator.draw(
                new Line2D.Double(i * stepWidthX, 0, i * stepWidthX, config.dimensionY)));
        IntStream.range(0, rowCount).forEach(i -> svgGenerator.draw(
                new Line2D.Double(0, i * stepWidthY, config.dimensionX, i * stepWidthY)));
    }

    private void initSVGGenerator() {
        DOMImplementation domImpl =
                GenericDOMImplementation.getDOMImplementation();

        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        svgGenerator = new SVGGraphics2D(document);
        svgGenerator.setSVGCanvasSize(new Dimension(config.dimensionX, config.dimensionY));
        drawBackground();
    }

    private void drawBackground() {
        svgGenerator.setColor(Color.white);
        svgGenerator.fillRect(0, 0, config.dimensionX, config.dimensionY);

        BufferedImage heightMap = TerrainParser.getHeightMap();

        if (ContextProvider.getBoolean(HEIGHT_MAP_ENABLED) && heightMap != null) {
            svgGenerator.drawImage(heightMap, 0, 0, heightMap.getWidth(), heightMap.getHeight(), null);
        }

        if (ContextProvider.getBoolean(GRID_ENABLED)) {
            drawGrid(EntityContainer.getInstance().getSegmentGrid().getRowCount(),
                    EntityContainer.getInstance().getSegmentGrid().getColumnCount());
        }
    }
}