package ru.sidey383.view;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.events.PlayerKeyEvent;

public abstract class Scene {

    private javafx.scene.Scene scene;

    public javafx.scene.Scene getScene() {
        return scene;
    }

    void setScene(javafx.scene.Scene scene) {
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
