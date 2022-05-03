package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.record.Record;
import edu.hitsz.strategy.StraightShoot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static edu.hitsz.aircraft.HeroAircraft.BOSS_APPEAR_SCORE;

/**
 * 游戏主面板，游戏启动
 *
 * @author JerryYang
 */
public class Game extends AbstractGame {

    private Context heroContext;
    private Context enemyContext;

    public Game(int gameLevel, boolean enableAudio) {
        super(gameLevel, enableAudio);
        this.level = gameLevel;
        this.enableAudio = enableAudio;
        heroContext = new Context(new StraightShoot());
        enemyContext = new Context(new StraightShoot());
        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }

    public void bulletPropStageCount() {
        if (heroAircraft.getBulletPropStage() > 0) {
            bulletValidTimeCnt -= 1;
            bulletValidTimeCnt = Math.max(bulletValidTimeCnt, 0);
        }
        if (bulletValidTimeCnt <= 0) {
            bulletValidTimeCnt = 0;
            heroAircraft.setBulletPropStage(heroAircraft.getBulletPropStage() - 1);
            if (heroAircraft.getBulletPropStage() > 0) {
                bulletValidTimeCnt = 200;
            }
        }
        switch (heroAircraft.getBulletPropStage()) {
            case 0 -> {
                heroAircraft.setShootNum(1);
                heroAircraft.setBulletSpeedUp(false);
            }
            case 1, 2 -> {
                heroAircraft.setBulletSpeedUp(false);
                heroAircraft.setShootNum(heroAircraft.getBulletPropStage() * 2);
            }
            case 3 -> {
                heroAircraft.setShootNum(5);
            }
            default -> {
                heroAircraft.setBulletPropStage(3);
            }
        }

        System.out.print("Bullet State: " + heroAircraft.getBulletPropStage());
        System.out.println("\tBullet Time Cnt: " + bulletValidTimeCnt);

    }

    public void generateEnemyAircrafts() {
        System.out.println(time);
        // 新敌机产生
        boolean moveRight = Math.random() * 2 < 1;
        if (enemyAircrafts.size() <= enemyMaxNumber && enemyMaxNumber <= enemyMaxNumberUpperBound) {
            // 随机数控制产生精英敌机
            boolean createElite = Math.random() * 3 < 1;
            if (mobCnt < mobCntMax && !createElite) {
                enemyAircrafts.add(mobFactory.createEnemy(this.level));
                mobCnt++;
            } else {
                enemyAircrafts.add(eliteFactory.createEnemy(this.level));
                mobCnt = 0;
            }
        }
        // 控制生成boss敌机
        System.out.println("score: " + score + " scoreCnt: " + scoreCnt + " bossFlag: " + bossFlag);
        if (score > BOSS_APPEAR_SCORE && scoreCnt <= 0) {
            enemyAircrafts.add(bossFactory.createEnemy(this.level));
            scoreCnt = BOSS_APPEAR_SCORE;
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
            bgmThread = new MusicThread("src/video/bgm.wav");
            bgmThread.start();
        }

        if (bossFlag && (bgmBossThread == null || !bgmBossThread.isAlive())) {
            if (bgmThread != null) {
                bgmThread.setInterrupt(true);
            }
            bgmBossThread = new MusicThread("src/video/bgm_boss.wav");
            bgmBossThread.start();
        }
    }
    //***********************
    //      Action 各部分
    //***********************

    public boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
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

    public void bulletsMoveAction() {
        // 子弹移动
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    public void aircraftsMoveAction() {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            // [feature]:
            // 如果敌机失效是超出下边界导致，按照特定规则扣除一定的积分，表示敌军越过了本方防线
            // subNum 相当于是代表敌军的威胁程度，若扣除后分数低于0，则归零处理
            // subNum 计算原则:
            // [if] enemy type is "mob",  then subNum = enemy's HP / 2.0
            // [else if] type is "elite", then subNum = enemy's HP / 2.0 * 1.5
            // [else if] type is "boss",  then subNum = enemy's HP / 2.0 * 2.0
            // [else] type is the others (not exist), subNum = 0
            boolean outOfBound = enemyAircraft.notValid();
            enemyAircraft.forward();
            if (enemyAircraft.notValid() != outOfBound) {
                double subNum = enemyAircraft.getHp() / 2.0;
                var type = enemyAircraft.getClass();
                subNum *= MobEnemy.class.equals(type) ? 1 : EliteEnemy.class.equals(type) ? 1.5 : BossEnemy.class.equals(type) ? 2.0 : 0;
                score -= subNum;
                scoreCnt += bossFlag ? 0 : subNum;
                score = Math.max(score, 0);
            }
        }
    }

