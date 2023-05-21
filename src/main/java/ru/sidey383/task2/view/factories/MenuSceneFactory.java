package ru.sidey383.task2.view.factories;

import ru.sidey383.task2.view.SceneFactory;
import ru.sidey383.task2.view.menu.MenuAppScene;

import java.net.URL;


public class MenuSceneFactory extends SceneFactory<MenuAppScene> {

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/MainScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new MenuAppScene();
    }
}
