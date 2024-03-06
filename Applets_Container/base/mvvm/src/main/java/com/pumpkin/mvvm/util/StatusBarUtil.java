package com.pumpkin.mvvm.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.pumpkin.mvvm.BuildConfig;
import com.pumpkin.ui.util.DeviceParams;

import java.lang.reflect.Method;

public final class StatusBarUtil {

    public static void hideLayoutNavigation(Window window) {
        View decorView = window.getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                //内容布局延伸至虚拟键，但是保留虚拟键可见
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(visibility);
    }

    public static void fullScreenAndLayoutStable(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //内容延伸至状态栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //内容布局延伸至虚拟键，但是保留虚拟键可见
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            decorView.setSystemUiVisibility(visibility);

        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void hideBar(Window window) {
        if (window != null) {
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(visibility);
        }
    }

    public static void contentfullScreen(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //内容延伸至状态栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //内容布局延伸至虚拟键，但是保留虚拟键可见
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            decorView.setSystemUiVisibility(visibility);
            window.setNavigationBarColor(Color.TRANSPARENT);

        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public static void setSystemUiFlagLightStatusBar(Activity activity, boolean lightStatusBar) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decor = activity.getWindow().getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (lightStatusBar) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }

            decor.setSystemUiVisibility(ui);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        } else {

            View view = new View(activity);
            view.setBackgroundColor(color);
            ((ViewGroup) window.getDecorView()).addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    DeviceParams.getStatusBarHeight(activity));
        }
    }

    public static boolean isWaterfallScreen(Context context) {
        try {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("waterfall_display_left_edge_size", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result > 0;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean hasDisplayCutoutForP(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null) {
            WindowInsets windowInsets = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                windowInsets = decorView.getRootWindowInsets();
            }
            if (windowInsets != null) {
                try {
                    Class<?> clazz = Class.forName("android.view.WindowInsets");
                    Method method = clazz.getMethod("getDisplayCutout");
                    return method.invoke(windowInsets) != null;
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        Log.e("NotchScreen", "hasDisplayCutout error : " + e);
                    }
                }
            }
        }
        return false;
    }
}
