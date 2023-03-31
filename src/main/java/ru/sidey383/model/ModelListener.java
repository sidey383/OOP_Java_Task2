package ru.sidey383.model;

import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.GameLore;

import java.util.List;

public interface ModelListener {

    void startGame(TileLinesGame game);

    void openGameChoose(List<GameLore> gameList);

    void openMenu();

}
