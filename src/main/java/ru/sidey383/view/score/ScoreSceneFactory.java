package ru.sidey383.view.score;

import ru.sidey383.view.AppScene;
import ru.sidey383.view.SceneFactory;

import java.net.URL;

public class ScoreSceneFactory extends SceneFactory<ScoreAppScene> {

    @Override
    public boolean canProduceType(Class<? extends AppScene> clazz) {
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
