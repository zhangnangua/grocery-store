package com.pumpkin.applets_container.view.widget.search

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText


class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val justShow: Boolean = false
) : AppCompatEditText(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (justShow) {
            false
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

}