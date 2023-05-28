package ru.sidey383.task2.model.data.game.read;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.CustomFileSystem;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.exception.ModelIOException;
import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.level.PianoGame;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZIPGameReaderTest {


    @RegisterExtension
    public static CustomFileSystem fileSystem = new CustomFileSystem();

    private static final String simpleGame = "simpleGame.zip";

    private final static String[] gameValues = new String[] {
            "center",
            "left",
            "right",
            "music",
            "gameLore.json",
            "tiles1.json",
            "tiles2.json",
            "tiles3.json",
            "tiles4.json",
            "tiles5.json",
            "tiles6.json"
    };

    private final static Class<?>[] valueTypes = new Class<?>[] {
            byte[].class,
            byte[].class,
            byte[].class,
            byte[].class,
            GameLore.class,
            Tile[].class,
            Tile[].class,
            Tile[].class,
            Tile[].class,
            Tile[].class,
            Tile[].class
    };

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
        Path simple = fileSystem.getRoot().resolve(simpleGame);
        try (InputStream is = ZIPReaderTest.class.getResourceAsStream("/" + simpleGame)) {
            assert is != null;
            Files.copy(is, simple);
        }
    }

    @Order(0)
    @Test
    public void simpleTest() throws ModelException, IOException {
        ZIPGameReader reader = new ZIPGameReader();
        for (int i = 0; i < files.length; i++) {
            Path p = fileSystem.getRoot().resolve(files[i]);
            RawDataContainer dataContainer = reader.readZIP(p);
            assertEquals(hashes[i], dataContainer.getHash(), "Wrong file hash: " + files[i]);
            if (names[i] == null) {
                assertThrows(ModelIOException.class, () -> reader.readGame(dataContainer), "Wrong file not null: " + files[i]);
                continue;
            }
            for (int j = 0; j < gameValues.length; j++) {
                assertTrue(dataContainer.getData(valueTypes[i], gameValues[i]).isPresent(), "Value " + gameValues[i] + " with type " + valueTypes[i] + " not present for file " + files[i]);
            }
            PianoGame game = reader.readGame(dataContainer);
            assertNotNull(game, "Game is null: " + files[i]);
            assertEquals(names[i], game.name());
        }
    }


    @Order(1)
    @Test
    public void gameDataTest() throws ModelException, IOException {
        Path simpleGamePath = fileSystem.getRoot().resolve(simpleGame);
        ZIPGameReader reader = new ZIPGameReader();
        RawDataContainer container = reader.readZIP(simpleGamePath);
        PianoGame game = reader.readGame(container);

        assertEquals("simpleGame", game.name(), "Wrong game name");
        assertEquals(410000000, game.totalTime(), "Wrong total time");

        for (int i = 0; i < ClickType.values().length; i++) {
            List<Tile> tiles = new ArrayList<>();
            ClickType click = ClickType.values()[i];

            game.getLine(click).getTiles(tiles, 0, 100000000000L);

            assertEquals(2, tiles.size(), "Wrong tiles count " + click);

            tiles.sort(Comparator.comparingLong(Tile::startTime));

            Tile tile1 = tiles.get(0);
            Tile tile2 = tiles.get(1);

            assertEquals(TileType.DEFAULT, tile1.type(), "Wrong first tile type " + click);
            assertEquals(TileType.LONG, tile2.type(), "Wrong second tile type " + click);
            assertEquals(100 + i * 100L, tile1.startTime(), "Wrong first tile start time " + click);
            assertEquals(400000000L, tile2.startTime(), "Wrong second tile start time " + click);
            assertEquals(100L + i * 100L + 300_000_000, tile1.endTime(), "Wrong first tile end time " + click);
            assertEquals(400000000L + 1000L + 1000L * i, tile2.endTime(), "Wrong second tile end time " + click);
        }
    }

    @Order(2)
    @Test
    public void errorTest() {
        ZIPGameDescriptionReader reader = new ZIPGameDescriptionReader();
        for (String name : files) {
            assertThrows(IOException.class, () -> reader.readZIP(fileSystem.getRoot().resolve("a"+name)));
        }
    }

}
