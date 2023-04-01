package ru.sidey383;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.View;
import ru.sidey383.view.game.GameView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View(primaryStage);
        SceneControllerFactory<? extends GameView> factory = view.getFactory(GameView.class);
        GameView gameView = factory.createScene();
        view.setScene(gameView);
        gameView.showScore();

    }
}