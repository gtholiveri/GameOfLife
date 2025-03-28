package com.GameOfLife;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main implements ApplicationListener {

    // stages
    private GameStage gameStage;
    private Stage uiStage;

    private final ArgParser argParser;

    private final int UI_HEIGHT = 300;

    public Main(String[] args) {
        argParser = new ArgParser(args);
    }

    @Override
    public void create() {
        int scrWidth = Gdx.graphics.getWidth();
        int scrHeight = Gdx.graphics.getHeight();

        // camera/viewport setup for gameStage
        OrthographicCamera gameCamera = new OrthographicCamera();
        FitViewport gameViewport = new FitViewport(1000, 1000, gameCamera);

        gameStage = new GameStage(gameViewport, argParser, UI_HEIGHT);


        // camera/viewport setup for uiStage
        OrthographicCamera uiCamera = new OrthographicCamera();
        ScreenViewport uiViewport = new ScreenViewport(uiCamera);
        uiStage = new UIStage(uiViewport, gameStage.getEngine(), gameStage.getTimeCont(), gameStage.getRenderer(), UI_HEIGHT);

        // Set input processors (UI first, then game for priority)
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputMultiplexer(uiStage, /*gameStage,*/ gameStage.getPanner()));

        // oddly, this is necessary to start the board off aligned in the right place
        resize(scrWidth, scrHeight);
    }

    @Override
    public void render() {

        gameStage.getViewport().apply();
        gameStage.draw();

        uiStage.getViewport().apply();
        uiStage.draw();
        uiStage.act();

/*
        Gdx.app.log("Camera", "World X Pos: " + gameStage.getViewport().getCamera().position.x);
        Gdx.app.log("Camera", "World Y Pos: " + gameStage.getViewport().getCamera().position.y);

        Gdx.app.log("Viewport", "World Width: " + gameStage.getViewport().getWorldWidth());
        Gdx.app.log("Viewport", "World Height: " + gameStage.getViewport().getWorldHeight());
        Gdx.app.log("Viewport", "Bottom Gutter: " + gameStage.getViewport().getBottomGutterHeight());
        Gdx.app.log("Viewport", "Top Gutter: " + gameStage.getViewport().getTopGutterHeight());

        Gdx.app.log("Window", "Screen Width: " + Gdx.graphics.getWidth());
        Gdx.app.log("Window", "Screen Height: " + Gdx.graphics.getHeight());

        float actualHeight = gameStage.getRenderer().getBoardActualHeight(gameStage.getEngine().getBoard());
        Gdx.app.log("Board", "Actual Height: " + actualHeight);

        Gdx.app.log("Camera", "Zoom: " + ((OrthographicCamera) gameStage.getViewport().getCamera()).zoom);

        System.out.println("\n\n");
*/

    }

    @Override
    public void resize(int width, int height) {

        // get game viewport and camera
        Viewport gVP = gameStage.getViewport();
        OrthographicCamera gCam = (OrthographicCamera) gameStage.getViewport().getCamera();

        // get ui viewport and camera
        Viewport uiVP = uiStage.getViewport();
        OrthographicCamera uiCam = (OrthographicCamera) uiStage.getViewport().getCamera();

        // control area of game viewport
        gVP.apply(true);
        gVP.setScreenBounds(0, UI_HEIGHT, width, height/* - UI_HEIGHT*/);


        //gVP.update(width, height, true);
        gVP.setWorldWidth(width);
        gVP.setWorldHeight(height/* - UI_HEIGHT*/);

        // get screen width/height
        int scrWidth = Gdx.graphics.getWidth();
        int scrHeight = Gdx.graphics.getHeight();

        // reset zoom on resize
        gameStage.getPanner().zoomToFit();

        // get world position for a horizontally AND vertically centered camera

        float actualHeight = gameStage.getRenderer().getBoardActualHeight(gameStage.getEngine().getBoard());

        float x = (scrWidth / 2) /* - (gVP.getWorldWidth() / 2)*/;
        float y = (actualHeight + UI_HEIGHT) / (2);

        Vector3 centeredPos = new Vector3(x, y, 1);
        //centeredPos = gVP.unproject(centeredPos);

        // ncenteredWorldPos.y -= UI_HEIGHT / (2 * gCam.zoom);


//        float xPos = gVP.getWorldWidth() / 2;
//        float actualHeight = gameStage.getRenderer().getBoardActualHeight(gameStage.getEngine().getBoard());
//        float yPos = (actualHeight / 2) + ((float) UI_HEIGHT / 2);


        // set that camera position
        gCam.position.x = /*xPos;*/ centeredPos.x;
        gCam.position.y = /*yPos;*/ centeredPos.y;
        gCam.update();

        uiVP.apply(true);
        uiVP.update(width, height, true);

    }

    @Override
    public void dispose() {
        gameStage.dispose();
        uiStage.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
