package ru.sidey383.task2.view;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.events.PlayerKeyEvent;

public abstract class AppScene {

    @FXML
    private Parent root;

    public Parent content() {
        return root;
    }

        @FXML
        public void onKeyPress(KeyEvent keyEvent) {
            EventManager.runEvent(new PlayerKeyEvent(true, keyEvent.getCode()));
        }

        @FXML
        public void onKeyRelease(KeyEvent keyEvent) {
        }

}
