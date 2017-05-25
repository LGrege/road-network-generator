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

import com.lukasgregori.util.Segment;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Lukas Gregori
 */
public interface OutputWriterStrategy {

    void openFile(File file);

    void closeFile();

    void handleHighways(ArrayList<Segment> highwaySegments);

    void handleStreets(ArrayList<Segment> streetSegments);
}
