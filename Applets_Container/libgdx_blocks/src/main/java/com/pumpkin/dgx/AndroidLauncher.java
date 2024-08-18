/*
    1010! Klooni, a free customizable puzzle game for Android and Desktop
    Copyright (C) 2017-2019  Lonami Exo @ lonami.dev

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.pumpkin.dgx;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pumpkin.data.AppUtil;
import com.pumpkin.mvvm.util.WidgetUtil;
import com.pumpkin.mvvm.widget.exit_dialog.ExitDialogManager;

import java.lang.reflect.Method;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AndroidLauncher extends AndroidApplication {
    ExitDialogManager pixelDungeon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIXME: Hack to allow us use the old way to share files
        // https://stackoverflow.com/a/42437379/
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        final AndroidShareChallenge shareChallenge = new AndroidShareChallenge(this);
        initialize(new Klooni(shareChallenge), config);

        String name = "Blocks";
        String icon = "file:///android_asset/n_icon/blocks.jpg";
        String clzName = "com.pumpkin.dgx.AndroidLauncher";
        String id = "n_block_1";
        pixelDungeon = new ExitDialogManager(this, name, icon, id);
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
