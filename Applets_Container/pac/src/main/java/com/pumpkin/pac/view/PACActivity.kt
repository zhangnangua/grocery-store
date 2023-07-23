package com.pumpkin.pac.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.mvvm.view.SuperBaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.databinding.ActivityPacBinding
import com.pumpkin.pac.viewmodel.GameViewModel

/**
 * game container
 *
 *
 * todo 接受 http/https 的能力
 */
class PACActivity : SuperBaseActivity() {

    private lateinit var binding: ActivityPacBinding

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

        initView()

        loadData(gameEntity)
    }

    private fun initView() {
        binding = ActivityPacBinding.inflate(layoutInflater)
        setPACContentView(binding.root)
        //todo show loading

    }

    private fun loadData(gameEntity: GameEntity) {
        val gameViewModel = PACViewModelProviders.of(this).get(GameViewModel::class.java)
        gameViewModel.attach(gameEntity)

    }


    private fun finishPAC(reason: String? = null) {
        reason?.toLogD(TAG)

        finish()
    }

    companion object {
        private const val TAG = "PACActivity"

        internal fun go(context: Context, gameEntity: GameEntity) {
            context.startActivity(Intent(context, PACActivity::class.java).apply {
                putExtra(Constant.FIRST_PARAMETER, gameEntity)
            })
        }
    }
}