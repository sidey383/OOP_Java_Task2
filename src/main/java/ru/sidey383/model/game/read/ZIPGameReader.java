package ru.sidey383.model.game.read;

import java.io.FilterInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZIPGameReader {


    private final Map<String, DataReader> gameStructure;

    public ZIPGameReader(Map<String, DataReader> structure) {
        this.gameStructure = structure;
    }

    public Map<String, Object> readZIPGame(URL path) throws IOException {
        Map<String, Object> readObjects = new HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(path.openStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                var func = gameStructure.get(name);
                if (func != null) {
                    try {
                        readObjects.put(name, func.read(new FilterInputStream(zis) {
                            @Override
                            public void close() throws IOException {
                                zis.closeEntry();
                            }
                        }));
                    } catch (Throwable t) {
                        System.err.println("error while read entry " + name);
                        t.printStackTrace();
                        //TODO: add logger or something like this
                    }
                }
            }
        }
        return readObjects;
    }

    public interface DataReader {
        java.lang.Object read(java.io.InputStream is) throws Throwable;
    }

}
