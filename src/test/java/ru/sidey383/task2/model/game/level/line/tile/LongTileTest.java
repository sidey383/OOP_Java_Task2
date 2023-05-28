package ru.sidey383.task2.model.game.level.line.tile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LongTileTest {

    private static final String[] tileJsons = new String[]{
            "{\"type\":\"LongTile\",\"startTime\":100,\"tileTime\":55554}",
            "{\"type\":\"LongTile\",\"startTime\":545,\"tileTime\":743723}",
            "{\"type\":\"LongTile\",\"startTime\":2345,\"tileTime\":71435}",
            "{\"type\":\"LongTile\",\"startTime\":612,\"tileTime\":61235}",
            "{\"type\":\"LongTile\",\"startTime\":6123,\"tileTime\":6124}"
    };

    private static final Long[] startTime = new Long[]{
            100L, 545L, 2345L, 612L, 6123L
    };

    private static final Long[] endTime = new Long[] {
            55554 + 100L, 743723 + 545L, 71435 + 2345L, 61235 + 612L, 6124 + 6123L
    };

    @Test
    @Order(0)
    public void loadTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < tileJsons.length; i++) {
            Tile t = mapper.readValue(tileJsons[i], Tile.class);
            assertEquals(LongTile.class, t.getClass(), "Wrong class. Load tile "+ tileJsons[i]);
            assertEquals(startTime[i], t.startTime(), "Wrong start time. Load tile "+ tileJsons[i]);
            assertEquals(endTime[i], t.endTime(), "Wrong end time. Load tile "+ tileJsons[i]);
            assertEquals(TileType.LONG, t.type(), "Wrong type. Load tile "+ tileJsons[i]);
        }
    }

    @Test
    @Order(1)
    public void saveTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < tileJsons.length; i++) {
            Tile t = new LongTile(startTime[i], endTime[i] - startTime[i ]);
            String json = mapper.writeValueAsString(t);
            assertEquals(tileJsons[i], json);
        }
    }

    @Test
    @Order(2)
    public void loadSaveTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (String tileJson : tileJsons) {
            Tile t = mapper.readValue(tileJson, Tile.class);
            String json = mapper.writeValueAsString(t);
            assertEquals(tileJson, json);
        }
    }

    @Test
    @Order(3)
    public void clickTest() {
        Tile t = new LongTile(100_000_000, 1_000_000_000);
        TileStatus status = t.getStatus();
        assertFalse(status.clicked(), "Tile was not clicked");
        assertEquals(0, status.score(), "Tile was not clicked, wrong score");

        t = new LongTile(100_000_000, 1_000_000_000);
        t.press(100);
        status = t.getStatus();
        assertFalse(status.clicked(), "Tile was clicked early");
        assertEquals(0, status.score(), "Tile was clicked early, wrong score");

        t = new LongTile(100_000_000, 1_000_000_000);
        t.press(1_200_000_000);
        status = t.getStatus();
        assertFalse(status.clicked(), "Tile was clicked late");
        assertEquals(0, status.score(), "Tile was clicked late, wrong score");

        t = new LongTile(100_000_000, 1_000_000_000);
        t.press(100_000_200);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was clicked");
        assertEquals(2, status.score(), "Tile was clicked, but not realize");
        t.release(400_000_500);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was clicked");
        assertEquals(3, status.score(), "Tile was clicked, and realized after 300_000_300 ns");

        t = new LongTile(100_000_000, 1_000_000_000);

        t.release(100_000_500);
        t.press(100_000_200);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was clicked");
        assertEquals(2, status.score(), "Tile was clicked, but not realize");

        t.release(400_000_500);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was just released");
        assertEquals(3, status.score(), "Tile was just released");

        t = new LongTile(100_000_000, 1_000_000_000);

        t.release(100_000_500);
        status = t.getStatus();
        assertFalse(status.clicked(), "Tile was released");
        assertEquals(0, status.score(), "Tile was released, but not clicked");

        t.press(100_000_200);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was clicked");
        assertEquals(2, status.score(), "Tile was clicked, but not realize");

        t.release(900_000_500);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was clicked");
        assertEquals(6, status.score(), "Tile was clicked, and realized after 800_000_300 ns");
    }
}
