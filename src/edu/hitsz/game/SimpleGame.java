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

    private Context heroContext;
    private Context enemyContext;

    public SimpleGame(int gameLevel, boolean enableAudio) {
        super(1, enableAudio);
        gameLevel = 1;
        enemyMaxNumber = 2; // actually is 3
        enemyMaxNumberUpperBound = 2; // actually is 3
        this.baseLevel = gameLevel;
        this.level = gameLevel;
        this.enableAudio = enableAudio;
        heroContext = new Context(new StraightShoot());
        enemyContext = new Context(new StraightShoot());
        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }


    public void generateEnemyAircrafts() {
        System.out.printf("Time: %7d    Level:%7.4f    MobSpeed:%4d    EliteHp:%4d    PropValidMaxTime:%4d\n", time, (double) baseLevel, Math.min((int) (5 * Math.sqrt(baseLevel)), 15), (int) (60 * Math.sqrt(this.baseLevel)), (int) (2000 / (5 + baseLevel)))
        ;

        // 新敌机产生
        if (enemyAircrafts.size() <= enemyMaxNumber && enemyMaxNumber <= enemyMaxNumberUpperBound) {
            // 随机数控制产生精英敌机
            boolean createElite = Math.random() < (1 / 3.0);
            if (mobCnt < mobCntMax && !createElite) {
                enemyAircrafts.add(mobFactory.createEnemy(this.baseLevel));
                mobCnt++;
            } else {
                enemyAircrafts.add(eliteFactory.createEnemy(this.baseLevel));
                mobCnt = 0;
            }
        }
        // 不生成boss敌机
    }

    public void playBGM() {
        if ((bgmThread == null || !bgmThread.isAlive())) {
            bgmThread = new MusicThread("src/video/bgm.wav");
            bgmThread.start();
        }
    }

    public void shootAction() {
        // [DONE] 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyContext.executeShootStrategy(enemyAircraft));
        }
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
                    bulletHitThread = new MusicThread("src/video/bullet_hit.wav");
                    bulletHitThread.start();
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
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
                    enemyAircraft.vanish();
                    // 护盾道具开启时
                    if (heroAircraft.getBulletPropStage() == 1) {
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
                            }
                        }
                    } else {
                        heroAircraft.decreaseHp(Integer.MAX_VALUE);
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
                    bombExplodeThread = new MusicThread("src/video/bomb_explosion.wav");
                    bombExplodeThread.start();
                } else if (prop.getClass().equals(BulletProp.class)) {
                    bulletFlag = true;
                    bulletPropThread = new MusicThread("src/video/bullet.wav");
                    bulletPropThread.start();
                    heroAircraft.setBulletPropStage(heroAircraft.getBulletPropStage() + 1);
                    bulletValidTimeCnt = (int) (2000 / (5 + baseLevel));
                } else if (prop.getClass().equals(BloodProp.class)) {
                    bloodFlag = true;
                    bloodPropThread = new MusicThread("src/video/get_supply.wav");
                    bloodPropThread.start();
                    bloodValidTimeCnt = (int) (2000 / (5 + baseLevel));

                }
                int increment = prop.getScore();
                score += increment;
                prop.vanish();
                // my Todo: 添加加血特效
                // my Todo: 添加爆炸特效
            }
        }

    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, props);
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2, heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        // 绘制得分和生命值
        paintScoreAndLife(g);
        // 绘制道具时间条
        paintHeroAttributes(g);
        // 绘制敌机生命条
        paintEnemyLife(g);

    }
}
