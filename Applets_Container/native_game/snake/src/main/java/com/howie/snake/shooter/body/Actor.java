package com.howie.snake.shooter.body;


import android.graphics.Canvas;

public abstract class Actor extends Body {

    /**
     * 旋转角度
     */
    public float rotationAngle;

    /**
     * 碰撞半径
     */
    final float collisionRadius;

    Actor(float _collisionRadius) {
        collisionRadius = _collisionRadius;
    }

    public abstract void act(Canvas mCanvas);

    public boolean isCollided(Actor other) {
        return getDistance(other) < this.collisionRadius + other.collisionRadius;
    }
}