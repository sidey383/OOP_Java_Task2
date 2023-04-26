package ru.sidey383.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.events.PlayerKeyEvent;

public abstract class AppScene {

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    public void onKeyPress(KeyEvent keyEvent) {
        EventManager.manager.runEvent(new PlayerKeyEvent(true, keyEvent.getCode()));
    }

    @FXML
    public void onKeyRelease(KeyEvent keyEvent) {
        EventManager.manager.runEvent(new PlayerKeyEvent(false, keyEvent.getCode()));
    }
}
