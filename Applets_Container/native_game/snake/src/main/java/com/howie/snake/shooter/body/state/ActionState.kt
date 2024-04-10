package com.howie.snake.shooter.body.state

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.howie.snake.shooter.body.Actor
import com.howie.snake.shooter.body.PlayerActor
import com.pumpkin.ui.util.dpToPx
import java.util.*


class ActionState(val actor: Actor) {
    //方向
    var direction: Double = 0.0

    //状态队列
    private val attackStates = ArrayDeque<Int>()

    //按下状态变化记录
    private val btStatusRecord = ArrayDeque<Int>()

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    fun decorate(canvas: Canvas) {
        var state = attackStates.poll()
        if (state == null) {
            // TODO: 根据上一个状态来
            val peek = btStatusRecord.peek()
            if (peek == ATTACK_STATE_MOVE) {
                bow(canvas)
            }

            return
        }

        while (state != null) {
            when (state) {
                ATTACK_STATE_UNKNOWN -> {
                    empty(canvas)

                    //last 清除
                    btStatusRecord.clear()
                }
                ATTACK_STATE_CLICK -> {
                    shortArrow(canvas)
                }
                ATTACK_STATE_LONG_CLICK -> {
                    longArrow(canvas)
                }
                ATTACK_STATE_MOVE -> {
                    bow(canvas)
                }
                ATTACK_STATE_DOWN -> {
                    down(canvas)

                    //last 清除
                    btStatusRecord.clear()
                }
                ATTACK_STATE_FINISH -> {
                    finish(canvas)
                }
                ATTACK_STATE_INJURED -> {
                    injured(canvas)
                }
            }
            //记录
            val peek = btStatusRecord.peek()
            if (peek != state) {
                btStatusRecord.push(state)
            }

            state = attackStates.poll()
        }
    }

    fun pushAttackState(state: Int) {
        attackStates.offerLast(state)
    }

    private fun injured(canvas: Canvas) {

    }


    private fun finish(canvas: Canvas) {
        Log.d(TAG, "finish ")

    }

    private fun down(canvas: Canvas) {
        Log.d(TAG, "down ")

    }

    private fun shortArrow(canvas: Canvas) {
        Log.d(TAG, "shortArrow ")

    }

    private fun longArrow(canvas: Canvas) {
        Log.d(TAG, "longArrow ")

    }

    private fun bow(canvas: Canvas) {
        canvas.save()
        canvas.translate(actor.xPosition - PlayerActor.halfBodySize, actor.yPosition - PlayerActor.halfBodySize)
        val end = PlayerActor.bodySize + PlayerActor.bodySize
        paint.strokeWidth = 2F.dpToPx.toFloat()
        paint.color = Color.GREEN
        canvas.drawArc(0F, 0F, end.toFloat(), end.toFloat(), ((180F / Math.PI * direction.toFloat() - 60F).toFloat()), 120F, false, paint)
        canvas.restore()

//        val centerX = PlayerActor.halfBodySize
//        val centerY = PlayerActor.halfBodySize
//        canvas.save()
//        canvas.translate(PlayerActor.halfBodySize, PlayerActor.halfBodySize)
//        canvas.drawCircle(centerX, centerY, PlayerActor.bodySize * 1.5F, paint)
//        canvas.restore()
    }

    private fun damaged(canvas: Canvas) {
        Log.d(TAG, "damaged ")
    }

    private fun empty(canvas: Canvas) {
        Log.d(TAG, "empty ")
    }

    companion object {
        const val TAG = "ActionState"

        const val ATTACK_STATE_UNKNOWN = 1 shl 1
        const val ATTACK_STATE_CLICK = 1 shl 2
        const val ATTACK_STATE_LONG_CLICK = 1 shl 3
        const val ATTACK_STATE_MOVE = 1 shl 4
        const val ATTACK_STATE_DOWN = 1 shl 5
        const val ATTACK_STATE_FINISH = 1 shl 6
        const val ATTACK_STATE_INJURED = 1 shl 7
    }
}