package ru.sidey383.view.menu;

import ru.sidey383.view.Scene;
import ru.sidey383.view.SceneFactory;

import java.net.URL;


public class MenuSceneFactory extends SceneFactory<MenuScene> {

    @Override
    public boolean canProduceType(Class<? extends Scene> clazz) {
        return clazz.isAssignableFrom(MenuScene.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/MainScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new MenuScene();
    }
}
