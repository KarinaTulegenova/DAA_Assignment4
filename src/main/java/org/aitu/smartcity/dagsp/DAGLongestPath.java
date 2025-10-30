package org.aitu.smartcity.dagsp;

import org.aitu.smartcity.util.Metrics;

import java.util.*;

public class DAGLongestPath {
    public static int[] longest(Map<Integer,List<int[]>> g, int n, int src, List<Integer> topo, Metrics m) {
        int[] d = new int[n];
        Arrays.fill(d, Integer.MIN_VALUE);
        d[src] = 0;
        for (int u : topo) if (d[u] != Integer.MIN_VALUE) {
            for (int[] vw : g.getOrDefault(u, List.of())) {
                int v = vw[0], w = vw[1];
                m.counters().inc("lp_relax");
                if (d[u] + w > d[v]) d[v] = d[u] + w;
            }
        }
        return d;
    }
}
