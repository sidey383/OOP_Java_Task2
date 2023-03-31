package ru.sidey383.model.game;

public interface ClickableGame {

    void press(ClickType type, long globalTime);

    void release(ClickType type, long globalTime);

}
