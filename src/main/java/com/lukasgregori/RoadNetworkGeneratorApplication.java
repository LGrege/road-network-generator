package com.lukasgregori;

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.lsystem.LTask;
import com.lukasgregori.lsystem.LTaskScheduler;
import com.lukasgregori.lsystem.nonterminals.NTStart;
import com.lukasgregori.util.EntityContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class RoadNetworkGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadNetworkGeneratorApplication.class, args);

        System.out.println("Ready for work...");

        ApplicationContext context = new ClassPathXmlApplicationContext("roadgen-config.xml");
        RoadNetworkConfiguration config = context.getBean(RoadNetworkConfiguration.class);
        System.out.println(config);

        LTaskScheduler.getInstance().addTask(new LTask(new NTStart()));
        LTaskScheduler.getInstance().shutDown();

        System.out.println("Print Network...");
        EntityContainer.getInstance().printRoadNetwork();

        System.out.println("Work done...");
    }
}
