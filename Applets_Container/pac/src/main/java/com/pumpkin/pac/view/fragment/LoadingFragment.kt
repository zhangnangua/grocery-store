package com.pumpkin.pac.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.BaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.R
import com.pumpkin.pac.databinding.FragmentLoadingBinding
import com.pumpkin.pac.util.GameProgressHelper
import com.pumpkin.pac.viewmodel.GameViewModel
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.launch

class LoadingFragment : BaseFragment() {

    private var gameViewModel: GameViewModel? = null

    lateinit var binding: FragmentLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun loadData() {
        val fragmentActivity = activity
        val localContext = context
        if (fragmentActivity == null || localContext == null) {
            close()
            return
        }
        gameViewModel = PACViewModelProviders.of(fragmentActivity).get(GameViewModel::class.java)
        val data = gameViewModel?.getGameEntity() ?: return


        Glide.with(localContext)
            .load(data.icon)
            .transform(RoundedCorners(12F.dpToPx))
            .into(binding.icon)

        binding.name.text = data.name

        lifecycle.coroutineScope.launch {
            // 每次生命周期处于 STARTED 状态（或更高状态）时，repeatOnLifecycle 在新的协程中启动块，并在它停止时取消它。
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                gameViewModel?.getProgressFlow()?.collect {
                    if (AppUtil.isDebug) {
                        "current progress is $it".toLogD(TAG)
                    }
                    if (it >= GameProgressHelper.MAX_PROGRESS) {
                        close()
                    } else {
                        binding.loading.progress = it
                        binding.loadingProgress.text = String.format(AppUtil.application.getString(R.string.loading), "$it")
                    }
                }
            }
            //如果这里被执行，则代表生命周期已经走到了onDestroy，因为repeatOnLifecycle是挂起函数，在生命周期为onDestroy的时候进行了恢复。

        }
    }

    /**
     * 关闭loading
     */
    private fun close() {
        val fragmentActivity = activity ?: return
        UIHelper.removeFragment(fragmentActivity.supportFragmentManager, this)
        //close 可以在这里被监听
    }

    companion object {
        const val TAG = "LoadingFragment"
    }
}