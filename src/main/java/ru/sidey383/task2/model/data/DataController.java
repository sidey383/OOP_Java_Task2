package ru.sidey383.task2.model.data;

import ru.sidey383.task2.model.data.game.GameContainer;
import ru.sidey383.task2.model.data.game.GameProvider;
import ru.sidey383.task2.model.data.score.ScoreContainer;
import ru.sidey383.task2.model.data.score.ScoreProvider;
import ru.sidey383.task2.model.data.settings.SettingsController;
import ru.sidey383.task2.model.data.settings.SettingsProvider;
import ru.sidey383.task2.model.exception.ModelException;

import java.nio.file.Path;

public class DataController implements DataProvider {

    private final GameProvider gameProvider;
    private final ScoreProvider scoreProvider;
    private final SettingsProvider settingsProvider;

    private DataController(GameProvider gameProvider, ScoreProvider scoreProvider, SettingsProvider settingsProvider) {
        this.gameProvider = gameProvider;
        this.scoreProvider = scoreProvider;
        this.settingsProvider = settingsProvider;
    }

    public static DataController createController(Path mainPath) throws ModelException {
        return createController(mainPath.resolve("scores"), mainPath.resolve("settings"));
    }

    public static DataController createController(Path scores, Path settings) throws ModelException {
        SettingsProvider settingsProvider = SettingsController.createSettingsController(settings);
        ScoreProvider scoreProvider = ScoreContainer.createScoreContainer(scores);
        GameProvider gameProvider = new GameContainer(settingsProvider);
        return new DataController(gameProvider, scoreProvider, settingsProvider);
    }

    public GameProvider getGameProvider() {
        return gameProvider;
    }

    public ScoreProvider getScoreProvider() {
        return scoreProvider;
    }

    public SettingsProvider getSettingProvider() {
        return settingsProvider;
    }

}
