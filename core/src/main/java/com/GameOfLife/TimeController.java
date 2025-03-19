package com.GameOfLife;

import com.badlogic.gdx.Gdx;

public class TimeController {
    private float interval;
    private float timeAccumulator;

    public TimeController(float interval) {
        this.interval = interval;
        timeAccumulator = 0f;
    }

    public boolean timeToGo() {
        if (timeAccumulator > interval) {
            timeAccumulator = 0;
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        timeAccumulator += Gdx.graphics.getDeltaTime();
    }


}
