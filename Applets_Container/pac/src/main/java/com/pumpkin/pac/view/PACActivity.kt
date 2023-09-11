package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.Toolbar.LayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.SuperBaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.R
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.databinding.ActivityPacBinding
import com.pumpkin.pac.pool.WebViewPool
import com.pumpkin.pac.util.GameProgressHelper
import com.pumpkin.pac.viewmodel.GameViewModel
import com.pumpkin.pac.widget.loading.LoadingView
import com.pumpkin.pac.widget.loadingFish.FishRelativeLayout
import com.pumpkin.pac_core.webview.PACWebView
import com.pumpkin.pac_core.webview.Webinterface
import com.pumpkin.ui.util.AppUtil
import com.pumpkin.ui.widget.MultiStateView
import kotlinx.coroutines.launch

/**
 * game container
 *
 *
 * todo 接受 http/https 的能力
 *
 * todo webview 剥离 预热 ！！！！
 */
class PACActivity : SuperBaseActivity() {

    private lateinit var binding: ActivityPacBinding

    private var wv: PACWebView? = null

    private lateinit var loadingView: LoadingView

    private val gameViewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    private lateinit var progressHelper: GameProgressHelper

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        val bundle: Bundle? = savedInstanceState ?: intent.extras
        if (bundle == null) {
            finishPAC("bundle is null .")
            return
        }
        val gameEntity = bundle.getParcelable<GameEntity>(Constant.FIRST_PARAMETER)
        if (gameEntity == null) {
            finishPAC("game entity is null .")
            return
        }

        initView(gameEntity)

        loadData(gameEntity)
    }

    private fun initView(gameEntity: GameEntity) {
        binding = ActivityPacBinding.inflate(layoutInflater)
        setPACContentView(binding.root)

        fillBaseContent()

        //show loading
        switchState(MultiStateView.ViewState.LOADING)

        //wv
        wv = getWV(gameEntity)
        binding.wvContainer.removeAllViews()
        binding.wvContainer.addView(wv)
    }

    private fun loadData(gameEntity: GameEntity) {
        progressHelper = GameProgressHelper(lifecycle)

        gameViewModel.attach(gameEntity)

        lifecycle.coroutineScope.launch {
            // 每次生命周期处于 STARTED 状态（或更高状态）时，repeatOnLifecycle 在新的协程中启动块，并在它停止时取消它。
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                progressHelper.getProgressFlow().collect {
                    if (AppUtil.isDebug) {
                        "current progress is $it".toLogD(TAG)
                    }
                    if (it >= GameProgressHelper.MAX_PROGRESS) {
                        // TODO: close loading
//                        switchState(MultiStateView.ViewState.CONTENT)
                    } else {
                        loadingView.progress(it)
                    }
                }
            }
            //如果这里被执行，则代表生命周期已经走到了onDestroy，因为repeatOnLifecycle是挂起函数，在生命周期为onDestroy的时候进行了恢复。

        }

        load(gameEntity)

    }

    override fun onBackPressed() {
        if (wv != null) {
            if (wv!!.canGoBack()) {
                wv!!.goBack()
                return
            }
        }
        super.onBackPressed()
    }

    private fun load(gameEntity: GameEntity) {
        wv?.loadUrl(gameEntity.link)
    }

    private fun fillBaseContent() {
        //loading
        loadingView =
            LayoutInflater.from(this).inflate(R.layout.fragment_loading, null).run {
                //fill
                setBaseContentForState(this, MultiStateView.ViewState.LOADING)

                findViewById(R.id.loading)
            }


        //other
    }

    private fun getWV(gameEntity: GameEntity): PACWebView {
        return WebViewPool.obtain(this, gameEntity.id).apply {
            layoutParams =
                FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            addClient()
            setPageInterface(object : Webinterface.DefaultPage() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (TextUtils.isEmpty(url) || TextUtils.equals(url, "about:blank")) {
                        return
                    }
                    progressHelper.progressFinished()
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressHelper.setProgress(newProgress)
                }
            })
            setResourceInterface(object : Webinterface.ResourceInterface {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    return gameViewModel.interceptionGlobal?.intercept(view, request)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    // TODO: Temporarily not processed .
                    return false
                }

            })
            // TODO: error temporarily not processed .
        }
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
        private const val TAG = "PACActivity"

        fun go(context: Context, gameEntity: GameEntity) {
            context.startActivity(Intent(context, PACActivity::class.java).apply {
                putExtra(Constant.FIRST_PARAMETER, gameEntity)
            })
        }
    }
}