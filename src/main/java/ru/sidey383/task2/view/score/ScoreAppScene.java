package ru.sidey383.task2.view.score;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.events.PlayerOpenMenuEvent;
import ru.sidey383.task2.view.score.component.ScoreBox;

import java.util.Collection;

public class ScoreAppScene extends ScoreView {

    public VBox scrollBox;

    public void setGameScores(Collection<ScoreUnit> scores) {
        Platform.runLater(() -> scrollBox.getChildren().addAll(scores.stream().map(ScoreBox::new).toList()));
    }

    @FXML
    public void pressMenu() {
        EventManager.runEvent(new PlayerOpenMenuEvent());
    }

}
