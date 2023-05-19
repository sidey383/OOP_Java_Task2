package ru.sidey383.task2.control.session;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.view.game.DrawnTile;
import ru.sidey383.task2.view.game.DrawnTileType;
import ru.sidey383.task2.view.game.TimeProvider;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;
import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.level.tile.line.TileLinesGame;
import ru.sidey383.task2.model.game.level.tile.line.line.tile.Tile;
import ru.sidey383.task2.model.game.level.tile.line.line.tile.TileStatus;
import ru.sidey383.task2.view.AppScene;
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

public class GameSession extends ControllerSession {

    private final static Logger logger = LogManager.getLogger(GameSession.class);

    private final TileLinesGame game;

    private final GameView gameView;

    private final Map<Integer, ClickType> keyMap;

    private final Object graphicLock = new Object();

    private final Timer timer = new Timer();

    private TimerTask graphicTask = null;


    public GameSession(Controller controller, TileLinesGame game, GameView gameView, Map<Integer, ClickType> keyMap) {
        super(controller);
        this.game = game;
        this.gameView = gameView;
        this.keyMap = keyMap;
    }

    public static GameSession create(Controller controller, RawDataContainer container, TileLinesGame game) throws IOException {
        GameView gameView = controller.getView().getScene(GameView.class);
        setGameStyle(gameView, container);

        Map<Integer, ClickType> keyMap = new HashMap<>();
        for (Map.Entry<ClickType, Integer> e : controller.getModel().getSettings().getGameKeys().entrySet()) {
            keyMap.put(e.getValue(), e.getKey());
        }

        gameView.setTimeAdapter(new SimpleTimeProvider(game));
        controller.getView().setScene(gameView);
        return new GameSession(controller, game, gameView, keyMap);
    }

    private static void setGameStyle(GameView gameView, RawDataContainer container) throws IOException {
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

    @Override
    public AppScene getScene() {
        return gameView;
    }

    public void gameEnd() {
        if (game.stop()) {
            gameView.stop();
            timer.cancel();
            showScore();
        }
    }

    public void gamePause() {
        if (game.pause()) {
            graphicPause();
        }
    }

    public void gameResume() {
        if (game.resume()) {
            graphicStart();
        }
    }

    private void showScore() {
        Collection<TileStatus> statistic = game.getTileStatistic();
        long score = game.getScore();
        long clicked = statistic.stream().filter(TileStatus::isClicked).count();
        long missed = game.getMissCount();
        gameView.showScore(String.format("""
                Score: %d
                Clicked: %d
                Missed: %d
                Miss clicked: %d
                """, score, clicked, statistic.size() - clicked, missed));
    }

    private void graphicPause() {
        gameView.stop();
        synchronized (graphicLock) {
            if (graphicTask != null) {
                graphicTask.cancel();
                graphicTask = null;
            }
        }
    }

    private void graphicStart() {
        gameView.start();
        synchronized (graphicLock) {
            if (graphicTask != null) {
                graphicTask.cancel();
            }
            graphicTask = new GraphicUpdaterTask();
            logger.info(String.format("Start graphic task %d %d", 0, game.getTimeToShow() / 4_000_000));
            timer.schedule(graphicTask, 0, game.getTimeToShow() / 4_000_000);
        }
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

    private class GraphicUpdaterTask extends TimerTask {

        @Override
        public void run() {
            long nTime = System.nanoTime();
            if (game.isOutOfTime(nTime)) {
                gameEnd();
            }
            long time = game.toLocalTime(nTime);
            long endTime = time + game.getTimeToShow() * 2;
            ClickType[] types = game.getAvailableTypes();
            DrawnTile[][] tiles = new DrawnTile[types.length][];
            for (int i = 0; i < types.length; i++) {
                tiles[i] = game.getLine(types[i]).getTiles(time, endTime).stream().map(TileAdapter::new).toArray(TileAdapter[]::new);
            }
            gameView.updateTiles(tiles);
        }

    }

    private record TileAdapter(Tile tile) implements DrawnTile {

        @Override
        public long getEndTime() {
            return tile.getEndTime();
        }

        @Override
        public long getStartTime() {
            return tile.getStartTime();
        }

        @Override
        public DrawnTileType getType() {
            return switch (tile.getType()) {
                case LONG -> DrawnTileType.LONG;
                case DEFAULT -> DrawnTileType.SHORT;
            };
        }
    }

    private record SimpleTimeProvider(TileLinesGame game) implements TimeProvider {

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
