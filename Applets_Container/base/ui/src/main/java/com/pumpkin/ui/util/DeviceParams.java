package com.pumpkin.ui.util;


import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.pumpkin.ui.BuildConfig;

/**
 * 设备参数
 */
public class DeviceParams {

    private static final String TAG = DeviceParams.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 状态栏高度
     */
    private static Integer sStatusBarHeight;

    private static String sRamSize;

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight == null) {
            Resources r = context.getResources();
            //状态栏高度
            int statusBarResId = r.getIdentifier("status_bar_height", "dimen", "android");
            if (statusBarResId > 0) {
                sStatusBarHeight = r.getDimensionPixelSize(statusBarResId);
            } else {
                sStatusBarHeight = 0;
            }
            if (DEBUG) {
                Log.d(TAG, "getStatusBarHeight()-> sStatusBarHeight: " + sStatusBarHeight);
            }
        }
        return sStatusBarHeight;
    }


    public static String getRamSize(Context context) {
        if (sRamSize == null) {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memoryInfo);
            sRamSize = FormatUtil.formatMemorySize(memoryInfo.totalMem);
            if (DEBUG) {
                Log.d(TAG, "getRamSize()-> sRamSize = " + sRamSize);
            }
        }
        return sRamSize;
    }
}
