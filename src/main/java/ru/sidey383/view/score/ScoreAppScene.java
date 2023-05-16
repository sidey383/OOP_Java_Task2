package ru.sidey383.view.score;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.sidey383.control.AvailableScene;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.events.PlayerChangeSceneEvent;
import ru.sidey383.view.score.component.ScoreBox;

import java.util.Collection;

public class ScoreAppScene extends ScoreView {

    public VBox scrollBox;

    public void setGameChoice(Collection<ScoreUnit> scores) {
        Platform.runLater(() -> scrollBox.getChildren().addAll(scores.stream().map(ScoreBox::new).toList()));
    }

    @FXML
    public void pressMenu() {
        EventManager.manager.runEvent(new PlayerChangeSceneEvent(AvailableScene.MENU));
    }

}
