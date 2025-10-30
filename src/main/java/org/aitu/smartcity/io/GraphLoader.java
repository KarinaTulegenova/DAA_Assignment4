package org.aitu.smartcity.io;

import com.google.gson.*;
import org.aitu.smartcity.model.WeightedDiGraph;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class GraphLoader {
    public static WeightedDiGraph loadWeighted(Path path) {
        try {
            String json = Files.readString(path);
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            int n = obj.get("n").getAsInt();
            WeightedDiGraph g = new WeightedDiGraph(n);
            for (JsonElement e : obj.getAsJsonArray("edges")) {
                List<JsonElement> arr = e.getAsJsonArray().asList();
                int u = arr.get(0).getAsInt();
                int v = arr.get(1).getAsInt();
                int w = arr.size() >= 3 ? arr.get(2).getAsInt() : 1;
                g.addEdge(u, v, w);
            }
            return g;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
