package com.pumpkin.applets_container.view.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.FragmentGdprBinding
import com.pumpkin.applets_container.helper.GDPRHelper
import com.pumpkin.data.AppUtil
import com.pumpkin.ui.util.DeviceParams

class GDPRFragment : DialogFragment(), DialogInterface.OnKeyListener {
    lateinit var binding: FragmentGdprBinding
    var agreeRunnable: Runnable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            window!!.setBackgroundDrawableResource(R.color.transparent)
            window.decorView.setPadding(0, 0, 0, DeviceParams.getStatusBarHeight(AppUtil.application) / 2)
            val wlp = window.attributes
            wlp.gravity = Gravity.CENTER
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setAttributes(wlp)
        }
        //只能通过点击❌关闭
        setCancelable(false)
        binding = FragmentGdprBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.text = getClickableHtml(AppUtil.application.resources.getString(R.string.gdpr_content, AppUtil.application.resources.getString(R.string.privacy_policy) + "\u200b", AppUtil.application.resources.getString(R.string.terms_of_service) + "\u200b"))
        binding.content.movementMethod = LinkMovementMethod.getInstance()

        binding.agree.setOnClickListener {
            agreeRunnable?.run()
            close()
        }

        dialog?.setOnKeyListener(this)
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            activity?.finish()
        }
        return false
    }

    private fun close() {
        try {
            dismissAllowingStateLoss()
        } catch (_: Exception) {
        }
    }


    private fun getClickableHtml(html: String): CharSequence {
        val spannable: Spannable = SpannableString(html)
        setLinkClickable(spannable, html, AppUtil.application.resources.getString(R.string.privacy_policy), USER_AGREEMENT)
        setLinkClickable(spannable, html, AppUtil.application.resources.getString(R.string.terms_of_service), PRIVACY_POLICY)
        return spannable
    }

    private fun setLinkClickable(spannable: Spannable, originStr: String, handleString: String, tag: Int) {
        val indexStart = originStr.indexOf(handleString)
        val indexEnd = indexStart + handleString.length
        spannable.setSpan(PhoneClickSpan(tag), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    companion object {
        const val TAG = "GDPRFragment"
        private const val USER_AGREEMENT = 1
        private const val PRIVACY_POLICY = 2

        fun showNoRepeat(activity: FragmentActivity, runnable: Runnable) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "show dialog () -> ")
            }
            val manager = activity.supportFragmentManager
            val tag: String = GDPRFragment::class.java.getSimpleName()
            try {
                val oldFragment = manager.findFragmentByTag(tag)
                if (oldFragment != null && oldFragment.isResumed && oldFragment is GDPRFragment) {
                    return
                }
                val ft = manager.beginTransaction()
                if (oldFragment != null) {
                    ft.remove(oldFragment)
                }
                GDPRFragment().apply {
                    agreeRunnable = runnable
                }.show(ft, tag)
            } catch (ignored: Exception) {
            }
        }
    }

    class PhoneClickSpan(private val mTag: Int) : ClickableSpan() {
        override fun onClick(widget: View) {
            val context = widget.context ?: return
            when (mTag) {
                USER_AGREEMENT -> GDPRHelper.goUserAgreement(context)
                PRIVACY_POLICY -> GDPRHelper.goPrivacyPolicy(context)
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.setColor(Color.BLUE)
        }
    }


}




















