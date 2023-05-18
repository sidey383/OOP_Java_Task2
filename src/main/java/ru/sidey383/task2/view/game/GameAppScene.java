package ru.sidey383.task2.view.game;


import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ru.sidey383.task2.control.AvailableScene;
import ru.sidey383.task2.control.TimeAdapter;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.view.events.PlayerChangeSceneEvent;
import ru.sidey383.task2.view.events.game.PlayerGameStopEvent;
import ru.sidey383.task2.view.events.game.PlayerPauseEvent;
import ru.sidey383.task2.view.events.game.PlayerResumeEvent;
import ru.sidey383.task2.view.game.component.GameCanvas;

import java.net.URL;
import java.util.ResourceBundle;

public class GameAppScene extends GameView  implements Initializable {

    @FXML
    public Label score;
    @FXML
    private GameCanvas gameField;
    @FXML
    private ImageView leftBackground;
    @FXML
    private ImageView centerBackground;
    @FXML
    private ImageView rightBackground;

    @FXML
    private Node scorePane;

    @FXML
    private Button menuButton;

    private MediaPlayer mediaPlayer;

    private TimeAdapter timeAdapter;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            gameField.update(now);
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Override
    public void updateTiles(Tile[][] nTiles) {
        gameField.updateTiles(nTiles);
    }

    @Override
    public void setTimeAdapter(TimeAdapter adapter) {
        this.timeAdapter = adapter;
        gameField.setTimeAdapter(adapter);
    }

    @Override
    public void start() {
        if (timeAdapter == null)
            throw new IllegalStateException("Time adapter must be initialized");
        if (mediaPlayer != null) {
            mediaPlayer.setStartTime(new Duration((double) (timeAdapter.getRelativeFromNano(System.nanoTime()) / 1_000_000L)));
            mediaPlayer.setVolume(1);
            mediaPlayer.play();
        }
        timer.start();
    }

    @Override
    public void stop() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        timer.stop();
    }

    @Override
    public void showScore(String scoreStr) {
        score.setText(scoreStr);
        menuButton.setDisable(false);
        menuButton.setVisible(true);
        scorePane.setVisible(true);
    }

    @Override
    public void setLeftImage(Image image) {
        leftBackground.setImage(image);
    }

    @Override
    public void setRightImage(Image image) {
        rightBackground.setImage(image);
    }

    @Override
    public void setCenterImage(Image image) {
        centerBackground.setImage(image);
    }

    @Override
    public void setMusic(Media sound) {
        mediaPlayer = new MediaPlayer(sound);
    }

    @FXML
    public void pressExit() {
        EventManager.manager.runEvent(new PlayerGameStopEvent());
    }

    @FXML
    public void pressPause() {
        EventManager.manager.runEvent(new PlayerPauseEvent());
    }

    @FXML
    public void pressResume() {
        EventManager.manager.runEvent(new PlayerResumeEvent());
    }

    @FXML
    public void toMenu() {
        EventManager.manager.runEvent(new PlayerChangeSceneEvent(AvailableScene.MENU));
    }

}
