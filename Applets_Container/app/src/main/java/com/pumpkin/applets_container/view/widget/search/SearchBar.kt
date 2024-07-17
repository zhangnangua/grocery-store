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
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

class SearchBar
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle) {
    private val editText: EditText

    var onTextChange: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    private var justShow: Boolean

    init {
        //解析xml属性
        val styleAttr = context.obtainStyledAttributes(attrs, R.styleable.SearchBar)
        justShow = styleAttr.getBoolean(R.styleable.SearchBar_just_show, false)
        styleAttr.recycle()

        editText = createInputSearchView(context, attrs, defStyle)
        addView(createContainer(context, editText))
        if (!justShow) {
            editText.requestFocus()
        }
    }

    private fun createContainer(context: Context, editText: EditText): LinearLayout {
        return LinearLayout(context).apply {

            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER

            //Consistent with BottomNavigationView
            ViewCompat.setBackground(this, createMaterialShapeDrawableBackground(context, AppCompatResources.getDrawable(AppUtil.application, R.color.colorSurface)!!))

            addView(leftImageView())
            addView(editText)
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

    private fun createInputSearchView(context: Context, attrs: AttributeSet?, defStyle: Int): EditText {
        return CustomEditText(context, attrs, defStyle, justShow).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                marginStart = 5F.dpToPx
            }
            setBackgroundColor(Color.TRANSPARENT)
            hint = AppUtil.getString(R.string.search_hint)
            gravity = Gravity.CENTER_VERTICAL
            maxLines = 1
            isSingleLine = true
            isClickable = true
            isFocusable = !justShow
            isEnabled = !justShow
            isFocusableInTouchMode = !justShow
            imeOptions = EditorInfo.IME_ACTION_DONE
            val appearance = R.style.ThemeOverlay_Material3_AutoCompleteTextView_FilledBox
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(appearance)
            } else {
                setTextAppearance(context, appearance)
            }

            setOnFocusChangeListener { _, hasFocus ->
                visibility = if (hasFocus) {
                    showKeyboard()
                    View.VISIBLE
                } else {
                    hideKeyboard()
                    View.GONE
                }
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
        }
    }

    fun getSearchText(): String {
        return editText.text.toString().trim()
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
//        materialShapeDrawable.strokeWidth = 1F.dpToPx.toFloat()
//        materialShapeDrawable.strokeColor = ColorStateList.valueOf(resources.getColor(R.color.colorSurface))
        return materialShapeDrawable
    }

    fun showKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            editText,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    fun hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            editText.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

}