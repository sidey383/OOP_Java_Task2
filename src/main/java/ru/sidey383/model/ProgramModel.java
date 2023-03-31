package ru.sidey383.model;

import ru.sidey383.model.game.GameLore;

public interface ProgramModel {

    void startGame(GameLore lore);

    void openGameChoose();

    void openMenu();

    void addListener(ModelListener listener);

    void removeListener(ModelListener listener);

}
