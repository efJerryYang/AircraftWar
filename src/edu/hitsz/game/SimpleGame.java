package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.Context;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;
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

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    public void crashCheckAction() {
        // [DONE] 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    bulletCrash = true;
                    bulletHitThread = new MusicThread("src/audios/bullet_hit.wav");
                    bulletHitThread.start();
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        crashWithShieldThread = new MusicThread("src/audios/crash.wav");
                        crashWithShieldThread.start();
                        crashFlag = true;
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            // [DONE] 获得分数，产生道具补给
                            // 以 99% 概率生成道具
                            double randNum = Math.random();
                            if (randNum < 0.33) {
                                props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.66) {
                                props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.99) {
                                props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } // else do nothing

                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
//                    System.out.println(bloodValidTimeCnt);
                    // 护盾道具开启时
                    crashWithShieldThread = new MusicThread("src/audios/crash.wav");
                    crashWithShieldThread.start();
                    if (bloodValidTimeCnt > 0) {
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            enemyAircraft.vanish();
                            // [DONE] 获得分数，产生道具补给
                            // 以 99% 概率生成道具
                            int increment = enemyAircraft.getScore();
                            score += increment;
                            scoreCnt -= bossFlag ? 0 : increment;
                            double randNum = Math.random();
                            if (randNum < 0.33) {
                                props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.66) {
                                props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.99) {
                                props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            }
                        } else {
                            enemyAircraft.vanish();
                            int increment = enemyAircraft.getScore();
                            score += increment;
                            scoreCnt -= bossFlag ? 0 : increment;
                        }
                    } else {
                        heroAircraft.decreaseHp(heroAircraft.getMaxHp());
                        enemyAircraft.vanish();
                    }
                    // my Todo: 添加爆炸特效
                }
            }
        }
        // (DONE) 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (prop.crash(heroAircraft)) {
                prop.activate(heroAircraft, enemyAircrafts, heroBullets, enemyBullets, time);
                if (prop.getClass().equals(BombProp.class)) {
                    ((BombProp) prop).notifyAllSubscribers();
                    bombFlag = true;
                    bombExplodeThread = new MusicThread("src/audios/bomb_explosion.wav");
                    bombExplodeThread.start();
                } else if (prop.getClass().equals(BulletProp.class)) {
                    bulletFlag = true;
                    bulletPropThread = new MusicThread("src/audios/bullet.wav");
                    bulletPropThread.start();
                    heroAircraft.setBulletPropStage(heroAircraft.getBulletPropStage() + 1);
                    bulletValidTimeCnt = (int) (2000 / (5 + baseLevel));
                } else if (prop.getClass().equals(BloodProp.class)) {
                    bloodFlag = true;
                    bloodPropThread = new MusicThread("src/audios/get_supply.wav");
                    bloodPropThread.start();
                    if (heroAircraft.getHp() == heroAircraft.getMaxHp()) {
                        bloodValidTimeCnt = (int) (2000 / (5 + baseLevel));
                    }
                }
                int increment = prop.getScore();
                score += increment;
                prop.vanish();
                // my Todo: 添加加血特效
                // my Todo: 添加爆炸特效
            }
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
