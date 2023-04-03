package ru.sidey383.control;

import javafx.application.Platform;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.model.game.ClickType;
import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.level.line.tile.Tile;
import ru.sidey383.model.game.level.line.tile.TileStatus;
import ru.sidey383.view.events.*;
import ru.sidey383.view.game.GameView;

import java.util.*;

public class GameController {

    private final TileLinesGame game;

    private final GameView gameView;

    private Timer graphicUpdateTimer;

    private final Map<Integer, ClickType> keyMap;


    public GameController(TileLinesGame game, GameView gameView, Map<Integer, ClickType> keyMap) {
        this.game = game;
        this.gameView = gameView;
        this.keyMap = keyMap;
        EventManager.manager.registerListener(this);
        gameView.setTimeAdapter(new TimeAdapter() {
            @Override
            public long getRelativeFromNano(long timeNS) {
                return game.toLocalTime(timeNS);
            }

            @Override
            public long getTimeToShow() {
                return game.getTimeToShow();
            }
        });
        gameStart();
    }

    private void gameStart() {
        game.start();
        graphicStart();
    }

    private void gameEnd() {
        graphicStop();
        game.stop();
        Collection<TileStatus> statistic = game.getStatistic();
        int score = statistic.stream().mapToInt(TileStatus::getScore).sum();
        long clicked = statistic.stream().filter(TileStatus::isClicked).count();
        Platform.runLater( ()  ->
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
        graphicUpdateTimer.schedule(graphicUpdateTask, 0, 500);
    }

    @EventHandler
    public void onWindowsClose(WindowCloseEvent e) {
        gameEnd();
    }

    @EventHandler
    public void onGameExit(GameExitEvent e) {
        gameEnd();
    }

    @EventHandler
    public void onPause(GamePauseEvent e) {
        gamePause();
    }

    @EventHandler
    public void onResume(GameResumeEvent e) {
        gameResume();
    }

    @EventHandler
    public void onGameKey(GameKeyEvent e) {
        if (e.isPress()) {
            game.press(keyMap.get(e.getKeyCode().getCode()), game.toLocalTime(e.getCreateNanoTine()));
        } else {
            game.release(keyMap.get(e.getKeyCode().getCode()), game.toLocalTime(e.getCreateNanoTine()));
        }
    }

}
