package org.aitu.smartcity.model;

import java.util.*;

public class DiGraph {
    protected final int n;
    protected final Map<Integer, List<Integer>> adj;

    public DiGraph(int n) {
        this.n = n;
        this.adj = new HashMap<>();
    }

    public void addEdge(int u, int v) {
        adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
    }

    public int n() { return n; }
    public Map<Integer, List<Integer>> adj() { return adj; }
}
