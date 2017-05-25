package com.lukasgregori.util;

import com.lukasgregori.input.RoadNetworkConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContextProviderTests {

    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("roadgen-config.xml");
        System.out.println(context.getApplicationName());
    }

    @Test
    public void getRoadNetworkConfigTest() {
        RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();
        Assert.assertEquals(true, config != null);
    }

    @Test
    public void getDoubleTest() {
        // Note: Depends on properties file
        double randFactor = ContextProvider.getDouble("highway.rand.factor");
        Assert.assertEquals(0.01f, randFactor, 0.001f);
    }

    @Test
    public void getIntTest() {
        // Note: Depends on properties file
        int angle = ContextProvider.getInt("terrain.rays.angle");
        Assert.assertEquals(10, angle);
    }

    @Test
    public void getString() {
        // Note: Depends on properties file
        String logLevel = ContextProvider.getString("logging.level.");
        Assert.assertEquals("ERROR", logLevel);
    }
}
