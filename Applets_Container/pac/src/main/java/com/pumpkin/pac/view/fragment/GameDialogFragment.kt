//package com.pumpkin.pac.view.fragment
//
//import android.os.Bundle
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import androidx.appcompat.app.AppCompatDialogFragment
//import androidx.fragment.app.FragmentActivity
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners
//import com.pumpkin.data.AppUtil
//import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
//import com.pumpkin.pac.R
//import com.pumpkin.pac.databinding.FragmentGameDialogBinding
//import com.pumpkin.pac.util.GameHelper
//import com.pumpkin.pac.view.GameActivity
//import com.pumpkin.pac.viewmodel.GameViewModel
//import com.pumpkin.ui.util.DeviceParams
//import com.pumpkin.ui.util.dpToPx
//
//class GameDialogFragment : AppCompatDialogFragment(), View.OnClickListener {
//    lateinit var binding: FragmentGameDialogBinding
//    private lateinit var gameViewModel: GameViewModel
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val dialog = dialog
//        if (dialog != null) {
//            val window = dialog.window
//            window!!.setBackgroundDrawableResource(R.color.transparent)
//            window.decorView.setPadding(0, 0, 0, DeviceParams.getStatusBarHeight(AppUtil.application) / 2)
//            val wlp = window.attributes
//            wlp.gravity = Gravity.CENTER
//            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT
//            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
//            window.setAttributes(wlp)
//        }
//        setCancelable(true)
//        binding = FragmentGameDialogBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val localActivity = activity
//        if (localActivity == null) {
//            dismissAllowingStateLoss()
//            return
//        }
//        gameViewModel = PACViewModelProviders.of(localActivity)[GameViewModel::class.java]
//        val gameEntity = gameViewModel.getGameEntity()
//        if (gameEntity != null) {
//            binding.ivName.text = gameEntity.name
//            Glide.with(localActivity)
//                .load(gameEntity.icon)
//                .transform(RoundedCorners(8F.dpToPx))
//                .into(binding.ivIcon)
//        }
//
//        binding.flExit.setOnClickListener(this)
//        binding.flNext.setOnClickListener(this)
//        binding.flCreateShortCut.setOnClickListener(this)
//        binding.flOrientation.setOnClickListener(this)
//    }
//
//    override fun onClick(v: View?) {
//        if (v == binding.flExit) {
//            val localActivity = activity
//            if (localActivity is GameActivity) {
//                localActivity.exit()
//            }
//        } else if (v == binding.flCreateShortCut) {
//            gameViewModel.createShortcut()
//            close()
//
//        } else if (v == binding.flOrientation) {
//            val localActivity = activity
//            if (localActivity is GameActivity) {
//                localActivity.switchOrientation()
//                close()
//            }
//
//        } else if (v == binding.flNext) {
//            val localActivity = activity
//            if (localActivity is GameActivity) {
//                GameHelper.starrRandomNextGame(localActivity)
//                localActivity.exit()
//            }
//        }
//    }
//
//    private fun close() {
//        val localActivity = activity
//        if (localActivity is GameActivity) {
//            localActivity.exitDialog()
//        }
//    }
//
//
//    companion object {
//        fun show(activity: FragmentActivity) {
//            val manager = activity.supportFragmentManager
//            val tag: String = GameDialogFragment::class.java.getSimpleName()
//            try {
//                val oldFragment = manager.findFragmentByTag(tag)
//                if (oldFragment != null && oldFragment.isResumed) {
//                    return
//                }
//                val ft = manager.beginTransaction()
//                if (oldFragment != null) {
//                    ft.remove(oldFragment)
//                }
//                GameDialogFragment().show(ft, tag)
//            } catch (ignored: Exception) {
//            }
//        }
//
//        fun isExit(activity: FragmentActivity): Boolean {
//            val manager = activity.supportFragmentManager
//            val tag: String = GameDialogFragment::class.java.getSimpleName()
//            val oldFragment = manager.findFragmentByTag(tag)
//            return oldFragment is GameDialogFragment
//        }
//
//        fun hide(activity: FragmentActivity) {
//            val manager = activity.supportFragmentManager
//            val tag: String = GameDialogFragment::class.java.getSimpleName()
//            try {
//                val oldFragment = manager.findFragmentByTag(tag)
//                if (oldFragment is GameDialogFragment) {
//                    oldFragment.dismissAllowingStateLoss()
//                }
//            } catch (ignored: Exception) {
//            }
//        }
//    }
//}