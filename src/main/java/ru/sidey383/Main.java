package ru.sidey383;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey383.control.TimeAdapter;
import ru.sidey383.model.game.tile.Tile;
import ru.sidey383.model.game.tile.TileStatus;
import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.View;
import ru.sidey383.view.game.GameView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View(primaryStage);
        SceneControllerFactory<? extends GameView> factory = view.getFactory(GameView.class);
        GameView gameView = factory.createScene();
        view.setScene(gameView);
        gameView.setTimeAdapter(new TimeAdapter() {

            private final long startMS = System.currentTimeMillis();

            private final long startNano = System.nanoTime();

            @Override
            public long getRelativeFromNano(long timeNS) {
                return (timeNS - startNano) / 1000000;
            }

            @Override
            public long getRelativeFromMS(long timeMS) {
                return timeMS - startMS;
            }

            @Override
            public long getTimeToShow() {
                return 500;
            }
        });
        //gameView.showScore();
        gameView.updateTiles(new Tile[][] {
                {tile(1000), tile(1500), tile(1720), tile(2450), tile(4000)},
                {tile(3300), tile(3600), tile(1720), tile(2450), tile(4000)},
                {tile(2400), tile(2800), tile(1720), tile(2450), tile(4000)},
                {tile(6400), tile(3200), tile(6800), tile(10020), tile(11450), tile(12000)}
        });
        gameView.startRender();

    }

    private static Tile tile(long t) {
        return new SomeTile(t);
    }

    private static class SomeTile implements Tile {

        private long time;

        public SomeTile(long time) {
            this.time = time + 3000;
        }

        @Override
        public long getStartTime() {
            return time;
        }

        @Override
        public long getEndTime() {
            return time + 300;
        }

        @Override
        public TileStatus getStatus() {
            return null;
        }

        @Override
        public void press(long relativeTime) {

        }

        @Override
        public void release(long relativeTime) {

        }
    }

}