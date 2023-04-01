package ru.sidey383.view;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.choose.GameChooseSceneControllerFactory;
import ru.sidey383.view.events.GameKeyEvent;
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
        stage.setFullScreenExitHint("");
        stage.setMaximized(true);
        stage.setTitle("Piano tiles");
        stage.getIcons().add(new Image("/icon.png"));
        EventManager.manager.registerListener(this);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void setScene(SceneController controller) {
        stage.setScene(controller.getScene());
        stage.show();
    }

    @EventHandler
    public void onKeyPress(GameKeyEvent keyEvent) {
        System.out.println("key pressed");
        if (keyEvent.getKeyCode() == KeyCode.F11) {
            stage.setFullScreen(!stage.fullScreenProperty().get());
        }
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
