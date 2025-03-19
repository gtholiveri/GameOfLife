package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Panner extends InputAdapter {
    private final Vector3 lastTouch; // Store last mouse position
    private final OrthographicCamera camera;


    // Variables for mouse dragging
    private boolean isDragging = false;
    Vector2 lastMousePos = new Vector2(); // Last mouse position

    public Panner(OrthographicCamera camera) {
        this.camera = camera;
        lastTouch = new Vector3();

    }

    public void handleKeyPanInput() {
        float zoomSpeed = 0.02f;
        float panSpeed = 10;

        // Zoom in with Z key (decrease the viewport size)
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            // Gdx.app.log("Panner", "Zooming out");
            camera.zoom *= 1f + zoomSpeed; // Zoom out
        }
        // Zoom out with X key (increase the viewport size)
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            // Gdx.app.log("Panner", "Zooming in");
            camera.zoom *= 1f - zoomSpeed;
        }

        // Pan the camera left or right with arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // Gdx.app.log("Panner", "Panning left");

            camera.position.x -= panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // Gdx.app.log("Panner", "Panning right");

            camera.position.x += panSpeed;
        }
        // Pan the camera up or down with arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // Gdx.app.log("Panner", "Panning up");

            camera.position.y += panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // Gdx.app.log("Panner", "Panning down");

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
