package com.howie.snake.shooter.bg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.howie.snake.BuildConfig;
import com.howie.snake.shooter.GameEngine;

public abstract class BackgroundLine {
    private static final String TAG = "BackgroundLine";
    float position;
    float velocity;
    GameEngine.Param param;

    BackgroundLine(float initialPosition, GameEngine.Param param) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "BackgroundLine () -> initialPosition is " + initialPosition);
        }
        position = initialPosition;
        this.param = param;
    }

    void update(float acceleration) {
        position += velocity;
        velocity += acceleration;
        if (position < 0.0 || position > getMaxPosition()) velocity = -velocity;
    }

    abstract void display(Canvas canvas, Paint paint);

    abstract float getMaxPosition();
}
