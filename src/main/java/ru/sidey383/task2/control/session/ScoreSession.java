package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.view.AppScene;
import ru.sidey383.task2.view.score.ScoreUnit;
import ru.sidey383.task2.view.score.ScoreView;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class ScoreSession extends ControllerSession {

    private final ScoreView choiceView;

    public ScoreSession(Controller controller, ScoreView choiceView) {
        super(controller);
        this.choiceView = choiceView;
    }

    public static ScoreSession create(Controller controller) throws IOException {
        ScoreView choiceView = controller.getView().getScene(ScoreView.class);
        ScoreSession scoreSession = new ScoreSession(controller, choiceView);
        scoreSession.setScores(controller.getModel().getScores());
        return scoreSession;
    }

    public void setScores(Collection<GameScore> scores) {
        choiceView.setGameScores(scores.stream()
                .map(ViewScore::new)
                .collect(Collectors.toList()));
    }

    @Override
    public AppScene getScene() {
        return choiceView;
    }

    private record ViewScore(GameScore gameScore) implements ScoreUnit {

        @Override
            public String getGameName() {
                return gameScore.name();
            }

            @Override
            public long getScore() {
                return gameScore.maxScore();
            }
        }

}
