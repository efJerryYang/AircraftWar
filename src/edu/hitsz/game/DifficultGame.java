package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.MobEnemy;
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

import static edu.hitsz.aircraft.HeroAircraft.BOSS_APPEAR_SCORE;

public class DifficultGame extends AbstractGame {
    public DifficultGame(int gameLevel, boolean enableAudio) {
        super(gameLevel, enableAudio);
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
                    // 如果击毁的敌机是boss机，代表boss机在当前窗口消失
                    // 潜在的bug，如果boss机因为超过底线而消失，会不会导致之后boss机不出现？
                    if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                        bossFlag = false;
                    }
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
                        if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                            props.add(bloodPropFactory.createProp(
                                    (int) (Math.random() * enemyAircraft.getLocationX() + ImageManager.BOSS_ENEMY_IMAGE.getWidth() / 2),
                                    (int) (Math.random() * enemyAircraft.getLocationY() + ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2)
                            ));
                            props.add(bombPropFactory.createProp(
                                    (int) (Math.random() * enemyAircraft.getLocationX() + ImageManager.BOSS_ENEMY_IMAGE.getWidth() / 2),
                                    (int) (Math.random() * enemyAircraft.getLocationY() + ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2)
                            ));
                            props.add(bulletPropFactory.createProp(
                                    (int) (Math.random() * enemyAircraft.getLocationX() + ImageManager.BOSS_ENEMY_IMAGE.getWidth() / 2),
                                    (int) (Math.random() * enemyAircraft.getLocationY() + ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2)
                            ));
                            bossFlag = false;
                            bossCnt += 1;
                            levelScalar += 1;
                        }
                        crashFlag = true;
                        int increment = enemyAircraft.getClass().equals(BossEnemy.class) ? 50 : enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            // [DONE] 获得分数，产生道具补给
                            // 以 70% 概率生成道具
                            double randNum = Math.random();
                            if (randNum < 0.2) {
                                props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.4) {
                                props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.7) {
                                props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } // else do nothing

                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
//                System.out.println("bloodValidTimeCntCrash: " + this.bloodValidTimeCnt);
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    // 护盾道具开启时
                    crashWithShieldThread = new MusicThread("src/audios/crash.wav");
                    crashWithShieldThread.start();
                    if (bloodValidTimeCnt > 0) {
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                            enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                            // [DONE] 获得分数，产生道具补给
                            // 以 70% 概率生成道具
                            if (enemyAircraft.notValid()) {
                                int increment = enemyAircraft.getScore();
                                score += increment;
                                scoreCnt -= bossFlag ? 0 : increment;
                                double randNum = Math.random();
                                if (randNum < 0.2) {
                                    props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                                } else if (randNum < 0.4) {
                                    props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                                } else if (randNum < 0.7) {
                                    props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                                }
                            }
                        } else if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                            bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                            enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                            // 撞毁boss不加分
                        } else {
                            bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                            enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                            if (enemyAircraft.notValid()) {
                                int increment = enemyAircraft.getScore();
                                score += increment;
                                scoreCnt -= bossFlag ? 0 : increment;
                            }
                        }
                    } else {
                        heroAircraft.decreaseHp(heroAircraft.getMaxHp());
                        enemyAircraft.vanish();
                        // my Todo: 添加爆炸特效
                    }
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
                    bulletValidTimeCnt = (int) (2000 / (5 + level));
                } else if (prop.getClass().equals(BloodProp.class)) {
                    bloodFlag = true;
                    bloodPropThread = new MusicThread("src/audios/get_supply.wav");
                    bloodPropThread.start();
                    if (heroAircraft.getHp() == heroAircraft.getMaxHp()) {
                        bloodValidTimeCnt = (int) (2000 / (5 + level));
                    }
                }
                int increment = prop.getScore();
                score += increment;
                scoreCnt -= bossFlag ? 0 : increment;
                prop.vanish();
            }
        }

    }
    public void paintBackground(Graphics g){
        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL4, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL4, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }
    }

}
