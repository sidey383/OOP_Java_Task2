package ru.sidey383.view.score;

import ru.sidey383.view.Scene;
import ru.sidey383.view.SceneFactory;

import java.net.URL;

public class ScoreSceneFactory extends SceneFactory<ScoreScene> {

    @Override
    public boolean canProduceType(Class<? extends Scene> clazz) {
        return clazz.isAssignableFrom(ScoreSceneFactory.class);
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
