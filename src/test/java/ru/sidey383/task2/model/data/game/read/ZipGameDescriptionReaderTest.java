package ru.sidey383.task2.model.data.game.read;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.model.CustomFileSystem;
import ru.sidey383.task2.model.data.game.GameDescription;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZipGameDescriptionReaderTest {

    @RegisterExtension
    public static CustomFileSystem fileSystem = new CustomFileSystem();

    private static final String[] files = new String[] {
            "archive.zip",
            "exampleGame1.zip",
            "exampleGame2.zip"
    };

    private static final String[] hashes = new String[] {
            "a7c92a17754fd6794e262e876b9be675",
            "15005739c899b18d3d62580a927606e5",
            "b64da659d7232dfcbbfe4af7d6951d34"
    };

    private static final String[] names = new String[] {
            null,
            "ExampleGame1",
            "ExampleGame2"
    };

    @BeforeAll
    public static void copy() throws IOException {
        for (String name : files) {
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
        ZIPGameDescriptionReader reader = new ZIPGameDescriptionReader();
        for (int i = 0; i < files.length; i++) {
            Path p = fileSystem.getRoot().resolve(files[i]);
            GameDescription description = reader.readDescription(p);
            if (names[i] == null) {
                assertNull(description, "Description from wrong file must be null");
                continue;
            }
            assertEquals(names[i], description.name(), "Wrong game name: " + files[i]);
            assertEquals(hashes[i], description.hash(), "Wrong game hash: " + files[i]);
            assertEquals(names[i] + "_" + hashes[i], description.gameKey(), "Wrong game key: " + files[i]);
            assertEquals(p, description.gameContainer(), "Wrong game container for: " + files[i]);
        }
    }

    @Order(1)
    @Test
    public void errorTest() {
        ZIPGameDescriptionReader reader = new ZIPGameDescriptionReader();
        for (String name : files) {
            assertThrows(IOException.class, () -> reader.readZIP(fileSystem.getRoot().resolve("a"+name)));
        }
    }

}
