package org.aitu.smartcity;

import org.aitu.smartcity.io.CSVExporter;
import org.aitu.smartcity.io.GraphLoader;
import org.aitu.smartcity.model.*;
import org.aitu.smartcity.scc.TarjanSCC;
import org.aitu.smartcity.topo.TopologicalSort;
import org.aitu.smartcity.dagsp.*;
import org.aitu.smartcity.util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class BatchRunner {
    private static List<Integer> reconstruct(int sink, int[] prev) {
        List<Integer> path = new ArrayList<>();
        for (int v = sink; v != -1; v = prev[v]) path.add(v);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) throws Exception {
        Path dataDir = Paths.get("data");
        Path outDir  = Paths.get("results");
        Files.createDirectories(outDir);

        Path summaryCsv = outDir.resolve("final_summary.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(summaryCsv))) {
            pw.println("file,n,edges,scc,dag_nodes,dag_edges,src_comp,sp_relax,lp_relax,time_ms,time_us");

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

                    int dagEdges = cg.graph().values().stream().mapToInt(List::size).sum();
                    int srcComp = cg.compIdOf(0);
                    PathResult sp = DAGShortestPath.shortest(cg.weightedGraph(), cg.n(), srcComp, topo, m);
                    PathResult lp = DAGLongestPath.longest (cg.weightedGraph(), cg.n(), srcComp, topo, m);

                    int sink = -1;
                    for (int i = topo.size() - 1; i >= 0; i--) {
                        int cand = topo.get(i);
                        if (sp.dist[cand] < 1_000_000_000 || lp.dist[cand] > -1_000_000_000) {
                            sink = cand;
                            break;
                        }
                    }
                    if (sink == -1 && !topo.isEmpty()) sink = topo.get(topo.size() - 1);

                    List<Integer> spRoute = (sink == -1) ? List.of() : reconstruct(sink, sp.prev);
                    List<Integer> lpRoute = (sink == -1) ? List.of() : reconstruct(sink, lp.prev);

                    m.stopwatch().stop();
                    double tMs = m.stopwatch().elapsedMillis();
                    double tUs = m.stopwatch().elapsedMicros();

                    long spRelax = m.counters().snapshot().getOrDefault("sp_relax", 0L);
                    long lpRelax = m.counters().snapshot().getOrDefault("lp_relax", 0L);

                    pw.printf(Locale.ROOT,
                            "%s,%d,%d,%d,%d,%d,%d,%d,%d,%.2f,%.0f%n",
                            f.getFileName(), n, edges, sccs.size(), cg.n(), dagEdges,
                            srcComp, spRelax, lpRelax, tMs, tUs
                    );

                    {
                        Path sccCsv = outDir.resolve("scc.csv");
                        List<String> rows = new ArrayList<>();
                        for (int cid = 0; cid < sccs.size(); cid++) {
                            String verts = sccs.get(cid).stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining(" "));
                            rows.add(String.format("%s,%d,%d,%s",
                                    f.getFileName(), cid, sccs.get(cid).size(), verts));
                        }
                        CSVExporter.append(sccCsv, "file,comp_id,size,vertices_space_sep", rows);
                    }

                    {
                        Path condCsv = outDir.resolve("condensation_edges.csv");
                        List<String> rows = new ArrayList<>();
                        cg.graph().forEach((u, nbrs) ->
                                nbrs.forEach(v ->
                                        rows.add(String.format("%s,%d,%d", f.getFileName(), u, v))));
                        CSVExporter.append(condCsv, "file,from_comp,to_comp", rows);
                    }

                    {
                        Path topoCompCsv = outDir.resolve("topo_components.csv");
                        List<String> rows = new ArrayList<>();
                        for (int i = 0; i < topo.size(); i++) {
                            int cid = topo.get(i);
                            rows.add(String.format("%s,%d,%d,%d",
                                    f.getFileName(), i, cid, sccs.get(cid).size()));
                        }
                        CSVExporter.append(topoCompCsv, "file,order_index,comp_id,size", rows);
                    }

                    {
                        Path topoTasksCsv = outDir.resolve("topo_tasks.csv");
                        List<String> rows = new ArrayList<>();
                        int order = 0;
                        for (int cid : topo) {
                            for (int v : sccs.get(cid)) {
                                rows.add(String.format("%s,%d,%d,%d",
                                        f.getFileName(), order++, v, cid));
                            }
                        }
                        CSVExporter.append(topoTasksCsv, "file,order_index,vertex_id,comp_id", rows);
                    }

                    {
                        Path spCsv = outDir.resolve("dag_shortest.csv");
                        String route = spRoute.stream().map(Object::toString).collect(Collectors.joining(" "));
                        int len = (sink == -1) ? Integer.MAX_VALUE : sp.dist[sink];
                        List<String> rows = List.of(String.format(Locale.ROOT,
                                "%s,%d,%d,%d,%d,%.2f,%s",
                                f.getFileName(), srcComp, sink, len, spRelax, tMs, route));
                        CSVExporter.append(spCsv, "file,src_comp,sink_comp,length,relaxations,time_ms,route_space_sep", rows);
                    }

                    {
                        Path lpCsv = outDir.resolve("dag_longest.csv");
                        String route = lpRoute.stream().map(Object::toString).collect(Collectors.joining(" "));
                        int len = (sink == -1) ? Integer.MIN_VALUE : lp.dist[sink];
                        List<String> rows = List.of(String.format(Locale.ROOT,
                                "%s,%d,%d,%d,%d,%.2f,%s",
                                f.getFileName(), srcComp, sink, len, lpRelax, tMs, route));
                        CSVExporter.append(lpCsv, "file,src_comp,sink_comp,length,relaxations,time_ms,route_space_sep", rows);
                    }

                    System.out.println("OK: " + f.getFileName());
                }
            }
        }
        System.out.println("Summary written to " + summaryCsv);
    }
}
