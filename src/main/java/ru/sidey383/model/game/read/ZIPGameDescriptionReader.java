package ru.sidey383.model.game.read;

import ru.sidey383.model.game.GameDescription;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

public class ZIPGameDescriptionReader extends ZIPReader {
    public ZIPGameDescriptionReader() {
        super(new HashMap<>(), (is) -> null);
        readerStructure.put("gameLore.json", ReaderMethods::readGameLore);
    }

    public GameDescription readDescription(URL url) throws IOException {
        DataContainer data = readZIP(url);
        Optional<GameLore> lore = data.getData(GameLore.class, "gameLore.json");
        return lore.map(gameLore -> new DefaultGameDescription(gameLore, url)).orElse(null);
    }

    private static class DefaultGameDescription implements GameDescription {

        private final GameLore gameLore;

        private final URL gameContainer;

        public DefaultGameDescription(GameLore lore, URL gameContainer) {
            this.gameContainer = gameContainer;
            this.gameLore = lore;
        }

        @Override
        public String getName() {
            return gameLore.name();
        }

        @Override
        public URL getGameContainer() {
            return gameContainer;
        }
    }

}
