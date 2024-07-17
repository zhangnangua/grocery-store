package com.pumpkin.dgx.game;

import android.util.Log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    private static final String TAG = "MyGdxGame";
    SpriteBatch batch;
    private World world;
    private Stage stage;
    private Box2DDebugRenderer debugRenderer;
    private int count;

    static public int ballNbr;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        ballNbr = 0;
        count = 0;
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new B2dContactListener());
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(stage);
        float ratio = (float) (Gdx.graphics.getWidth()) / (float) (Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        stage.getCamera().position.set(0, 0, 10);
        stage.getCamera().lookAt(0, 0, 0);
        stage.getCamera().viewportWidth = 10;
        stage.getCamera().viewportHeight = 10 / ratio;
        debugRenderer = new Box2DDebugRenderer();

        GearActor gearActor = new GearActor(world, 0, 0, 6, 6);
        stage.addActor(gearActor);


        new WindowsFrame(world, stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);

        generateBall();
    }

    @Override
    public void render() {
        Log.d(TAG, "render () -> " + ballNbr);
        Gdx.gl.glClearColor(0.5f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        //debugRenderer.render(world, stage.getCamera().combined);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        if (ballNbr < 100) {
            generateBall();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void generateBall() {
        Random rand = new Random();
        Ball ball = new Ball(world, (float) ((rand.nextInt(60) - 30)) / 10, 9);
        stage.addActor(ball);
        ballNbr++;
        Gdx.app.debug("generatBalls", "Balls:" + ballNbr);
    }
}
