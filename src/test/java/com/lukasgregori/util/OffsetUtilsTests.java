package com.lukasgregori.util;

import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OffsetUtilsTests {

    private static final String RAND_ANGLE_FACTOR = "angle.rand.factor";

    @Test
    public void getRandomOffsetTest() {
        double randomOffset = OffsetUtils.getRandomOffset(RAND_ANGLE_FACTOR);
        Assert.assertTrue(randomOffset != 0.0f);
    }

    @Test
    public void applyRandomOffsetTest() {
        Coordinate source = new Coordinate(100, 100);
        Coordinate offsetCoordinate = OffsetUtils.addRandomOffset(source, RAND_ANGLE_FACTOR);

        Assert.assertNotNull(offsetCoordinate);
        Assert.assertNotEquals(source.x, offsetCoordinate.x);
        Assert.assertNotEquals(source.y, offsetCoordinate.y);
    }
}
