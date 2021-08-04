package com.simple.dragdeletesample.sample1

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.Px
import androidx.recyclerview.R
import androidx.recyclerview.widget.RecyclerView

/**
 * 作者： zxf
 * 描述： 适配最大高度的RecyclerView
 */
class RecyclerViewMaxHeight
@JvmOverloads
constructor(
    @NonNull context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyleAttr) {

    /**
     * 最大高度设置
     */
    @Px
    var maxHeight: Int = -1
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }


    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var mHeightSpec = heightSpec
        if (maxHeight > 0 && MeasureSpec.getSize(heightSpec) > maxHeight) {
            mHeightSpec = if (MeasureSpec.getMode(heightSpec) == MeasureSpec.AT_MOST) {
                MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
            } else {
                MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthSpec, mHeightSpec)
    }


}