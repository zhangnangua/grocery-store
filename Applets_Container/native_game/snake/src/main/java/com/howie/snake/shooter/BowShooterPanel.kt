package com.howie.snake.shooter

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.howie.snake.R
import com.howie.snake.rocker.RockerView
import com.howie.snake.shooter.input.AttackView
import com.pumpkin.ui.util.dpToPx

class BowShooterPanel
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val engine: GameEngine
    private lateinit var rockerView: RockerView
    private lateinit var attackView: AttackView


    private val dp150 = 150F.dpToPx
    private val dp80 = 80F.dpToPx
    private val dp20 = 20F.dpToPx

    init {
        engine = GameEngine(context, attrs, defStyleAttr).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }


        addView(engine)
    }

    fun attachMoveHandlerView() {
        rockerView = RockerView(context, null).apply {
            val lp = LayoutParams(dp150, dp150)
            layoutParams = lp
            lp.gravity = Gravity.BOTTOM
            lp.marginStart = dp80
            lp.marginEnd = dp20
            setOnAngleChangeListener(MoveHandler(engine))
        }
        addView(rockerView)
    }

    fun attachAttackHandlerView() {
        post {
            attackView = AttackView(context).apply {
                val lp = LayoutParams(this@BowShooterPanel.width / 2, LayoutParams.MATCH_PARENT)
                layoutParams = lp
                lp.gravity = Gravity.END
                setOnAngleChangeListener(AttackHandler(engine))
                setOnClickListener(AttackClick(engine))
                setOnLongClickListener(AttackLongClick(engine))
            }
            attackView.setBackgroundResource(R.color.seed)
            addView(attackView)
        }
    }

    class MoveHandler(private val engine: GameEngine) : RockerView.OnAngleChangeListener {

        override fun onStart() {

        }

        override fun angle(angle: Double) {
            engine.myPlayerMove(angle.toFloat(), 20F)
        }

        override fun onFinish() {
            engine.myPlayerMove(0F, 0F)
        }
    }

    class AttackHandler(private val engine: GameEngine) : RockerView.OnAngleChangeListener {

        override fun onStart() {

        }

        override fun angle(angle: Double) {
            Log.d("AttackHandler", "angle $angle")
        }

        override fun onFinish() {

        }
    }

    class AttackClick(private val engine: GameEngine) : View.OnClickListener {
        override fun onClick(v: View?) {
            Log.d("AttackHandler", "onClick")
        }

    }

    class AttackLongClick(private val engine: GameEngine) : View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            Log.d("AttackHandler", "onLongClick")
            return true
        }

    }

}












