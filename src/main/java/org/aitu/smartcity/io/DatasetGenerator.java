package org.aitu.smartcity.io;

import com.google.gson.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class DatasetGenerator {
    private static final Random rnd = new Random();

    private static JsonObject makeGraph(int n, double density, boolean withCycles, int seed) {
        rnd.setSeed(seed);
        int target = Math.max(1, (int)Math.round(density * n * (n - 1)));

        Set<Long> seen = new HashSet<>();
        List<int[]> edges = new ArrayList<>();

        while (edges.size() < target) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u == v) continue;

            if (!withCycles && u > v) { int t = u; u = v; v = t; }

            long key = ((long)u << 32) ^ v;
            if (seen.add(key)) {
                int w = 1 + rnd.nextInt(9);
                edges.add(new int[]{u, v, w});
            }
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("n", n);
        JsonArray arr = new JsonArray();
        for (int[] e : edges) {
            JsonArray a = new JsonArray();
            a.add(e[0]); a.add(e[1]); a.add(e[2]);
            arr.add(a);
        }
        obj.add("edges", arr);
        return obj;
    }

    private static void write(Path path, JsonObject g) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, new GsonBuilder().setPrettyPrinting().create().toJson(g));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void main(String[] args) {
        Object[][] cfg = new Object[][]{
                {"small_01.json",  6,  0.14, false, 1001},
                {"small_02.json",  8,  0.22, true,  1002},
                {"small_03.json", 10,  0.30, true,  1003},
                {"medium_01.json", 12, 0.20, false, 2001},
                {"medium_02.json", 16, 0.28, true,  2002},
                {"medium_03.json", 18, 0.36, true,  2003},
                {"large_01.json",  24, 0.16, false, 3001},
                {"large_02.json",  30, 0.26, true,  3002},
                {"large_03.json",  40, 0.40, true,  3003}
        };

        Path dir = Path.of("data");
        for (Object[] c : cfg) {
            String name = (String)c[0];
            int n = (Integer)c[1];
            double d = (Double)c[2];
            boolean cyc = (Boolean)c[3];
            int seed = (Integer)c[4];

            JsonObject g = makeGraph(n, d, cyc, seed);
            write(dir.resolve(name), g);
            System.out.printf("Wrote %s (n=%d, density=%.2f, cycles=%s, seed=%d)%n",
                    name, n, d, cyc, seed);
        }
        System.out.println("Done. Files are in ./data");
    }
}
