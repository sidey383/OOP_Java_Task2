package ru.sidey383.task2.model.data.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.sidey383.task2.model.game.ClickType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppSettingsTest {

    private static final String[] jsonProperties = new String[] {
            """
            {
                "keys" : {"values":[1, 2, 3, 4, 5, 6]},
                "gamePath": "games"
            }
            """,
            """
            {
                "keys" : {"values":[6, 5, 4, 3, 2, 1]},
                "gamePath": "foo"
            }
            """,
            """
            {
                "keys" : {"values":[383, 383, 383, 383, 383, 383]},
                "gamePath": "bar"
            }
            """
    };

    private static final Path[] gameDirs = new Path[] {
            Path.of("games").toAbsolutePath(),
            Path.of("foo").toAbsolutePath(),
            Path.of("bar").toAbsolutePath()
    };

    @SuppressWarnings("unchecked")
    private static final Map<ClickType, Integer>[] gameKeys = new Map[] {
            Map.of(ClickType.LINE_1, 1,
                    ClickType.LINE_2, 2,
                    ClickType.LINE_3, 3,
                    ClickType.LINE_4, 4,
                    ClickType.LINE_5, 5,
                    ClickType.LINE_6, 6),
            Map.of(ClickType.LINE_1, 6,
                    ClickType.LINE_2, 5,
                    ClickType.LINE_3, 4,
                    ClickType.LINE_4, 3,
                    ClickType.LINE_5, 2,
                    ClickType.LINE_6, 1),
            Map.of(ClickType.LINE_1, 383,
                    ClickType.LINE_2, 383,
                    ClickType.LINE_3, 383,
                    ClickType.LINE_4, 383,
                    ClickType.LINE_5, 383,
                    ClickType.LINE_6, 383)
    };

    private static final AppSettings[] settingsArray = new AppSettings[] {
            new AppSettings(
                    new AppSettings.ClickKeys(
                            new Integer[] {1, 2, 3, 4, 5, 6}
                    ),
                    Path.of("games")
            ),
            new AppSettings(
                    new AppSettings.ClickKeys(
                            new Integer[] {6, 5, 4, 3, 2, 1}
                    ),
                    Path.of("foo")
            ),
            new AppSettings(
                    new AppSettings.ClickKeys(
                            new Integer[] {383, 383, 383, 383, 383, 383}
                    ),
                    Path.of("bar")
            )
    };

    @Test
    @Order(0)
    public void deserializeTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < jsonProperties.length; i++) {
            AppSettings settings = mapper.readValue(jsonProperties[i], AppSettings.class);
            assertEquals(gameDirs[i], settings.getGamesDir(), "Wrong game path from json " + jsonProperties[i]);
            Map<ClickType, Integer> keys = settings.getGameKeys();
            assertEquals(ClickType.values().length, keys.size(), "Wrong size of key map from json " + jsonProperties[i]);
            for (ClickType type : ClickType.values()) {
                assertEquals(gameKeys[i].get(type), keys.get(type), "Wrong key " + type + " value in map from json " + jsonProperties[i]);
            }
        }
    }

    @Test
    @Order(1)
    public void serializeTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (AppSettings settings : settingsArray) {
            AppSettings actual = mapper.readValue(mapper.writeValueAsBytes(settings), AppSettings.class);
            assertEquals(settings.getGamesDir(), actual.getGamesDir(), "Serialize and deserialize settings");
            Map<ClickType, Integer> expectKeys = settings.getGameKeys();
            Map<ClickType, Integer> actualKeys = actual.getGameKeys();
            for (ClickType type : ClickType.values()) {
                assertEquals(expectKeys.get(type), actualKeys.get(type), "Serialize and deserialize settings");
            }
        }
    }

    @Test
    @Order(2)
    public void changeTest() throws JsonProcessingException {
        AppSettings settings = new ObjectMapper().readValue(jsonProperties[0], AppSettings.class);
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
}
