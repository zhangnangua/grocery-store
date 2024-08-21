package com.pumpkin.applets_container.helper

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.pumpkin.applets_container.view.fragment.GDPRFragment
import com.pumpkin.data.AppUtil
import com.pumpkin.data.mmkv.KV
import com.pumpkin.pac.view.BrowserActivity

object GDPRHelper {
    private const val USER_AGREEMENT_PATH = "file:///android_asset/user_agreement_and_privacy_policy/user_agreement.html"
    private const val PRIVACY_POLICY_PATH = "file:///android_asset/user_agreement_and_privacy_policy/privacy_policy.html"
    private const val KEY_GDPR = "key.gdpr"

    fun goUserAgreement(context: Context) {
        BrowserActivity.go(context, USER_AGREEMENT_PATH)
    }

    fun goPrivacyPolicy(context: Context) {
        BrowserActivity.go(context, PRIVACY_POLICY_PATH)
    }

    fun tryShowGdpr(activity: FragmentActivity) {
        val defaultModule = KV.getDefaultModule(AppUtil.application)
        if (!defaultModule.decodeBool(KEY_GDPR, false)) {
            GDPRFragment.showNoRepeat(activity) {
                defaultModule.encode(KEY_GDPR, true)
            }
        }
    }
}