package com.pumpkin.automatic_execution.View.SelfView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * pumpkin
 * SurfaceView测试Activity，绘制小Demo
 */
class SurfaceViewHandWriting
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, Runnable {

    /**
     * 画笔
     */
    private var mPaint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    /**
     * 路径
     */
    private var mPath = Path().apply {
        moveTo(0F, 100F)
    }

    /**
     * SurfaceHolder
     */
    private var mSurfaceHolder = holder

    /**
     * 绘制标志
     */
    private var isDrawingFlag = false

    init {
        //增加回调，设置其他布局属性
        mSurfaceHolder.addCallback(this)
        isFocusable = true;
        keepScreenOn = true;
        isFocusableInTouchMode = true;
    }

    /**
     * 创建SurfaceView  启动绘制
     */
    override fun surfaceCreated(holder: SurfaceHolder) {
        isDrawingFlag = true
        Thread(this).start()
    }

    /**
     * surfaceView大小改变的时候进行回调
     */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    /**
     * 销毁
     */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDrawingFlag = false
    }

    /**
     * 触摸控制滑动
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mPath.moveTo(x, y)
                    performClick()
                }
                MotionEvent.ACTION_MOVE -> mPath.lineTo(x, y)
            }
            return true
        }
        return super.onTouchEvent(event)

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    /**
     * 视图销毁
     */
    override fun onDetachedFromWindow() {
        isDrawingFlag = false
        super.onDetachedFromWindow()
    }

    /**
     * 并没有让线程一直运行，而是让它休眠一会，从而节约系统资源，\
     * 一般建议判断的阈值为50-100之间即可保证用户体验同时节约系统资源。
     *
     * fix 为了绘制的流畅，去除对于间隔的控制
     */
    override fun run() {
        while (isDrawingFlag) {
//            val start = System.currentTimeMillis()
            drawSomething()
//            val end = System.currentTimeMillis()
//            val intervalTime = end - start
//            val maxInterval = 50
//            if (intervalTime < maxInterval) {
//                try {
//                    Thread.sleep(maxInterval - intervalTime)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
        }
    }

    /**
     * 绘制
     */
    private fun drawSomething() {
        var canvas: Canvas? = null
        try {
            //获得canvas对象
            canvas = mSurfaceHolder.lockCanvas()
            //绘制背景
            canvas?.drawColor(Color.WHITE)
            //绘制路径
            canvas?.drawPath(mPath, mPaint)
        } finally {
            if (canvas != null) {
                //并提交画布
                mSurfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }


}