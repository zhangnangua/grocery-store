package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.JsResult
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.pac.R
import com.pumpkin.pac.WebViewPool
import com.pumpkin.pac.bean.WordCardStyle
import com.pumpkin.pac.databinding.ActivtyBrowserBinding
import com.pumpkin.pac.parseStrategy.IParsed
import com.pumpkin.pac.parseStrategy.ParseContext
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

        initView(wordCardStyle, wordCardStyle.parsed)
        loadData(wordCardStyle)
    }

    private fun initView(wordCardStyle: WordCardStyle, parsed: IParsed?) {
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

        if (parsed != null) {
            parseContext = ParseContext(parsed, pacWebEngine)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (wv != null) {
            if (wv!!.canGoBack()) {
                wv!!.goBack()
                return
            }
        }
        exitDialog()
    }

    private fun exitDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.sure_exit_game))
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(getString(R.string.exit)) { dialog, which ->
                exit()
            }
            .show()
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

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d(TAG, "onPageStarted url is $url")
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    Log.d(TAG, "onPageCommitVisible url is $url")
                }

                override fun doUpdateVisitedHistory(view: WebView?, url: String?, reload: Boolean) {
                    super.doUpdateVisitedHistory(view, url, reload)
                    Log.d(TAG, "doUpdateVisitedHistory url is $url is reload $reload .")
                }

                override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean? {
                    Log.d(TAG, "onJsBeforeUnload url is $url.")
                    return super.onJsBeforeUnload(view, url, message, result)
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
                    Log.d(TAG, "shouldOverrideUrlLoading () -> ${request?.url}")
                    // TODO: loading
                    parseContext?.parse(request?.url) { entity ->
                        // TODO: hide loading
                        if (entity != null) {
                            GameActivity.go(this@BrowserActivity, entity)
                        } else {
                            shouldUrlLoading(view, request)
                        }
                    } ?: shouldUrlLoading(view, request)
                    return true
                }

                private fun shouldUrlLoading(view: WebView?, request: WebResourceRequest?) {
                    GameHelper.shouldOverrideUrlLoading(view = view, request = request) { url ->
                        loadEntrance(url)
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

        fun go(context: Context, entity: WordCardStyle, parse: IParsed? = null) {
            context.startActivity(Intent(context, BrowserActivity::class.java).apply {
                putExtra(Constant.FIRST_PARAMETER, entity)
                putExtra(Constant.SECOND_PARAMETER, parse)
                putExtra(Constant.PAGE_PARAMETER, ActivitySettingBean().apply {
                    enableImmersiveBar = true
                    enterAnim = R.anim.slide_in_from_bottom
                    exitAnim = R.anim.slide_out_to_bottom
                })
            })
        }
    }


}