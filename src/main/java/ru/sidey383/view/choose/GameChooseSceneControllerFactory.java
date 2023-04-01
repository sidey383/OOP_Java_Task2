package ru.sidey383.view.choose;

import ru.sidey383.view.SceneController;
import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.menu.MenuSceneController;

import java.net.URL;

public class GameChooseSceneControllerFactory extends SceneControllerFactory<GameChooseSceneController> {

    @Override
    public boolean canProduceType(Class<? extends SceneController> clazz) {
        return clazz.isAssignableFrom(MenuSceneController.class);
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
