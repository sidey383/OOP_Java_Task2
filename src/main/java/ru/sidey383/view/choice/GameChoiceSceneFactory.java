package ru.sidey383.view.choice;

import ru.sidey383.view.AppScene;
import ru.sidey383.view.SceneFactory;
import ru.sidey383.view.menu.MenuAppScene;

import java.net.URL;

public class GameChoiceSceneFactory extends SceneFactory<GameChoiceAppScene> {

    @Override
    public boolean canProduceType(Class<? extends AppScene> clazz) {
        return clazz.isAssignableFrom(MenuAppScene.class);
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
