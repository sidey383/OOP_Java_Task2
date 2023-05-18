package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.view.score.ScoreUnit;
import ru.sidey383.task2.view.score.ScoreView;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class ScoreSession extends ControllerSession {

    public ScoreSession(Controller controller) {
        super(controller);
    }

    public static ScoreSession create(Controller controller) throws IOException {
        ScoreView choiceView = controller.getView().getScene(ScoreView.class);
        controller.getView().setScene(choiceView);
        choiceView.setGameChoice(controller.getModel().getScores().stream()
                .map(gameScore ->
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
                )
                .collect(Collectors.toList()));
        return new ScoreSession(controller);
    }

}
