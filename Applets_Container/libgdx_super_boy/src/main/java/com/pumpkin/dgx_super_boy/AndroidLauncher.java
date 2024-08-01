package com.pumpkin.dgx_super_boy;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pumpkin.data.AppUtil;
import com.pumpkin.dgx_super_boy.entrance.MyGame;
import com.pumpkin.mvvm.util.WidgetUtil;
import com.pumpkin.mvvm.widget.exit_dialog.ExitDialogManager;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AndroidLauncher extends AndroidApplication {
    ExitDialogManager pixelDungeon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//        initialize(new SuperSpineboy(), config);
        initialize(new MyGame(), config);

        String name = "SuperBoy";
        String icon = "file:///android_asset/n_icon/super_main.jpeg";
        String clzName = "com.pumpkin.dgx_super_boy.AndroidLauncher";
        pixelDungeon = new ExitDialogManager(this, name, icon);
        pixelDungeon.register(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                if (integer == ExitDialogManager.BT_EXIT) {
                    finish();
                } else if (integer == ExitDialogManager.BT_CREATE_CUT) {
                    WidgetUtil.INSTANCE.createShortcut(AndroidLauncher.this, icon, name, String.valueOf(name.hashCode()), clzName);
                } else if (integer == ExitDialogManager.BT_NEXT) {
                    AppUtil.INSTANCE.getGameHelper().starrRandomNextGame(AndroidLauncher.this);
                    finish();
                } else if (integer == ExitDialogManager.BT_ORIENTATION) {
                    android.widget.Toast.makeText(AndroidLauncher.this, "no treatment required", android.widget.Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
        pixelDungeon.attach();
    }

    @Override
    public void finish() {
        if (pixelDungeon != null) {
            if (pixelDungeon.lastIsCanExit()) {
                super.finish();
            } else {
                pixelDungeon.onBackPressed();
            }
        }

    }
}