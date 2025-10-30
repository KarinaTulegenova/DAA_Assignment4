package org.aitu.smartcity.util;

public class Metrics {
    private final Stopwatch sw;
    private final OpCounters counters;
    public Metrics(Stopwatch sw, OpCounters counters) { this.sw = sw; this.counters = counters; sw.start(); }
    public Stopwatch stopwatch() { return sw; }
    public OpCounters counters() { return counters; }
}
