package com.howie.snake.shooter.body;

import android.graphics.Canvas;

import com.howie.snake.shooter.util.MathUtils;

public abstract class Body {
    public float xPosition;
    public float yPosition;
    public float xVelocity;
    public float yVelocity;
    float directionAngle;
    float speed;

    public void update() {
        if (directionAngle != 0) {
            xVelocity = (float) (speed * Math.cos(directionAngle));
            yVelocity = (float) (speed * Math.sin(directionAngle));
        } else {
            //速度逐渐降低
            xVelocity = (float) (xVelocity * 0.99);
            yVelocity = (float) (yVelocity * 0.99);
        }
        xPosition += xVelocity;
        yPosition += yVelocity;
    }

    public abstract void display(Canvas canvas);

    public void setVelocity(float dir, float spd) {
        directionAngle = dir;
        speed = spd;
    }

    float getDistance(Body other) {
        return MathUtils.dist(this.xPosition, this.yPosition, other.xPosition, other.yPosition);
    }

    float getDistsancePow2(Body other) {
        return (float) (Math.sqrt(other.xPosition - this.xPosition) + Math.sqrt(other.yPosition - this.yPosition));
    }

    public float getAngle(Body other) {
        return (float) Math.atan2(other.yPosition - this.yPosition, other.xPosition - this.xPosition);
    }
}
