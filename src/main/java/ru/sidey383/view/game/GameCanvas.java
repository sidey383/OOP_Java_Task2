package ru.sidey383.view.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.sidey383.model.game.tile.Tile;
import ru.sidey383.control.TimeAdapter;

public class GameCanvas extends Canvas {

    private TimeAdapter timeAdapter;

    private Tile[][] tiles;

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

    public void updateTiles(Tile[][] nTiles) {
        synchronized (this) {
            this.tiles = nTiles;
        }
    }

    public void setTimeAdapter(TimeAdapter adapter) {
        synchronized (this) {
            this.timeAdapter = adapter;
        }
    }

    public void update(long timeNS) {
        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        graphicsContext.clearRect(0, 0, width, height);
        synchronized (this) {
            if (timeAdapter == null || tiles == null)
                return;
            long time = timeAdapter.getRelativeFromNano(timeNS);
            long timeToShow = timeAdapter.getTimeToShow();
            int lineCount = tiles.length;
            graphicsContext.setFill(new Color(1, 1, 1, 0.7));
            graphicsContext.fillRect(0, 0, 0.05 * width / lineCount, height);
            for (int  i = 1; i < lineCount; i++) {
                double x = (i - 0.05) * width / lineCount;
                double lWidth = 0.1 * width / lineCount;
                graphicsContext.fillRect(x, 0, lWidth, height);
            }
            graphicsContext.fillRect(width * (lineCount - 0.05) / lineCount, 0, 0.05 * width / lineCount, height);
            graphicsContext.setFill(new Color(0, 0, 0, 1));
            for (int i = 0; i < lineCount; i++) {
                double x = (i + 0.1) * width / lineCount;
                double tWidth = 0.8 * width / lineCount;
                for (Tile t : tiles[i]) {
                    double pose1 = (double) (t.getEndTime() - time) / timeToShow;
                    double pose2 = (double) (t.getEndTime() - t.getStartTime()) / timeToShow;
                    graphicsContext.fillRoundRect(
                           x, height - height * pose1,
                           tWidth,  height * pose2,
                           width/20, height/20);
                }
            }
        }
    }

}
