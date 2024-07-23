package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.Toolbar.LayoutParams
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.pumpkin.data.AppUtil
import com.pumpkin.data.BuildConfig
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.R
import com.pumpkin.pac.WebViewPool
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.databinding.ActivityPacBinding
import com.pumpkin.pac.internal.InternalManager
import com.pumpkin.pac.repo.GameRepo
import com.pumpkin.pac.util.FloatDragHelper
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.pac.view.fragment.GameDialogFragment
import com.pumpkin.pac.view.fragment.LoadingFragment
import com.pumpkin.pac.viewmodel.GameViewModel
import com.pumpkin.pac_core.webview.PACWebEngine
import com.pumpkin.pac_core.webview.Webinterface
import com.pumpkin.ui.util.toShortToast
import kotlinx.coroutines.launch

/**
 * game container
 *
 *
 * todo 内置游戏load页面处理
 *
 * todo webview 剥离 预热 ！！！！
 */
class GameActivity : BaseActivity(), View.OnClickListener {

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
        val gson = Gson()
        val gameEntity = bundle.getParcelable(Constant.FIRST_PARAMETER)
            ?: GameRepo.shortCurParseGetEntity(bundle, gson)
        val gParameter = bundle.getParcelable(Constant.SECOND_PARAMETER)
            ?: GameRepo.shortCurParseGetParameter(bundle, gson)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate () -> game entity is $gameEntity , g parameter is $gParameter .")
        }
        if (gameEntity == null) {

            finishPAC("game entity is null .")
            return
        }
        binding = ActivityPacBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!showGuide()) {
        }
        setOrientation(gParameter)
        initView(gameEntity, gParameter)
        loadData(gameEntity, gParameter)
    }

    private fun showGuide(): Boolean {
        // TODO: 是否展示引导页面
        // TODO: 引导界面如何展示
        return false
    }

    private fun setOrientation(gParameter: GParameter?) {
        if (gParameter != null && gParameter.orientation != Constant.INVALID_ID) {
            if (requestedOrientation != gParameter.orientation) {
                requestedOrientation = if (gParameter.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
    }

    private fun initView(gameEntity: GameEntity, gParameter: GParameter?) {
        //show loading
        if (gParameter?.notShowLoading != true && !InternalManager.isInternalGame(gameEntity.id)) {
            UIHelper.showFragmentRemove(
                LoadingFragment::class.simpleName,
                supportFragmentManager,
                LoadingFragment(),
                R.id.loading_container
            )
        }

        //wv
        val pacWebEngine = getWV()
        if (pacWebEngine == null) {
            finishPAC("wv is null .")
            return
        }
        wv = pacWebEngine
        binding.wvContainer.removeAllViews()
        binding.wvContainer.addView(wv)

        //float
        binding.vFloat.setOnClickListener(this)
        binding.vFloat.setOnTouchListener(FloatDragHelper(this))
    }

    private fun getWV(): PACWebEngine? {
        return WebViewPool.obtain(AppUtil.application)?.apply {
            layoutParams =
                FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            addClient()
            setPageInterface(object : Webinterface.DefaultPage() {
                val PROGRESS_100 = 100
                val CLOSE_PROGRESS_40 = 40

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (TextUtils.isEmpty(url) || TextUtils.equals(url, "about:blank")) {
                        return
                    }
                    gameViewModel.progressFinished()
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress >= CLOSE_PROGRESS_40) {
                        gameViewModel.progressFinished()
                    } else {
                        gameViewModel.setProgress(newProgress * PROGRESS_100 / CLOSE_PROGRESS_40)
                    }
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
                    return GameHelper.shouldOverrideUrlLoading(view = view, request = request) {
                        loadEntrance(it)
                    }
                }

            })
            // TODO: error temporarily not processed .
        }
    }

    private fun loadData(gameEntity: GameEntity, gParameter: GParameter?) {
        gameViewModel.attach(gameEntity, gParameter)
        load(gameEntity)
    }

    private fun load(gameEntity: GameEntity) {
        loadEntrance(gameEntity.link)
        lifecycleScope.launch {
            gameViewModel.recordToRecently()
        }
    }

    fun exitDialog() {
        if (GameDialogFragment.isExit(this)) {
            GameDialogFragment.hide(this)
        } else {
            GameDialogFragment.show(this)
        }
    }

    private fun loadEntrance(url: String) {
        wv?.loadUrl(url)
    }

    override fun onClick(v: View?) {
        if (v == binding.vFloat) {
            exitDialog()
        }
    }

    override fun onBackPressed() {
        exitDialog()
    }

    fun exit() {
        finishPAC("exit")
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

    /**
     * 被外部调用 切换横竖屏
     */
    fun switchOrientation() {
        val gParameter = gameViewModel.getGParameter()
        if (gParameter != null && gParameter.orientation != Constant.INVALID_ID) {
            if (requestedOrientation == gParameter.orientation) {
                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    "Already vertical screen".toShortToast()
                } else {
                    "Already horizontal screen".toShortToast()
                }
                return
            }
        }
        requestedOrientation = if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    companion object {
        private const val TAG = "PACActivity"

        fun go(context: Context, gameEntity: GameEntity, gp: GParameter = GParameter(notShowLoading = false)) {
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