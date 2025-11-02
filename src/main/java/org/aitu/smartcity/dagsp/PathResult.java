package org.aitu.smartcity.dagsp;

public final class PathResult {
    public final int[] dist;
    public final int[] prev;
    public PathResult(int[] dist, int[] prev) {
        this.dist = dist;
        this.prev = prev;
    }
}
