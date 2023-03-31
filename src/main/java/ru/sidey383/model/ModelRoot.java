package ru.sidey383.model;

import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.GameLore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelRoot implements ProgramModel {

    Set<ModelListener> listeners = new HashSet<>();

    @Override
    public void startGame(GameLore lore) {
        TileLinesGame game = null;
        //TODO: game create logic
        for (ModelListener listener : listeners)
            listener.startGame(game);
    }

    @Override
    public void openGameChoose() {
        List<GameLore> gameLoreList = new ArrayList<>();
        //TODO: read game lore
        for (ModelListener listener : listeners)
            listener.openGameChoose(gameLoreList);
    }

    @Override
    public void openMenu() {
        for (ModelListener listener : listeners)
            listener.openMenu();
    }

    @Override
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }
}
