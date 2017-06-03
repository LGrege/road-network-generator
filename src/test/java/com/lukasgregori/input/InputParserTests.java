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
        assertEquals(600, config.dimensionX);
        assertEquals(800, config.dimensionY);

        assertEquals(1, config.highways.size());
        assertEquals(40, config.highways.get(0).start.x, 0.01f);
        assertEquals(107, config.highways.get(0).start.y, 0.01f);
        assertEquals(639, config.highways.get(0).target.x, 0.01f);
        assertEquals(119, config.highways.get(0).target.y, 0.01f);
    }
}
