package org.aitu.smartcity;

import org.aitu.smartcity.topo.TopologicalSort;
import org.aitu.smartcity.util.Metrics;
import org.aitu.smartcity.util.OpCounters;
import org.aitu.smartcity.util.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TopoTest {

    @Test
    void simpleDagValidOrder() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, List.of(1,2));
        g.put(1, List.of(3));
        g.put(2, List.of(3));
        g.put(3, List.of());

        var order = TopologicalSort.kahn(g, 4, new Metrics(new Stopwatch(), new OpCounters()));
        assertEquals(4, order.size());

        Map<Integer,Integer> pos = new HashMap<>();
        for (int i = 0; i < order.size(); i++) pos.put(order.get(i), i);

        assertTrue(pos.get(0) < pos.get(1));
        assertTrue(pos.get(0) < pos.get(2));
        assertTrue(pos.get(1) < pos.get(3));
        assertTrue(pos.get(2) < pos.get(3));
    }

    @Test
    void cyclicGraphProducesPartialOrder() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, List.of(1));
        g.put(1, List.of(2));
        g.put(2, List.of(0));

        var order = TopologicalSort.kahn(g, 3, new Metrics(new Stopwatch(), new OpCounters()));
        assertTrue(order.size() < 3, "Topological order must be incomplete for cyclic graphs");
    }
}
