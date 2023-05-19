package ru.sidey383.task2.model.game;

/**
 * use nano time
 * **/
public interface TimerGame {

    void start();

    boolean stop();

    boolean isStarted();

    boolean pause();

    boolean resume();

    boolean isGoing();

    long getStartTime();

    boolean isOutOfTime(long systemTime);

    long toLocalTime(long systemTime);

    long getTotalTime();

}
