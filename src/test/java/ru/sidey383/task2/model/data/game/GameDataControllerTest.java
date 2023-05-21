package ru.sidey383.task2.model.data.game;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.model.CustomFileSystem;
import ru.sidey383.task2.model.data.game.read.ZIPReaderTest;
import ru.sidey383.task2.model.data.settings.SettingsProvider;
import ru.sidey383.task2.model.game.ClickType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDataControllerTest {

    @RegisterExtension
    public static final CustomFileSystem fileSystem = new CustomFileSystem();

    private static final String[] files = new String[] {
            "exampleGame1.zip",
            "exampleGame2.zip",
            "simpleGame.zip"
    };

    private static final String[] hashes = new String[] {
            "15005739c899b18d3d62580a927606e5",
            "b64da659d7232dfcbbfe4af7d6951d34",
            "3fb21f899d256b701cf75c728bf83aa6"
    };

    private static final String[] names = new String[] {
            "ExampleGame1",
            "ExampleGame2",
            "simpleGame"
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

    private static SettingsProvider getProvider(Path dir) {
        return new SettingsProvider() {
            @Override
            public Map<ClickType, Integer> getGameKeys() {
                return null;
            }

            @Override
            public void setGameKey(ClickType type, int key) {
            }

            @Override
            public Path getGamesDir() {
                return dir;
            }

            @Override
            public void setGamesDir(Path path) {
            }
        };
    }

    @Test
    @Order(0)
    public void createTest() {
        SettingsProvider settingsProvider = getProvider(fileSystem.getRoot().resolve("games"));

        GameDataController controller = new GameDataController(settingsProvider);

        Collection<GameDescription> descriptions = controller.readGameDescriptions();

        assertEquals(1, descriptions.size(), "Expect one game in new directory");
        GameDescription description = descriptions.iterator().next();
        assertEquals("ExampleGame", description.name(), "Wrong game name");
        assertEquals("603c9c19f99c451f06e971b2b80de518", description.hash(), "Wrong game hash");
        assertEquals("ExampleGame_603c9c19f99c451f06e971b2b80de518", description.gameKey(), "Wrong game key");
    }

    @Test
    @Order(1)
    public void gamesTest() {
        SettingsProvider settingsProvider = getProvider(fileSystem.getRoot());
        GameDataController controller = new GameDataController(settingsProvider);

        Collection<GameDescription> descriptions = controller.readGameDescriptions();

        assertEquals(3, descriptions.size(), "Wrong count of games");

        int i = 0;
        for (GameDescription description : descriptions.stream().sorted(Comparator.comparing(GameDescription::name)).toList()) {
            assertEquals(names[i], description.name(), "Wrong game name");
            assertEquals(hashes[i], description.hash(), "Wrong game hash");
            assertEquals(names[i] + "_" + hashes[i], description.gameKey(), "Wrong game key");
            i++;
        }
    }

}
