package com.pumpkin.dgx_super_boy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FlashScreen implements Screen {
    private final Image splashImage;
    private final Stage stage;

    public FlashScreen(IFlashScreenControl control) {
        stage = new Stage(new ScreenViewport());
        splashImage = new Image();
        splashImage.setScaling(Scaling.fit);
        final TextureRegion textureRegion = loadRegion("title.png");
        splashImage.setDrawable(new TextureRegionDrawable(textureRegion));

        //图片大小
        splashImage.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        //居中展示
        splashImage.setPosition(stage.getWidth() / 2 - splashImage.getWidth() / 2,
                stage.getHeight() / 2 - splashImage.getHeight() / 2);

        RunnableAction run = Actions.run(new Runnable() {
            @Override
            public void run() {
                if (control != null) {
                    control.done();
                }
            }
        });

        stage.addAction(Actions.sequence(Actions.delay(0.1F), run));
        stage.addActor(splashImage);
    }

    TextureRegion loadRegion(String name) {
        Texture texture = new Texture(name);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        return new TextureRegion(texture);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.getViewport().apply(true);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }

    public interface IFlashScreenControl {
        void done();
    }


}
