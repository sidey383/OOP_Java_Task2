package ru.sidey383.model.game;

public abstract class AbstractTimerGame implements TimerGame {

    protected final static long NO_TIME = -1;

    protected long startTime = NO_TIME;

    protected long pauseTime = NO_TIME;

    @Override
    public void start() {
        if (startTime != NO_TIME)
            throw new IllegalStateException("Game already stared");
        startTime = System.nanoTime();
    }

    @Override
    public boolean isOn() {
        return startTime != NO_TIME;
    }

    @Override
    public void pause() {
        pauseTime = System.nanoTime();
    }

    @Override
    public void resume() {
        startTime = startTime + System.nanoTime() - pauseTime;
        pauseTime = NO_TIME;
    }

    public void stop() {
        startTime = NO_TIME;
        pauseTime = NO_TIME;
    }

    @Override
    public boolean isPaused() {
        return pauseTime != NO_TIME;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long toLocalTime(long systemTime) {
        return systemTime - startTime;
    }
}
