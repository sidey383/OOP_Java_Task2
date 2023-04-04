package ru.sidey383.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.events.menu.PlayerGameExitEvent;
import ru.sidey383.view.events.menu.PlayerGameScoreEvent;
import ru.sidey383.view.events.menu.PlayerMenuGameStartEvent;

public class MenuSceneController extends MenuView {

    public Label mainTitle;

    @FXML
    private Button startButton;

    @FXML
    private Button scoreButton;

    @FXML
    private Button exitButton;

    public void startAction(ActionEvent actionEvent) {
        EventManager.manager.runEvent(new PlayerMenuGameStartEvent());
    }

    public void scoreAction(ActionEvent actionEvent) {
        EventManager.manager.runEvent(new PlayerGameScoreEvent());
    }

    public void exitAction(ActionEvent actionEvent) {
        EventManager.manager.runEvent(new PlayerGameExitEvent());
    }
}
