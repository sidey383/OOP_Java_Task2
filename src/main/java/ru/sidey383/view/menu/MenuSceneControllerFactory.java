package ru.sidey383.view.menu;

import ru.sidey383.view.SceneController;
import ru.sidey383.view.SceneControllerFactory;

import java.net.URL;


public class MenuSceneControllerFactory extends SceneControllerFactory<MenuSceneController> {

    @Override
    public boolean canProduceType(Class<? extends SceneController> clazz) {
        return clazz.isAssignableFrom(MenuSceneController.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/MainScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new MenuSceneController();
    }
}
