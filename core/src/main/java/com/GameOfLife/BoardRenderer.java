package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class BoardRenderer implements Disposable {
    private final FitViewport viewport;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Texture aliveCell;
    private final Texture deadCell;

    public BoardRenderer(FitViewport viewport, String style) {
        this.viewport = viewport;

        camera = (OrthographicCamera) viewport.getCamera();

        aliveCell = new Texture(Gdx.files.internal("aliveCell-" + style + ".png"));

        deadCell = new Texture(Gdx.files.internal("deadCell-" + style + ".png"));

        batch = new SpriteBatch();

    }


    public void renderBoard(boolean[][] board) {

        batch.setProjectionMatrix(camera.combined);

        int scrWidth = viewport.getScreenWidth();
        int scrHeight = viewport.getScreenHeight();

        int cellSize = Math.min(scrHeight / board.length, scrWidth / board[0].length);

        batch.begin();
        for (int r = board.length - 1; r >= 0; r--) {
            for (int c = 0; c < board[r].length; c++) {
                int x = c * cellSize;
                int y = (board.length - r - 1)  * cellSize;
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


}
