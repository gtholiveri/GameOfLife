package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStage extends Stage {

    private OrthographicCamera gameCamera;

    private BoardRenderer renderer;

    private ArgParser parser;
    private Panner panner;
    private TimeController timeCont;
    private GameEngine engine;

    private int UI_HEIGHT;

    public GameStage(Viewport viewport, ArgParser parser, int UI_HEIGHT) {

        // call the main Stage constructor
        super(viewport, new SpriteBatch());

        // get the parser up and running
        this.parser = parser;

        // initialize the game camera
        gameCamera = (OrthographicCamera) viewport.getCamera();

        // initialize renderer
        renderer = new BoardRenderer(viewport, parser.getStyle(), (SpriteBatch) this.getBatch());

        // initialize board
        boolean[][] board = getBoardFromFile();

        int gens = parser.getGens();
        log(gens);

        // initialize engine
        engine = new GameEngine(board, gens);

        // initialize TimeController
        double interval = parser.getGenDelay();
        Gdx.app.log("Args", "GenDelay: " + interval);
        timeCont = new TimeController((float) interval);

        // initialize panner with the gameCamera
        panner = new Panner(gameCamera, this);

        this.UI_HEIGHT = UI_HEIGHT;
    }

    private void log(int gens) {
        Gdx.app.log("Args", "Generations: " + gens);
    }

    private static void log(int rows, int cols, boolean centered, String filename) {
        Gdx.app.log("Args", "Rows: " + rows);
        Gdx.app.log("Args", "Cols: " + cols);
        Gdx.app.log("Args", "Centered: " + centered);
        Gdx.app.log("Args", "Filename: " + filename);
    }

    public void update(float delta) {
        gameCamera.update();
    }

    @Override
    public void draw() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        panner.handleKeyPanInput();
        panner.applyZoomToFit();

        timeCont.update();
        if (engine.getIsRunning() && engine.gensRemain() && timeCont.timeToGo()) {
            engine.advanceGeneration();
        }

        renderer.renderBoard(engine.getBoard());


        super.draw(); // Draw any actors if added
    }

    @Override
    public void dispose() {
        renderer.dispose();
        super.dispose();
    }

    public Panner getPanner() {
        return panner;
    }

    private boolean[][] getBoardFromFile() {
        boolean centered = parser.getCentered();
        int rows = parser.getRows();
        int cols = parser.getCols();
        String filename = parser.getPatternFilename();
        log(rows, cols, centered, filename);

        return FileReader.readFile(filename, centered, rows, cols);
    }

    public GameEngine getEngine() {
        return engine;
    }

    public TimeController getTimeCont() {
        return timeCont;
    }

    public BoardRenderer getRenderer() {
        return renderer;
    }

    public int getUIHeight() {
        return UI_HEIGHT;
    }
}
