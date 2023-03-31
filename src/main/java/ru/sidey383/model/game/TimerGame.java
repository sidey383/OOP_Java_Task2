package ru.sidey383.model.game;

public interface TimerGame {

    void start();

    void stop();

    boolean isOn();

    void pause();

    void resume();

    boolean isPaused();

    long getStartTime();

    long toLocalTime(long systemTime);

}
