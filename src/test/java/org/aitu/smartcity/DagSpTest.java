package org.aitu.smartcity;

import org.aitu.smartcity.dagsp.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DagSpTest {
    @Test
    void shortestOnDag() {
        Map<Integer,List<int[]>> g = new HashMap<>();
        g.put(0, List.of(new int[]{1,2}, new int[]{2,5}));
        g.put(1, List.of(new int[]{3,1}));
        g.put(2, List.of(new int[]{3,1}));
        var topo = List.of(0,1,2,3);
        int[] d = DAGShortestPath.shortest(g,4,0,topo,
                new org.aitu.smartcity.util.Metrics(new org.aitu.smartcity.util.Stopwatch(),
                        new org.aitu.smartcity.util.OpCounters()));
        assertEquals(3, d[3]);
    }
}
