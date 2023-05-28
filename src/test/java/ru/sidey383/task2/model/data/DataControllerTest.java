package ru.sidey383.task2.model.data;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.CustomFileSystem;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.model.data.game.GameProvider;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.model.data.score.ScoreProvider;
import ru.sidey383.task2.model.data.settings.SettingsProvider;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.game.ClickType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataControllerTest {

    @RegisterExtension
    public static final CustomFileSystem fileSystem = new CustomFileSystem();

    private static final String settingsPath = "/settings.json";

    private static final String scorePath = "/scores";

    @BeforeAll
    public static void copy() throws IOException {
        Path copyPath = fileSystem.getRoot().resolve("test");
        Files.createDirectories(copyPath);
        Path scoreOutPath = copyPath.resolve("scores");
        Path settingsOutPath = copyPath.resolve("settings");
        try (InputStream is = DataControllerTest.class.getResourceAsStream(scorePath)) {
            assert is != null;
            Files.copy(is, scoreOutPath);
        }
        try (InputStream is = DataControllerTest.class.getResourceAsStream(settingsPath)) {
            assert is != null;
            Files.copy(is, settingsOutPath);
        }
    }

    @Test
    @Order(0)
    public void createTest() throws ModelException, IOException {
        Path f = fileSystem.getRoot().resolve("createTest");
        Files.createDirectories(f);
        DataController controller = DataController.createController(f);
        SettingsProvider settingsProvider = controller.settingProvider();
        GameProvider gameProvider = controller.gameProvider();
        ScoreProvider scoreProvider = controller.scoreProvider();
        assertNotNull(settingsProvider, "New settings provider");
        assertNotNull(gameProvider, "New game provider");
        assertNotNull(scoreProvider, "New score provider");
        Map<ClickType, Integer> keys = settingsProvider.getGameKeys();
        for (ClickType type : ClickType.values())
            assertNotNull(keys.get(type), "Expect not null for new settings provider");
        Path gameDir = settingsProvider.getGamesDir();
        assertEquals(f.resolve("games").toAbsolutePath(), gameDir, "Expect correct games dir for new settings provider");
        assertEquals(0, scoreProvider.getScores().size(), "Expect void scores for new score provider");
        Collection<GameDescription> gameDescriptions = gameProvider.readGameDescriptions();
        assertEquals(1, gameDescriptions.size(), "Expect one game for new game description");
    }

    @Test
    @Order(1)
    public void loadTest() throws ModelException {
        Path copyPath = fileSystem.getRoot().resolve("test");
        DataController controller = DataController.createController(copyPath);
        SettingsProvider settingsProvider = controller.settingProvider();
        GameProvider gameProvider = controller.gameProvider();
        ScoreProvider scoreProvider = controller.scoreProvider();
        assertNotNull(settingsProvider, "New settings provider");
        assertNotNull(gameProvider, "New game provider");
        assertNotNull(scoreProvider, "New score provider");
        Map<ClickType, Integer> keys = settingsProvider.getGameKeys();
        for (int i = 0; i < ClickType.values().length; i++)
            assertEquals(i+1, keys.get(ClickType.values()[i]), "Load value from /settings/settings1.json");
        Path gameDir = settingsProvider.getGamesDir();
        assertEquals(Path.of("testGames").toAbsolutePath(), gameDir, "Expect correct games dir for new settings provider");
        assertEquals(1, scoreProvider.getScores().size(), "Load value from /scores");
        GameScore score = scoreProvider.getScores().iterator().next();
        assertEquals(224 ,score.maxScore(), "Load value from /scores");
        assertEquals("ExampleGame_603c9c19f99c451f06e971b2b80de518" ,score.gameKey(), "Load value from /scores");
        assertEquals("ExampleGame" ,score.name(), "Load value from /scores");
    }

}
