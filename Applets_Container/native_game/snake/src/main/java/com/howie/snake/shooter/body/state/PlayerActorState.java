package com.howie.snake.shooter.body.state;

import com.howie.snake.shooter.body.PlayerActor;

public abstract class PlayerActorState {
    public abstract void act(PlayerActor parentActor);

    public abstract void displayEffect(PlayerActor parentActor);

    public float getEnemyPlayerActorAngle(PlayerActor parentActor) {
//        final AbstractPlayerActor enemyPlayer = parentActor.group.enemyGroup.player;
//        return atan2(enemyPlayer.yPosition - parentActor.yPosition, enemyPlayer.xPosition - parentActor.xPosition);

        // TODO: 2024/3/2
        return 0;
    }

    public boolean isDamaged() {
        return false;
    }

    boolean isDrawingLongBow() {
        return false;
    }

    boolean hasCompletedLongBowCharge(PlayerActor parentActor) {
        return false;
    }
}
