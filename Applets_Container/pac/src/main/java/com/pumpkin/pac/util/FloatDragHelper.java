package com.pumpkin.pac.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.pumpkin.pac.BuildConfig;
import com.pumpkin.ui.util.ExtensionHelperKt;

public class FloatDragHelper extends DragHelper {
    private static final String TAG = "FloatDragHelper";
    private final int screenWidth;
    private final int halfScreenWidth;
    private final int screenHeight;
    private final int length = ExtensionHelperKt.getDpToPx(38);
    private final int dp10 = ExtensionHelperKt.getDpToPx(10);

    public FloatDragHelper(@NonNull Activity activity) {
        this.screenWidth = ExtensionHelperKt.obtainPhoneCurrentWidth(activity);
        this.halfScreenWidth = this.screenWidth / 2;
        this.screenHeight = ExtensionHelperKt.obtainPhoneCurrentHeight(activity);
    }

    @Override
    protected void isMovingUp(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "isMovingUp screenWidth is " + screenWidth + " , screenHeight is " + screenHeight + " , lastMoveX is " + lastMoveX + " , lastMoveY is " + lastMoveY);
        }
        final boolean isStart = this.lastMoveX >= halfScreenWidth;
        float indexX = isStart ? this.screenWidth - length - dp10 : dp10;
        float indexY = this.lastMoveY > (this.screenHeight - length - dp10) ? this.screenHeight - length - dp10 : lastMoveY < (dp10 + dp10) ? dp10 + dp10 : lastMoveY;
        lastMoveX = indexX;
        lastMoveY = indexY;
        v.setX(lastMoveX);
        v.setY(lastMoveY);
    }

    @Override
    protected void isMoving(View v) {

    }
}
