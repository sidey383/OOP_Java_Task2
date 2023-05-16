package ru.sidey383.model.data.game.read;

import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZIPReader {


    protected final Map<String, DataReader> readerStructure;

    protected DataReader defaultReader;

    public ZIPReader(@NotNull Map<String, DataReader> structure, @NotNull DataReader defaultReader) {
        this.readerStructure = structure;
        this.defaultReader = defaultReader;
    }

    public ZIPReader(@NotNull Map<String, DataReader> structure) {
        this.readerStructure = structure;
        this.defaultReader = InputStream::readAllBytes;
    }

    public RawDataContainer readZIP(URL path) throws IOException {
        RawDataContainer rawDataContainer = new RawDataContainer();
        try (ZipInputStream zis = new ZipInputStream(path.openStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                var func = readerStructure.get(name);
                if (func != null) {
                    try {
                        rawDataContainer.addObject(name, func.read(new FilterInputStream(zis) {
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
                } else {
                    rawDataContainer.addObject(name, zis.readAllBytes());
                }
            }
        }
        return rawDataContainer;
    }

    @FunctionalInterface
    public interface DataReader {
        Object read(InputStream is) throws Throwable;

    }

}