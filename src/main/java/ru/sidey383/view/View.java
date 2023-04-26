package ru.sidey383.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.factories.GameChoiceSceneFactory;
import ru.sidey383.view.events.WindowCloseEvent;
import ru.sidey383.view.events.PlayerKeyEvent;
import ru.sidey383.view.factories.GameSceneFactory;
import ru.sidey383.view.factories.MenuSceneFactory;
import ru.sidey383.view.factories.ScoreSceneFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class View implements ViewInterface {

    private final Stage stage;

    private final Object dialogSynchronizer = new Object();

    private Stage dialog = null;

    List<SceneFactory<? extends AppScene>> sceneFactories = List.of(
            new MenuSceneFactory(),
            new GameChoiceSceneFactory(),
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
    public void setScene(AppScene controller) {
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
    public <T extends AppScene> T getScene(Class<T> clazz) throws IOException {
        for (SceneFactory<? extends AppScene> factory : sceneFactories) {
            Class<?> factoryClazz = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (clazz.isAssignableFrom(factoryClazz)) {
                return (T) factory.createScene();
            }
        }
        return null;
    }

    @Override
    public void showException(String message, Exception e) {
        Platform.runLater(() -> {
            Stage dialogStage = new Stage();
            dialogStage.getIcons().addAll(stage.getIcons());
            dialogStage.initModality(Modality.NONE);
            dialogStage.setResizable(false);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(os));
            Button button = new Button("Ok.");
            button.setOnAction((a) -> {

                synchronized (dialogSynchronizer) {
                    dialogStage.close();
                    if (dialog == dialogStage) {
                        dialog = null;
                    }
                }
            });
            VBox vbox = new VBox(new Text((message == null ? "" : message + ": ") + os), button);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(15));

            dialogStage.setScene(new Scene(vbox));
            dialogStage.show();
            synchronized (dialogSynchronizer) {
                if (dialog != null)
                    dialog.close();
                dialog = dialogStage;
            }


        });
    }

    @Override
    public void showException(Exception e) {
        showException(null, e);
    }

    @Override
    public void close() {
        Platform.runLater(() -> {
            if (dialog != null)
                dialog.close();
            stage.close();
        });
    }

}
