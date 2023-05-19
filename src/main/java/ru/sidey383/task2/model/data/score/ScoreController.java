package ru.sidey383.task2.model.data.score;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.exception.ModelIOException;
import ru.sidey383.task2.model.exception.NotRegularFileException;

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

    private final Map<String, GameScore> scoreList;

    private final Path path;

    private ScoreController(List<GameScore> scoreList, Path path) {
        this.scoreList = scoreList.stream()
                .collect(
                        Collectors.toMap(
                                GameScore::gameKey,
                                Function.identity())
                );
        this.path = path;
    }

    public static ScoreController createScoreContainer(Path path) throws ModelException {
        ScoreController scoreController;
        if (Files.exists(path)) {
            if (Files.isRegularFile(path)) {
                try (InputStream is = Files.newInputStream(path)) {
                    List<GameScore> scores = mapper.reader().forType(listType).readValue(is);
                    scoreController = new ScoreController(scores, path);
                } catch (IOException e) {
                    throw new ModelIOException(e);
                }
            } else {
                throw new NotRegularFileException(path);
            }
        } else {
            try {
                return createNew(path);
            } catch (IOException e) {
                throw new ModelIOException(e);
            }
        }
        return scoreController;
    }

    @Override
    public Collection<GameScore> getScores() {
        return scoreList.values();
    }

    @Override
    public void addScore(GameDescription description, long score) {
        if (!scoreList.containsKey(description.getGameKey()) || scoreList.get(description.getGameKey()).maxScore() < score) {
            scoreList.put(description.getGameKey(), new GameScore(description.getGameKey(), description.getName(), score));
            try {
                write();
            } catch (IOException e) {
                logger.error(() -> String.format("Score write error %s", path), e);
            }
        }
    }

    private static ScoreController createNew(Path path) throws IOException {
        if (Files.exists(path))
            Files.delete(path);
        ArrayList<GameScore> scores = new ArrayList<>();
        ScoreController scoreController = new ScoreController(scores, path);
        scoreController.write();
        return scoreController;
    }

    private void write() throws IOException {
        try (OutputStream os = Files.newOutputStream(path)) {
            mapper.writer().forType(listType).writeValue(os, new ArrayList<>(scoreList.values()));
        }
    }

}
