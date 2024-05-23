package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.JsPromptResult
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
import com.pumpkin.pac.ParseContext
import com.pumpkin.pac.R
import com.pumpkin.pac.WebViewPool
import com.pumpkin.pac.bean.WordCardStyle
import com.pumpkin.pac.databinding.ActivtyBrowserBinding
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
    private var parseContext: ParseContext? = null
    private var firstLoadFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = savedInstanceState ?: intent.extras
        if (bundle == null) {
            finishPAC("bundle is null .")
            return
        }
        val wordCardStyle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(Constant.FIRST_PARAMETER, WordCardStyle::class.java)
        } else {
            bundle.getParcelable(Constant.FIRST_PARAMETER)
        }
        if (wordCardStyle == null) {
            finishPAC("wordCardStyle entity is null .")
            return
        }

        initView(wordCardStyle.injectName)
        loadData(wordCardStyle)
    }

    private fun initView(injectName: String?) {
        binding = ActivtyBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //wv
        val pacWebEngine = getWV()
        if (pacWebEngine == null) {
            finishPAC("wv is null .")
            return
        }
        wv = pacWebEngine
        binding.wvContainer.removeAllViews()
        binding.wvContainer.addView(wv)

        if (injectName != null) {
            parseContext = ParseContext(injectName, pacWebEngine)
        }
    }

    override fun onBackPressed() {
        if (wv?.canGoBack() == true) {
            wv!!.goBack()
            return
        }
        super.onBackPressed()
    }

    private fun exit() {
        finishPAC("exit")
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

    private fun firstLoadEnd() {
        if (!firstLoadFinish) {
            loadFinish()
            firstLoadFinish = true
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

    private fun getWV(): PACWebEngine? {
        return WebViewPool.obtain(AppUtil.application, true)?.apply {
            layoutParams = FrameLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT)
            addClient()
            setPageInterface(pageCallback())
            setResourceInterface(resourceCallback())
            // TODO: error temporarily not processed .
        }
    }

    private fun resourceCallback() = object : Webinterface.ResourceInterface {
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
            shouldUrlLoading(view, request)
            return true
        }

        private fun shouldUrlLoading(view: WebView?, request: WebResourceRequest?) {
            GameHelper.shouldOverrideUrlLoading(view = view, request = request) { url ->
                loadEntrance(url)
            }
        }

    }

    private fun pageCallback() = object : Webinterface.DefaultPage() {
        override fun onPageFinished(view: WebView?, url: String?) {
            if (TextUtils.isEmpty(url) || TextUtils.equals(url, "about:blank")) {
                return
            }
            firstLoadEnd()
            progressUI(100)
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressUI(newProgress)
        }

        override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean? {
            val parseResult = parseContext?.callback(this@BrowserActivity, url, message)
            if (parseResult == null || !parseResult) {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            //todo 直接关闭返回恢复 还是 go back？
            Log.d(TAG, "onJsPrompt can go back ${wv?.canGoBack()} ")
            wv?.goBack()
            return super.onJsPrompt(view, url, message, defaultValue, result)
        }
    }

    private fun loadFinish() {
        parseContext?.inject()
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
        parseContext?.destroy()
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