package com.pumpkin.pac.widget.gloading

import android.graphics.Canvas
import android.graphics.Paint

class Food {

    private val foodPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        setARGB(1000, 244, 92, 71);
    }

    private val foodRadius = 15F

    fun draw(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, foodRadius, foodPaint)
    }
}