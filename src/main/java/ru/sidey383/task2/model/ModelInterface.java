package ru.sidey383.task2.model;

import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.model.data.settings.SettingsProvider;
import ru.sidey383.task2.model.exception.ModelException;

import java.util.Collection;

public interface ModelInterface {

    void startGame(GameDescription gameDescription) throws ModelException;

    Collection<GameDescription> getGameDescriptions();

    Collection<GameScore> getScores();

    SettingsProvider getSettings();

}
