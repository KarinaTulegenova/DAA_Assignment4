package org.aitu.smartcity;

import org.aitu.smartcity.model.WeightedDiGraph;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.util.Metrics;
import org.aitu.smartcity.util.OpCounters;
import org.aitu.smartcity.util.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    @Test
    void singleCycle() {
        WeightedDiGraph g = new WeightedDiGraph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        var scc = new TarjanSCC(g.asUnweighted(), 3)
                .findSCCs(new Metrics(new Stopwatch(), new OpCounters()));

        assertEquals(1, scc.size());
        assertEquals(3, scc.get(0).size());
    }

    @Test
    void twoComponentsAndBridge() {
        WeightedDiGraph g = new WeightedDiGraph(5);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        g.addEdge(3,4,1); g.addEdge(4,3,1);
        g.addEdge(2,3,1);

        var scc = new TarjanSCC(g.asUnweighted(), 5)
                .findSCCs(new Metrics(new Stopwatch(), new OpCounters()));

        assertEquals(2, scc.size());
        assertTrue(scc.stream().mapToInt(List::size).sum() <= 5); // на всякий
    }

    @Test
    void fullyStronglyConnected() {
        int n = 4;
        WeightedDiGraph g = new WeightedDiGraph(n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i != j) g.addEdge(i, j, 1);

        var scc = new TarjanSCC(g.asUnweighted(), n)
                .findSCCs(new Metrics(new Stopwatch(), new OpCounters()));

        assertEquals(1, scc.size());
        assertEquals(n, scc.get(0).size());
    }

    @Test
    void singleVertex() {
        WeightedDiGraph g = new WeightedDiGraph(1);
        var scc = new TarjanSCC(g.asUnweighted(), 1)
                .findSCCs(new Metrics(new Stopwatch(), new OpCounters()));
        assertEquals(1, scc.size());
        assertEquals(1, scc.get(0).size());
    }
}
