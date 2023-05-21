package ru.sidey383.task2.view.game;


import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.events.PlayerOpenMenuEvent;
import ru.sidey383.task2.view.events.game.PlayerGameStopEvent;
import ru.sidey383.task2.view.events.game.PlayerPauseEvent;
import ru.sidey383.task2.view.events.game.PlayerResumeEvent;
import ru.sidey383.task2.view.game.component.GameCanvas;

public class GameAppScene extends GameView {

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

    private TimeProvider timeProvider;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            gameField.update(now);
        }
    };

    @Override
    public void updateTiles(DrawnTile[][] nTiles) {
        gameField.updateTiles(nTiles);
    }

    @Override
    public void setTimeAdapter(TimeProvider adapter) {
        this.timeProvider = adapter;
        gameField.setTimeAdapter(adapter);
    }

    @Override
    public void start() {
        Platform.runLater(() -> {
            if (timeProvider == null)
                throw new IllegalStateException("Time adapter must be initialized");
            if (mediaPlayer != null) {
                mediaPlayer.setStartTime(new Duration((double) (timeProvider.getRelativeFromNano(System.nanoTime()) / 1_000_000L)));
                mediaPlayer.setVolume(1);
                mediaPlayer.play();
            }
            timer.start();
        });
    }

    @Override
    public void stop() {
        Platform.runLater(() -> {
            if (mediaPlayer != null)
                mediaPlayer.stop();
            timer.stop();
        });
    }

    @Override
    public void showScore(String scoreStr) {
        Platform.runLater(() -> {
            score.setText(scoreStr);
            menuButton.setDisable(false);
            menuButton.setVisible(true);
            scorePane.setVisible(true);
        });
    }

    @Override
    public void setLeftImage(Image image) {
        Platform.runLater(() -> leftBackground.setImage(image));
    }

    @Override
    public void setRightImage(Image image) {
        Platform.runLater(() -> rightBackground.setImage(image));
    }

    @Override
    public void setCenterImage(Image image) {
        Platform.runLater(() -> centerBackground.setImage(image));
    }

    @Override
    public void setMusic(Media sound) {
        Platform.runLater(() -> mediaPlayer = new MediaPlayer(sound));
    }

    @FXML
    public void pressExit() {
        EventManager.runEvent(new PlayerGameStopEvent());
    }

    @FXML
    public void pressPause() {
        EventManager.runEvent(new PlayerPauseEvent());
    }

    @FXML
    public void pressResume() {
        EventManager.runEvent(new PlayerResumeEvent());
    }

    @FXML
    public void toMenu() {
        EventManager.runEvent(new PlayerOpenMenuEvent());
    }

}
