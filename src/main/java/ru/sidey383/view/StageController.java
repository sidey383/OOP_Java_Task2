package ru.sidey383.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class StageController {

    private final Stage stage;

    private final Scene mainScene;
    private final Scene gameScene;

    public StageController(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        primaryStage.setTitle("piano tile game");
        primaryStage.getIcons().add(new Image("/icon.png"));
        mainScene = loadScene("/fxml/MainScene.fxml");
        gameScene = loadScene("/fxml/GameScene.fxml");
        gameScene.getRoot().requestFocus();
        mainScene.getRoot().requestFocus();
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private Scene loadScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(this::controllerFactory);
        return new Scene(loader.load());
    }

    public Stage getStage() {
        return stage;
    }


    public Scene getMainScene() {
        return mainScene;
    }

    public Scene getGameStartScene() {
        return gameScene;
    }

    private Object controllerFactory(Class<?> clazz) {
        System.out.println(clazz.getSimpleName());
        if (RootNode.class.isAssignableFrom(clazz)) {
            try {
                System.out.println("create "+ clazz.getSimpleName());
                return clazz.getConstructor(StageController.class).newInstance(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
