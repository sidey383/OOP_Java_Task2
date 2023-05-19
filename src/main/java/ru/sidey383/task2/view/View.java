package ru.sidey383.task2.view;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.events.PlayerKeyEvent;
import ru.sidey383.task2.view.events.WindowCloseEvent;
import ru.sidey383.task2.view.factories.GameChoiceSceneFactory;
import ru.sidey383.task2.view.factories.GameSceneFactory;
import ru.sidey383.task2.view.factories.MenuSceneFactory;
import ru.sidey383.task2.view.factories.ScoreSceneFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class View implements ViewInterface {

    private final Stage stage;

    List<SceneFactory<? extends AppScene>> sceneFactories = List.of(
            new MenuSceneFactory(),
            new GameChoiceSceneFactory(),
            new ScoreSceneFactory(),
            new GameSceneFactory()
    );

    public View(Stage stage) {
        this.stage = stage;
        this.stage.setMaximized(true);
        stage.setOnCloseRequest(this::windowsClose);
        stage.setFullScreenExitHint("");
        stage.setResizable(true);
        stage.setTitle("Piano tiles");
        stage.getIcons().add(new Image("/icon.png"));
        EventManager.registerListener(this);
    }

    private void windowsClose(WindowEvent windowEvent) {
        EventManager.runEvent(new WindowCloseEvent());
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void setScene(AppScene controller) {
        Platform.runLater(() -> {
            boolean fullScreen = stage.isFullScreen();
            stage.setScene(controller.getScene());
            stage.setFullScreen(fullScreen);
            if (!stage.isShowing()) {
                stage.show();
            }
        });

    }

    @EventHandler
    public void onKeyPress(PlayerKeyEvent keyEvent) {
        if (keyEvent.isPress() && keyEvent.getKeyCode() == KeyCode.F11) {
            stage.setFullScreen(!stage.fullScreenProperty().get());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AppScene> T getScene(Class<T> clazz) throws IOException {
        for (SceneFactory<? extends AppScene> factory : sceneFactories) {
            Class<?> factoryClazz = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (clazz.isAssignableFrom(factoryClazz)) {
                if (stage.getScene() == null) {
                    return (T) factory.createScene(-1, -1);
                } else {
                    return (T) factory.createScene(stage.getScene().getWidth(), stage.getScene().getHeight());
                }
            }
        }
        return null;
    }

    @Override
    public void close() {
        Platform.runLater(stage::close);
    }

}
