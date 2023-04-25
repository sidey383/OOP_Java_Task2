package ru.sidey383.view.game;

import ru.sidey383.view.Scene;
import ru.sidey383.view.SceneFactory;

import java.net.URL;

public class GameSceneFactory extends SceneFactory<GameScene> {

    @Override
    public boolean canProduceType(Class<? extends Scene> clazz) {
        return clazz.isAssignableFrom(GameScene.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/GameScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new GameScene();
    }
}
