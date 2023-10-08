package com.pumpkin.pac.widget.gloading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.pumpkin.data.AppUtil
import kotlin.math.abs
import kotlin.math.atan2

class FishControl(private val fish: Fish, var fishHeadPoint: PointF ) {

    private val mPath = Path()

    var fishShakeValue = 0F
    private val fishShakeAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 2000
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            fishShakeValue = it.animatedValue as Float
        }
    }

    private var runAnimator: ValueAnimator? = null

    fun fishShake() {
        if (!fishShakeAnimator.isRunning) {
            fishShakeAnimator.start()
        }
    }

    fun stopShake() {
        fishShakeAnimator.pause()
    }


    fun go(end: PointF) {
        //鱼的重心坐标
        val fishMiddle: PointF = fish.middlePoint

        // 鱼头坐标 ---- 控制点一
        val fishHead = fish.headPoint

        /**
         * 先用cos公式向量+三角函数算出AOB的度数
         * 控制点2在AOB的角平分线上（人为规定的）
         */
        //控制点2的坐标  重心 终点 鱼头夹角
        val angle = includeAngle(fishMiddle, fishHead, end) / 2
        //重心 x轴 鱼头夹角
        val delta = includeAngle(fishMiddle, PointF(fishMiddle.x + 1, fishMiddle.y), fishHead)
        val control2: PointF = fish.calculatePoint(
            fishMiddle,
            fish.heaD_RADIUS * 1.6f,
            abs(delta - angle)
        )

        mPath.reset()
        mPath.moveTo(fishHead.x, fishHead.y)
        mPath.cubicTo(
            fishHead.x,
            fishHead.y,
            control2.x,
            control2.y,
            end.x,
            end.y
        )
        if (runAnimator != null) {
            runAnimator!!.cancel()
            runAnimator = null
        }
        runAnimator = ValueAnimator.ofFloat(0F, 1F)
        runAnimator!!.duration = 2500
        runAnimator!!.interpolator = AccelerateDecelerateInterpolator()
        //鱼游动时胃部加快摆动
        runAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                fish.setFrequance(1f)
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                fish.setFrequance(3f)
            }
        })
        runAnimator!!.addUpdateListener(object : AnimatorUpdateListener {
            val pathMeasure = PathMeasure(mPath, false)
            val tan = FloatArray(2)
            val pos = FloatArray(2)
            override fun onAnimationUpdate(animation: ValueAnimator) {
                //执行了周期的多少百分比
                val fraction = animation.animatedFraction
                //得到路径的切线
                pathMeasure.getPosTan(pathMeasure.length * fraction, pos, tan)
                val mainAngle =
                    Math.toDegrees(atan2(-tan[1].toDouble(), tan[0].toDouble())).toFloat()

                if (AppUtil.isDebug) {
                    Log.d(
                        TAG,
                        "onAnimationUpdate () -> ${pos[0].toInt().toFloat()} 、${
                            pos[1].toInt().toFloat()
                        }"
                    )
                }

                fishHeadPoint = PointF(pos[0].toInt().toFloat(), pos[1].toInt().toFloat())

                fish.setFishMainAngle(mainAngle)
            }
        })
        runAnimator!!.start()
    }


    /**
     * @param A
     * @param O
     * @param B
     * @return
     */
    private fun includeAngle(A: PointF, O: PointF, B: PointF): Float {
        //0A*0B向量积
        val AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y)
        //OA*OB绝对值
        val OALength =
            Math.sqrt(((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y)).toDouble()).toFloat()
        val OBLength =
            Math.sqrt(((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y)).toDouble()).toFloat()
        val cosAOB = AOB / (OALength * OBLength)
        val angleAOB = Math.toDegrees(Math.acos(cosAOB.toDouble())).toFloat()
        val direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x)
        return if (direction == 0f) {
            if (AOB >= 0) {
                0F
            } else {
                180F
            }
        } else {
            if (direction > 0) {
                -angleAOB
            } else {
                angleAOB
            }
        }
    }

    fun destroy() {
        stopShake()
        runAnimator?.cancel()
    }

    companion object {
        const val TAG = "FishControl"
    }
}