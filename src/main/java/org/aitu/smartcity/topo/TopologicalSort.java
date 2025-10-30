package org.aitu.smartcity.topo;

import org.aitu.smartcity.util.Metrics;

import java.util.*;

public class TopologicalSort {
    public static List<Integer> kahn(Map<Integer, List<Integer>> g, int n, Metrics m) {
        int[] indeg = new int[n];
        for (var e : g.entrySet()) for (int v : e.getValue()) { indeg[v]++; m.counters().inc("indegree"); }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll(); ans.add(u);
            for (int v : g.getOrDefault(u, List.of())) {
                m.counters().inc("kahn_relax");
                if (--indeg[v] == 0) q.add(v);
            }
        }
        return ans;
    }
}
