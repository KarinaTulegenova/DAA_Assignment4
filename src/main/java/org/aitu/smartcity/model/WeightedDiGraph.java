package org.aitu.smartcity.model;

import java.util.*;

public class WeightedDiGraph {
    private final int n;
    private final Map<Integer, List<int[]>> adj; // v,w

    public WeightedDiGraph(int n) {
        this.n = n;
        this.adj = new HashMap<>();
    }

    public void addEdge(int u, int v, int w) {
        adj.computeIfAbsent(u, k -> new ArrayList<>()).add(new int[]{v, w});
    }

    public int n() { return n; }
    public Map<Integer, List<int[]>> adj() { return adj; }

    public DiGraph asUnweighted() {
        DiGraph g = new DiGraph(n);
        for (var e : adj.entrySet()) {
            int u = e.getKey();
            for (int[] vw : e.getValue()) g.addEdge(u, vw[0]);
        }
        return g;
    }
}
