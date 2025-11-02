package org.aitu.smartcity;

import org.aitu.smartcity.model.CondensationGraph;
import org.aitu.smartcity.model.WeightedDiGraph;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.util.Metrics;
import org.aitu.smartcity.util.OpCounters;
import org.aitu.smartcity.util.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CondensationTest {

    @Test
    void condensationIsDagAndCountsEdges() {
        WeightedDiGraph g = new WeightedDiGraph(5);
        g.addEdge(0,1,2); g.addEdge(1,2,3); g.addEdge(2,0,4);
        g.addEdge(3,4,5); g.addEdge(4,3,6);
        g.addEdge(1,3,10);
        g.addEdge(2,4,1);

        var scc = new TarjanSCC(g.asUnweighted(), 5)
                .findSCCs(new Metrics(new Stopwatch(), new OpCounters()));
        var cg  = CondensationGraph.fromSCCs(g, scc);
        assertEquals(2, cg.n());
        int dagEdges = cg.graph().values().stream().mapToInt(List::size).sum();
        assertEquals(1, dagEdges);
        List<int[]> outs = cg.weightedGraph().getOrDefault(0, Collections.emptyList());
        int w = outs.isEmpty() ? -1 : outs.get(0)[1];
        assertEquals(1, w);
    }
}
