package ru.sidey383.task2.control.session;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.control.TimeAdapter;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;
import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.TileLinesGame;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;
import ru.sidey383.task2.view.game.GameView;
import ru.sidey383.task2.view.events.PlayerKeyEvent;
import ru.sidey383.task2.view.events.game.PlayerGameStopEvent;
import ru.sidey383.task2.view.events.game.PlayerPauseEvent;
import ru.sidey383.task2.view.events.game.PlayerResumeEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

//TODO: fix repeated pressing of pause and play
public class GameSession extends ControllerSession {

    private final static Logger logger = LogManager.getLogger(GameSession.class);

    private final TileLinesGame game;

    private final GameView gameView;

    private Timer graphicUpdateTimer;

    private final Map<Integer, ClickType> keyMap;


    public GameSession(Controller controller, TileLinesGame game, GameView gameView, Map<Integer, ClickType> keyMap) {
        super(controller);
        this.game = game;
        this.gameView = gameView;
        this.keyMap = keyMap;
    }

    public static GameSession create(Controller controller, RawDataContainer container, TileLinesGame game) throws IOException {
        GameView gameView;

        gameView = controller.getView().getScene(GameView.class);
        gameView.setRightImage(new Image(new ByteArrayInputStream(
                container.getData(byte[].class, "right")
                        .orElse(new byte[0])
        )));
        gameView.setLeftImage(new Image(new ByteArrayInputStream(
                container.getData(byte[].class, "left")
                        .orElse(new byte[0])
        )));
        gameView.setCenterImage(new Image(new ByteArrayInputStream(
                container.getData(byte[].class, "center")
                        .orElse(new byte[0])
        )));
        Path tempMusicPath = Files.createTempFile("gameMedia", "");
        try (OutputStream os = Files.newOutputStream(tempMusicPath)) {
            os.write(container
                    .getData(byte[].class, "music")
                    .orElse(new byte[0]));
        } catch (IOException e) {
            logger.error(() -> String.format("Music write error %s", tempMusicPath), e);
        }
        gameView.setMusic(new Media(tempMusicPath.toUri().toString()));
        Map<Integer, ClickType> keyMap = new HashMap<>();
        for (Map.Entry<ClickType, Integer> e : controller.getSettings().getGameKeys().entrySet()) {
            keyMap.put(e.getValue(), e.getKey());
        }
        gameView.setTimeAdapter(new SimpleTimeAdapter(game));
        controller.getView().setScene(gameView);
        return new GameSession(controller, game, gameView, keyMap);
    }

    @Override
    public void start() {
        super.start();
        game.start();
        graphicStart();
    }

    @Override
    public void end() {
        super.end();
        gameEnd();
    }

    private void gameEnd() {
        graphicStop();
        showScore();
        if (game.isOn())
            game.stop();
    }

    private void showScore() {
        Collection<TileStatus> statistic = game.getStatistic();
        int score = statistic.stream().mapToInt(TileStatus::getScore).sum();
        long clicked = statistic.stream().filter(TileStatus::isClicked).count();
        Platform.runLater(() ->
                gameView.showScore(String.format("""
                        Score: %d
                        Clicked: %d
                        Missed: %d
                        """, score, clicked, statistic.size() - clicked))
        );
    }

    private void gamePause() {
        game.pause();
        graphicStop();
    }

    private void gameResume() {
        game.resume();
        graphicStart();
    }

    private void graphicStop() {
        gameView.stop();
        graphicUpdateTimer.cancel();
    }

    private void graphicStart() {
        gameView.start();
        TimerTask graphicUpdateTask = new TimerTask() {
            @Override
            public void run() {
                long time = game.toLocalTime(System.nanoTime());
                if (time > game.getTotalTime()) {
                    gameEnd();
                    cancel();
                }
                long endTime = time + game.getTimeToShow() * 2;
                ClickType[] types = game.getAvailableTypes();
                Tile[][] tiles = new Tile[types.length][];
                for (int i = 0; i < types.length; i++) {
                    tiles[i] = game.getLine(types[i]).getTiles(time, endTime).toArray(Tile[]::new);
                }
                gameView.updateTiles(tiles);
            }
        };
        graphicUpdateTimer = new Timer();
        graphicUpdateTimer.schedule(graphicUpdateTask, 0, game.getTimeToShow() / 4_000_000);
    }

    @EventHandler
    public void onGameExit(PlayerGameStopEvent e) {
        gameEnd();
    }

    @EventHandler
    public void onPause(PlayerPauseEvent e) {
        gamePause();
    }

    @EventHandler
    public void onResume(PlayerResumeEvent e) {
        gameResume();
    }

    @EventHandler
    public void onGameKey(PlayerKeyEvent e) {
        if (e.isPress()) {
            game.press(keyMap.get(e.getKeyCode().getCode()), game.toLocalTime(e.getCreateNanoTine()));
        } else {
            game.release(keyMap.get(e.getKeyCode().getCode()), game.toLocalTime(e.getCreateNanoTine()));
        }
    }

    private static class SimpleTimeAdapter implements TimeAdapter {

        private final TileLinesGame game;

        public SimpleTimeAdapter(TileLinesGame game) {
            this.game = game;
        }

        @Override
        public long getRelativeFromNano(long timeNS) {
            return game.toLocalTime(timeNS);
        }

        @Override
        public long getTimeToShow() {
            return game.getTimeToShow();
        }

    }

}
