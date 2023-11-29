package com.pumpkin.mvvm.widget.loading

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.pumpkin.mvvm.R


class LoadingView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.progressBarStyle)
    : ProgressBar(context, attrs, defStyleAttr) {
    private var mSprite: Sprite? = null

    init {
        init()
    }

    private fun init() {
        val sprite: Sprite = ThreeBounce()
        sprite.color = ContextCompat.getColor(context, R.color.seed)
        setIndeterminateDrawable(sprite)
    }

    override fun setIndeterminateDrawable(d: Drawable?) {
        if (d !is Sprite) {
            throw IllegalArgumentException("this d must be instance of Sprite");
        }
        setIndeterminateDrawable(d as Sprite?)
    }

    fun setIndeterminateDrawable(d: Sprite?) {
        super.setIndeterminateDrawable(d)
        mSprite = d
        onSizeChanged(width, height, width, height)
        if (visibility == VISIBLE) {
            mSprite?.start()
        }
    }

    override fun getIndeterminateDrawable(): Sprite? {
        return mSprite
    }

    override fun unscheduleDrawable(who: Drawable) {
        super.unscheduleDrawable(who)
        if (who is Sprite) {
            who.stop()
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            if (mSprite != null && visibility == VISIBLE) {
                mSprite?.start()
            }
        }
    }

    fun setColor(color: Int) {
        if (mSprite != null) {
            mSprite!!.color = color
        }
        invalidate()
    }


    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == View.SCREEN_STATE_OFF) {
            if (mSprite != null) {
                mSprite?.stop()
            }
        }
    }

    companion object {
        const val TAG = "LoadingView"
    }
}