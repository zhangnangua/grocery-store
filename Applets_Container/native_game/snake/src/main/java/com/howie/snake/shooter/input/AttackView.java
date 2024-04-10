package com.howie.snake.shooter.input;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.howie.snake.rocker.RockerView;
import com.pumpkin.data.thread.ThreadHelper;

public class AttackView extends View {
    private static final int TOUCH_SLOP = 7;
    private Point mCenterPoint = null;
    private boolean isMoving = false;
    private boolean longPress = false;
    private LongPressCheck longPressCheck = null;

    private RockerView.OnAngleChangeListener mOnAngleChangeListener;

    public AttackView(Context context) {
        super(context);
    }

    public void setOnAngleChangeListener(RockerView.OnAngleChangeListener listener) {
        mOnAngleChangeListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                callBackStart();
                float x = event.getX();
                float y = event.getY();
                mCenterPoint = new Point((int) x, (int) y);
                longPressCheck = new LongPressCheck(x, y);
                longPressCheck.rememberWindowAttachCount();
                longPressCheck.rememberPressedState();
                longPress = false;
                ThreadHelper.INSTANCE.runOnUiThreadDelay(longPressCheck, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                final Point localCenter = mCenterPoint;
                if (localCenter != null) {
                    float deltaX = moveX - mCenterPoint.x;
                    float deltaY = moveY - mCenterPoint.y;
                    if (isMoving || (Math.abs(deltaX) > TOUCH_SLOP || Math.abs(deltaY) > TOUCH_SLOP)) {
                        callBack(localCenter, new Point((int) moveX, (int) moveY));
                        isMoving = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // up 之后判断是否可以click
                if (!isMoving && !longPress) {
                    performClick();
                }
            case MotionEvent.ACTION_CANCEL:
                callBackFinish();
                mCenterPoint = null;
                isMoving = false;
                longPress = false;
                LongPressCheck localPressCheck = longPressCheck;
                if (localPressCheck != null) {
                    ThreadHelper.INSTANCE.removeUiThread(localPressCheck);
                }
                longPressCheck = null;
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void callBack(Point centerPoint, Point touchPoint) {
        // 两点在X轴的距离
        float lenX = (float) (touchPoint.x - centerPoint.x);
        // 两点在Y轴距离
        float lenY = (float) (touchPoint.y - centerPoint.y);
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        double radian = Math.acos(lenX / lenXY) * (touchPoint.y < centerPoint.y ? -1 : 1);

        // 回调 返回参数
        // 大于一定值 才进行回调 防止误触
        // 计算角度
        final int border = 50;
        if (lenXY > border) {
            if (mOnAngleChangeListener != null) {
                mOnAngleChangeListener.angle(radian);
            }
        }
    }

    private void callBackStart() {
        if (mOnAngleChangeListener != null) {
            mOnAngleChangeListener.onStart();
        }
    }

    private void callBackFinish() {
        if (mOnAngleChangeListener != null) {
            mOnAngleChangeListener.onFinish();
        }
    }

    private class LongPressCheck implements Runnable {
        float x;
        float y;
        private int mOriginalWindowAttachCount;
        private boolean mOriginalPressedState;

        public LongPressCheck(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            if (!isMoving
                    && mOriginalPressedState == isPressed()
                    && getWindowAttachCount() == mOriginalWindowAttachCount) {
                performLongClick();
                longPress = true;
                longPressCheck = null;
            }
        }


        public void rememberWindowAttachCount() {
            mOriginalWindowAttachCount = getWindowAttachCount();
        }

        public void rememberPressedState() {
            mOriginalPressedState = isPressed();
        }
    }
}
