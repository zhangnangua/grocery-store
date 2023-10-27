package com.pumpkin.data.fb

import android.text.TextUtils
import android.util.Log
import com.google.firebase.remoteconfig.*
import com.pumpkin.data.AppUtil
import com.pumpkin.data.BuildConfig
import com.pumpkin.data.db.FB
import com.pumpkin.data.mmkv.KV
import com.tencent.mmkv.MMKV


/**
 * fb fetch
 */
class FBFetch {

    private val kv: MMKV by lazy(LazyThreadSafetyMode.NONE) {
        KV.getDefaultModule(AppUtil.application)
    }

    fun fetchListener() {
        try {
            val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings: FirebaseRemoteConfigSettings =
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build()
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        syncData(mFirebaseRemoteConfig)
                    }
                }

            mFirebaseRemoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    mFirebaseRemoteConfig.activate()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                syncData(mFirebaseRemoteConfig)
                            }
                        }
                }

                override fun onError(error: FirebaseRemoteConfigException) {
//                    if (BuildConfig.DEBUG) {
//                        throw error
//                    }
                }
            })
        } catch (_: Exception) {
        }
    }

    private fun syncData(mFirebaseRemoteConfig: FirebaseRemoteConfig) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "syncData () -> $mFirebaseRemoteConfig")
        }
        syncHomeBigFeedData(mFirebaseRemoteConfig)
    }

    private fun syncHomeBigFeedData(remoteConfig: FirebaseRemoteConfig) {
        val json = remoteConfig.getString(FB.FB_HOME_BIG_FEED_DATA)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "syncHomeBigFeedData () -> $json")
        }

        if (TextUtils.isEmpty(json)) {
            kv.remove(FB.FB_HOME_BIG_FEED_DATA)
        } else {
            kv.putString(FB.FB_HOME_BIG_FEED_DATA, json)
        }
    }

    companion object {
        private const val TAG = "FB"
    }

}