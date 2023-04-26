package ru.sidey383.control.session;

import javafx.application.Platform;
import ru.sidey383.control.Controller;
import ru.sidey383.control.ControllerSession;
import ru.sidey383.control.TimeAdapter;
import ru.sidey383.event.EventHandler;
import ru.sidey383.model.game.ClickType;
import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.level.line.tile.Tile;
import ru.sidey383.model.game.level.line.tile.TileStatus;
import ru.sidey383.view.events.*;
import ru.sidey383.view.events.game.PlayerGameStopEvent;
import ru.sidey383.view.events.game.PlayerPauseEvent;
import ru.sidey383.view.events.game.PlayerResumeEvent;
import ru.sidey383.view.game.GameView;

import java.util.*;

public class GameSession extends ControllerSession {

    private final TileLinesGame game;

    private final GameView gameView;

    private Timer graphicUpdateTimer;

    private final Map<Integer, ClickType> keyMap = new HashMap<>();


    public GameSession(Controller controller, TileLinesGame game, GameView gameView) {
        super(controller);
        this.game = game;
        this.gameView = gameView;
        for (Map.Entry<ClickType, Integer> e : getController().getSettings().gameKeys().entrySet()) {
            keyMap.put(e.getValue(), e.getKey());
        }
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

}
