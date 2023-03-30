package ru.sidey383.view.game;

import org.jetbrains.annotations.NotNull;
import ru.sidey383.model.intarface.TileLine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class TileLineCalculator {

    private final long showStartTime;

    private final long deathTime;

    private final TileLine tileLine;

    private final Deque<ShowTile> tiles = new ArrayDeque<>();

    public TileLineCalculator(long preShowTime, long deathTime, TileLine tileLinesGame) {
        this.showStartTime = preShowTime;
        this.tileLine = tileLinesGame;
        this.deathTime = deathTime;
    }

    @NotNull
    public Iterator<ShowTile> iterator(long gameTime) {
        tileLine.popTiles(showStartTime).stream()
                .map(t -> new ShowTile(gameTime, showStartTime, deathTime, t))
                .forEach(tiles::addLast);
        return new TileIterator(gameTime);
    }

    private class TileIterator implements Iterator<ShowTile> {

        private final Iterator<ShowTile> tileIterator;

        private final long gameTime;


        public TileIterator(long gameTime) {
            this.gameTime = gameTime;
            while (!tiles.isEmpty()) {
                ShowTile tile = tiles.removeLast();
                if (tile.updatePose(gameTime)) {
                    tiles.addLast(tile);
                    break;
                }
            }
            tileIterator = tiles.iterator();
        }

        @Override
        public boolean hasNext() {
            return tileIterator.hasNext();
        }

        @Override
        public ShowTile next() {
            tileIterator.next().updatePose(gameTime);
            return tileIterator.next();
        }
    }
}
