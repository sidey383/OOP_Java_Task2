package ru.sidey383.task2.model.data.game;

import java.nio.file.Path;

public interface GameDescription {

    String gameKey();

    String name();

    String hash();

    Path gameContainer();

}
