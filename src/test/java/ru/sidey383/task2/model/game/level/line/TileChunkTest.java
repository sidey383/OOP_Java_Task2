package ru.sidey383.task2.model.game.level.line;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.sidey383.task2.model.game.level.line.tile.DefaultTile;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;
import ru.sidey383.task2.model.game.level.line.tile.TileType;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TileChunkTest {

    @Test
    @Order(0)
    public void collectTest() {
        Tile tile1 = new DefaultTile(400_000_000);
        Tile tile2 = new DefaultTile(500_000_000);
        Tile tile3 = new DefaultTile(600_000_000);
        TileChunk chunk = new TileChunk(new Tile[] {
                tile1,
                tile2,
                tile3
        });
        Collection<Tile> collection = new ArrayList<>();
        chunk.getTiles(collection, (t) -> t.endTime() > 0 && t.startTime() < 300_000_000);
        assertEquals(0, collection.size(), "No tiles by this time");
        collection.clear();
        chunk.getTiles(collection, (t) -> t.endTime() > 300_000_000 && t.startTime() <  450_000_000);
        assertEquals(1, collection.size(), "Only first tile in collection");
        assertEquals(tile1, collection.iterator().next(), "Wrong tile object");
        collection.clear();
        chunk.getTiles(collection, (t) -> t.endTime() > 500_000_000 && t.startTime() < 599_999_999);
        assertEquals(2, collection.size(), "Expect to tiles by this time");
        assertTrue(collection.contains(tile1), "Don't contain first tile");
        assertTrue(collection.contains(tile2), "Don't contain second tile");
        collection.clear();
        chunk.getTiles(collection, (t) -> t.endTime() > 599_999_999 && t.startTime() < 600_000_001);
        assertEquals(3, collection.size(), "Expect to tiles by this time");
        assertTrue(collection.contains(tile1), "Don't contain first tile");
        assertTrue(collection.contains(tile2), "Don't contain second tile");
        assertTrue(collection.contains(tile3), "Don't contain third tile");
        collection.clear();
        chunk.getTiles(collection, (t) -> false);
        assertEquals(0, collection.size(), "Wrong time, expect no tiles");
    }

    @Test
    @Order(1)
    public void tileGetTest() {
        Tile tile1 = new DefaultTile(400_000_000);
        Tile tile2 = new DefaultTile(800_000_000);
        Tile tile3 = new DefaultTile(1_200_000_000);
        TileChunk chunk = new TileChunk(new Tile[] {
                tile1,
                tile2,
                tile3
        });
        assertEquals(tile1, chunk.getTile(400_000_001));
        assertEquals(tile2, chunk.getTile(800_000_001));
        assertEquals(tile3, chunk.getTile(1_200_000_001));
        assertNull(chunk.getTile(399_999_999));
        assertNull(chunk.getTile(700_000_001));
        assertNull(chunk.getTile(1_100_000_001));
        assertNull(chunk.getTile(1_500_000_001));
    }

    @Test
    @Order(2)
    public void scoreTest() {
        Tile tile1 = new DefaultTile(400_000_000);
        Tile tile2 = new DefaultTile(800_000_000);
        Tile tile3 = new DefaultTile(1_200_000_000);
        TileChunk chunk = new TileChunk(new Tile[] {
                tile1,
                tile2,
                tile3
        });
        assertEquals(0, chunk.getScore(t -> true), "Not clicked tiles");
        tile1.press(400_000_001);
        assertEquals(3, chunk.getScore(t -> true), "Click one tile");
        tile3.press(1_200_000_001);
        assertEquals(6, chunk.getScore(t -> true), "Click two tile");
        assertEquals(3, chunk.getScore(t -> t.startTime() > 800_000_000), "Predicate check");
    }

    private static TileStatus getAnonymusTileStatus() {
        return new TileStatus() {
            @Override
            public boolean clicked() {
                return false;
            }

            @Override
            public int score() {
                return 0;
            }
        };
    }

    private static Tile getAnonymusTile(TileStatus status) {
            return new Tile() {
                @Override
                public long startTime() {
                    return 0;
                }

                @Override
                public long endTime() {
                    return 0;
                }

                @Override
                public TileStatus getStatus() {
                    return status;
                }

                @Override
                public void press(long relativeTime) {

                }

                @Override
                public void release(long relativeTime) {

                }

                @Override
                public TileType type() {
                    return null;
                }
            };
    }


    @Test
    @Order(3)
    public void statusTest() {
        TileStatus status1 = getAnonymusTileStatus();
        TileStatus status2 = getAnonymusTileStatus();
        TileStatus status3 = getAnonymusTileStatus();
        Tile tile1 = getAnonymusTile(status1);
        Tile tile2 = getAnonymusTile(status2);
        Tile tile3 = getAnonymusTile(status3);
        TileChunk chunk = new TileChunk(new Tile[] {
                tile1,
                tile2,
                tile3
        });
        Collection<TileStatus> collection = new ArrayList<>();
        chunk.getTileStatistic(collection);
        assertEquals(3, collection.size(), "Wrong TileStatus size");
        assertTrue(collection.contains(status1), "No tile status in tile chunk");
        assertTrue(collection.contains(status2), "No tile status in tile chunk");
        assertTrue(collection.contains(status3), "No tile status in tile chunk");
    }

}
