package com.pumpkin.dgx_super_boy.rocker;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class RockerManager {
    public static final int STATE_NULL = -1;
    public static final int STATE_WALK_LEFT = 1;
    public static final int STATE_WALK_RIGHT = 2;
    public static final int STATE_WALK_UP = 3;
    public static final int STATE_ATTACK_SHOOT = 5;
    public static final int STATE_ATTACK_NOT_SHOOT = 6;


    private final Stage stage;
    public final WalkState walkState = new WalkState();
    public final AttackState attackState = new AttackState();

    public RockerManager(Stage stage) {
        this.stage = stage;
        walkRock();
        attackRock();
        Gdx.input.setInputProcessor(stage);
    }

    private void attackRock() {
        int widthHeight = 300;
        int position = 100;
        float centerP = widthHeight / 2;
        Touchpad.TouchpadStyle pad_style = new Touchpad.TouchpadStyle();
        pad_style.knob = new TextureRegionDrawable(loadRegion("rock/rocker_area.png")); // 摇杆
        pad_style.background = new TextureRegionDrawable(loadRegion("rock/rocker_bg.png")); // 背景
        final Touchpad touchpad = new Touchpad(0f, pad_style);
        touchpad.setSize(widthHeight, widthHeight);
        // position  基于左下角
        touchpad.setPosition(position, position);
        touchpad.setSize(300, 300);
        // position  基于左下角
        touchpad.setPosition(Gdx.graphics.getWidth() - 400, 100);
        touchpad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                attackState.setShootState(STATE_ATTACK_SHOOT);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                // knob依touchpad的中心点向右水平线，
                float knobPercentX = touchpad.getKnobPercentX(); // 手指离开中心向左趋于-1，向右趋于1
                float knobPercentY = touchpad.getKnobPercentY(); // 手指离开中心向上趋于1，向下趋于-1
//                Log.d("RockerManager", "touchDragged () -> " + touchpad.getKnobX() + " , " + touchpad.getKnobY() + " , " + knobPercentX + " , " + knobPercentY);
//                Log.d("RockerManager", "touchDragged () -> " + x + " , " + y);
//                Log.d("RockerManager", "touchDragged () -> " + touchpad.getPrefWidth() + " , " + touchpad.getPrefHeight());

                attackState.setDegrees(rocker(centerP - touchpad.getKnobX(), touchpad.getKnobY() - centerP));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                attackState.setShootState(STATE_ATTACK_NOT_SHOOT);
            }
        });

        stage.addActor(touchpad);
    }

    private void walkRock() {
        int widthHeight = 300;
        int position = 100;
        Touchpad.TouchpadStyle pad_style = new Touchpad.TouchpadStyle();
        pad_style.knob = new TextureRegionDrawable(loadRegion("rock/rocker_area.png")); // 摇杆
        pad_style.background = new TextureRegionDrawable(loadRegion("rock/rocker_bg.png")); // 背景
        final Touchpad touchpad = new Touchpad(0f, pad_style);
        touchpad.setSize(widthHeight, widthHeight);
        // position  基于左下角
        touchpad.setPosition(position, position);
        touchpad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                // knob依touchpad的中心点向右水平线，
                float knobPercentX = touchpad.getKnobPercentX(); // 手指离开中心向左趋于-1，向右趋于1
                float knobPercentY = touchpad.getKnobPercentY(); // 手指离开中心向上趋于1，向下趋于-1

                if (knobPercentY > 0.8) {
                    walkState.setUpDownState(STATE_WALK_UP);
                } else {
                    walkState.setUpDownState(STATE_NULL);
                }

                if (knobPercentX < -0.8) {
                    walkState.setDirectionState(STATE_WALK_LEFT);
                } else if (knobPercentX > 0.8) {
                    walkState.setDirectionState(STATE_WALK_RIGHT);
                } else {
                    walkState.setDirectionState(STATE_NULL);
                }

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                walkState.setDirectionState(STATE_NULL);
                walkState.setUpDownState(STATE_NULL);
            }
        });

        stage.addActor(touchpad);
    }

    private double rocker(float lenX, float lenY) {
//        // 两点在X轴的距离
//        float lenX = (float) (touchPoint.x - centerPoint.x);
//        // 两点在Y轴距离
//        float lenY = (float) (touchPoint.y - centerPoint.y);
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        double radian = Math.acos(lenX / lenXY) * (lenY > 0 ? -1 : 1);

        // 回调 返回参数
        // 大于一定值 才进行回调 防止误触
        // 计算角度
//        final int border = 50;
//        if (lenXY > border) {
        // 0 - 360
        double degrees = Math.toDegrees(radian)  + 180;
//        Log.d("RockerManager", "getRockerPositionPoint () -> " + degrees);
//        }
        return degrees;
    }

    TextureRegion loadRegion(String name) {
        Texture texture = new Texture(name);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        return new TextureRegion(texture);
    }

    public static class WalkState {
        private int directionState = STATE_NULL;

        private int upDownState = STATE_NULL;

        private IState state;

        public void setState(IState state) {
            this.state = state;
        }

        public int getUpDownState() {
            return upDownState;
        }

        public void setUpDownState(int upDownState) {
            this.upDownState = upDownState;
            callback();
        }

        public int getDirectionState() {
            return directionState;
        }

        public void setDirectionState(int directionState) {
            this.directionState = directionState;
            callback();
        }

        public boolean isLeft() {
            return directionState == STATE_WALK_LEFT;
        }

        public boolean isRight() {
            return directionState == STATE_WALK_RIGHT;
        }

        private void callback() {
            if (state != null) {
                state.state(directionState, upDownState);
            }
        }

        public interface IState {
            void state(int directionState, int upDownState);
        }
    }

    public static class AttackState {
        private int shootState = STATE_NULL;
        private AttackState.IState state;
        private double degrees;

        public double getDegrees() {
            return degrees;
        }

        public void setDegrees(double degrees) {
            this.degrees = degrees;
        }

        public void setState(AttackState.IState state) {
            this.state = state;
        }

        public int getShootState() {
            return shootState;
        }

        public void setShootState(int shootState) {
            this.shootState = shootState;
            callback();
        }

        private void callback() {
            if (state != null) {
                state.state(shootState == STATE_ATTACK_SHOOT);
            }
        }

        public interface IState {
            void state(boolean isShoot);
        }

    }


