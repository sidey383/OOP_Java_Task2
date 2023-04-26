package ru.sidey383.view.factories;

import ru.sidey383.view.AppScene;
import ru.sidey383.view.SceneFactory;
import ru.sidey383.view.menu.MenuAppScene;

import java.net.URL;


public class MenuSceneFactory extends SceneFactory<MenuAppScene> {

    @Override
    public boolean canProduceType(Class<? extends AppScene> clazz) {
        return clazz.isAssignableFrom(MenuAppScene.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/MainScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new MenuAppScene();
    }
}
