package org.aitu.smartcity.dagsp;

import org.aitu.smartcity.util.Metrics;
import java.util.*;

public final class DAGShortestPath {
    private DAGShortestPath() {}
    public static PathResult shortest(Map<Integer, List<int[]>> dagW,
                                      int n, int src, List<Integer> topo, Metrics m) {
        final int INF = 1_000_000_000;
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        for (int v : topo) {
            if (dist[v] == INF) continue;
            List<int[]> out = dagW.getOrDefault(v, Collections.emptyList());
            for (int[] e : out) {
                int u = e[0], w = e[1];
                int nd = dist[v] + w;
                if (nd < dist[u]) {
                    dist[u] = nd;
                    prev[u] = v;
                }
                m.counters().inc("sp_relax");
            }
        }
        return new PathResult(dist, prev);
    }
}
