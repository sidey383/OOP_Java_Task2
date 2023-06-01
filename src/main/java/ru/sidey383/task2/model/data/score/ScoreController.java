package ru.sidey383.task2.model.data.score;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.model.data.game.GameDescription;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScoreController implements ScoreProvider {

    private static final Logger logger = LogManager.getLogger(ScoreController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final JavaType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, GameScore.class);

    private Map<String, GameScore> scoreList = null;

    private final Path scorePath;

    public ScoreController(Path scorePath) {
        this.scorePath = scorePath;
    }

    @Override
    public Collection<GameScore> getScores() {
        if (scoreList == null) {
            readScores();
        }
        return scoreList.values();
    }

    @Override
    public void addScore(GameDescription description, long score) {
        if (scoreList == null) {
            readScores();
        }
        if (!scoreList.containsKey(description.gameKey()) || scoreList.get(description.gameKey()).maxScore() < score) {
            scoreList.put(description.gameKey(), new GameScore(description.gameKey(), description.name(), score));
            try {
                writeScores();
            } catch (IOException e) {
                logger.error(() -> String.format("Score write error %s", scorePath), e);
            }
        }
    }

    private void readScores() {
        if (!Files.exists(scorePath)) {
            scoreList = new HashMap<>();
            return;
        }
        try (InputStream is = Files.newInputStream(scorePath)) {
            List<GameScore> scores = mapper.reader().forType(listType).readValue(is);
            this.scoreList = scores.stream()
                    .collect(
                            Collectors.toMap(
                                    GameScore::gameKey,
                                    Function.identity(),
                                    (r1, r2) -> r1,
                                    HashMap::new)
                    );
        } catch (IOException e) {
            logger.warn("Score read error", e);
            scoreList = new HashMap<>();
        }
    }

    private void writeScores() throws IOException {
        if (!Files.exists(scorePath))
            Files.createFile(scorePath);
        try (OutputStream os = Files.newOutputStream(scorePath)) {
            mapper.writer().forType(listType).writeValue(os, new ArrayList<>(scoreList.values()));
        }
    }

}
