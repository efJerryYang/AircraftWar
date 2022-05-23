package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.application.Config;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;

import java.awt.*;


public class SimpleGame extends AbstractGame {

    public SimpleGame(int gameLevel, boolean enableAudio) {
        super(gameLevel, enableAudio);
        bloodPropGeneration = Config.BLOOD_PROP_GENERATION_SIMPLE;
        bombPropGeneration = Config.BOMB_PROP_GENERATION_SIMPLE;
        bulletPropGeneration = Config.BULLET_PROP_GENERATION_SIMPLE;
        backgroundImage = ImageManager.BACKGROUND_IMAGE_LEVEL1;
    }

    @Override
    public void generateBoss() {
        // do not generate boss
    }

    public void playBGM() {
        if (bgmThread == null || !bgmThread.isAlive()) {
            bgmThread = new MusicThread("src/audios/bgm.wav");
            bgmThread.start();
        }
    }

}
