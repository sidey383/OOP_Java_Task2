package ru.sidey383.view.choose;

import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.menu.MenuSceneController;

import java.net.URL;

public class GameChooseSceneControllerFactory extends SceneControllerFactory<MenuSceneController> {

    @Override
    protected URL getFXMLPath() {
        return null;
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return null;
    }
}