    public void propMoveAction() {
        // 道具移动
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    public void gameOverCheck() {
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            System.out.println(executorService.shutdownNow());
            gameOverFlag = true;
            Record record = null;
            gameOverThread = new MusicThread("src/video/game_over.wav");
            gameOverThread.start();
            System.out.println("Game Over!");
            Config.setScore(score);
            if (bgmThread != null && bgmThread.isAlive()) bgmThread.setInterrupt(true);
            if (bgmBossThread != null && bgmBossThread.isAlive()) bgmBossThread.setInterrupt(true);
            executorService.shutdownNow();
            synchronized (Main.class) {
                (Main.class).notify();
            }
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
                    bulletHitThread = new MusicThread("src/video/bullet_hit.wav");
                    bulletHitThread.start();
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                            props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX() + 10, enemyAircraft.getLocationY() + 30));
                            props.add(bombPropFactory.createProp(enemyAircraft.getLocationX() + 50, enemyAircraft.getLocationY() + 20));
                            props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX() + 90, enemyAircraft.getLocationY() + 60));
                            bossFlag = false;
                        }
                        crashFlag = true;
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            // [DONE] 获得分数，产生道具补给
                            // 以 8/9 概率生成道具
                            double randNum = Math.random();
                            if (randNum < 0.3) {
                                props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.6) {
                                props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } else if (randNum < 0.9) {
                                props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
                            } // else do nothing

                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
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
                    bulletValidTimeCnt = 200;
                } else if (prop.getClass().equals(BloodProp.class)) {
                    bloodFlag = true;
                    bloodPropThread = new MusicThread("src/video/get_supply.wav");
                    bloodPropThread.start();
                }
                int increment = prop.getScore();
                score += increment;
                scoreCnt -= bossFlag ? 0 : increment;
                prop.vanish();
                // my Todo: 添加加血特效
                // my Todo: 添加爆炸特效
            }
        }

    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    public void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
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
        if (level == 1) {
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop, null);
        } else if (level == 2) {
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL2, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL2, 0, this.backGroundTop, null);
        } else if (level == 3) {
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL3, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL3, 0, this.backGroundTop, null);

        } else if (level == 4) {
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL4, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL4, 0, this.backGroundTop, null);

        } else if (level == 5) {
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL5, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL5, 0, this.backGroundTop, null);
        } else {
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(ImageManager.BACKGROUND_IMAGE_LEVEL1, 0, this.backGroundTop, null);
        }
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
        // 绘制敌机生命条
        paintEnemyLife(g);

    }

    public void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2, object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    public void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    public void paintEnemyLife(Graphics g) {
        for (AbstractEnemy enemy : enemyAircrafts) {
            if (enemy.getClass().equals(EliteEnemy.class)) {
                int x = enemy.getLocationX() - 50;
                int y = enemy.getLocationY() - 50;
//                g.setColor(Color.RED);
//                g.drawRect(x, y, 120, 10);
                g.setColor(Color.RED);
                g.draw3DRect(x, y, 100, 10, true);
                g.fill3DRect(x, y, (int) (100 * ((enemy.getHp()) / (double) enemy.getMaxHp())), 10, true);
            }else if(enemy.getClass().equals(BossEnemy.class)){
                int x = 50;
                int y = 200;
//                g.setColor(Color.RED);
//                g.drawRect(x, y, 120, 10);
                g.setColor(Color.RED);
                g.draw3DRect(x, y, 300, 10, true);
                g.fill3DRect(x, y, (int) (300 * ((enemy.getHp()) / (double) enemy.getMaxHp())), 10, true);

            }
        }

    }


    public boolean isBombFlag() {
        return bombFlag;
    }

    public void setBombFlag(boolean bombFlag) {
        this.bombFlag = bombFlag;
    }

    public boolean isBloodFlag() {
        return bloodFlag;
    }

    public void setBloodFlag(boolean bloodFlag) {
        this.bloodFlag = bloodFlag;
    }

    public boolean isBulletFlag() {
        return bulletFlag;
    }

    public void setBulletFlag(boolean bulletFlag) {
        this.bulletFlag = bulletFlag;
    }

}
