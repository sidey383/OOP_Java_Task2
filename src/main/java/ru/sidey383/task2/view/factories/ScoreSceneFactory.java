package ru.sidey383.task2.view.factories;

import ru.sidey383.task2.view.SceneFactory;
import ru.sidey383.task2.view.score.ScoreAppScene;

import java.net.URL;

public class ScoreSceneFactory extends SceneFactory<ScoreAppScene> {

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/ScoreScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new ScoreAppScene();
    }
}
