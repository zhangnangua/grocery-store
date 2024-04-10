package com.howie.snake.shooter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import com.howie.snake.base.GamePanel
import com.howie.snake.shooter.bg.GameBackground
import com.howie.snake.shooter.body.PlayerActor

class GameEngine
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : GamePanel(context, attrs, defStyleAttr) {

    private lateinit var bg: GameBackground
    private lateinit var player1: PlayerActor

    var initListener: (() -> Unit)? = null

    override fun init() {
        setParam(Param(width, height))
        bg = GameBackground(0.1F, param)
        player1 = PlayerActor(Color.parseColor("#FF5F0F"))

    }

    override fun initAfter() {
        initListener?.invoke()
    }

    override fun render(mCanvas: Canvas, frameTimeNanos: Long) {
        bg.update()
        bg.display(mCanvas)

        playersRender(mCanvas)
    }

    private fun playersRender(mCanvas: Canvas) {
        player1.act(mCanvas)
        player1.update()
        player1.display(mCanvas)
    }

    override fun onPanelChange(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        setParam(Param(width, height))
    }

    override fun onDestroyed(holder: SurfaceHolder) {

    }

    fun myPlayerMove(dir: Float, speed: Float) {
        player1.setVelocity(dir, speed)
    }

    fun myPlayerActionState() = player1.actionState

    private fun setParam(p: Param) {
        param.heightSideLength = p.heightSideLength
        param.widthSideLength = p.widthSideLength
    }

    companion object {
        var param = Param(0, 0)
    }

    class Param(var widthSideLength: Int, var heightSideLength: Int)

}