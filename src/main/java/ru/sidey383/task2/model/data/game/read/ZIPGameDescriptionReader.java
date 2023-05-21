package ru.sidey383.task2.model.data.game.read;

import ru.sidey383.task2.model.data.game.GameDescription;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

public class ZIPGameDescriptionReader extends ZIPReader {
    public ZIPGameDescriptionReader() {
        super(new HashMap<>(), is -> null);
        readerStructure.put("gameLore.json", ReaderMethods::readGameLore);
    }

    public GameDescription readDescription(Path path) throws IOException {
        RawDataContainer data = readZIP(path);
        Optional<GameLore> lore = data.getData(GameLore.class, "gameLore.json");
        return lore
                .map(gameLore -> new DefaultGameDescription(gameLore, path, gameLore.name() + "_" + data.getHash(), data.getHash()))
                .orElse(null);
    }

    private record DefaultGameDescription(GameLore gameLore,
                                          Path gameContainer,
                                          String gameKey,
                                          String hash) implements GameDescription {

        @Override
        public String name() {
            return gameLore.name();
        }
    }

}
