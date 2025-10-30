package org.aitu.smartcity;

import org.aitu.smartcity.topo.TopologicalSort;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TopoTest {
    @Test
    void simpleDag() {
        Map<Integer,List<Integer>> g = new HashMap<>();
        g.put(0, List.of(1,2));
        g.put(1, List.of(3));
        g.put(2, List.of(3));
        var topo = TopologicalSort.kahn(g, 4, new org.aitu.smartcity.util.Metrics(
                new org.aitu.smartcity.util.Stopwatch(), new org.aitu.smartcity.util.OpCounters()));
        assertEquals(4, topo.size());
    }
}
