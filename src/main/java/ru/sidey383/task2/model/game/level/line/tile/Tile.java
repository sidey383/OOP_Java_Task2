package ru.sidey383.task2.model.game.level.line.tile;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DefaultTile.class),
                @JsonSubTypes.Type(value = LongTile.class)
        }
)
public interface Tile {

    long startTime();

    long endTime();

    TileStatus getStatus();

    void press(long relativeTime);

    void release(long relativeTime);

    TileType type();

}
