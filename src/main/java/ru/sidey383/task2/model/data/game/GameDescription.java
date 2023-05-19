package ru.sidey383.task2.model.data.game;

import java.nio.file.Path;

public interface GameDescription {

    String getGameKey();

    String getName();

    String getHash();

    Path getGameContainer();

}
