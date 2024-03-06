package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.Toolbar
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.pac.R
import com.pumpkin.pac.bean.WordCardStyle
import com.pumpkin.pac.databinding.ActivtyBrowserBinding
import com.pumpkin.pac.pool.WebViewPool
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.pac_core.webview.PACWebEngine
import com.pumpkin.pac_core.webview.Webinterface
import com.pumpkin.webCache.WVCacheClient
import com.pumpkin.webCache.interceptors.AdvertiseInterceptor
import com.pumpkin.webCache.requestHelper.BaseInterceptorConfig

class BrowserActivity : BaseActivity() {

    private lateinit var binding: ActivtyBrowserBinding
    private var wv: PACWebEngine? = null
    private var cacheClient: WVCacheClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = savedInstanceState ?: intent.extras
        if (bundle == null) {
            finishPAC("bundle is null .")
            return
        }
        val wordCardStyle = bundle.getParcelable<WordCardStyle>(Constant.FIRST_PARAMETER)
        if (wordCardStyle == null) {
            finishPAC("wordCardStyle entity is null .")
            return
        }

        initView(wordCardStyle)

        loadData(wordCardStyle)
    }

    private fun initView(wordCardStyle: WordCardStyle) {
        binding = ActivtyBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //wv
        wv = getWV()
        binding.wvContainer.removeAllViews()
        binding.wvContainer.addView(wv)
    }


    private fun progressUI(progress: Int) {
        val pUI = binding.progress
        if (progress >= 100) {
            pUI.visibility = View.GONE
            pUI.progress = 0
        } else {
            pUI.visibility = View.VISIBLE
            pUI.progress = progress
        }
    }


    private fun loadData(wordCardStyle: WordCardStyle) {
        cacheClient = WVCacheClient.Builder(AppUtil.application)
            .originUrl(wordCardStyle.link)
            .addInterceptor(AdvertiseInterceptor())
            .dynamicAbility(object : BaseInterceptorConfig() {
                override fun cacheDay(): String {
                    // default two day .
                    return dayToMaxAge(2)
                }
            })
            .build()

        loadEntrance(wordCardStyle.link)
    }

    private fun getWV(): PACWebEngine {
        return WebViewPool.obtain(AppUtil.application).apply {
            layoutParams =
                FrameLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT)
            addClient()
            setPageInterface(object : Webinterface.DefaultPage() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (TextUtils.isEmpty(url) || TextUtils.equals(url, "about:blank")) {
                        return
                    }
                    progressUI(100)
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressUI(newProgress)
                }
            })
            setResourceInterface(object : Webinterface.ResourceInterface {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    return cacheClient?.engine?.interceptRequest(request)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return GameHelper.shouldOverrideUrlLoading(view, request) {
                        loadEntrance(it)
                    }
                }

            })
            // TODO: error temporarily not processed .
        }
    }


    private fun loadEntrance(url: String) {
        wv?.loadUrl(url)
    }

    private fun finishPAC(reason: String? = null) {
        reason?.toLogD(TAG)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wv != null) {
            WebViewPool.recycle(wv)
            wv = null
        }
    }

    companion object {
        private const val TAG = "BrowserActivity"

        fun go(context: Context, entity: WordCardStyle) {
            context.startActivity(Intent(context, BrowserActivity::class.java).apply {
                putExtra(Constant.FIRST_PARAMETER, entity)
                putExtra(Constant.PAGE_PARAMETER, ActivitySettingBean().apply {
                    enableImmersiveBar = true
                    enterAnim = R.anim.slide_in_from_bottom
                    exitAnim = R.anim.slide_out_to_bottom
                })
            })
        }
    }

}