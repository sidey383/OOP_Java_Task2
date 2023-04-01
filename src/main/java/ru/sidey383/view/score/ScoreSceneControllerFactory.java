package ru.sidey383.view.score;

import ru.sidey383.view.SceneController;
import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.menu.MenuSceneController;

import java.net.URL;

public class ScoreSceneControllerFactory extends SceneControllerFactory<ScoreSceneController> {

    @Override
    public boolean canProduceType(Class<? extends SceneController> clazz) {
        return clazz.isAssignableFrom(ScoreSceneControllerFactory.class);
    }

    @Override
    protected URL getFXMLPath() {
        return null;
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return null;
    }
}
