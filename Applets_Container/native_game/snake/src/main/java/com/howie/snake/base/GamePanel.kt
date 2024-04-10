package com.howie.snake.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.Choreographer.FrameCallback
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.pumpkin.data.AppUtil

abstract class GamePanel
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

    init {
        mHolder.setFormat(PixelFormat.TRANSPARENT)

        mHolder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        //do
        init()

        // execute
        renderThread = RenderThread({
            Looper.prepare()
            renderHandler = RenderHandler(Looper.myLooper()!!, renderThread)
                .apply {
                    registerNextRender()
                }
            Looper.loop()
        },
            this
        ).apply {
            start()
        }

        initAfter()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        onPanelChange(holder, format, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDestroy = true
        renderHandler?.destroy()
        onDestroyed(holder)
    }

    abstract fun init()

    abstract fun initAfter()

    abstract fun onPanelChange(holder: SurfaceHolder, format: Int, width: Int, height: Int)

    abstract fun onDestroyed(holder: SurfaceHolder)

    abstract fun render(mCanvas: Canvas, frameTimeNanos: Long)

    internal fun draw(frameTimeNanos: Long) {
        try {
            mCanvas = mHolder.lockCanvas()
            //draw something
            //上一次清屏
            clearSurface()
            //do 绘制
            render(mCanvas!!, frameTimeNanos)

        } catch (e: Exception) {
            if (AppUtil.isDebug) {
                Log.d(TAG, "draw () ->", e)
            }
        } finally {
            if (null != mCanvas && !isDestroy) {
                mHolder.unlockCanvasAndPost(mCanvas)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // TODO: do touch
        return super.onTouchEvent(event)
    }

    private fun clearSurface() {
        mCanvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), clearPaint)
    }


    companion object {
        const val TAG = "GamePanel"
    }
}

class RenderThread(run: Runnable, private val view: GamePanel) : Thread(run) {
    fun doFrame(frameTimeNanos: Long) {
        view.draw(frameTimeNanos)
    }
}

class RenderHandler(looper: Looper, private val renderThread: RenderThread) : Handler(looper) {

    private var currentCallback: FrameCallback? = null

    @Volatile
    private var isDestroy = false

    fun sendDoFrame(frameTimeNanos: Long) {
        if (isDestroy) {
            return
        }
        sendMessage(obtainMessage(MSG_DO_FRAME, frameTimeNanos))
    }

    override fun handleMessage(msg: Message) {
        //Log.d(TAG, "RenderHandler [" + this + "]: what=" + what);
        when (val what: Int = msg.what) {
            MSG_DO_FRAME -> {
                renderThread.doFrame(msg.obj as Long)
                registerNextRender()
            }
            else -> throw RuntimeException("unknown message $what")
        }
    }

    fun registerNextRender() {
        triggerDoFrame {
            sendDoFrame(it)
        }
    }

    fun triggerDoFrame(callback: FrameCallback) {
        currentCallback = FrameCallback {
            callback.doFrame(it)
        }
        Choreographer.getInstance().postFrameCallback(currentCallback)
    }

    fun destroy() {
        isDestroy = true
        looper.quit()
        if (currentCallback != null) {
            val local = currentCallback
            Choreographer.getInstance().removeFrameCallback(local)
        }
    }

    companion object {
        private const val MSG_DO_FRAME = 1
    }
}