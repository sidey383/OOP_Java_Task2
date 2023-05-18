package ru.sidey383.task2.model.game.level.line.tile;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = DefaultTile.class, name = "DefaultTile"), @JsonSubTypes.Type(value = LongTile.class, name = "LongTile")})
public interface Tile {

    long getStartTime();

    long getEndTime();

    TileStatus getStatus();

    void press(long relativeTime);

    void release(long relativeTime);

    TileType getType();

}
