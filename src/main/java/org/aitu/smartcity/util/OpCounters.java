package org.aitu.smartcity.util;

import java.util.*;

public class OpCounters {
    private final Map<String, Long> map = new HashMap<>();
    public void inc(String key) { map.merge(key, 1L, Long::sum); }
    public Map<String, Long> snapshot() { return Map.copyOf(map); }
    @Override public String toString() { return map.toString(); }
}
