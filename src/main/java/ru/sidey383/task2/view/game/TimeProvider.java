package ru.sidey383.task2.view.game;

public interface TimeProvider {

    long getRelativeFromNano(long timeNS);

    long getTimeToShow();

}
