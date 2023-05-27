package ru.sidey383.task2.model.data.score;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.sidey383.task2.model.CustomFileSystem;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.model.exception.ModelException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScoreControllerTest {

    @RegisterExtension
    public static final CustomFileSystem fileSystem = new CustomFileSystem();

    @Test
    @Order(0)
    public void initTest() throws ModelException {
        ScoreController scoreController = ScoreController.createScoreContainer(fileSystem.getRoot().resolve("initTest"));
        assertIterableEquals(scoreController.getScores(), Collections.emptyList());
    }

    private GameDescription createDescription(String val) {
        return new GameDescription() {
            @Override
            public String gameKey() {
                return "key"+val;
            }

            @Override
            public String name() {
                return val;
            }

            @Override
            public String hash() {
                return null;
            }

            @Override
            public Path gameContainer() {
                return null;
            }
        };
    }

    @Test
    @Order(1)
    public void singleScoreTest() throws ModelException {
        ScoreController scoreController = ScoreController.createScoreContainer(fileSystem.getRoot().resolve("singleScoreTest"));
        for (int i = 0; i < 100; i++) {
            scoreController.addScore(createDescription("Foo"), i*1000L);
            Collection<GameScore> scores = scoreController.getScores();
            assertEquals(1, scores.size(), "Wrong score record count " + i);
            assertEquals(i * 1000L, scores.iterator().next().maxScore(), "Wrong score " + i );
            assertEquals("Foo", scores.iterator().next().name(), "Wrong name " + i);
            assertEquals("keyFoo", scores.iterator().next().gameKey(), "Wrong game key " + i);
        }
    }

    @Test
    @Order(2)
    public void someGameTestTest() throws ModelException {
        ScoreController scoreController = ScoreController.createScoreContainer(fileSystem.getRoot().resolve("someGameTestTest"));
        for (int i = 1; i <= 100; i++) {
            scoreController.addScore(createDescription("Foo"+i), i*1000L);
            Collection<GameScore> scores = scoreController.getScores();
            assertEquals(i, scores.size(), "Wrong score record count " + i);
            List<GameScore> sorted = scores.stream().sorted(Comparator.comparing(GameScore::maxScore)).toList();
            for (int j = 0; j < i; j++) {
                assertEquals(j * 1000L + 1000L, sorted.get(j).maxScore(), "Wrong score at iteration " + i + " for game number" + j);
                assertEquals("Foo" + (j + 1), sorted.get(j).name(), "Wrong name at iteration " + i + " for game number" + j);
                assertEquals("keyFoo" + (j + 1), sorted.get(j).gameKey(), "Wrong game key at iteration " + i + " for game number" + j);
            }
        }
    }

    @Test
    @Order(3)
    public void multiGameChangeScoreTest() throws ModelException {
        ScoreController scoreController = ScoreController.createScoreContainer(fileSystem.getRoot().resolve("multiGameChangeScoreTest"));
        for (int i = 1; i <= 100; i++) {
            scoreController.addScore(createDescription("Foo"+i), i*1000L);
        }

        // Change on highest score
        for (int i = 1; i <= 100; i++) {
            scoreController.addScore(createDescription("Foo"+i), i*1001L);
            Collection<GameScore> scores = scoreController.getScores();
            assertEquals(100, scores.size(), "Wrong score record count at iteration " + i);
            List<GameScore> sorted = scores.stream().sorted(Comparator.comparing(GameScore::maxScore)).toList();
            for (int j = 0; j < i; j++) {
                assertEquals(j * 1001L + 1001L, sorted.get(j).maxScore(), "Wrong score at iteration " + i + " for game " + j);
                assertEquals("Foo" + (j + 1), sorted.get(j).name(), "Wrong name at iteration " + i + " for game " + j);
                assertEquals("keyFoo" + (j + 1), sorted.get(j).gameKey(), "Wrong game key at iteration " + i + " for game " + j);
            }
            for (int j = i; j < 100; j++) {
                assertEquals(j * 1000L + 1000L, sorted.get(j).maxScore(), "Wrong score at iteration " + i + " for game " + j);
                assertEquals("Foo" + (j + 1), sorted.get(j).name(), "Wrong name at iteration " + i + " for game " + j);
                assertEquals("keyFoo" + (j + 1), sorted.get(j).gameKey(), "Wrong game key at iteration " + i + " for game " + j);
            }
        }

        // Don't change on lower score
        for (int i = 1; i <= 100; i++) {
            scoreController.addScore(createDescription("Foo"+i), i*990L);
            Collection<GameScore> scores = scoreController.getScores();
            assertEquals(100, scores.size(), "Wrong score record count " + i);
            List<GameScore> sorted = scores.stream().sorted(Comparator.comparing(GameScore::maxScore)).toList();
            for (int j = 0; j < 100; j++) {
                assertEquals(j * 1001L + 1001L, sorted.get(j).maxScore(), "Wrong score at iteration " + i + " for game " + j);
                assertEquals("Foo" + (j + 1), sorted.get(j).name(), "Wrong name at iteration " + i + " for game " + j);
                assertEquals("keyFoo" + (j + 1), sorted.get(j).gameKey(), "Wrong game key at iteration " + i + " for game " + j);
            }
        }
    }

}
