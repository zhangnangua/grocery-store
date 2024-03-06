package com.howie.snake.shooter.bg;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.howie.snake.shooter.GameEngine;
import com.howie.snake.shooter.util.MathUtils;

public final class HorizontalLine extends BackgroundLine {
    HorizontalLine(GameEngine.Param param) {
        super((float) MathUtils.random(param.getHeightSideLength(), 0), param);
    }

    void display(Canvas canvas, Paint paint) {
        canvas.drawLine(0.0F, position, param.getWidthSideLength(), position, paint);
    }

    float getMaxPosition() {
        return param.getHeightSideLength();
    }
}
