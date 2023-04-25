package ru.sidey383.model.game.read;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import ru.sidey383.model.game.ClickType;
import ru.sidey383.model.game.description.GameDescription;
import ru.sidey383.model.game.level.PianoGame;
import ru.sidey383.model.game.level.line.tile.Tile;
import ru.sidey383.read.ZIPGameReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PianoGameReader {

    private final ZIPGameReader gameReader;

    private final ZIPGameReader gameLoreReader;

    private final ObjectReader jsonReader = new ObjectMapper().reader();

    public PianoGameReader() {
        Map<String, ZIPGameReader.DataReader> loreReaderMap = new HashMap<>();
        loreReaderMap.put("gameLore.json", this::readGameDescription);
        gameLoreReader = new ZIPGameReader(loreReaderMap);
        Map<String, ZIPGameReader.DataReader> gameReaderMap = new HashMap<>();
        gameReaderMap.put("gameLore.json", this::readGameDescription);
        gameReaderMap.put("tiles1.json", this::readTiles);
        gameReaderMap.put("tiles2.json", this::readTiles);
        gameReaderMap.put("tiles3.json", this::readTiles);
        gameReaderMap.put("tiles4.json", this::readTiles);
        gameReaderMap.put("tiles5.json", this::readTiles);
        gameReaderMap.put("tiles6.json", this::readTiles);
        gameReader = new ZIPGameReader(gameReaderMap);
    }

    private byte[] toBytes(InputStream is) throws IOException {
        return is.readAllBytes();
    }

    private Tile[] readTiles(InputStream is) throws IOException {
        return jsonReader.readValue(is, Tile[].class);
    }

    private GameLore readGameDescription(InputStream is) throws IOException {
        return jsonReader.readValue(is, GameLore.class);
    }

    public PianoGame readGame(URL url) throws IOException {
        Map<String, Object> gameParts = gameReader.readZIPGame(url);
        GameLore lore = null;
        try {
            lore = (GameLore) gameParts.get("gameLore.json");
        } catch (Exception e) {
            throw new IOException("Can't read game lore from file, see other errors", e);
        }
        Tile[][] gameTiles = new Tile[6][];
        for (int i = 1; i <= 6; i++) {
            try {
                Tile[] tiles = (Tile[]) gameParts.get("tiles"+i+".json");
                gameTiles[i-1] = tiles;
            } catch (Exception e) {
                throw new IOException("Can't read tiles" + i + " from file, see other errors", e);
            }
        }
        byte[] leftImage = null;
        byte[] centerImage = null;
        byte[] rightImage = null;
        byte[] music = null;
        try {
            leftImage = (byte[]) gameParts.get("left");
        }catch (Exception e) {
            throw new IOException("Can't read left image from file, see other errors", e);
        }
        try {
            rightImage = (byte[]) gameParts.get("right");
        }catch (Exception e) {
            throw new IOException("Can't read right image from file, see other errors", e);
        }
        try {
            centerImage = (byte[]) gameParts.get("center");
        }catch (Exception e) {
            throw new IOException("Can't read center image from file, see other errors", e);
        }
        try {
            music = (byte[]) gameParts.get("music");
        }catch (Exception e) {
            throw new IOException("Can't read music from file, see other errors", e);
        }
        return new PianoGame(lore.name, lore.levelTime, convert(gameTiles), music, leftImage, centerImage, rightImage);
    }

    public Optional<PianoGameDescription> readGameDescription(URL url) throws IOException {
        Map<String, Object> gameLore = gameLoreReader.readZIPGame(url);
        GameLore gl =  (GameLore) gameLore.get("gameLore.json");
        if (gl == null)
            return Optional.empty();
        else
            return Optional.of(new PianoGameDescription(gl, url));
    }

    private Map<ClickType, Tile[]> convert(Tile[][] tiles) {
        Map<ClickType, Tile[]> map = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            map.put(ClickType.values()[i], tiles[i]);
        }
        return map;
    }

    private record GameLore(String name, Long levelTime) {}

    public class PianoGameDescription implements GameDescription {

        private final GameLore gameLore;

        private final URL url;

        private PianoGameDescription(GameLore gameLore, URL url) {
            this.gameLore = gameLore;
            this.url = url;
        }

        @Override
        public String getName() {
            return gameLore.name();
        }

        @Override
        public URL getGameContainer() {
            return url;
        }

        public long getLevelTime() {
            return gameLore.levelTime();
        }

    }

}
