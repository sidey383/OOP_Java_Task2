package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.view.AppScene;
import ru.sidey383.task2.view.score.ScoreUnit;
import ru.sidey383.task2.view.score.ScoreView;

import java.util.stream.Collectors;

public class ScoreSession extends ControllerSession {

    private final ScoreView choiceView;

    public ScoreSession(ScoreView choiceView) {
        this.choiceView = choiceView;
    }

    public void updateScores() {
        choiceView.setGameScores(
                controller().model().getScores()
                        .stream()
                        .map(ViewScore::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public AppScene scene() {
        return choiceView;
    }

    private record ViewScore(GameScore gameScore) implements ScoreUnit {

        @Override
        public String name() {
            return gameScore.name();
        }

        @Override
        public long score() {
            return gameScore.maxScore();
        }
    }

}
