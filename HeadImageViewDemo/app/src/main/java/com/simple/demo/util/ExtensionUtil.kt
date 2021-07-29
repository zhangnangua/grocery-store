package com.simple.demo.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 作者： zxf
 * 描述： kotlin扩展工具类方法集合
 */

//region

/**
 * recyclerView 点击
 */
typealias RvItemClick = (adapter: RecyclerView.Adapter<*>, view: View, position: Int) -> Unit

/**
 * recyclerView 长按
 */
typealias RvItemLongClickListener = (adapter: RecyclerView.Adapter<*>, view: View, position: Int) -> Unit


//endregion

//region property 扩展属性

/**
 * dpToPx
 */
val Float.dpToPx
    get() = run {
        val scale: Float = AppUtil.application.resources.displayMetrics.density
        this * scale + 0.5F
    }

/**
 * pxToDp
 */
val Float.pxToDp
    get() = run {
        val scale: Float = AppUtil.application.resources.displayMetrics.density
        this / scale + 0.5f
    }

/**
 * pxToSp
 */
val Float.pxToSp
    get() = run {
        val fontScale: Float = AppUtil.application.resources.displayMetrics.scaledDensity
        this / fontScale + 0.5f
    }

/**
 * spToPx
 */
val Float.spToPx
    get() = run {
        val fontScale: Float = AppUtil.application.resources.displayMetrics.scaledDensity
        this * fontScale + 0.5f
    }

//endregion


//region  String 扩展函数

/**
 * String 转换为boolean类型 含null
 */
fun String?.parseBooleanMayNull(): Boolean? {
    return this?.let {
        when (it.toLowerCase(Locale.ROOT)) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }
}

//endregion

//region  EditText 扩展函数

/**
 * afterTextChanged
 */
inline fun EditText.afterTextChanged(crossinline block: (Editable?) -> Unit = {}) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            block(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/**
 * beforeTextChanged
 */
inline fun EditText.beforeTextChanged(crossinline block: (CharSequence?, Int, Int) -> Unit = { _: CharSequence?, _: Int, _: Int -> }) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            block(s, start, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/**
 * onTextChanged
 */
inline fun EditText.onTextChanged(crossinline block: (CharSequence?, Int, Int, Int) -> Unit = { _: CharSequence?, _: Int, _: Int, _: Int -> }) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block(s, start, before, count)
        }
    })
}

/**
 * addTextChangedListener
 */
inline fun EditText.addTextChangedListener(
    crossinline afterTextChanged: (Editable?) -> Unit = {},
    crossinline beforeTextChanged: (CharSequence?, Int, Int) -> Unit = { _: CharSequence?, _: Int, _: Int -> },
    crossinline onTextChanged: (CharSequence?, Int, Int, Int) -> Unit = { _: CharSequence?, _: Int, _: Int, _: Int -> }
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }

    })
}

//endregion