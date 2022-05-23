package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.application.Config;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;

import java.awt.*;

import static edu.hitsz.aircraft.HeroAircraft.BOSS_APPEAR_SCORE;

public class MediumGame extends AbstractGame {

    public MediumGame(int gameLevel, boolean enableAudio) {
        super(gameLevel, enableAudio);
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

    public void playBGM() {
        if (!bossFlag && (bgmThread == null || !bgmThread.isAlive())) {
            if (bgmBossThread != null) {
                bgmBossThread.setInterrupt(true);
            }
            bgmThread = new MusicThread("src/audios/bgm.wav");
            bgmThread.start();
        }
        if (bossFlag && (bgmBossThread == null || !bgmBossThread.isAlive())) {
            if (bgmThread != null) {
                bgmThread.setInterrupt(true);
            }
            bgmBossThread = new MusicThread("src/audios/bgm_vsboss.wav");
            bgmBossThread.start();
        }
    }

}
