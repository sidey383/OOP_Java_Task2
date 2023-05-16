package ru.sidey383;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey383.control.Controller;
import ru.sidey383.control.TimeAdapter;
import ru.sidey383.model.RootModel;
import ru.sidey383.model.data.game.GameDescription;
import ru.sidey383.model.game.level.line.tile.*;
import ru.sidey383.view.View;
import ru.sidey383.view.game.GameView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameMenuCheck(primaryStage);
    }

    private void jsonTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writer().writeValueAsString(new DefaultTile(1000)));
        System.out.println(mapper.writer().writeValueAsString(new ru.sidey383.model.game.level.line.tile.LongTile(1000, 1200)));
    }

    private void gameMenuCheck(Stage primaryStage) throws Exception {
        View view = new View(primaryStage);
        RootModel model = RootModel.createModel(Path.of(""));
        Controller controller = new Controller(view, model);
        controller.openMenu();
    }

    private void gameStartCheck(Stage primaryStage) throws Exception {
        View view = new View(primaryStage);
        RootModel model = RootModel.createModel(Path.of(""));
        Controller controller = new Controller(view, model);
        model.startGame(new GameDescription() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public URL getGameContainer() {
                return getClass().getResource("/exampleGame.zip");
            }
        });
    }

    private static void viewCheck(Stage primaryStage) throws IOException {
                View view = new View(primaryStage);
        GameView gameView = view.getScene(GameView.class);
        view.setScene(gameView);
        gameView.setTimeAdapter(new TimeAdapter() {

            private final long startNano = System.nanoTime();

            @Override
            public long getRelativeFromNano(long timeNS) {
                return (timeNS - startNano);
            }

            @Override
            public long getTimeToShow() {
                return 2_000_000_000L;
            }
        });
        //gameView.showScore();
        gameView.updateTiles(new Tile[][] {
                {tile(1000_000_000L), tile(1500_000_000L), tile(1720_000_000L), lTile(2450_000_000L), tile(4000_000_000L)},
                {tile(3300_000_000L), tile(3600_000_000L), tile(1720_000_000L), tile(2450_000_000L), tile(4000_000_000L)},
                {tile(2400_000_000L), tile(2800_000_000L), lTile(720_000_000L), tile(3150_000_000L), lTile(4000_000_000L)},
                {tile(6400_000_000L), tile(3200_000_000L), tile(6800_000_000L), tile(10020_000_000L), tile(11450_000_000L), tile(12000_000_000L)}
        });
        gameView.start();
    }

    private static Tile tile(long t) {
        return new SomeTile(t);
    }

    private static Tile lTile(long t) {
        return new LongTile(t);
    }

    private static class SomeTile implements Tile {

        private long time;

        public SomeTile(long time) {
            this.time = time + 300_000_000L;
        }

        @Override
        public long getStartTime() {
            return time;
        }

        @Override
        public long getEndTime() {
            return time + 300_000_000L;
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

        @Override
        public TileType getType() {
            return TileType.DEFAULT;
        }
    }

    private static class LongTile implements Tile {

        private long time;

        public LongTile(long time) {
            this.time = time + 300_000_000L;
        }

        @Override
        public long getStartTime() {
            return time;
        }

        @Override
        public long getEndTime() {
            return time + 800_000_000L;
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

        @Override
        public TileType getType() {
            return TileType.LONG;
        }
    }

}