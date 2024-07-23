package com.pumpkin.dgx_super_boy;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;

public interface IGameController {
    void restart();

    void eventHitPlayer(Enemy enemy);

    void eventHitEnemy(Enemy enemy);

    void eventHitBullet(float x, float y, float vx, float vy);

    void eventGameOver(boolean win);
}
