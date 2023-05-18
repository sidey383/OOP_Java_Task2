package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.view.score.ScoreUnit;
import ru.sidey383.task2.view.score.ScoreView;

import java.util.Collection;
import java.util.stream.Collectors;

public class ScoreSession extends ControllerSession {

    private final ScoreView view;

    public ScoreSession(Controller controller, ScoreView view, Collection<GameScore> scores) {
        super(controller);
        view.setGameChoice(scores.stream().map(gameScore ->
            new ScoreUnit() {

                @Override
                public String getGameName() {
                    return gameScore.name();
                }

                @Override
                public long getScore() {
                    return gameScore.maxScore();
                }
            }
        ).collect(Collectors.toList()));
        this.view = view;
    }

}
