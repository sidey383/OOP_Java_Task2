package ru.sidey383.view.game;

import ru.sidey383.view.SceneController;
import ru.sidey383.view.SceneControllerFactory;

import java.net.URL;

public class GameSceneControllerFactory extends SceneControllerFactory<GameSceneController> {

    @Override
    public boolean canProduceType(Class<? extends SceneController> clazz) {
        return clazz.isAssignableFrom(GameSceneController.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/GameScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new GameSceneController();
    }
}
