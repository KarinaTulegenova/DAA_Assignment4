package org.aitu.smartcity;

import org.aitu.smartcity.io.GraphLoader;
import org.aitu.smartcity.model.*;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.topo.TopologicalSort;
import org.aitu.smartcity.dagsp.DAGShortestPath;
import org.aitu.smartcity.dagsp.DAGLongestPath;
import org.aitu.smartcity.util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BatchRunner {
    public static void main(String[] args) throws Exception {
        Path dataDir = Paths.get("data");
        Path outDir  = Paths.get("results");
        Files.createDirectories(outDir);
        Path csv = outDir.resolve("summary.csv");

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(csv))) {
            pw.println("file,n,edges,scc,dag_nodes,dag_edges,src_comp,sp_relax,lp_relax,time_ms");
            try (var files = Files.list(dataDir)) {
                for (Path f : (Iterable<Path>) files.sorted()::iterator) {
                    if (!f.toString().endsWith(".json")) continue;

                    WeightedDiGraph g = GraphLoader.loadWeighted(f);
                    int n = g.n();
                    int edges = g.adj().values().stream().mapToInt(List::size).sum();

                    Metrics m = new Metrics(new Stopwatch(), new OpCounters());
                    TarjanSCC tarjan = new TarjanSCC(g.asUnweighted(), n);
                    List<List<Integer>> sccs = tarjan.findSCCs(m);
                    CondensationGraph cg = CondensationGraph.fromSCCs(g, sccs);
                    List<Integer> topo = TopologicalSort.kahn(cg.graph(), cg.n(), m);
                    int src = 0;
                    int srcComp = cg.compIdOf(src);
                    int[] sp = DAGShortestPath.shortest(cg.weightedGraph(), cg.n(), srcComp, topo, m);
                    int[] lp = DAGLongestPath.longest(cg.weightedGraph(), cg.n(), srcComp, topo, m);
                    m.stopwatch().stop();

                    int dagEdges = cg.graph().values().stream().mapToInt(List::size).sum();
                    long timeMs = Math.max(0, m.stopwatch().elapsedNanos() / 1_000_000);

                    pw.printf(Locale.ROOT,
                            "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                            f.getFileName(), n, edges, sccs.size(), cg.n(), dagEdges,
                            srcComp,
                            m.counters().snapshot().getOrDefault("sp_relax", 0L),
                            m.counters().snapshot().getOrDefault("lp_relax", 0L),
                            timeMs
                    );
                    System.out.println("OK: " + f.getFileName());
                }
            }
        }
        System.out.println("Summary written to " + csv.toString());
    }
}
