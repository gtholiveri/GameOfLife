package com.GameOfLife;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main implements ApplicationListener {

    //  libgdx classes
    private FitViewport viewport;
    private OrthographicCamera camera;

    // custom classes
    private BoardRenderer renderer;
    private GameEngine engine;
    private ArgParser argParser;
    private Panner panner;
    private TimeController timeCont;

    public Main(String[] args) {
        argParser = new ArgParser(args);
    }

    @Override
    public void create() {
        // camera/viewport setup
        camera = new OrthographicCamera();
        viewport = new FitViewport(1000, 1000, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();


        // renderer initialization
        String style = argParser.getStyle();
        Gdx.app.log("Args", "Style: " + style);
        renderer = new BoardRenderer(viewport, style);

        // board initialization
        boolean centered = argParser.getCentered();
        int rows = argParser.getRows();
        int cols = argParser.getCols();
        String filename = argParser.getPatternFilename();

        Gdx.app.log("Args", "Rows: " + rows);
        Gdx.app.log("Args", "Cols: " + cols);
        Gdx.app.log("Args", "Centered: " + centered);
        Gdx.app.log("Args", "Filename: " + filename);
        boolean[][] board = FileReader.readFile(filename, centered, rows, cols);

        // engine initialization
        int gens = argParser.getGens();
        Gdx.app.log("Args", "Generations: " + gens);

        engine = new GameEngine(board, gens);


        // panner initialization (has the methods that enable panning camera)
        panner = new Panner(camera);
        Gdx.input.setInputProcessor(panner);

        // TimeController initialization
        double interval = argParser.getGenDelay();
        Gdx.app.log("Args", "GenDelay: " + interval);
        timeCont = new TimeController((float) interval);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        panner.handleKeyPanInput();

        timeCont.update();
        if (engine.gensRemain() && timeCont.timeToGo()) {
            engine.advanceGeneration();
            // Gdx.app.log("Engine", "Advanced generation");
            // Gdx.app.log("Engine", "Current generation: " + engine.getCurrGen());
        }

        renderer.renderBoard(engine.getBoard());

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
