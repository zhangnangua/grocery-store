package com.pumpkin.dgx_super_boy.rocker;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class RockerManager {


    private void walkRock() {
        Pixmap pixmap1 = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap1.setColor(1f, 0.6f, 0.3f, 1);
        pixmap1.fillRectangle(0, 0, pixmap1.getWidth(), pixmap1.getHeight());
        Texture img1 = new Texture(pixmap1);
        pixmap1.dispose();
        int widthAndHeight = 300;
        Pixmap pixmap2 = new Pixmap(widthAndHeight, widthAndHeight, Pixmap.Format.RGBA8888);
        pixmap2.setColor(0, 0, 0, 1);
        pixmap2.drawCircle(widthAndHeight / 2, widthAndHeight / 2, widthAndHeight / 2); // 圆心放在图形中心，半径刚好贴着图形边框
        Texture img2 = new Texture(pixmap2);
        pixmap2.dispose();
        Touchpad.TouchpadStyle pad_style = new Touchpad.TouchpadStyle();
        pad_style.knob = new TextureRegionDrawable(new TextureRegion(img1)); // 摇杆
        pad_style.background = new TextureRegionDrawable(new TextureRegion(img2)); // 背景。

        final Touchpad touchpad = new Touchpad(0f, pad_style);
        touchpad.setSize(widthAndHeight, widthAndHeight);
        touchpad.setPosition(
                (float) Gdx.graphics.getWidth() / 2 - (float) widthAndHeight / 2,
                (float) Gdx.graphics.getHeight() / 2 - (float) widthAndHeight / 2);
        touchpad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //消耗事件
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);

                // knob相对touchpad左下角的位置
                float knobX = touchpad.getKnobX();
                float knobY = touchpad.getKnobY();
                // knob依touchpad的中心点向右水平线，
                float knobPercentX = touchpad.getKnobPercentX(); // 手指离开中心向左趋于-1，向右趋于1
                float knobPercentY = touchpad.getKnobPercentY(); // 手指离开中心向下趋于1，向上趋于-1

                Log.e("zxfzxf", "knobX：" + knobX);
                Log.e("zxfzxf", "knobY：" + knobY);
                Log.e("zxfzxf", "knobPercentX：" + knobPercentX);
                Log.e("zxfzxf", "knobPercentY：" + knobPercentY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

            }
        });

        stage.addActor(touchpad);

        Gdx.input.setInputProcessor(stage);
    }
}
