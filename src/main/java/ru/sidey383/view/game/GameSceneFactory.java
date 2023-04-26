package ru.sidey383.view.game;

import ru.sidey383.view.AppScene;
import ru.sidey383.view.SceneFactory;

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
