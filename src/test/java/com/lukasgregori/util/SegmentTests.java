package com.lukasgregori.util;

import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SegmentTests {

    @Test
    public void segmentCreationTest() {
        Coordinate p0 = new Coordinate(0, 0);
        Coordinate p1 = new Coordinate(10, 0);
        Segment s1 = new Segment(p0, p1);
        Assert.assertEquals(0.0f, s1.angle(), 0.1f);

        Coordinate p2 = new Coordinate(0, 0);
        Coordinate p3 = new Coordinate(0, 10);
        Segment s2 = new Segment(p2, p3);
        Assert.assertEquals(90.0f, s2.angle(), 0.1f);
    }

    @Test
    public void intersectionValidityTest() {
        Coordinate p0 = new Coordinate(0, 0);
        Coordinate p1 = new Coordinate(10, 0);
        Segment s1 = new Segment(p0, p1);

        Coordinate p2 = new Coordinate(10, 0);
        Coordinate p3 = new Coordinate(20, 0);
        Segment s2 = new Segment(p2, p3);

        Coordinate intersection = s1.intersection(s2);
        Assert.assertNull(intersection);

        Coordinate p4 = new Coordinate(5, -5);
        Coordinate p5 = new Coordinate(5, 5);
        Segment s3 = new Segment(p4, p5);

        intersection = s1.intersection(s3);
        Assert.assertNotNull(intersection);
        Assert.assertEquals(5.0f, intersection.x, 0.01f);
        Assert.assertEquals(0.0f, intersection.y, 0.01f);
    }


}
