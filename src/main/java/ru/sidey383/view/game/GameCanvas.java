package ru.sidey383.view.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
            for (int i = 0; i < lineCount; i++) {
                double x = i * width / lineCount;
                double tWidth = width / lineCount;
                for (Tile t : tiles[i]) {
                    double pose1 = (double) (t.getEndTime() - time) / timeToShow;
                    double pose2 = (double) (t.getEndTime() - t.getStartTime()) / timeToShow;
                    System.out.println(time + " " + pose1 + " " +pose2);
                    graphicsContext.fillRoundRect(
                           x, height - height * pose1,
                           tWidth,  height * pose2,
                           5, 5);
                }
            }
        }
    }

}
