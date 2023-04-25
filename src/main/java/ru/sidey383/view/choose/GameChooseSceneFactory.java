package ru.sidey383.view.choose;

import ru.sidey383.view.Scene;
import ru.sidey383.view.SceneFactory;
import ru.sidey383.view.menu.MenuScene;

import java.net.URL;

public class GameChooseSceneFactory extends SceneFactory<GameChooseScene> {

    @Override
    public boolean canProduceType(Class<? extends Scene> clazz) {
        return clazz.isAssignableFrom(MenuScene.class);
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
