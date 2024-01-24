package com.pumpkin.applets_container.view.widget.search

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.pumpkin.applets_container.R
import com.pumpkin.data.AppUtil
import com.pumpkin.ui.util.dpToPx

class SearchView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle) {

    init {
        addView(createContainer(context))
    }

    var onTextChange: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    private fun createContainer(context: Context): LinearLayout {
        return LinearLayout(context).apply {

            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER

            //Consistent with BottomNavigationView
            ViewCompat.setBackground(this, createMaterialShapeDrawableBackground(context, AppCompatResources.getDrawable(AppUtil.application, R.color.colorSurface)!!))

            addView(leftImageView())
            addView(createInputSearchView(context))
        }
    }

    private fun leftImageView(): ImageView {
        val widthHeight = 30F.dpToPx
        return ImageView(context).apply {
            layoutParams = LayoutParams(widthHeight, widthHeight).apply {
                marginStart = 16F.dpToPx
            }
            setImageResource(R.drawable.ic_baseline_search_24)
        }
    }

    private fun createInputSearchView(context: Context): EditText {
        return EditText(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                marginStart = 5F.dpToPx
            }
            setBackgroundColor(Color.TRANSPARENT)
            hint = AppUtil.getString(R.string.search_hint)
            gravity = Gravity.CENTER_VERTICAL
            val appearance = R.style.ThemeOverlay_Material3_AutoCompleteTextView_FilledBox
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(appearance)
            } else {
                setTextAppearance(context, appearance)
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChange?.invoke(s, start, before, count)
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

//            setOnTouchListener(object :OnTouchListener{
//                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                    if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                        this.isCursorVisible = true
//                    }
//
//                }
//
//            })

            setOnFocusChangeListener { v, hasFocus ->
                if (v == this && !hasFocus) {
                    //hide pointer
                    this.isCursorVisible = false
                }
            }

        }
    }

    private fun createMaterialShapeDrawableBackground(context: Context, bg: Drawable): MaterialShapeDrawable {
        val materialShapeDrawable = MaterialShapeDrawable()
        if (bg is ColorDrawable) {
            materialShapeDrawable.fillColor = ColorStateList.valueOf(bg.color)
        }
        materialShapeDrawable.initializeElevationOverlay(context)
        //do
        materialShapeDrawable.elevation = resources.getDimension(R.dimen.design_bottom_navigation_elevation)
        materialShapeDrawable.setCornerSize(160F.dpToPx.toFloat())
        materialShapeDrawable.strokeWidth = 1F.dpToPx.toFloat()
        materialShapeDrawable.strokeColor = ColorStateList.valueOf(resources.getColor(R.color.colorSurface))
        return materialShapeDrawable
    }

}