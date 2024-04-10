package com.howie.snake.shooter.body

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import androidx.annotation.ColorInt
import com.howie.snake.shooter.GameEngine
import com.howie.snake.shooter.body.state.ActionState
import com.pumpkin.ui.util.dpToPx
import kotlin.math.sqrt


abstract class AbstractPlayerActor(collisionRadius: Float) : Actor(collisionRadius) {
    val actionState = ActionState(this)
}

class PlayerActor(@ColorInt val colorInt: Int) : AbstractPlayerActor(16F) {

    private val paint = Paint().apply {
        color = colorInt
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    override fun update() {
        super.update()
        if (xPosition < 0) {
            xPosition = 0F
            xVelocity = (-0.9 * xVelocity).toFloat()
        }
        if (xPosition > GameEngine.param.widthSideLength - bodySize) {
            xPosition = (GameEngine.param.widthSideLength - bodySize).toFloat()
            xVelocity = (-0.9 * xVelocity).toFloat()
        }
        if (yPosition < 0) {
            yPosition = 0F
            yVelocity = (-0.9 * yVelocity).toFloat()
        }
        if (yPosition > GameEngine.param.heightSideLength - bodySize) {
            yPosition = (GameEngine.param.heightSideLength - bodySize).toFloat()
            yVelocity = (-0.9 * yVelocity).toFloat()
        }
        rotationAngle += ((0.1 + 0.04 * (sqrt(if (xVelocity < 0) -xVelocity else xVelocity) * 2 + sqrt(if (yVelocity < 0) -yVelocity else yVelocity) * 2)) * Math.PI * 3).toFloat()
    }

    override fun display(canvas: Canvas) {

//        Log.d("PlayerActor", "display xPosition $xPosition , yPosition $yPosition , rotationAngle $rotationAngle")
        //抗锯齿
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        canvas.save()
        canvas.translate(xPosition, yPosition)
        canvas.rotate(rotationAngle, halfBodySize, halfBodySize)
        canvas.drawRect(0F, 0F, bodySize.toFloat(), bodySize.toFloat(), paint)
        canvas.restore()

        actionState.decorate(canvas)

    }

    override fun act(mCanvas: Canvas) {
    }

    companion object {
        val bodySize = 32.0f.dpToPx
        val halfBodySize = (bodySize / 2).toFloat()
    }

}