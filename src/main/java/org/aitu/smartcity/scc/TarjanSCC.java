package org.aitu.smartcity.scc;

import org.aitu.smartcity.model.DiGraph;
import org.aitu.smartcity.util.Metrics;

import java.util.*;

public class TarjanSCC {
    private int time;
    private int[] disc, low;
    private boolean[] inStack;
    private Deque<Integer> st;
    private final DiGraph g;
    private final List<List<Integer>> res = new ArrayList<>();

    public TarjanSCC(DiGraph g, int n) {
        this.g = g;
        this.disc = new int[n];
        this.low  = new int[n];
        this.inStack = new boolean[n];
        Arrays.fill(disc, -1);
        this.st = new ArrayDeque<>();
    }

    public List<List<Integer>> findSCCs(Metrics m) {
        for (int v = 0; v < disc.length; v++) if (disc[v] == -1) dfs(v, m);
        return res;
    }

    private void dfs(int u, Metrics m) {
        disc[u] = low[u] = ++time;
        st.push(u); inStack[u] = true;
        for (int v : g.adj().getOrDefault(u, List.of())) {
            m.counters().inc("dfs_edges");
            if (disc[v] == -1) { dfs(v, m); low[u] = Math.min(low[u], low[v]); }
            else if (inStack[v]) { low[u] = Math.min(low[u], disc[v]); }
        }
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            int w;
            do { w = st.pop(); inStack[w] = false; comp.add(w); } while (w != u);
            res.add(comp);
        }
    }
}
