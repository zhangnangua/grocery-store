package com.howie.snake.shooter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceHolder
import com.howie.snake.base.GamePanel
import com.howie.snake.shooter.bg.GameBackground
import com.howie.snake.shooter.body.PlayerActor

class GameEngine
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : GamePanel(context, attrs, defStyleAttr) {

    private lateinit var bg: GameBackground
    private val players = ArrayList<PlayerActor>()

    override fun init() {
        setParam(Param(width, height))
        bg = GameBackground(0.1F, param)
        players.add(PlayerActor(Color.parseColor("#FF5F0F")))

    }

    override fun render(mCanvas: Canvas, frameTimeNanos: Long) {
        bg.update()
        bg.display(mCanvas)

        playersRender(mCanvas)
    }

    private fun playersRender(mCanvas: Canvas) {
        players.forEach {
            it.act()
            it.update()
            it.display(mCanvas)
        }
    }

    override fun onPanelChange(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        setParam(Param(width, height))
    }

    override fun onDestroyed(holder: SurfaceHolder) {

    }

    fun myPlayerMove(dir: Float, speed: Float) {
        players.forEach {
            it.setVelocity(dir, speed)
        }
    }

    private fun setParam(p: Param) {
        param.heightSideLength = p.heightSideLength
        param.widthSideLength = p.widthSideLength
    }

    companion object {
        var param = Param(0, 0)
    }

    class Param(var widthSideLength: Int, var heightSideLength: Int)

}