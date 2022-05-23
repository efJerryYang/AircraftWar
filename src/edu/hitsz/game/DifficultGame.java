package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.application.Config;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;

import java.awt.*;

public class DifficultGame extends AbstractGame {
    public DifficultGame(int gameLevel, boolean enableAudio) {
        super(gameLevel, enableAudio);
        bloodPropGeneration = Config.BLOOD_PROP_GENERATION_DIFFICULT;
        bombPropGeneration = Config.BOMB_PROP_GENERATION_DIFFICULT;
        bulletPropGeneration = Config.BULLET_PROP_GENERATION_DIFFICULT;
        backgroundImage = ImageManager.BACKGROUND_IMAGE_LEVEL4;
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
