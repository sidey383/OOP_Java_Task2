package ru.sidey383.task2.model.data.settings;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.CustomFileSystem;
import ru.sidey383.task2.model.data.game.read.ZIPReaderTest;
import ru.sidey383.task2.model.game.ClickType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SettingsControllerTest {

    @RegisterExtension
    public static final CustomFileSystem fileSystem = new CustomFileSystem();

    private static final String[] files = new String[] {
            "settings1.json",
            "settings2.json",
            "settings3.json"
    };

    private static final Path[] gameDirs = new Path[] {
            Path.of("games").toAbsolutePath(),
            Path.of("foo1").toAbsolutePath(),
            Path.of("foo2").toAbsolutePath()
    };

    @SuppressWarnings("unchecked")
    private static final Map<ClickType, Integer>[] gameKeys = new Map[] {
            Map.of(ClickType.LINE_1, 1,
                    ClickType.LINE_2, 2,
                    ClickType.LINE_3, 3,
                    ClickType.LINE_4, 4,
                    ClickType.LINE_5, 5,
                    ClickType.LINE_6, 6),
            Map.of(ClickType.LINE_1, 34,
                    ClickType.LINE_2, 12,
                    ClickType.LINE_3, 52,
                    ClickType.LINE_4,  64,
                    ClickType.LINE_5,  -23,
                    ClickType.LINE_6,  -44),
            Map.of(ClickType.LINE_1, 541,
                    ClickType.LINE_2, -23,
                    ClickType.LINE_3, -83,
                    ClickType.LINE_4, -6123,
                    ClickType.LINE_5, 5612,
                    ClickType.LINE_6, -125)
    };

    @BeforeAll
    public static void copy() throws IOException {
        for (String name : files) {
            Path p = fileSystem.getRoot().resolve(name);
            try (InputStream is = ZIPReaderTest.class.getResourceAsStream("/settings/" + name)) {
                assert is != null;
                Files.copy(is, p);
            }
        }
    }

    @Test
    @Order(0)
    public void loadTest() {
        for (int i = 0; i < files.length; i++) {
            Path p = fileSystem.getRoot().resolve(files[i]);
            SettingsController controller = new SettingsController(p);
            assertEquals(gameDirs[i], controller.getGamesDir(), "Wrong game dir for file " + files[i]);
            Map<ClickType, Integer> keys = controller.getGameKeys();
            for (ClickType type : ClickType.values()) {
                assertEquals(gameKeys[i].get(type), keys.get(type), "Wrong key value " + type + " for file " + files[i]);
            }
        }
    }

    @Test
    @Order(1)
    public void initTest() {
        Path p = fileSystem.getRoot().resolve("initSettings");
        SettingsController controller = new SettingsController(p);
        AppSettings defaultSettings = AppSettings.getDefault(p.getParent());
        assertEquals(defaultSettings.getGamesDir(), controller.getGamesDir(), "Wrong games dir for new  settings");
        for (ClickType type : ClickType.values()) {
            assertEquals(defaultSettings.getGameKeys().get(type), controller.getGameKeys().get(type), "Wrong key value " + type + " for new settings" );
        }
    }

    @Test
    @Order(2)
    public void changeTest() {
        SettingsController settings = new SettingsController(fileSystem.getRoot().resolve("settingsChange"));
        settings.setGameKey(ClickType.LINE_1, 455);
        assertEquals(455, settings.getGameKeys().get(ClickType.LINE_1), "Value was changed");
        settings.setGameKey(ClickType.LINE_2, -455);
        assertEquals(-455, settings.getGameKeys().get(ClickType.LINE_2), "Value was changed");
        settings.setGameKey(ClickType.LINE_3, 455);
        assertEquals(455, settings.getGameKeys().get(ClickType.LINE_3), "Value was changed");
        settings.setGameKey(ClickType.LINE_4, -455);
        assertEquals(-455, settings.getGameKeys().get(ClickType.LINE_4), "Value was changed");
        settings.setGameKey(ClickType.LINE_5, 455);
        assertEquals(455, settings.getGameKeys().get(ClickType.LINE_5), "Value was changed");
        settings.setGameKey(ClickType.LINE_6, -455);
        assertEquals(-455, settings.getGameKeys().get(ClickType.LINE_6), "Value was changed");
        Path p = Path.of("test");
        settings.setGamesDir(p);
        assertEquals(p.toAbsolutePath(), settings.getGamesDir(), "Value was changed");
    }

    @Test
    @Order(3)
    public void reloadChange() {
        Path settingsPath = fileSystem.getRoot().resolve("settingsChange");
        SettingsController settings = new SettingsController(settingsPath);
        settings.setGameKey(ClickType.LINE_1, 455);
        settings.setGameKey(ClickType.LINE_2, -455);
        settings.setGameKey(ClickType.LINE_3, 455);
        settings.setGameKey(ClickType.LINE_4, -455);
        settings.setGameKey(ClickType.LINE_5, 455);
        settings.setGameKey(ClickType.LINE_6, -455);
        Path p = Path.of("test");
        settings.setGamesDir(p);

        settings = new SettingsController(settingsPath);
        assertEquals(455, settings.getGameKeys().get(ClickType.LINE_1), "Value was changed");
        assertEquals(-455, settings.getGameKeys().get(ClickType.LINE_2), "Value was changed");
        assertEquals(455, settings.getGameKeys().get(ClickType.LINE_3), "Value was changed");
        assertEquals(-455, settings.getGameKeys().get(ClickType.LINE_4), "Value was changed");
        assertEquals(455, settings.getGameKeys().get(ClickType.LINE_5), "Value was changed");
        assertEquals(-455, settings.getGameKeys().get(ClickType.LINE_6), "Value was changed");
        assertEquals(p.toAbsolutePath(), settings.getGamesDir(), "Value was changed");
    }

}
