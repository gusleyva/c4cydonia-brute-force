/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */

package com.c4cydonia.brute.force.utils;

/**
 * Utils methods to benchmark the application.
 */
public class Utils {
    private static final long MEGABYTE = 1024L * 1024L;

    public void calculateMemoryUsed(Runtime runtime) {
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));
    }

    private long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public void calculateExecutionTime(long startTime) {
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.printf("Elapsed time %d%n", elapsedTime);
        var seconds = (elapsedTime / 1000);
        var min = (seconds / 60);
        System.out.printf("Elapsed times in [minutes=%d], [seconds=%d]", min, seconds);
    }
}
