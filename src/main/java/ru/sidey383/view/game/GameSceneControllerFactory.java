package ru.sidey383.view.game;

import ru.sidey383.view.SceneControllerFactory;

import java.net.URL;

public class GameSceneControllerFactory extends SceneControllerFactory<GameSceneController> {
    @Override
    protected URL getFXMLPath() {
        return null;
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return null;
    }
}
