package com.pumpkin.dgx_super_boy;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pumpkin.dgx_super_boy.entrance.MyGame;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//        initialize(new SuperSpineboy(), config);
        initialize(new MyGame(), config);
    }
}