//        构建颜色的摇杆
//        int widthHeight1 = 100;
//        int widthHeightHalf1 = widthHeight1 / 2;
//        Pixmap pixmap1 = new Pixmap(widthHeight1, widthHeight1, Pixmap.Format.RGBA8888);
//        pixmap1.setColor(new Color(0XD3C4B4CC));
//        pixmap1.fillCircle(widthHeightHalf1, widthHeightHalf1, widthHeightHalf1);
//        Texture img1 = new Texture(pixmap1);
//        pixmap1.dispose();
//        int widthAndHeight = 300;
//        Pixmap pixmap2 = new Pixmap(widthAndHeight, widthAndHeight, Pixmap.Format.RGBA8888);
//        pixmap2.setColor(new Color(0xFFBA4ACC));
//        pixmap2.fillCircle(widthAndHeight / 2, widthAndHeight / 2, widthAndHeight / 2); // 圆心放在图形中心，半径刚好贴着图形边框
//        Texture img2 = new Texture(pixmap2);
//        pixmap2.dispose();
//        Touchpad.TouchpadStyle pad_style = new Touchpad.TouchpadStyle();
//        pad_style.knob = new TextureRegionDrawable(new TextureRegion(img1)); // 摇杆
//        pad_style.background = new TextureRegionDrawable(new TextureRegion(img2)); // 背景


}
