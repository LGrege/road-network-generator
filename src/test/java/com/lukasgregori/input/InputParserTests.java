package com.lukasgregori.input;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InputParserTests {

    @Test(expected = FileNotFoundException.class)
    public void fileNotFoundTest() throws FileNotFoundException, UnsupportedEncodingException {
        RoadNetworkConfiguration config = InputParser.parseInput("nonexistingfile.json");
    }

    @Test
    public void parsedContentTest() throws FileNotFoundException, UnsupportedEncodingException {
        RoadNetworkConfiguration config = InputParser.parseInput("testInput.json");

        assertNotNull(config);
        assertEquals(87, config.branchProbability);
        assertEquals(1, config.riverCount);
        assertEquals(600, config.dimensionX);
        assertEquals(800, config.dimensionY);

        assertEquals(1, config.startPoints.size());
        assertEquals(87, config.startPoints.get(0).x, 0.01f);
        assertEquals(124, config.startPoints.get(0).y, 0.01f);

        assertEquals(1, config.endPoints.size());
        assertEquals(312, config.endPoints.get(0).x, 0.01f);
        assertEquals(115, config.endPoints.get(0).y, 0.01f);
    }
}
