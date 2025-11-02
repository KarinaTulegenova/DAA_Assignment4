package org.aitu.smartcity;

import org.aitu.smartcity.util.Metrics;
import org.aitu.smartcity.util.OpCounters;
import org.aitu.smartcity.util.Stopwatch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetricsTest {

    @Test
    void countersAndStopwatchWork() {
        Metrics m = new Metrics(new Stopwatch(), new OpCounters());
        m.counters().inc("dfs");
        m.counters().inc("dfs");
        m.stopwatch().stop();

        assertEquals(2L, m.counters().snapshot().get("dfs"));
        assertTrue(m.stopwatch().elapsedNanos() > 0);
    }
}
