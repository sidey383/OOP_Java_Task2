package ru.sidey383.task2.model.game.level;

import ru.sidey383.task2.model.game.TimerGame;

/**
 * use nano time
 * **/
public abstract class AbstractTimerGame implements TimerGame {

    protected long startTime = 0;

    protected long pauseTime = 0;

    private GameStatus gameStatus = GameStatus.NOT_STARTED;

    private enum GameStatus {
        NOT_STARTED, PAUSED, ON, IS_OVER
    }

    @Override
    public synchronized boolean start() {
        if (gameStatus == GameStatus.NOT_STARTED) {
            startTime = System.nanoTime();
            gameStatus = GameStatus.ON;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean pause() {
        if (gameStatus == GameStatus.ON) {
            pauseTime = System.nanoTime();
            gameStatus = GameStatus.PAUSED;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean resume() {
        if (gameStatus == GameStatus.PAUSED) {
            startTime = startTime + System.nanoTime() - pauseTime;
            gameStatus = GameStatus.ON;
            return true;
        }
        return false;
    }

    public synchronized boolean stop() {
        if (gameStatus != GameStatus.IS_OVER) {
            gameStatus = GameStatus.IS_OVER;
            pauseTime = System.nanoTime();
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean isGoing() {
        return gameStatus == GameStatus.ON;
    }

    @Override
    public synchronized long toLocalTime(long systemTime) {
        return switch (gameStatus) {
            case ON -> systemTime - startTime;
            case PAUSED, IS_OVER -> pauseTime - startTime;
            case NOT_STARTED -> 0;
        };
    }

}
