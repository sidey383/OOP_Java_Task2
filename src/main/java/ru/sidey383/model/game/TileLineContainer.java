package ru.sidey383.model.game;

import org.jetbrains.annotations.Nullable;
import ru.sidey383.model.intarface.TileLine;
import ru.sidey383.model.intarface.TimerGame;
import ru.sidey383.model.intarface.tile.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TileLineContainer implements TileLine {

    private final Tile[] tiles;

    private int popIterator = 0;

    private int currentTile = 0;

    private TimerGame timerGame;

    public TileLineContainer(TimerGame timerGame, Collection<Tile> tiles) {
        this.tiles = tiles.stream().sorted(Comparator.comparingLong(Tile::getStartTime)).toArray(Tile[]::new);
        for (int i = 1; i < tiles.size(); i++) {
            if (this.tiles[i-1].getEndTime() > this.tiles[i].getStartTime())
                throw new IllegalArgumentException("Tile overlap each other");
        }
        this.timerGame = timerGame;
    }


    @Override
    public Collection<Tile> popTiles(long viewEndTime) {
        List<Tile> returnedTiles = new ArrayList<>();
        long gameTime = timerGame.gameTime();
        while (popIterator < tiles.length) {
            Tile current = tiles[popIterator];
            if (current.getStartTime() > (viewEndTime + gameTime))
                break;
            returnedTiles.add(current);
            popIterator++;
        }
        return returnedTiles;
    }

    @Override
    public Tile[] getTiles() {
        return tiles;
    }

    @Override
    public void press() {
        Tile t = currentTile();
        if (t != null)
            t.press();
        //TODO: no tile logic
    }

    @Override
    public void release() {
        Tile t = currentTile();
        if (t != null)
            t.release();
        //TODO: no tile logic
    }

    @Nullable
    private Tile currentTile() {
        long gameTime = timerGame.gameTime();
        while (currentTile < tiles.length) {
            Tile tile = tiles[currentTile];
            if (tile.getEndTime() < gameTime) {
                tile.onEnd();
                currentTile++;
                continue;
            }
            if (tile.getStartTime() < gameTime) {
                return tile;
            }
            break;
        }
        return null;
    }

    @Override
    public void onEnd() {
        for (; currentTile < tiles.length; currentTile++) {
            tiles[currentTile].onEnd();
        }
    }

}
