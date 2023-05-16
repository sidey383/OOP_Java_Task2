package ru.sidey383.model.data.score;

import ru.sidey383.model.data.game.GameDescription;

import java.util.Collection;

public interface ScoreProvider {

    public Collection<GameScore> getScores();

    public void addScore(GameDescription description, long score);

}
