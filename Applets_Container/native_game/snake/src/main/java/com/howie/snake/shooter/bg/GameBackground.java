package com.howie.snake.shooter.bg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.howie.snake.BuildConfig;
import com.howie.snake.shooter.GameEngine;
import com.howie.snake.shooter.util.MathUtils;
import com.pumpkin.ui.util.ExtensionHelperKt;

import java.util.ArrayList;

public class GameBackground {
    final ArrayList<BackgroundLine> lineList = new ArrayList<>();
    final float maxAccelerationMagnitude;
    private Paint bgPLine;
    private Paint bgP;
    private final GameEngine.Param param;

    public GameBackground(float maxAcc, GameEngine.Param param) {
        initPaint();
        maxAccelerationMagnitude = maxAcc;
        this.param = param;
        for (int i = 0; i < 5; i++) {
            lineList.add(new HorizontalLine(param));
        }
        for (int i = 0; i < 15; i++) {
            lineList.add(new VerticalLine(param));
        }
    }

    private void initPaint() {
        bgPLine = new Paint();
        bgPLine.setColor(Color.parseColor("#33000000"));
        bgPLine.setAntiAlias(true);
        bgPLine.setStyle(Paint.Style.STROKE);
        bgPLine.setStrokeWidth(ExtensionHelperKt.getDpToPx(2));
        bgP = new Paint();
        bgP.setColor(Color.parseColor("#E0E0E0"));
        bgP.setAntiAlias(true);
        bgP.setStyle(Paint.Style.FILL);
    }

    public void update() {
        for (BackgroundLine eachLine : lineList) {
            if (BuildConfig.DEBUG) {
                Log.d("GameBackground", "update () -> " + (float) MathUtils.random(-maxAccelerationMagnitude, maxAccelerationMagnitude));
            }
            eachLine.update((float) MathUtils.random(-maxAccelerationMagnitude, maxAccelerationMagnitude));
        }
    }

    public void display(Canvas canvas) {
        canvas.drawRect(new Rect(0, 0, param.getWidthSideLength(), param.getHeightSideLength()), bgP);
        for (BackgroundLine eachLine : lineList) {
            eachLine.display(canvas, bgPLine);
        }
    }
}
