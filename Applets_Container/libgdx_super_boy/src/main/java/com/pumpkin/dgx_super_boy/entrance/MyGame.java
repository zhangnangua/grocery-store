package com.pumpkin.dgx_super_boy.entrance;

import com.badlogic.gdx.Game;
import com.pumpkin.dgx_super_boy.screen.FlashScreen;
import com.pumpkin.dgx_super_boy.screen.GameScreen;

public class MyGame extends Game {
    private FlashScreen flashScreen;
    private GameScreen gameScreen;


    @Override
    public void create() {
        flashScreen = new FlashScreen(new FlashScreen.IFlashScreenControl() {
            @Override
            public void done() {
                gameScreen = new GameScreen();
                setScreen(gameScreen);
            }
        });
        setScreen(flashScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (flashScreen != null) {
            flashScreen.dispose();
            flashScreen = null;
        }
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }
}
