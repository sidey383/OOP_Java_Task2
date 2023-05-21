package ru.sidey383.task2.view.game.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ru.sidey383.task2.view.game.DrawnTile;
import ru.sidey383.task2.view.game.DrawnTileType;
import ru.sidey383.task2.view.game.TimeProvider;

public class GameCanvas extends Canvas {

    private static final Paint FRAMES_PAINT = new Color(1, 1, 1, 0.7);

    private static final Paint TILES_PAINT = new Color(0, 0, 0, 1);

    private static final Paint LONG_TILES_PAINT = new Color(0, 0.5, 0.8, 1);

    private TimeProvider timeProvider;

    private DrawnTile[][] tiles;

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double minWidth(double height) {
        return 200D;
    }

    @Override
    public double minHeight(double width) {
        return 200D;
    }

    @Override
    public void resize(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public void updateTiles(DrawnTile[][] nTiles) {
        synchronized (this) {
            this.tiles = nTiles;
        }
    }

    public void setTimeAdapter(TimeProvider adapter) {
        synchronized (this) {
            this.timeProvider = adapter;
        }
    }

    public void update(long timeNS) {
        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        graphicsContext.clearRect(0, 0, width, height);
        synchronized (this) {
            if (timeProvider == null || tiles == null)
                return;
            long time = timeProvider.getRelativeFromNano(timeNS);
            long timeToShow = timeProvider.getTimeToShow();
            int lineCount = tiles.length;
            graphicsContext.setFill(FRAMES_PAINT);
            graphicsContext.fillRect(0, 0, 0.05 * width / lineCount, height);
            for (int  i = 1; i < lineCount; i++) {
                double x = (i - 0.05) * width / lineCount;
                double lWidth = 0.1 * width / lineCount;
                graphicsContext.fillRect(x, 0, lWidth, height);
            }
            graphicsContext.fillRect(width * (lineCount - 0.05) / lineCount, 0, 0.05 * width / lineCount, height);
            graphicsContext.setFill(TILES_PAINT);
            for (int i = 0; i < lineCount; i++) {
                double x = (i + 0.1) * width / lineCount;
                double tWidth = 0.8 * width / lineCount;
                double xLong = (i + 0.45) * width / lineCount;
                double tWidthLong = 0.1 * width / lineCount;
                for (DrawnTile t : tiles[i]) {
                    double pose1 = 1 - (double) (t.endTime() - time) / timeToShow;
                    double pose2 = (double) (t.endTime() - t.startTime()) / timeToShow;
                    graphicsContext.fillRoundRect(
                           x, height * pose1,
                           tWidth,  height * pose2,
                           width/20, height/20);
                    if (t.type() == DrawnTileType.LONG) {
                        graphicsContext.setFill(LONG_TILES_PAINT);
                        double pose1Long = pose1 + (pose2)/10;
                        double pose2Long = 8 * pose2 /10;
                        graphicsContext.fillRect(
                                xLong, height * pose1Long,
                                tWidthLong, height * pose2Long
                        );
                        graphicsContext.setFill(TILES_PAINT);
                    }
                }
            }
        }
    }

}
