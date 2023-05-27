package ru.sidey383.task2.model.data.game.read;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.CustomFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZIPReaderTest {

    @RegisterExtension
    public static CustomFileSystem fileSystem = new CustomFileSystem();

    private static final Map<String, String> hashMap = Map.of(
            "archive.zip", "a7c92a17754fd6794e262e876b9be675",
            "exampleGame1.zip", "15005739c899b18d3d62580a927606e5",
            "exampleGame2.zip", "b64da659d7232dfcbbfe4af7d6951d34"
    );


    @BeforeAll
    public static void copy() throws IOException {
        for (String name : hashMap.keySet()) {
            Path p = fileSystem.getRoot().resolve(name);
            try (InputStream is = ZIPReaderTest.class.getResourceAsStream("/" + name)) {
                assert is != null;
                Files.copy(is, p);
            }
        }
    }

    @Order(0)
    @Test
    public void simpleTest() throws IOException {
        ZIPReader reader = new ZIPReader(Map.of());
        RawDataContainer container = reader.readZIP(fileSystem.getRoot().resolve("archive.zip"));
        Optional<byte[]> foo1 = container.getData(byte[].class, "foo1");
        Optional<byte[]> foo2 = container.getData(byte[].class, "foo2");
        Optional<byte[]> foo3 = container.getData(byte[].class, "foo3");
        assertTrue(foo1.isPresent(), "No foo1 value");
        assertArrayEquals("bar1".getBytes(StandardCharsets.UTF_8), foo1.get(), "Wrong foo1 value");
        assertTrue(foo2.isPresent(), "No foo2 value");
        assertArrayEquals("bar2".getBytes(StandardCharsets.UTF_8), foo2.get(), "Wrong foo2 value");
        assertTrue(foo3.isPresent(), "No foo3 value");
        assertArrayEquals("bar3".getBytes(StandardCharsets.UTF_8), foo3.get(), "Wrong foo3 value");
    }

    @Order(1)
    @Test
    public void baseDataReaderTest() throws IOException {
        ZIPReader reader = new ZIPReader(Map.of(), (is) -> new String(is.readAllBytes()));
        RawDataContainer container = reader.readZIP(fileSystem.getRoot().resolve("archive.zip"));
        Optional<String> foo1 = container.getData(String.class, "foo1");
        Optional<String> foo2 = container.getData(String.class, "foo2");
        Optional<String> foo3 = container.getData(String.class, "foo3");
        assertTrue(foo1.isPresent(), "No foo1 value");
        assertEquals("bar1", foo1.get(), "Wrong foo1 value");
        assertTrue(foo2.isPresent(), "No foo2 value");
        assertEquals("bar2", foo2.get(), "Wrong foo2 value");
        assertTrue(foo3.isPresent(), "No foo3 value");
        assertEquals("bar3", foo3.get(), "Wrong foo3 value");
    }

    @Order(2)
    @Test
    public void dataReaderStructureTest() throws IOException {
        Object obj = new Object();
        ZIPReader reader = new ZIPReader(
                Map.of(
                        "foo1", (is) -> Optional.of(is.readAllBytes()),
                        "foo2", (is) -> obj
                ),
                (is) -> new String(is.readAllBytes())
        );
        RawDataContainer container = reader.readZIP(fileSystem.getRoot().resolve("archive.zip"));
        @SuppressWarnings("rawtypes")
        Optional<Optional> foo1 = container.getData(Optional.class, "foo1");
        Optional<Object> foo2 = container.getData(Object.class, "foo2");
        Optional<String> foo3 = container.getData(String.class, "foo3");

        assertTrue(foo1.isPresent(), "No foo1 value");
        @SuppressWarnings("unchecked")
        Optional<byte[]> foo1Val = (Optional<byte[]>) foo1.get();
        assertTrue(foo1.get().isPresent(), "No foo1.get() value");
        assertArrayEquals("bar1".getBytes(StandardCharsets.UTF_8), foo1Val.get(), "Wrong foo1 value");

        assertTrue(foo2.isPresent(), "No foo2 value");
        assertEquals(obj, foo2.get(), "Wrong foo2 value");

        assertTrue(foo3.isPresent(), "No foo3 value");
        assertEquals("bar3", foo3.get(), "Wrong foo3 value");
    }

    @Order(3)
    @Test
    public void hashTest() throws IOException {
        ZIPReader reader = new ZIPReader(Map.of());
        for (Map.Entry<String, String> entry : hashMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
            assertEquals(
                    entry.getValue(),
                    reader.readZIP(
                                    fileSystem.getRoot().resolve("").resolve(entry.getKey())
                            )
                            .getHash(),
                    "Wrong file hash for file " + entry.getKey() );
        }
    }

    @Order(4)
    @Test
    public void hashWithStrangeReader() throws IOException {
        String fName = "exampleGame1.zip";
        ZIPReader reader = new ZIPReader(
                Map.of(),
                (is) -> "foo"
        );
        RawDataContainer container = reader.readZIP(fileSystem.getRoot().resolve(fName));
        assertEquals(hashMap.get(fName), container.getHash(), "Hash for file " + fName);
    }


    @Order(5)
    @Test
    public void exceptionText() {
        ZIPReader reader = new ZIPReader(Map.of());
        for (String name : hashMap.keySet()) {
            assertThrows(IOException.class, () -> reader.readZIP(fileSystem.getRoot().resolve("a"+name)));
        }
    }

}
