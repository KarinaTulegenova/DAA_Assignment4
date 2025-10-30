package org.aitu.smartcity;

import com.google.gson.*;
import org.aitu.smartcity.io.GraphLoader;
import org.aitu.smartcity.model.*;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.topo.TopologicalSort;
import org.aitu.smartcity.dagsp.DAGShortestPath;
import org.aitu.smartcity.dagsp.DAGLongestPath;
import org.aitu.smartcity.util.*;

import java.nio.file.Path;
import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar app.jar data/small_01.json [sourceId=0]");
            return;
        }
        int src = args.length > 1 ? Integer.parseInt(args[1]) : 0;

        Path file = Path.of(args[0]);
        WeightedDiGraph g = GraphLoader.loadWeighted(file);

        Metrics m = new Metrics(new Stopwatch(), new OpCounters());

        TarjanSCC tarjan = new TarjanSCC(g.asUnweighted(), g.n());
        List<List<Integer>> sccs = tarjan.findSCCs(m);
        CondensationGraph cg = CondensationGraph.fromSCCs(g, sccs);

        List<Integer> topo = TopologicalSort.kahn(cg.graph(), cg.n(), m);

        int[] distShort = DAGShortestPath.shortest(cg.weightedGraph(), cg.n(), cg.compIdOf(src), topo, m);
        int[] distLong  = DAGLongestPath.longest(cg.weightedGraph(), cg.n(), cg.compIdOf(src), topo, m);

        System.out.println("SCC count: " + sccs.size());
        System.out.println("Topo order size: " + topo.size());
        System.out.println("Shortest (comp src=" + cg.compIdOf(src) + "): " + Arrays.toString(distShort));
        System.out.println("Longest  (comp src=" + cg.compIdOf(src) + "): " + Arrays.toString(distLong));
        System.out.println("Counters: " + m.counters());
        System.out.println("Time(ns): " + m.stopwatch().elapsedNanos());
    }
}
