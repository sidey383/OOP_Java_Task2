package ru.sidey383.task2.model.game;

/**
 * use nano time
 * **/
public interface TimerGame {

    boolean start();

    boolean stop();

    boolean pause();

    boolean resume();

    boolean isGoing();

    boolean isOutOfTime(long systemTime);

    long toLocalTime(long systemTime);

    long getTotalTime();

}
