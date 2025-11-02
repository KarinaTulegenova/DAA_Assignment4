package org.aitu.smartcity;

import org.aitu.smartcity.io.GraphLoader;
import org.aitu.smartcity.model.*;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.topo.TopologicalSort;
import org.aitu.smartcity.dagsp.DAGShortestPath;
import org.aitu.smartcity.dagsp.DAGLongestPath;
import org.aitu.smartcity.dagsp.PathResult;
import org.aitu.smartcity.util.*;

import java.nio.file.Path;
import java.util.*;

public class MainApp {

    private static List<Integer> reconstruct(int sink, int[] prev) {
        List<Integer> path = new ArrayList<>();
        for (int v = sink; v != -1; v = prev[v]) path.add(v);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar app.jar data/small_01.json [sourceId=0]");
            return;
        }
        int srcVertex = args.length > 1 ? Integer.parseInt(args[1]) : 0;

        Path file = Path.of(args[0]);
        WeightedDiGraph g = GraphLoader.loadWeighted(file);

        Metrics m = new Metrics(new Stopwatch(), new OpCounters());

        TarjanSCC tarjan = new TarjanSCC(g.asUnweighted(), g.n());
        List<List<Integer>> sccs = tarjan.findSCCs(m);
        CondensationGraph cg = CondensationGraph.fromSCCs(g, sccs);

        List<Integer> topo = TopologicalSort.kahn(cg.graph(), cg.n(), m);

        int srcComp = cg.compIdOf(srcVertex);

        PathResult sp = DAGShortestPath.shortest(cg.weightedGraph(), cg.n(), srcComp, topo, m);
        PathResult lp = DAGLongestPath.longest (cg.weightedGraph(), cg.n(), srcComp, topo, m);

        int sink = -1;
        for (int i = topo.size() - 1; i >= 0; i--) {
            int cand = topo.get(i);
            if (sp.dist[cand] < 1_000_000_000 || lp.dist[cand] > -1_000_000_000) { sink = cand; break; }
        }
        if (sink == -1 && !topo.isEmpty()) sink = topo.get(topo.size() - 1);

        List<Integer> spRoute = (sink == -1) ? List.of() : reconstruct(sink, sp.prev);
        List<Integer> lpRoute = (sink == -1) ? List.of() : reconstruct(sink, lp.prev);

        System.out.println("File: " + file);
        System.out.println("SCC count: " + sccs.size());
        System.out.println("Topo order size: " + topo.size());

        System.out.println("Source vertex: " + srcVertex + " -> source component: " + srcComp);
        if (sink != -1) {
            System.out.println("Sink component (chosen): " + sink);
            System.out.println("Shortest dist[to sink]: " + sp.dist[sink] + " | route: " + spRoute);
            System.out.println("Longest  dist[to sink]: " + lp.dist[sink] + " | route: " + lpRoute);
        } else {
            System.out.println("No reachable sink detected in topo order.");
        }

        System.out.println("Counters: " + m.counters());
        System.out.println("Time (ns): " + m.stopwatch().elapsedNanos());
    }
}
