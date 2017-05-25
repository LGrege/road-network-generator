package com.lukasgregori.lsystem;

import com.lukasgregori.lsystem.nonterminals.Replaceable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LTaskSchedulerTests {

    private static final int NUMB_TASK_CREATED = 100;

    class NTTEst implements Replaceable {

        private int id;

        private NTTEst(int id) {
            this.id = id;
        }

        @Override
        public void replace() {
            System.out.println("Test #" + id + ": I am another nonterminal!");
        }
    }

    @Test
    public void contextLoads() {
        IntStream.range(0, NUMB_TASK_CREATED).forEachOrdered(n -> {
            LTask task = new LTask(new NTTEst(n));
            LTaskScheduler.getInstance().addTask(task);
        });
    }
}
