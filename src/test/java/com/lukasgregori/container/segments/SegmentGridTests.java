package com.lukasgregori.container.segments;

import com.lukasgregori.containers.segments.SegmentGrid;
import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SegmentGridTests {

    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("roadgen-config.xml");
        RoadNetworkConfiguration config = context.getBean(RoadNetworkConfiguration.class);
        System.out.println(config);
    }

    @Test
    public void getterTest() throws FileNotFoundException, UnsupportedEncodingException {
        SegmentGrid grid = new SegmentGrid(100, 100);
        assertEquals(100, grid.getColumnCount());
        assertEquals(100, grid.getRowCount());
    }

    @Test(expected = InvalidParameterException.class)
    public void invalidSegmentInsertionTest() throws FileNotFoundException, UnsupportedEncodingException {
        SegmentGrid grid = new SegmentGrid(100, 100);

        Segment s1 = new Segment(new Coordinate(10000, 10000), new Coordinate(10000, 10000));
        grid.addSegment(s1);
    }

    @Test
    public void closestIntersectionTest() {
        SegmentGrid grid = new SegmentGrid(100, 100);

        Segment s0 = new Segment(new Coordinate(0.5f, -1), new Coordinate(0.5f, 1));
        Segment s1 = new Segment(new Coordinate(3, -1), new Coordinate(3, 1));
        Segment s2 = new Segment(new Coordinate(0, 0), new Coordinate(4, 0));

        grid.addSegment(s0);
        grid.addSegment(s1);

        Coordinate closestIntersection = grid.getClosestIntersection(s2);
        assertNotNull(closestIntersection);
        assertEquals(0.5f, closestIntersection.x, 0.01f);
        assertEquals(0.0f, closestIntersection.y, 0.01f);
    }
}
