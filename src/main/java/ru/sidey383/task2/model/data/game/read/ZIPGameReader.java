package ru.sidey383.task2.model.data.game.read;

import ru.sidey383.task2.model.exception.IncorrectGameFileException;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.exception.ModelIOException;
import ru.sidey383.task2.model.game.level.PianoGame;
import ru.sidey383.task2.model.game.level.line.tile.Tile;

import java.util.HashMap;
import java.util.Optional;

public class ZIPGameReader extends ZIPReader {

    public ZIPGameReader() {
        super(new HashMap<>());
        readerStructure.put("gameLore.json", ReaderMethods::readGameLore);
        readerStructure.put("tiles1.json", ReaderMethods::readTiles);
        readerStructure.put("tiles2.json", ReaderMethods::readTiles);
        readerStructure.put("tiles3.json", ReaderMethods::readTiles);
        readerStructure.put("tiles4.json", ReaderMethods::readTiles);
        readerStructure.put("tiles5.json", ReaderMethods::readTiles);
        readerStructure.put("tiles6.json", ReaderMethods::readTiles);
    }

    public PianoGame readGame(RawDataContainer data) throws ModelException {
        Optional<GameLore> lore = data.getData(GameLore.class, "gameLore.json");
        if (lore.isEmpty())
            throw new ModelIOException("Can't read game lore from file, see other errors");
        Tile[][] gameTiles = new Tile[6][];
        for (int i = 1; i <= 6; i++) {
            Optional<Tile[]> tiles = data.getData(Tile[].class, "tiles"+i+".json");
            if (tiles.isEmpty())
                throw new ModelIOException("Can't read tiles "+i+" from file, see other errors");
            gameTiles[i-1] = tiles.get();
        }
        try {
            return new PianoGame(lore.get().name(), lore.get().levelTime(), ReaderMethods.convert(gameTiles));
        } catch (IllegalArgumentException e) {
            throw new IncorrectGameFileException("Create game " + lore.get().name() + " error", e);
        }
    }

}
