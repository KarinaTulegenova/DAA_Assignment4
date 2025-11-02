package org.aitu.smartcity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import org.aitu.smartcity.io.GraphLoader;
import org.aitu.smartcity.model.WeightedDiGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphLoaderTest {

    @Test
    void loadWeightedFromJsonFile() throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("n", 4);
        JsonArray edges = new JsonArray();
        edges.add(array(0,1,2));
        edges.add(array(1,2,3));
        edges.add(array(2,3,4));
        obj.add("edges", edges);

        Path tmp = Files.createTempFile("graph", ".json");
        Files.writeString(tmp, new GsonBuilder().setPrettyPrinting().create().toJson(obj));

        WeightedDiGraph g = GraphLoader.loadWeighted(tmp);
        assertEquals(4, g.n());

        int m = g.adj().values().stream().mapToInt(List::size).sum();
        assertEquals(3, m);

        Files.deleteIfExists(tmp);
    }

    private static JsonArray array(int u, int v, int w) {
        JsonArray a = new JsonArray();
        a.add(u); a.add(v); a.add(w);
        return a;
    }
}
