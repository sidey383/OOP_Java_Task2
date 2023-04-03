package ru.sidey383.model.score;

import java.util.HashMap;

public class ScoreContainer {

    HashMap<String, GameScore> scoreHashMap;

    public static ScoreContainer loadScore() {
        return new ScoreContainer();
    }

    public void addScore(String gameKey, String game, long score) {

    }

}
