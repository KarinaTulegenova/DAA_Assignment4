package org.aitu.smartcity.model;

import java.util.*;


public final class CondensationGraph {
    private final int n;
    private final Map<Integer, List<Integer>> dag;
    private final Map<Integer, List<int[]>> weighted;
    private final int[] compIdOf;

    private CondensationGraph(int n,
                              Map<Integer, List<Integer>> dag,
                              Map<Integer, List<int[]>> weighted,
                              int[] compIdOf) {
        this.n = n;
        this.dag = dag;
        this.weighted = weighted;
        this.compIdOf = compIdOf;
    }

    public static CondensationGraph fromSCCs(WeightedDiGraph g, List<List<Integer>> sccs) {
        int compCount = sccs.size();
        int[] compIdOf = new int[g.n()];
        Arrays.fill(compIdOf, -1);
        for (int cid = 0; cid < compCount; cid++) {
            for (int v : sccs.get(cid)) compIdOf[v] = cid;
        }
        for (int v = 0; v < g.n(); v++) if (compIdOf[v] < 0) compIdOf[v] = v;
        Map<Integer, Set<Integer>> dagTmp = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> wTmp = new HashMap<>();
        for (var entry : g.adj().entrySet()) {
            int u = entry.getKey();
            int cu = compIdOf[u];
            for (int[] edge : entry.getValue()) {
                int v = edge[0], w = edge[1];
                int cv = compIdOf[v];
                if (cu == cv) continue;
                dagTmp.computeIfAbsent(cu, k -> new HashSet<>()).add(cv);
                Map<Integer, Integer> toMap = wTmp.computeIfAbsent(cu, k -> new HashMap<>());
                toMap.merge(cv, w, Math::min);
            }
        }
        Map<Integer, List<Integer>> dag = new HashMap<>();
        for (var e : dagTmp.entrySet()) {
            dag.put(e.getKey(), new ArrayList<>(e.getValue()));
        }

        Map<Integer, List<int[]>> weighted = new HashMap<>();
        for (var e : wTmp.entrySet()) {
            int cu = e.getKey();
            List<int[]> lst = new ArrayList<>();
            for (var toW : e.getValue().entrySet()) {
                lst.add(new int[]{toW.getKey(), toW.getValue()});
            }
            weighted.put(cu, lst);
        }

        return new CondensationGraph(compCount, dag, weighted, compIdOf);
    }

    public int n() { return n; }

    public Map<Integer, List<Integer>> graph() { return dag; }

    public Map<Integer, List<int[]>> weightedGraph() { return weighted; }

    public int compIdOf(int v) { return compIdOf[v]; }
}
