package org.aitu.smartcity;

import org.aitu.smartcity.dagsp.DAGLongestPath;
import org.aitu.smartcity.dagsp.DAGShortestPath;
import org.aitu.smartcity.dagsp.PathResult;
import org.aitu.smartcity.util.Metrics;
import org.aitu.smartcity.util.OpCounters;
import org.aitu.smartcity.util.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DagSpTest {

    @Test
    void shortestWithUnreachableAndNegatives() {
        Map<Integer, List<int[]>> g = new HashMap<>();
        g.put(0, List.of(new int[]{1,2}, new int[]{2,-1}));
        g.put(1, List.of());
        g.put(2, List.of(new int[]{3,5}));
        g.put(3, List.of());
        g.put(4, List.of());
        List<Integer> topo = List.of(0,2,1,3,4);

        PathResult sp = DAGShortestPath.shortest(
                g, 5, 0, topo, new Metrics(new Stopwatch(), new OpCounters()));

        int INF = Integer.MAX_VALUE;
        assertEquals(0, sp.dist()[0]);
        assertEquals(2, sp.dist()[1]);
        assertEquals(-1, sp.dist()[2]);
        assertEquals(4, sp.dist()[3]);
        assertEquals(INF, sp.dist()[4]);

        assertEquals(List.of(0,2,3), sp.pathTo(3));
    }

    @Test
    void longestCriticalPath() {
        Map<Integer, List<int[]>> g = new HashMap<>();
        g.put(0, List.of(new int[]{1,3}, new int[]{2,5}));
        g.put(1, List.of(new int[]{2,4}));
        g.put(2, List.of());
        List<Integer> topo = List.of(0,1,2);

        PathResult lp = DAGLongestPath.longest(
                g, 3, 0, topo, new Metrics(new Stopwatch(), new OpCounters()));

        assertEquals(7, lp.dist()[2]);
        assertEquals(List.of(0,1,2), lp.pathTo(2));
    }
}
