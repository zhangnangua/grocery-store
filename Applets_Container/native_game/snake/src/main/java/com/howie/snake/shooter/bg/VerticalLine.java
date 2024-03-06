package com.howie.snake.shooter.bg;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.howie.snake.shooter.GameEngine;
import com.howie.snake.shooter.util.MathUtils;

public final class VerticalLine extends BackgroundLine {

    VerticalLine(GameEngine.Param param) {
        super((float) MathUtils.random(param.getWidthSideLength(), 0), param);
    }

    void display(Canvas canvas, Paint paint) {
        canvas.drawLine(position, 0.0F, position, param.getHeightSideLength(), paint);
    }

    float getMaxPosition() {
        return param.getWidthSideLength();
    }
}
