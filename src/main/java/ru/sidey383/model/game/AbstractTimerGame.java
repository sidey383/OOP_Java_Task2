package ru.sidey383.model.game;

import ru.sidey383.model.intarface.TimerGame;

public abstract class AbstractTimerGame implements TimerGame {

    protected final static long NO_TIME = -1;

    protected long startTime = NO_TIME;

    protected long pauseTime = NO_TIME;

    @Override
    public void start() {
        if (startTime != NO_TIME)
            throw new IllegalStateException("Game already stared");
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isStarted() {
        return startTime != NO_TIME;
    }

    @Override
    public long gameTime() {
        if (pauseTime != NO_TIME)
            return System.currentTimeMillis() - startTime;
        return pauseTime - startTime;
    }

    @Override
    public void pause() {
        pauseTime = System.currentTimeMillis();
    }

    @Override
    public void resume() {
        startTime = startTime + System.currentTimeMillis() - pauseTime;
        pauseTime = NO_TIME;
    }

    @Override
    public boolean isPaused() {
        return pauseTime != NO_TIME;
    }
}
