package org.aitu.smartcity;

import org.aitu.smartcity.model.DiGraph;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.util.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {
    @Test
    void singleCycle() {
        DiGraph g = new DiGraph(3);
        g.addEdge(0,1); g.addEdge(1,2); g.addEdge(2,0);
        var scc = new TarjanSCC(g,3).findSCCs(new Metrics(new Stopwatch(), new OpCounters()));
        assertEquals(1, scc.size());
        assertEquals(3, scc.get(0).size());
    }
}
