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

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.lsystem.LTask;
import com.lukasgregori.lsystem.LTaskScheduler;
import com.lukasgregori.util.ContextProvider;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Lukas Gregori
 */
public class NTHighwayStart implements Replaceable {

    private Coordinate start;

    NTHighwayStart(Coordinate start) {
        this.start = start;
    }

    @Override
    public void replace() {
        RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();
        config.endPoints.forEach(p -> LTaskScheduler.getInstance().addTask(createHighwaySegmentTask(p)));
    }

    private LTask createHighwaySegmentTask(Coordinate target) {
        Segment startSegment = new Segment(start, start);
        NTHighwaySegment highwaySegment = new NTHighwaySegment(startSegment, target);
        return new LTask(highwaySegment);
    }
}