package com.pumpkin.mvvm.widget.exit_dialog

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.mvvm.R
import com.pumpkin.mvvm.databinding.FragmentGameDialogBinding
import com.pumpkin.mvvm.repo.CollectionKv
import com.pumpkin.mvvm.util.FloatDragHelper
import com.pumpkin.ui.util.dpToPx

/**
 * 返回处理
 */
class ExitDialogManager constructor(val activity: Activity,
                                    val title: String,
                                    val icon: String,
                                    val id: String) : View.OnClickListener {

    private lateinit var binding: FragmentGameDialogBinding
    private lateinit var floatView: View
    private var callback: DefaultCallback? = null
    private lateinit var viewGroup: ViewGroup
    private var add = false
    private var lastBtStatus = BT_NULL

    fun register(callback: (type: Int) -> Unit) {
        this.callback = DefaultCallback(this, callback)
    }

    fun attach() {
        viewGroup = (activity.window?.decorView as? ViewGroup) ?: return
        floatView = floatView()
        floatView.setOnClickListener(this)
        floatView.setOnTouchListener(FloatDragHelper(activity))
        viewGroup.addView(floatView)
        binding = FragmentGameDialogBinding.inflate(LayoutInflater.from(activity))
    }

    fun onBackPressed() {
        trigger()
    }

    fun lastIsCanExit() = lastBtStatus == BT_EXIT || lastBtStatus == BT_NEXT

    private fun floatView(): View {
        val dp38 = 38F.dpToPx
        val view = View(activity)
        val layoutParams = FrameLayout.LayoutParams(dp38, dp38)
        layoutParams.gravity = Gravity.TOP or Gravity.END
        layoutParams.topMargin = 150F.dpToPx
        layoutParams.marginEnd = 10F.dpToPx
        view.layoutParams = layoutParams
        view.background = ContextCompat.getDrawable(activity, R.drawable.layer_float_menu_bg)
        return view
    }

    override fun onClick(v: View?) {
        if (v == floatView) {
            trigger()
        } else if (v == binding.flExit) {
            lastBtStatus = BT_EXIT
            callback?.callback(BT_EXIT)
            hide()
        } else if (v == binding.flNext) {
            lastBtStatus = BT_NEXT
            callback?.callback(BT_NEXT)
            hide()
        } else if (v == binding.flCreateShortCut) {
            lastBtStatus = BT_CREATE_CUT
            callback?.callback(BT_CREATE_CUT)
            hide()
        } else if (v == binding.flOrientation) {
            lastBtStatus = BT_ORIENTATION
            callback?.callback(BT_ORIENTATION)
            hide()
        } else if (v == binding.vMask) {
            lastBtStatus = BT_EXIT
            trigger()
        } else if (v == binding.flCollect) {
            lastBtStatus = BT_COLLECTION
            callback?.callback(BT_COLLECTION)
        }
    }

    private fun trigger() {
        if (!add) {
            add = true
            binding.flExit.setOnClickListener(this)
            binding.flNext.setOnClickListener(this)
            binding.flCreateShortCut.setOnClickListener(this)
            binding.flOrientation.setOnClickListener(this)
            binding.vMask.setOnClickListener(this)
            binding.flCollect.setOnClickListener(this)
            if (CollectionKv.alreadySubscribed(id)) {
                alreadySubscribedUI()
            } else {
                unsubscribeUI()
            }
            binding.ivName.text = title
            Glide.with(activity)
                .load(icon)
                .transform(RoundedCorners(8F.dpToPx))
                .into(binding.ivIcon)
            viewGroup.addView(binding.root)
            return
        }
        if (binding.root.visibility == View.GONE) {
            show()
        } else {
            hide()
        }
    }

    internal fun alreadySubscribedUI() {
        binding.ivCollection.setImageResource(R.drawable.baseline_collections_24_filled)
    }

    internal fun unsubscribeUI() {
        binding.ivCollection.setImageResource(R.drawable.baseline_collections_24_outline)
    }

    private fun show() {
        binding.root.visibility = View.VISIBLE
    }

    private fun hide() {
        binding.root.visibility = View.GONE
    }

    companion object {
        const val BT_NULL = 0
        const val BT_EXIT = 1
        const val BT_NEXT = 2
        const val BT_CREATE_CUT = 3
        const val BT_ORIENTATION = 4
        const val BT_COLLECTION = 5
    }

}