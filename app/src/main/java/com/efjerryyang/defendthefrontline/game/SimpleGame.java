package com.efjerryyang.defendthefrontline.game;

import android.content.Context;
import android.util.Log;

import com.efjerryyang.defendthefrontline.application.Config;
import com.efjerryyang.defendthefrontline.application.ImageManager;


public class SimpleGame extends AbstractGame {

    public SimpleGame(Context context, int gameLevel, boolean enableAudio) {
        super(context, gameLevel, enableAudio);
        bloodPropGeneration = Config.BLOOD_PROP_GENERATION_SIMPLE;
        bombPropGeneration = Config.BOMB_PROP_GENERATION_SIMPLE;
        bulletPropGeneration = Config.BULLET_PROP_GENERATION_SIMPLE;
        backgroundImage = ImageManager.BACKGROUND_IMAGE_LEVEL1;

    }

    @Override
    public void generateBoss() {
        // do not generate boss
    }

//    public void playBGM() {
//        if (bgmThread == null || !bgmThread.isAlive()) {
//            bgmThread = new MusicThread("src/audios/bgm.wav");
//            bgmThread.start();
//        }
//    }


}
