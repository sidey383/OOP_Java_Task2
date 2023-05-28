package ru.sidey383.task2.model.game.level.line.tile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DefaultTileTest {

    private static final String[] tileJsons = new String[]{
            "{\"type\":\"DefaultTile\",\"startTime\":100}",
            "{\"type\":\"DefaultTile\",\"startTime\":545}",
            "{\"type\":\"DefaultTile\",\"startTime\":2345}",
            "{\"type\":\"DefaultTile\",\"startTime\":612}",
            "{\"type\":\"DefaultTile\",\"startTime\":6123}"
    };

    private static final Long[] startTime = new Long[]{
            100L, 545L, 2345L, 612L, 6123L
    };

    private static final Long[] endTime = new Long[] {
            300_000_000 + 100L, 300_000_000 + 545L, 300_000_000 + 2345L, 300_000_000 + 612L, 300_000_000 + 6123L
    };

    @Test
    @Order(0)
    public void loadTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < tileJsons.length; i++) {
            Tile t = mapper.readValue(tileJsons[i], Tile.class);
            assertEquals(DefaultTile.class, t.getClass(), "Wrong class. Load tile "+ tileJsons[i]);
            assertEquals(startTime[i], t.startTime(), "Wrong start time. Load tile "+ tileJsons[i]);
            assertEquals(endTime[i], t.endTime(), "Wrong end time. Load tile "+ tileJsons[i]);
            assertEquals(TileType.DEFAULT, t.type(), "Wrong type. Load tile "+ tileJsons[i]);
        }
    }

    @Test
    @Order(1)
    public void saveTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < tileJsons.length; i++) {
            Tile t = new DefaultTile(startTime[i]);
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
        Tile t = new DefaultTile(100_000);
        TileStatus status = t.getStatus();
        assertFalse(status.clicked(), "Tile was not clicked");
        assertEquals(0, status.score(), "Tile was not clicked, wrong score");
        t.press(100);
        status = t.getStatus();
        assertFalse(status.clicked(), "Tile was clicked early");
        assertEquals(0, status.score(), "Tile was clicked early, wrong score");
        t.press(100_000 + 300_000_000 + 100);
        status = t.getStatus();
        assertFalse(status.clicked(), "Tile was clicked late");
        assertEquals(0, status.score(), "Tile was clicked late, wrong score");
        t.press(100_000 + 2342);
        status = t.getStatus();
        assertTrue(status.clicked(), "Tile was clicked");
        assertEquals(3, status.score(), "Tile was clicked, wrong score");
    }

}
