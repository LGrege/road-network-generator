package com.lukasgregori.container.segments;

import com.lukasgregori.containers.segments.SegmentGridCell;
import com.lukasgregori.util.Segment;
import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SegmentGridCellTests {

    private static final int MAX_THREAD_COUNT = 1000;

    @Test
    public void intersectionTest() throws FileNotFoundException, UnsupportedEncodingException {
        SegmentGridCell cell = new SegmentGridCell();
        Segment s1 = new Segment(new Coordinate(0, 0), new Coordinate(1, 1));
        Segment s2 = new Segment(new Coordinate(1, 0), new Coordinate(0, 1));

        cell.addSegment(s1);
        ArrayList<Coordinate> intersections = cell.getIntersections(s2);
        assertEquals(1, intersections.size());
        assertEquals(0.5f, intersections.get(0).x, 0.01f);
        assertEquals(0.5f, intersections.get(0).y, 0.01f);
    }


    @Test
    public void concurrentInsertionTest() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        SegmentGridCell cell = new SegmentGridCell();

        IntStream.range(0, MAX_THREAD_COUNT).forEach(i -> executeInsertionThread(cell, i));
        Thread.sleep(100);

        Segment s2 = new Segment(new Coordinate(1, 0), new Coordinate(0, 1));
        ArrayList<Coordinate> intersections = cell.getIntersections(s2);
        assertEquals(MAX_THREAD_COUNT, intersections.size());
    }

    private void executeInsertionThread(SegmentGridCell cell, final int counter) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Segment segment = new Segment(new Coordinate(-counter, -counter), new Coordinate(1, 1));
            cell.addSegment(segment);
        });
    }
}
