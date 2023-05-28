package ru.sidey383.task2.model.game.level.line;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sidey383.task2.model.game.level.line.tile.DefaultTile;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;
import ru.sidey383.task2.model.game.level.line.tile.TileType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TileLineContainerTest {

    @Test
    @Order(0)
    public void collectTest() {
        Tile tile1 = new DefaultTile(400_000_000);
        Tile tile2 = new DefaultTile(10_000_000_000L);
        Tile tile3 = new DefaultTile(21_200_000_000L);
        TileLineContainer line = new TileLineContainer(30_000_000_000L, List.of(
                tile1,
                tile2,
                tile3
        ));
        Collection<Tile> collection = new ArrayList<>();
        line.getTiles(collection, 0, 300_000_000);
        Collection<Tile> retCollection = line.getTiles(0, 300_000_000);
        assertIterableEquals(collection, retCollection, "Different ways to get collection");
        assertEquals(0, collection.size(), "No tiles by this time");
        collection.clear();
        line.getTiles(collection, 300_000_000, 9_450_000_000L);
        retCollection = line.getTiles(300_000_000, 9_450_000_000L);
        assertIterableEquals(collection, retCollection, "Different ways to get collection");
        assertEquals(1, collection.size(), "Only first tile in collection");
        assertEquals(tile1, collection.iterator().next(), "Wrong tile object");
        collection.clear();
        line.getTiles(collection, 500_000_000, 21_199_999_999L);
        retCollection = line.getTiles(500_000_000, 21_199_999_999L);
        assertIterableEquals(collection, retCollection, "Different ways to get collection");
        assertEquals(2, collection.size(), "Expect to tiles by this time");
        assertTrue(collection.contains(tile1), "Don't contain first tile");
        assertTrue(collection.contains(tile2), "Don't contain second tile");
        collection.clear();
        line.getTiles(collection, 599_999_999, 21_200_000_001L);
        retCollection = line.getTiles(599_999_999, 21_200_000_001L);
        assertIterableEquals(collection, retCollection, "Different ways to get collection");
        assertEquals(3, collection.size(), "Expect to tiles by this time");
        assertTrue(collection.contains(tile1), "Don't contain first tile");
        assertTrue(collection.contains(tile2), "Don't contain second tile");
        assertTrue(collection.contains(tile3), "Don't contain third tile");
        collection.clear();
        line.getTiles(collection, 500_500_000_000L, -1);
        retCollection = line.getTiles(500_500_000_000L, -1);
        assertIterableEquals(collection, retCollection, "Different ways to get collection");
        assertEquals(0, collection.size(), "Wrong time, expect no tiles");
    }

    @Test
    @Order(1)
    public void tileGetTest() {
        Tile tile1 = new DefaultTile(400_000_000);
        Tile tile2 = new DefaultTile(10_000_000_000L);
        Tile tile3 = new DefaultTile(21_200_000_000L);
        TileLineContainer line = new TileLineContainer(30_000_000_000L, List.of(
                tile1,
                tile2,
                tile3
        ));
        assertEquals(tile1, line.getTile(400_000_001));
        assertEquals(tile2, line.getTile(10_000_000_001L));
        assertEquals(tile3, line.getTile(21_200_000_001L));
        assertNull(line.getTile(399_999_999));
        assertNull(line.getTile(700_000_001));
        assertNull(line.getTile(12_000_000_001L));
        assertNull(line.getTile(21_500_000_001L));
    }

    @Test
    @Order(2)
    public void scoreTest() {
        Tile tile1 = new DefaultTile(400_000_000);
        Tile tile2 = new DefaultTile(10_000_000_000L);
        Tile tile3 = new DefaultTile(21_200_000_000L);
        TileLineContainer line = new TileLineContainer(30_000_000_000L, List.of(
                tile1,
                tile2,
                tile3
        ));
        assertEquals(0, line.getScore(), "Not clicked tiles");
        tile1.press(400_000_001);
        assertEquals(3, line.getScore(), "Click one tile");
        tile2.press(10_000_000_001L);
        assertEquals(6, line.getScore(), "Click two tile");
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

    private static Tile getAnonymusTile(long start, long end, TileStatus status) {
        return new Tile() {
            @Override
            public long startTime() {
                return start;
            }

            @Override
            public long endTime() {
                return end;
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
        Tile tile1 = getAnonymusTile(400_000_000, 500_000_000, status1);
        Tile tile2 = getAnonymusTile(10_000_000_000L, 10_800_000_001L, status2);
        Tile tile3 = getAnonymusTile(21_200_000_000L, 21_200_000_001L, status3);
        TileLineContainer line = new TileLineContainer(30_000_000_000L, List.of(
                tile1,
                tile2,
                tile3
        ));
        Collection<TileStatus> collection = new ArrayList<>();
        line.getTileStatistic(collection);
        Collection<TileStatus> retCollection = line.getTileStatistic();
        assertIterableEquals(collection, retCollection, "Different ways to collect tiles");
        assertEquals(3, collection.size(), "Wrong TileStatus size");
        assertTrue(collection.contains(status1), "No tile status in tile chunk");
        assertTrue(collection.contains(status2), "No tile status in tile chunk");
        assertTrue(collection.contains(status3), "No tile status in tile chunk");
    }

    @Test
    @Order(4)
    public void clickTest() {
        ClickCollectorTile tile1 = new ClickCollectorTile(400_000_000, 700_000_000);
        ClickCollectorTile tile2 = new ClickCollectorTile(10_000_000_000L, 11_800_000_000L);
        ClickCollectorTile tile3 = new ClickCollectorTile(21_200_000_000L, 21_200_000_000L);
        TileLineContainer line = new TileLineContainer(30_000_000_000L, List.of(
                tile1,
                tile2,
                tile3
        ));
        line.press(400_000_001);
        line.release(400_000_383);
        line.press(399_000_001);
        line.release(399_000_545);
        assertIterableEquals(List.of(400_000_001L), tile1.pressTime, "Line was clicked with this time mark");
        assertIterableEquals(List.of(400_000_383L), tile1.realizeTime, "Line was realized with this time mark");
        assertIterableEquals(List.of(), tile2.pressTime, "No click on this tile");
        assertIterableEquals(List.of(), tile2.realizeTime, "No realize on this tile");
        assertIterableEquals(List.of(), tile3.pressTime, "No click on this tile");
        assertIterableEquals(List.of(), tile3.realizeTime, "No realize on this tile");
        assertEquals(1, line.getMissCount(), "One miss click, 2 click in tile");
        line.press(10_900_000_000L);
        line.release(10_800_000_383L);
        line.press(29_999_000_000L);
        line.release(29_399_000_000L);
        assertIterableEquals(List.of(400_000_001L), tile1.pressTime, "Line was clicked with this time mark");
        assertIterableEquals(List.of(400_000_383L), tile1.realizeTime, "Line was realized with this time mark");
        assertIterableEquals(List.of(10_900_000_000L), tile2.pressTime, "Line was clicked with this time mark");
        assertIterableEquals(List.of(10_800_000_383L), tile2.realizeTime, "Line was realized with this time mark");
        assertIterableEquals(List.of(), tile3.pressTime, "No click on this tile");
        assertIterableEquals(List.of(), tile3.realizeTime, "No realize on this tile");
        assertEquals(2, line.getMissCount(), "One miss click, 2 click in tile");
    }

    @Test
    @Order(5)
    public void overlapTest() {
        assertThrows(IllegalArgumentException.class,
                () ->
                        new TileLineContainer(
                                1_000_000_000L,
                                List.of(
                                        new DefaultTile(1000),
                                        new DefaultTile(300_000_999))
                        ),
                "expect Overlap");
        assertDoesNotThrow(() ->
                        new TileLineContainer(
                                100_000_000L,
                                List.of(
                                        new DefaultTile(1000),
                                        new DefaultTile(300_000_999))
                        ),
                "expect Overlap");
    }

}
