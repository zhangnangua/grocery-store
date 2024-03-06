package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.Toolbar.LayoutParams
import androidx.lifecycle.lifecycleScope
import com.pumpkin.data.AppUtil
import com.pumpkin.data.BuildConfig
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.R
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.databinding.ActivityPacBinding
import com.pumpkin.pac.pool.WebViewPool
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.pac.view.fragment.LoadingFragment
import com.pumpkin.pac.viewmodel.GameViewModel
import com.pumpkin.pac_core.webview.PACWebEngine
import com.pumpkin.pac_core.webview.Webinterface
import kotlinx.coroutines.launch

/**
 * game container
 *
 *
 * todo 内置游戏load页面处理
 *
 * todo webview 剥离 预热 ！！！！
 */
class GameActivity : BaseActivity() {

    private lateinit var binding: ActivityPacBinding

    private var wv: PACWebEngine? = null

    private val gameViewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = savedInstanceState ?: intent.extras
        if (bundle == null) {
            finishPAC("bundle is null .")
            return
        }
        val gameEntity = bundle.getParcelable<GameEntity>(Constant.FIRST_PARAMETER)
        val gParameter = bundle.getParcelable<GParameter>(Constant.SECOND_PARAMETER)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate () -> game entity is $gameEntity , g parameter is $gParameter .")
        }

        if (gameEntity == null) {
            finishPAC("game entity is null .")
            return
        }

        initView(gParameter)

        loadData(gameEntity, gParameter)
    }

    private fun initView(gParameter: GParameter?) {
        binding = ActivityPacBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //show loading
        if (gParameter?.isInternal != true) {
            UIHelper.showFragmentRemove(
                LoadingFragment::class.simpleName,
                supportFragmentManager,
                LoadingFragment(),
                R.id.loading_container
            )
        }

        //wv
        wv = getWV()
        binding.wvContainer.removeAllViews()
        binding.wvContainer.addView(wv)
    }

    private fun loadData(gameEntity: GameEntity, gParameter: GParameter?) {
        gameViewModel.attach(gameEntity, gParameter)

        load(gameEntity)
    }

//    override fun onBackPressed() {
//        if (wv != null) {
//            if (wv!!.canGoBack()) {
//                wv!!.goBack()
//                return
//            }
//        }
//        super.onBackPressed()
//    }

    private fun load(gameEntity: GameEntity) {
        loadEntrance(gameEntity.link)
        lifecycleScope.launch {
            gameViewModel.recordToRecently()
        }
    }

    private fun loadEntrance(url: String) {
        wv?.loadUrl(url)
    }

    private fun getWV(): PACWebEngine {
        return WebViewPool.obtain(AppUtil.application).apply {
            layoutParams =
                FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            addClient()
            setPageInterface(object : Webinterface.DefaultPage() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (TextUtils.isEmpty(url) || TextUtils.equals(url, "about:blank")) {
                        return
                    }
                    gameViewModel.progressFinished()
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    gameViewModel.setProgress(newProgress)
                }
            })
            setResourceInterface(object : Webinterface.ResourceInterface {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    return gameViewModel.resInterceptor(request)
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

        fun go(context: Context, gameEntity: GameEntity, gp: GParameter = GParameter(false)) {
            context.startActivity(Intent(context, GameActivity::class.java).apply {
                putExtra(Constant.FIRST_PARAMETER, gameEntity)
                putExtra(Constant.SECOND_PARAMETER, gp)
                putExtra(Constant.PAGE_PARAMETER, ActivitySettingBean().apply {
                    enableImmersiveBar = true
                })
            })
        }

    }
}