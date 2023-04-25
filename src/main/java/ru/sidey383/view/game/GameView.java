package ru.sidey383.view.game;

import ru.sidey383.read.ZIPGameReader;
import ru.sidey383.view.Scene;

import java.util.Map;

public abstract class GameView extends Scene implements GameRender {

    public abstract void start();

    public abstract void stop();

    public abstract void showScore(String scoreStr);

    public abstract Map<String, ZIPGameReader.DataReader> getReaders();

    public abstract Map<String, Object> setData() throws Exception;

}
