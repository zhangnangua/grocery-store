package com.howie.snake.shooter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.howie.snake.rocker.RockerView
import com.howie.snake.shooter.body.state.ActionState
import com.howie.snake.shooter.input.AttackView
import com.pumpkin.ui.util.dpToPx

class BowShooterPanel
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val engine: GameEngine
    private lateinit var rockerView: RockerView
    private lateinit var attackView: AttackView

    private lateinit var operateHandler: OperateHandler

    private val dp150 = 150F.dpToPx
    private val dp80 = 80F.dpToPx
    private val dp20 = 20F.dpToPx

    init {
        engine = GameEngine(context, attrs, defStyleAttr).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        engine.initListener = {
            operateHandler = OperateHandler(Looper.myLooper()
                ?: throw IllegalAccessException("my looper is null ."), engine)

            attachMoveHandlerView()
            attachAttackHandlerView()
        }

        addView(engine)
    }

    private fun attachMoveHandlerView() {
        rockerView = RockerView(context, null).apply {
            val lp = LayoutParams(dp150, dp150)
            layoutParams = lp
            lp.gravity = Gravity.BOTTOM
            lp.marginStart = dp80
            lp.marginEnd = dp20
            setOnAngleChangeListener(MoveHandler(operateHandler))
        }
        addView(rockerView)
    }

    private fun attachAttackHandlerView() {
        post {
            attackView = AttackView(context).apply {
                val lp = LayoutParams(this@BowShooterPanel.width / 2, LayoutParams.MATCH_PARENT)
                layoutParams = lp
                lp.gravity = Gravity.END
                setOnAngleChangeListener(AttackHandler(operateHandler))
                setOnClickListener(AttackClick(operateHandler))
                setOnLongClickListener(AttackLongClick(operateHandler))
            }
            addView(attackView)
        }
    }

    class MoveHandler(private val operateHandler: OperateHandler) : RockerView.OnAngleChangeListener {

        override fun onStart() {
        }

        override fun angle(angle: Double) {
            operateHandler.post(OperateHandler.MOVE_ANGLE, angle)
        }

        override fun onFinish() {
            operateHandler.post(OperateHandler.MOVE_FINISH, OperateHandler.DEFAULT_ILLEGAL)
        }
    }

    class AttackHandler(private val operateHandler: OperateHandler) : RockerView.OnAngleChangeListener {

        override fun onStart() {
            operateHandler.post(OperateHandler.ATTACK_START, OperateHandler.DEFAULT_ILLEGAL)
        }

        override fun angle(angle: Double) {
            operateHandler.post(OperateHandler.ATTACK_ANGLE, angle)
        }

        override fun onFinish() {
            operateHandler.post(OperateHandler.ATTACK_FINISH, OperateHandler.DEFAULT_ILLEGAL)
        }
    }

    class AttackClick(private val operateHandler: OperateHandler) : View.OnClickListener {

        override fun onClick(v: View?) {
            operateHandler.post(OperateHandler.ATTACK_CLICK, OperateHandler.DEFAULT_ILLEGAL)
        }

    }

    class AttackLongClick(private val operateHandler: OperateHandler) : View.OnLongClickListener {

        override fun onLongClick(v: View?): Boolean {
            operateHandler.post(OperateHandler.ATTACK_LONG_CLICK, OperateHandler.DEFAULT_ILLEGAL)
            return true
        }

    }

}

class OperateHandler(looper: Looper, private val engine: GameEngine) : Handler(looper) {
    private val map: ArrayMap<Int, Double> = ArrayMap()
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            MOVE_START -> {
            }
            MOVE_ANGLE -> {
                engine.myPlayerMove(angle(msg).toFloat(), 15F)
            }
            MOVE_FINISH -> {
                engine.myPlayerMove(0F, 0F)
            }
            ATTACK_START -> {
                engine.myPlayerActionState().pushAttackState(ActionState.ATTACK_STATE_DOWN)
            }
            ATTACK_ANGLE -> {
                engine.myPlayerActionState().apply {
                    direction = angle(msg)
                    pushAttackState(ActionState.ATTACK_STATE_MOVE)
                }
            }
            ATTACK_FINISH -> {
                engine.myPlayerActionState().pushAttackState(ActionState.ATTACK_STATE_FINISH)
            }
            ATTACK_CLICK -> {
                engine.myPlayerActionState().pushAttackState(ActionState.ATTACK_STATE_CLICK)
            }
            ATTACK_LONG_CLICK -> {
                engine.myPlayerActionState().pushAttackState(ActionState.ATTACK_STATE_LONG_CLICK)
            }

        }
    }

    fun post(what: Int, angle: Double) {
        // 防止频繁创建对象 故使用empty message , 对于angle 使用最新的就行 故不用担心上一个消息没有发送过去 然后就使用了最新的
        map[what] = angle
        sendEmptyMessage(what)
    }

    private fun angle(msg: Message): Double {
        return map[msg.what] ?: DEFAULT_ILLEGAL
    }

    companion object {
        const val MOVE_START = 1 shl 1
        const val MOVE_ANGLE = 1 shl 2
        const val MOVE_FINISH = 1 shl 3
        const val ATTACK_START = 1 shl 5
        const val ATTACK_ANGLE = 1 shl 6
        const val ATTACK_FINISH = 1 shl 7
        const val ATTACK_CLICK = 1 shl 9
        const val ATTACK_LONG_CLICK = 1 shl 10

        val DEFAULT_ILLEGAL = -1.0
        private const val ANGLE_KEY = "angle_key"
    }
}












