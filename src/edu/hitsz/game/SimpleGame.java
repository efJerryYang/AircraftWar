package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.strategy.StraightShoot;

import java.awt.*;


public class SimpleGame extends AbstractGame {

    public SimpleGame(int gameLevel, boolean enableAudio) {
        super(gameLevel, enableAudio);
        bloodPropGeneration = Config.BLOOD_PROP_GENERATION_SIMPLE;
        bombPropGeneration = Config.BOMB_PROP_GENERATION_SIMPLE;
        bulletPropGeneration = Config.BULLET_PROP_GENERATION_SIMPLE;
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
    public void enemyShootAction(){
        // [DONE] 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyContext.executeShootStrategy(enemyAircraft));
        }
    }
    public void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroContext.executeShootStrategy(heroAircraft));
    }
    public void aircraftsMoveAction() {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    public void paintBackground(Graphics g){
        g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }
    }

}
