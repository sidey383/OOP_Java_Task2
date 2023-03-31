package ru.sidey383.view;

import javafx.stage.Stage;
import ru.sidey383.view.choose.GameChooseSceneControllerFactory;
import ru.sidey383.view.game.GameSceneControllerFactory;
import ru.sidey383.view.menu.MenuSceneControllerFactory;
import ru.sidey383.view.score.ScoreSceneControllerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class View implements ViewInterface {

    private final Stage stage;

    List<SceneControllerFactory<? extends SceneController>> sceneFactories = List.of(
            new MenuSceneControllerFactory(),
            new GameChooseSceneControllerFactory(),
            new ScoreSceneControllerFactory(),
            new GameSceneControllerFactory()
    );

    public View(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void setScene(SceneController controller) {
        stage.setScene(controller.getScene());
    }


    @SuppressWarnings("unchecked")
    public <T extends SceneController> SceneControllerFactory<? extends T> getFactory(Class<T> clazz) {
        for (SceneControllerFactory<? extends SceneController> factory : sceneFactories) {
            Class<?> factoryClazz = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (clazz.isAssignableFrom(factoryClazz)) {
                return (SceneControllerFactory<? extends T>) factory;
            }
        }
        return null;
    }
}
