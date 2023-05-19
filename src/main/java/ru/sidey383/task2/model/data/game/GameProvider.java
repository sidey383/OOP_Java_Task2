package ru.sidey383.task2.model.data.game;

import java.util.Collection;

public interface GameProvider {

    Collection<GameDescription> readGameDescriptions();

}
