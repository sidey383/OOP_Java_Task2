package ru.sidey383.task2.view.factories;

import ru.sidey383.task2.view.SceneFactory;
import ru.sidey383.task2.view.game.GameAppScene;
import ru.sidey383.task2.view.AppScene;

import java.net.URL;

public class GameSceneFactory extends SceneFactory<GameAppScene> {

    @Override
    public boolean canProduceType(Class<? extends AppScene> clazz) {
        return clazz.isAssignableFrom(GameAppScene.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/GameScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new GameAppScene();
    }
}
