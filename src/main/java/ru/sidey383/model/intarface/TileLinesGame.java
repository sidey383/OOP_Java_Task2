package ru.sidey383.model.intarface;

public interface TileLinesGame extends TimerGame, ClickableGame {

    TileLine getLine(ClickType clickType);

    ClickType[] getAvailableTypes();

}
