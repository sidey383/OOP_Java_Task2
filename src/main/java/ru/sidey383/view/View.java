package ru.sidey383.view;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.choose.GameChooseSceneFactory;
import ru.sidey383.view.events.WindowCloseEvent;
import ru.sidey383.view.events.PlayerKeyEvent;
import ru.sidey383.view.game.GameSceneFactory;
import ru.sidey383.view.menu.MenuSceneFactory;
import ru.sidey383.view.score.ScoreSceneFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class View implements ViewInterface {

    private final Stage stage;

    List<SceneFactory<? extends Scene>> sceneFactories = List.of(
            new MenuSceneFactory(),
            new GameChooseSceneFactory(),
            new ScoreSceneFactory(),
            new GameSceneFactory()
    );

    public View(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(this::windowsClose);
        stage.setFullScreenExitHint("");
        stage.setResizable(true);
        stage.setTitle("Piano tiles");
        stage.getIcons().add(new Image("/icon.png"));
        EventManager.manager.registerListener(this);
    }

    private void windowsClose(WindowEvent windowEvent) {
        EventManager.manager.runEvent(new WindowCloseEvent());
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void setScene(Scene controller) {
        Platform.runLater(() -> {
            boolean fullScreen = stage.isFullScreen();
            stage.hide();
            stage.setScene(controller.getScene());
            stage.show();
            stage.setFullScreen(fullScreen);
        });

    }

    @EventHandler
    public void onKeyPress(PlayerKeyEvent keyEvent) {
        if (keyEvent.isPress() && keyEvent.getKeyCode() == KeyCode.F11) {
            stage.setFullScreen(!stage.fullScreenProperty().get());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Scene> T getScene(Class<T> clazz) throws IOException {
        for (SceneFactory<? extends Scene> factory : sceneFactories) {
            Class<?> factoryClazz = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (clazz.isAssignableFrom(factoryClazz)) {
                return (T) factory.createScene();
            }
        }
        return null;
    }

    @Override
    public void close() {
        Platform.runLater(stage::close);
    }
}
