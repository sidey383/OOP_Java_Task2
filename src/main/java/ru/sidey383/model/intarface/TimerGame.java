package ru.sidey383.model.intarface;

public interface TimerGame {

    void start();

    void stop();

    boolean isStarted();

    long gameTime();

    void pause();

    void resume();

    boolean isPaused();

}
