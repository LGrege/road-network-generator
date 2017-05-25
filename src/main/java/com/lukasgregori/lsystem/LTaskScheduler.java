/*********************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Lukas Gregori
 * lukas.gregori@student.tugraz.at
 * www.lukasgregori.com
 *
 * (c) 2017 by Lukas Gregori
 *********************************************************************/

package com.lukasgregori.lsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Lukas Gregori
 */
public class LTaskScheduler {

    private static ThreadPoolExecutor executor;

    private static LTaskScheduler instance;

    private LTaskScheduler() {
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public synchronized static LTaskScheduler getInstance() {
        if (instance == null) {
            instance = new LTaskScheduler();
        }
        return instance;
    }

    public synchronized void addTask(LTask task) {
        executor.submit(task::execute);
    }

    public void shutDown() {
        try {
            waitOnActiveThreads();
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            executor.shutdownNow();
        }
    }

    private void waitOnActiveThreads() throws InterruptedException {
        while (executor.getActiveCount() > 0) {
            Thread.sleep(100);
        }
    }
}