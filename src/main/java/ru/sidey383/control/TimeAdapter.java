package ru.sidey383.control;

public interface TimeAdapter {

    long getRelativeFromNano(long timeNS);

    long getRelativeFromMS(long timeMS);

    long getTimeToShow();

}
