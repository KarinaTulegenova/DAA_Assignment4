package org.aitu.smartcity.util;

public class Stopwatch {
    private long start, end;
    private boolean running;

    public void start() {
        running = true;
        start = System.nanoTime();
    }

    public void stop() {
        end = System.nanoTime();
        running = false;
    }

    public long elapsedNanos() {
        return (running ? System.nanoTime() : end) - start;
    }

    public double elapsedMillis() {
        return elapsedNanos() / 1_000_000.0;
    }

    public double elapsedMicros() {
        return elapsedNanos() / 1_000.0;
    }
}
