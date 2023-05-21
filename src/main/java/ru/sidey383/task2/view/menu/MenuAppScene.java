package ru.sidey383.task2.view.menu;

import javafx.fxml.FXML;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.events.PlayerOpenGameChooseEvent;
import ru.sidey383.task2.view.events.PlayerOpenScoreEvent;
import ru.sidey383.task2.view.events.menu.PlayerGameExitEvent;

public class MenuAppScene extends MenuView {

    @FXML
    public void startAction() {
        EventManager.runEvent(new PlayerOpenGameChooseEvent());
    }

    @FXML
    public void scoreAction() {
        EventManager.runEvent(new PlayerOpenScoreEvent());
    }

    @FXML
    public void exitAction() {
        EventManager.runEvent(new PlayerGameExitEvent());
    }
}
