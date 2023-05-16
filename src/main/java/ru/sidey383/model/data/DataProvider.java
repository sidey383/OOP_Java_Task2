package ru.sidey383.model.data;

import ru.sidey383.model.data.game.GameProvider;
import ru.sidey383.model.data.score.ScoreProvider;
import ru.sidey383.model.data.settings.SettingsProvider;
import ru.sidey383.model.exception.ModelException;

import java.nio.file.Path;

public interface DataProvider {

    public GameProvider getGameProvider();

    public ScoreProvider getScoreProvider();

    public SettingsProvider getSettingProvider();

    public static DataProvider createDataProvider(Path root) throws ModelException {
        return DataController.createController(root);
    }

}
