package com.pumpkin.dgx_super_boy.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.pumpkin.dgx_super_boy.Assets;
import com.pumpkin.dgx_super_boy.Enemy;
import com.pumpkin.dgx_super_boy.IGameController;
import com.pumpkin.dgx_super_boy.Model;
import com.pumpkin.dgx_super_boy.View;

public class GameScreen implements Screen, IGameController {
    static Vector2 temp = new Vector2();

    View view;
    Model model;

    @Override
    public void show() {
        model = new Model(this);
        view = new View(model);
    }

    @Override
    public void render(float d) {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f) * model.getTimeScale();
        if (delta > 0) {
            model.update(delta);
            view.update(delta);
        }
        view.render();
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void restart() {
        model.restart();
        view.restart();
    }

    public void eventHitPlayer(Enemy enemy) {
        Assets.SoundEffect.hurtPlayer.play();
        if (view.player.hp > 0 && view.player.view.hitAnimation != null) {
            AnimationState.TrackEntry entry = view.player.view.animationState.setAnimation(1, view.player.view.hitAnimation, false);
            entry.setTrackEnd(view.player.view.hitAnimation.getDuration());
        }
    }

    public void eventHitEnemy(Enemy enemy) {
        Assets.SoundEffect.hurtAlien.play();
        if (enemy.view.hitAnimation != null) {
            AnimationState.TrackEntry entry = enemy.view.animationState.setAnimation(1, enemy.view.hitAnimation, false);
            entry.setTrackEnd(enemy.view.hitAnimation.getDuration());
        }
    }

    public void eventHitBullet(float x, float y, float vx, float vy) {
        Vector2 offset = temp.set(vx, vy).nor().scl(15 * Model.scale);
        view.hits.add(View.bulletHitTime);
        view.hits.add(x + offset.x);
        view.hits.add(y + offset.y);
        view.hits.add(temp.angle() + 90);
        Assets.SoundEffect.hit.play();
    }

    public void eventGameOver(boolean win) {
        if (!view.ui.splashTable.hasParent()) {
            view.ui.showSplash(view.assets.gameOverRegion, win ? view.assets.youWinRegion : view.assets.youLoseRegion);
            view.ui.inputTimer = win ? 5 : 1;
        }
        view.jumpPressed = false;
        view.leftPressed = false;
        view.rightPressed = false;
    }
}
