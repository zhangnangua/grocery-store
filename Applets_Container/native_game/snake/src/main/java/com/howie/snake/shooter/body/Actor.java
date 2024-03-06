package com.howie.snake.shooter.body;


public abstract class Actor extends Body {

    /**
     * 旋转角度
     */
    float rotationAngle;

    /**
     * 碰撞半径
     */
    final float collisionRadius;

    Actor(float _collisionRadius) {
        collisionRadius = _collisionRadius;
    }

    public abstract void act();

    public boolean isCollided(Actor other) {
        return getDistance(other) < this.collisionRadius + other.collisionRadius;
    }
}