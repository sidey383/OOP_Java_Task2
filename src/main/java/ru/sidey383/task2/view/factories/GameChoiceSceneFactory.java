package ru.sidey383.task2.view.factories;

import ru.sidey383.task2.view.menu.MenuAppScene;
import ru.sidey383.task2.view.AppScene;
import ru.sidey383.task2.view.SceneFactory;
import ru.sidey383.task2.view.choice.GameChoiceAppScene;

import java.net.URL;

public class GameChoiceSceneFactory extends SceneFactory<GameChoiceAppScene> {

    @Override
    public boolean canProduceType(Class<? extends AppScene> clazz) {
        return clazz.isAssignableFrom(MenuAppScene.class);
    }

    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/ChoiceScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new GameChoiceAppScene();
    }
}
