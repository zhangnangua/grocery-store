package com.pumpkin.pac.util;

import android.view.MotionEvent;
import android.view.View;

public class DragHelper implements View.OnTouchListener {

    private static final String TAG = "DragHelper";

    private static final int TOUCH_SLOP = 7;

    private float lastX, lastY;

    protected boolean isMoving = false;
    protected float lastMoveX, lastMoveY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - lastX;
                float deltaY = y - lastY;
                if (isMoving || (Math.abs(deltaX) > TOUCH_SLOP || Math.abs(deltaY) > TOUCH_SLOP)) {
                    float newX = v.getX() + deltaX;
                    float newY = v.getY() + deltaY;
                    lastMoveX = newX;
                    lastMoveY = newY;
                    isMoving(v);
                    v.setX(newX);
                    v.setY(newY);
                    isMoving = true;
                }

                break;

            case MotionEvent.ACTION_UP:
                // click
                if (!isMoving) {
                    v.performClick();
                }
                if (isMoving) {
                    isMovingUp(v);
                }
                isMoving = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isMoving) {
                    isMovingUp(v);
                }
                isMoving = false;
                break;
        }

        return true;
    }

    protected void isMovingUp(View v) {

    }

    protected void isMoving(View v) {

    }
}
