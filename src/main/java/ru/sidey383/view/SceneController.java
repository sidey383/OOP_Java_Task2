package ru.sidey383.view;

import javafx.scene.Scene;

public abstract class SceneController {

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    void setScene(Scene scene) {
        this.scene = scene;
    }

}
