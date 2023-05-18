package ru.sidey383.task2.model.data;

import ru.sidey383.task2.model.data.game.GameProvider;
import ru.sidey383.task2.model.data.score.ScoreProvider;
import ru.sidey383.task2.model.data.settings.SettingsProvider;

public interface DataProvider {

    public GameProvider getGameProvider();

    public ScoreProvider getScoreProvider();

    public SettingsProvider getSettingProvider();

}
