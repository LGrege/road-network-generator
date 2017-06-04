package com.lukasgregori;

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.lsystem.LTask;
import com.lukasgregori.lsystem.LTaskScheduler;
import com.lukasgregori.lsystem.nonterminals.NTStart;
import com.lukasgregori.output.OutputWriterStrategy;
import com.lukasgregori.output.SVGOutputWriter;
import com.lukasgregori.util.EntityContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

@SpringBootApplication
public class RoadNetworkGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadNetworkGeneratorApplication.class, args);

        System.out.println("Ready for work...");

        ApplicationContext context = new ClassPathXmlApplicationContext("roadgen-config.xml");
        context.getBean(RoadNetworkConfiguration.class);

        LTaskScheduler.getInstance().addTask(new LTask(new NTStart()));
        LTaskScheduler.getInstance().shutDown();

        System.out.println("Print Network...");
        printRoadNetwork();

        System.out.println("Work done...");
    }

    private static void printRoadNetwork() {
        OutputWriterStrategy out = new SVGOutputWriter();
        out.openFile(new File("out.svg"));
        out.handleHighways(EntityContainer.getInstance().getAllHighwaySegments());
        out.handleStreets(EntityContainer.getInstance().getAllStreetSegments());
        out.closeFile();
    }
}
