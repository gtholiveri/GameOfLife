package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;


public class BoardRenderer implements Disposable {
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private Texture aliveCell;
    private Texture deadCell;
    private final Texture background;


    public BoardRenderer(Viewport viewport, String style, SpriteBatch batch) {
        this.viewport = viewport;

        camera = (OrthographicCamera) viewport.getCamera();

        aliveCell = new Texture(Gdx.files.internal("aliveCell-" + style + ".png"));

        deadCell = new Texture(Gdx.files.internal("deadCell-" + style + ".png"));

        this.batch = batch;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY); // Replace RED with your desired color
        pixmap.fill();

        background = new Texture(pixmap);
    }

    public void setStyle(String style) {
        aliveCell = new Texture(Gdx.files.internal("aliveCell-" + style + ".png"));
        deadCell = new Texture(Gdx.files.internal("deadCell-" + style + ".png"));

    }


    public void renderBoard(boolean[][] board) {

        batch.setProjectionMatrix(camera.combined);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float cellSize = Math.min(worldHeight / board.length, worldWidth / board[0].length);

        viewport.apply();
        batch.begin();
        // draw in background
        batch.draw(background, 0, 0, board[0].length * cellSize, board.length * cellSize);

        for (int r = board.length - 1; r >= 0; r--) {
            for (int c = 0; c < board[r].length; c++) {
                float x = c * cellSize;
                float y = (board.length - r - 1) * cellSize;
                if (board[r][c]) {
                    batch.draw(aliveCell, x, y, cellSize, cellSize);
                } else {
                    batch.draw(deadCell, x, y, cellSize, cellSize);
                }
            }
        }
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        aliveCell.dispose();
        deadCell.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public int getStyleInd() {
        String style = getStyle();
        // stylePicker.setItems("Default", "Sea Lantern", "Redstone Lamp", "Moss");
        switch (style) {
            case "seaLantern":
                return 1;
            case "redstone":
                return 2;
            case "moss":
                return 3;
            default:
                // includes explicit "default"
                return 0;
        }
    }

    public String getStyle() {
        String path = aliveCell.toString();

        return path.substring(path.lastIndexOf("-") + 1, path.lastIndexOf("."));
    }

    public float getBoardActualHeight(boolean[][] board) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldWidth();

        float cellSize = Math.min(worldHeight / board.length, worldWidth / board[0].length);

        return cellSize * board[0].length;
    }
}
