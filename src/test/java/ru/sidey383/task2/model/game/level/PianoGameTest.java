package ru.sidey383.task2.model.game.level;

import org.junit.jupiter.api.*;
import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.level.line.tile.DefaultTile;
import ru.sidey383.task2.model.game.level.line.tile.LongTile;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PianoGameTest {

    private PianoGame game = null;

    private final Map<ClickType, Tile[]> gameTiles = Map.of(
            ClickType.LINE_1, new Tile[] {
                    new DefaultTile(100_000_000L),
                    new LongTile(10_000_000_000L, 20_000_000_000L)
            },
            ClickType.LINE_2, new Tile[]{
                    new DefaultTile(100_000_000L),
                    new LongTile(20_000_000_000L, 20_000_000_000L)
            },
            ClickType.LINE_3, new Tile[]{
                    new DefaultTile(5_000_000_000L),
                    new LongTile(10_000_000_000L, 20_000_000_000L)
            },
            ClickType.LINE_4, new Tile[]{
                    new DefaultTile(100_000_000L),
                    new LongTile(10_000_000_000L, 20_000_000_000L)
            },
            ClickType.LINE_5, new Tile[]{
                    new DefaultTile(100_000_000L),
                    new LongTile(10_000_000_000L, 20_000_000_000L)
            },
            ClickType.LINE_6, new Tile[]{
                    new DefaultTile(100_000_000L),
                    new LongTile(10_000_000_000L, 20_000_000_000L)
            }
    );

    private final long gameTime = 100_000_000_000L;

    private final String gameName = "TestGame";

    @BeforeEach
    public void initGame() {
        game = new PianoGame(gameName, gameTime, gameTiles);
    }

    @Test
    @Order(0)
    public void initTest() {
        assertEquals(gameName, game.name());
        assertArrayEquals(ClickType.values(), game.availableClickTypes());
        assertEquals(0, game.getScore());
        assertEquals(0, game.getMissCount());
        assertEquals(gameTime, game.totalTime());
        assertEquals(1_000_000_000L, game.timeToShow());
        for (var entry : gameTiles.entrySet()) {
            assertIterableEquals(List.of(entry.getValue()), game.getLine(entry.getKey()).getTiles(0, gameTime));
            Collection<Tile> tiles = new ArrayList<>();
            game.getLine(entry.getKey()).getTiles(tiles, 0, gameTime);
            assertIterableEquals(game.getLine(entry.getKey()).getTiles(0, gameTime), tiles);
        }
        Collection<TileStatus> retStatistic = game.getTileStatistic();
        Collection<TileStatus> statistic = new ArrayList<>();
        game.getTileStatistic(statistic);
        assertEquals(12, retStatistic.size());
        for (TileStatus status : retStatistic) {
            assertFalse(status.clicked());
            assertEquals(0, status.score());
        }
        assertIterableEquals(statistic, retStatistic);
    }

    private static void check(PianoGame game, int clicked, Map<Integer, Integer> tileScores) {
        Collection<TileStatus> retStatuses = game.getTileStatistic();
        Collection<TileStatus> statuses = new ArrayList<>();
        game.getTileStatistic(statuses);
        Map<Integer, Integer> actualTileScores = new HashMap<>();
        int actualClicked = 0;
        for (TileStatus status : statuses) {
            actualTileScores.compute(status.score(), (k, v) -> v == null ? 1 : v + 1);
            actualClicked += status.clicked() ? 1 : 0;
        }

        assertEquals(clicked, actualClicked);
        for (var entry : tileScores.entrySet()) {
            assertTrue(actualTileScores.entrySet().contains(entry));
        }
        assertIterableEquals(statuses, retStatuses);
    }

    @Test
    @Order(1)
    public void clickTest() {
        game.start();
        game.press(ClickType.LINE_1, 200_000_000L);
        game.release(ClickType.LINE_1, 300_000_000L);
        game.press(ClickType.LINE_1, 10_000_000_100L);
        assertEquals(5, game.getScore());
        assertEquals(0, game.getMissCount());
        check(game, 2, Map.of(3 , 1, 2, 1));
        game.press(ClickType.LINE_2, 200_000_000L);
        game.release(ClickType.LINE_2, 300_000_000L);
        game.press(ClickType.LINE_2, 10_000_000_100L);
        assertEquals(6, game.getScore());
        assertEquals(1, game.getMissCount());
        check(game, 3, Map.of(3 , 2, 2, 1));
    }

}
