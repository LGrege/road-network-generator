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

import com.lukasgregori.input.InputParser;
import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.lsystem.LTask;
import com.lukasgregori.lsystem.LTaskScheduler;
import com.lukasgregori.util.EntityContainer;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author Lukas Gregori
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NTHighwaySegmentTests {

    @Before
    public void init() {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("roadgen-config.xml");
            RoadNetworkConfiguration config = InputParser.parseInput("testInput.json");
            System.out.println(context.getApplicationName());
            System.out.println(config);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMergeOfHighway() {
        long highwayCount = EntityContainer.getInstance().getRemainingHighwayCount();
        Coordinate p0 = new Coordinate(0, 0);
        Coordinate p1 = new Coordinate(10, 10);
        Coordinate target = new Coordinate(11, 11);
        createMergeSegment(p0, p1, target);
        checkMergeTestResult(highwayCount, p1, target);
    }

    private void checkMergeTestResult(long highwayCount, Coordinate p1, Coordinate target) {
        ArrayList<Segment> allHighwaySegments = EntityContainer.getInstance().getAllHighwaySegments();
        Segment desiredSegment = new Segment(p1, target);
        Assert.equals(true, allHighwaySegments.stream().anyMatch(s -> s.equalsTopo(new LineSegment(desiredSegment))));
        Assert.equals(highwayCount - 1, EntityContainer.getInstance().getRemainingHighwayCount());
    }

    private void createMergeSegment(Coordinate p0, Coordinate p1, Coordinate target) {
        Segment lastSegment = new Segment(p0, p1);
        NTHighwaySegment segment = new NTHighwaySegment(lastSegment, target);
        LTaskScheduler.getInstance().addTask(new LTask(segment));
        LTaskScheduler.getInstance().shutDown();
    }
}
