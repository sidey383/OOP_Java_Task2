package ru.sidey383.model;

import ru.sidey383.model.game.description.GameDescription;
import ru.sidey383.model.score.GameScore;
import ru.sidey383.model.settings.AppSettings;

import java.util.Collection;

public interface ModelInterface {

    void startGame(GameDescription gameDescription) throws Exception;

    Collection<GameDescription> getGameDescriptions();

    Collection<GameScore> getScores();

    AppSettings getSettings();

}
