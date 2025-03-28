package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Panner extends InputAdapter {
    private final Vector3 lastTouch; // Store last mouse position
    private final OrthographicCamera camera;
    private final GameStage gameStage;

    private boolean currentlyZoomingToFit;

    private final float zoomToFitSpeed = 0.2f;

    public Panner(OrthographicCamera camera, GameStage gameStage) {
        this.camera = camera;
        lastTouch = new Vector3();

        currentlyZoomingToFit = false;

        this.gameStage = gameStage;
    }

    public void handleKeyPanInput() {
        float zoomSpeed = 0.02f;
        float panSpeed = 10;

        // Zoom in with Z key (decrease the viewport size)
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom *= 1f + zoomSpeed; // Zoom out
        }
        // Zoom out with X key (increase the viewport size)
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {

            camera.zoom *= 1f - zoomSpeed;
        }

        // Pan the camera left or right with arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {


            camera.position.x -= panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

            camera.position.x += panSpeed;
        }
        // Pan the camera up or down with arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {


            camera.position.y += panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {


            camera.position.y -= panSpeed;
        }

        // Apply the camera update after handling input
        camera.update();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            lastTouch.set(screenX, screenY, 0);
        }
        return true;
    }


    public void zoomToFit() {
        currentlyZoomingToFit = true;
    }

    public  void applyZoomToFit() {
        //
        float maxVisWidth = gameStage.getViewport().getWorldWidth();
        float maxVisHeight = gameStage.getViewport().getWorldHeight() - gameStage.getUIHeight();
        float actualHeight = gameStage.getRenderer().getBoardActualHeight(gameStage.getEngine().getBoard());

        float UI_HEIGHT = gameStage.getUIHeight();

        int scrWidth = Gdx.graphics.getWidth();
        int scrHeight = Gdx.graphics.getHeight();

        float targetZoom = 1f;

        if (maxVisHeight < maxVisWidth) {
            // We can fit LESS stuff in VERTICALLY, so we need to use VERTICAL constraints
            float targVisHeight = actualHeight;
            targetZoom = targVisHeight / (scrHeight - UI_HEIGHT);



        } else if (maxVisWidth < maxVisHeight){
            // We can fit LESS stuff in HORIZONTALLY, so we need to use HORIZONTAL constraints
            float targVisWidth = maxVisWidth;
            targetZoom = targVisWidth / gameStage.getViewport().getWorldWidth();
        }

        if (currentlyZoomingToFit) {
            camera.zoom = Interpolation.smooth.apply(camera.zoom, targetZoom, zoomToFitSpeed);
            camera.update(); // Ensure the camera updates its transformations
        }
//Math.abs(a - b) <= tolerance;
        if (Math.abs(targetZoom - camera.zoom) < 0.005f) {
            currentlyZoomingToFit = false;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 currentTouch = new Vector3(screenX, screenY, 0);

        // Convert screen to world coordinates
        camera.unproject(currentTouch);
        camera.unproject(lastTouch);

        // Calculate movement delta
        float dx = lastTouch.x - currentTouch.x;
        float dy = lastTouch.y - currentTouch.y;

        // Move the camera
        camera.position.add(dx, dy, 0);
        camera.update();

        // Update lastTouch position
        lastTouch.set(screenX, screenY, 0);

        return true;
    }
}
