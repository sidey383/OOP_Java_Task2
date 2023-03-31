package ru.sidey383.model.game;

import ru.sidey383.model.game.line.TileLine;

public interface TileLinesGame extends TimerGame, ClickableGame {

    TileLine getLine(ClickType clickType);

    ClickType[] getAvailableTypes();

    long getTimeToShow();

}
