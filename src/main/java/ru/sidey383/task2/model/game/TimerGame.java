package ru.sidey383.task2.model.game;

/**
 * use nano time
 * **/
public interface TimerGame {

    void start();

    void stop();

    boolean isOn();

    void pause();

    void resume();

    boolean isPaused();

    long getStartTime();

    long toLocalTime(long systemTime);

    long getTotalTime();

}
