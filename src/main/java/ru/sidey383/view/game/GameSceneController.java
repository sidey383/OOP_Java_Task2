package ru.sidey383.view.game;


import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import ru.sidey383.control.TimeAdapter;
import ru.sidey383.event.EventManager;
import ru.sidey383.model.game.tile.Tile;
import ru.sidey383.view.events.GameKeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController extends GameView  implements Initializable{

    @FXML
    public Label score;

    @FXML
    private GameCanvas gameField;

    private GraphicsContext graphicsContext;

    @FXML
    private ImageView background;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            gameField.update(now);
        }
    };


    @FXML
    public void onKeyPress(KeyEvent keyEvent) {
        EventManager.manager.runEvent(new GameKeyEvent(true, keyEvent.getCode()));
    }

    @FXML
    public void onKeyRelease(KeyEvent keyEvent) {
        EventManager.manager.runEvent(new GameKeyEvent(false, keyEvent.getCode()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            background.setImage(new Image("/background.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        graphicsContext = gameField.getGraphicsContext2D();
    }

    public void sendTileInfo() {

    }

    @Override
    public void updateTiles(Tile[][] nTiles) {
        gameField.updateTiles(nTiles);
    }

    @Override
    public void setTimeAdapter(TimeAdapter adapter) {
        gameField.setTimeAdapter(adapter);
    }

    @Override
    public void startRender() {
        timer.start();
    }

    @Override
    public void stopRender() {
        timer.stop();
    }

    @Override
    public void showScore() {
        score.setText("SCORE: 222002");
        score.setVisible(true);
    }
}
