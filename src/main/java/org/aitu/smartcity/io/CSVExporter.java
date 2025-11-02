package org.aitu.smartcity.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public final class CSVExporter {
    private CSVExporter() {}

    public static void append(Path csv, String header, List<String> rows) throws IOException {
        Files.createDirectories(csv.getParent());
        boolean fresh = Files.notExists(csv);
        try (BufferedWriter w = Files.newBufferedWriter(csv, java.nio.charset.StandardCharsets.UTF_8, CREATE, APPEND);
             PrintWriter pw = new PrintWriter(w)) {
            if (fresh && header != null && !header.isEmpty()) pw.println(header);
            for (String r : rows) pw.println(r);
        }
    }
}
