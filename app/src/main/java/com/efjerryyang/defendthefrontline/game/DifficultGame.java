package com.efjerryyang.defendthefrontline.game;

import android.content.Context;

import com.efjerryyang.defendthefrontline.application.Config;
import com.efjerryyang.defendthefrontline.application.ImageManager;


public class DifficultGame extends AbstractGame {
    public DifficultGame(Context context, int gameLevel, boolean enableAudio) {
        super(context, gameLevel, enableAudio);
        bloodPropGeneration = Config.BLOOD_PROP_GENERATION_DIFFICULT;
        bombPropGeneration = Config.BOMB_PROP_GENERATION_DIFFICULT;
        bulletPropGeneration = Config.BULLET_PROP_GENERATION_DIFFICULT;
        backgroundImage = ImageManager.BACKGROUND_IMAGE_LEVEL4;
    }
//
//    public void playBGM() {
//        if (!bossFlag && (bgmThread == null || !bgmThread.isAlive())) {
//            if (bgmBossThread != null) {
//                bgmBossThread.setInterrupt(true);
//            }
//            bgmThread = new MusicThread("src/audios/bgm.wav");
//            bgmThread.start();
//        }
//
//        if (bossFlag && (bgmBossThread == null || !bgmBossThread.isAlive())) {
//            if (bgmThread != null) {
//                bgmThread.setInterrupt(true);
//            }
//            bgmBossThread = new MusicThread("src/audios/bgm_vsboss.wav");
//            bgmBossThread.start();
//        }
//    }
}
