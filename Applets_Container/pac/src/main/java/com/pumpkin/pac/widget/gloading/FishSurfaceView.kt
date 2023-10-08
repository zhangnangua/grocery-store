package com.pumpkin.pac.widget.gloading

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.Choreographer
import android.view.Choreographer.FrameCallback
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


/**
 * 鱼 动画 surface View
 */
class FishSurfaceView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private val mHolder: SurfaceHolder = holder
    private var isDestroy: Boolean = false

    private var mCanvas: Canvas? = null

    lateinit var renderThread: RenderThread
    private var renderHandler: RenderHandler? = null

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val redFish = Fish(PointF(350F, 350F))
    private val redFishControl = FishControl(redFish, redFish.headPoint)

    private val blackFish = Fish(PointF(650F, 550F)).apply {
        setColor(Color.parseColor("#213555"))
    }
    private val blackFishControl = FishControl(blackFish, blackFish.headPoint)

    private val food = Food()
    private var foodX = -1F
    private var foodY = -1F

    init {
        setZOrderOnTop(true)
        mHolder.setFormat(PixelFormat.TRANSPARENT)

        mHolder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        //fish 摇摆起来
        shake()
        // execute
        renderThread = RenderThread(
            {
                Looper.prepare()
                renderHandler = RenderHandler(Looper.myLooper()!!, renderThread)
                    .apply {
                        triggerDoFrame {
                            sendDoFrame(it)
                        }
                    }
                Looper.loop()
            },
            this
        ).apply {
            start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // TODO:
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        destroy()
        isDestroy = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        foodX = event!!.x
        foodY = event.y
        // 触发run
        run(PointF(foodX, foodY))
        return super.onTouchEvent(event)
    }

    private fun shake() {
        redFishControl.fishShake()
        blackFishControl.fishShake()
    }

    private fun run(end: PointF) {
        redFishControl.go(end)
        blackFishControl.go(PointF(end.x - 100, end.y - 100))
    }

    private fun destroy() {
        redFishControl.destroy()
        blackFishControl.destroy()
        renderHandler?.removeDoFrame()
    }

    internal fun draw() {
        try {
            mCanvas = mHolder.lockCanvas()
            //draw something

            //清屏
            clearSurface()

            //绘制🐟  保证画的时候 是同一个位置  防止闪烁
            redFish.headPoint = redFishControl.fishHeadPoint
            redFish.setCurrentValue(redFishControl.fishShakeValue)
            redFish.draw(mCanvas!!)

            blackFish.headPoint = blackFishControl.fishHeadPoint
            blackFish.setCurrentValue(blackFishControl.fishShakeValue)
            blackFish.draw(mCanvas!!)

            //绘制🍜
            if (foodX > 0 && foodY > 0) {
                food.draw(mCanvas!!, foodX, foodY)
            }

        } finally {
            if (null != mCanvas && !isDestroy) {
                mHolder.unlockCanvasAndPost(mCanvas)
            }
        }
    }

    private fun clearSurface() {
        mCanvas!!.drawRect(0F, 0F, width.toFloat(), height.toFloat(), clearPaint)
    }


    companion object {
        const val TAG = "FishSurfaceView"
    }
}

class RenderThread(run: Runnable, private val view: FishSurfaceView) : Thread(run) {
    fun doFrame(frameTimeNanos: Long) {
        view.draw()
    }
}

class RenderHandler(looper: Looper, private val renderThread: RenderThread) : Handler(looper) {

    private var currentCallback: FrameCallback? = null

    fun sendDoFrame(frameTimeNanos: Long) {
        sendMessage(obtainMessage(MSG_DO_FRAME, frameTimeNanos))
    }

    override fun handleMessage(msg: Message) {
        //Log.d(TAG, "RenderHandler [" + this + "]: what=" + what);
        when (val what: Int = msg.what) {
            MSG_DO_FRAME -> {
                renderThread.doFrame(msg.obj as Long)
                triggerDoFrame {
                    sendDoFrame(it)
                }
            }
            else -> throw RuntimeException("unknown message $what")
        }
    }

    fun triggerDoFrame(callback: FrameCallback) {
        currentCallback = FrameCallback {
            callback.doFrame(it)
        }
        Choreographer.getInstance().postFrameCallback(currentCallback)
    }

    fun removeDoFrame() {
        if (currentCallback != null) {
            val local = currentCallback
            Choreographer.getInstance().removeFrameCallback(local)
        }
    }

    companion object {
        private const val MSG_DO_FRAME = 1
    }
}
