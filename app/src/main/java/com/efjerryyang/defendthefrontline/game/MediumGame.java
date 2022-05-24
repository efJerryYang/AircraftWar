package com.efjerryyang.defendthefrontline.game;

import static com.efjerryyang.defendthefrontline.aircraft.HeroAircraft.BOSS_APPEAR_SCORE;


import android.content.Context;

import com.efjerryyang.defendthefrontline.application.Config;
import com.efjerryyang.defendthefrontline.application.ImageManager;

public class MediumGame extends AbstractGame {

    public MediumGame(Context context, int gameLevel, boolean enableAudio) {
        super(context, gameLevel, enableAudio);
        bloodPropGeneration = Config.BLOOD_PROP_GENERATION_MEDIUM;
        bombPropGeneration = Config.BOMB_PROP_GENERATION_MEDIUM;
        bulletPropGeneration = Config.BULLET_PROP_GENERATION_MEDIUM;
        backgroundImage = ImageManager.BACKGROUND_IMAGE_LEVEL2;
    }

    @Override
    public void generateBoss() {
        if (score > (BOSS_APPEAR_SCORE * level) && scoreCnt <= 0) {
            enemyAircrafts.add(bossFactory.createEnemy(this.baseLevel)); // 不改变boss血量
            scoreCnt = (int) (BOSS_APPEAR_SCORE * level);
            bossFlag = true;
            if (enemyMaxNumber < enemyMaxNumberUpperBound) {
                enemyMaxNumber++;
            }
        }
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
//        if (bossFlag && (bgmBossThread == null || !bgmBossThread.isAlive())) {
//            if (bgmThread != null) {
//                bgmThread.setInterrupt(true);
//            }
//            bgmBossThread = new MusicThread("src/audios/bgm_vsboss.wav");
//            bgmBossThread.start();
//        }
//    }


}
