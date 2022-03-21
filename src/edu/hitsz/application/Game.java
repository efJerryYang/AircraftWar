package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.*;
import edu.hitsz.basic.FlyingObject;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel implements KeyListener {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractEnemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;

    private int enemyMaxNumber = 3;

    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;
    private int mobCnt = 0;
    private int mobCntMax = 15;
    private int propSpeedX = 0;
    private int propSpeedY = 1;
    private int baseScore = 10;
    private int scoreCnt = 0;
    private boolean bossFlag = false;

    private boolean keyFlag = false;

    public Game() {
        heroAircraft = new HeroAircraft(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 300);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<BaseBullet>();
        enemyBullets = new LinkedList<BaseBullet>();
        props = new LinkedList<>();
        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(1);

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                boolean moveRight = Math.random() * 2 < 1;
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    boolean createElite = Math.random() * 5 < 1;
                    if (mobCnt < mobCntMax && !createElite) {
                        enemyAircrafts.add(new MobEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                                0,
                                10,
                                30,
                                10,
                                "mob"
                        ));
                        mobCnt++;
                    } else {
                        enemyAircrafts.add(new EliteEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())) * 1,
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                                moveRight ? 10 : -10,
                                5,
                                60,
                                50,
                                "elite"
                        ));
                        mobCnt = 0;
                    }
                }

                if (score > 500 && scoreCnt <= 0) {
                    enemyAircrafts.add(new BossEnemy(
                            (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())) * 1,
                            (int) (Main.WINDOW_HEIGHT * 0.1) * 1,
                            moveRight ? 10 : -10,
                            1,
                            300 * heroAircraft.getShootNum(),
                            1000,
                            "boss"
                    ));
                    scoreCnt = 500;
                    bossFlag = true;
                    enemyMaxNumber++;
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            propMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // [DONE] TODO 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft.shoot());
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // [DONE] TODO 敌机子弹攻击英雄
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
                    if (enemyAircraft.getType() == "boss") {
                        bossFlag = false;
                    }
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        if (enemyAircraft.getType().equals("elite")) {
                            // [DONE] TODO 获得分数，产生道具补给
                            if (Math.random() >= 1.0 / 3) {
                                double randNum = Math.random();
                                String type = randNum < 1.0 / 3 ? "blood"
                                        : randNum < 2.0 / 3 ? "bomb"
                                        : "bullet";
                                if (type == "blood") {
                                    props.add(new BloodProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY(),
                                            propSpeedX, propSpeedY, baseScore * 3, type));
                                } else if (type == "bomb") {
                                    props.add(new BombProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY(),
                                            propSpeedX, propSpeedY, baseScore * 3, type));

                                } else if (type == "bullet") {
                                    props.add(new BulletProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY(),
                                            propSpeedX, propSpeedY, baseScore * 3, type));
                                }

                            }

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
        // Todo: 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (prop.crash(heroAircraft)) {
                prop.activate(heroAircraft);
                prop.activate(enemyAircrafts);
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
    private void postProcessAction() {
        enemyBullets.removeIf(FlyingObject::notValid);
        heroBullets.removeIf(FlyingObject::notValid);
        enemyAircrafts.removeIf(FlyingObject::notValid);
        props.removeIf(FlyingObject::notValid);
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
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
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
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends FlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (FlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

//    public boolean isBossFlag() {
//        return bossFlag;
//    }
//
//    public void setBossFlag(boolean bossFlag) {
//        this.bossFlag = bossFlag;
//    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_BACK_QUOTE == e.getKeyCode() && !keyFlag) {
            System.out.println("press key: [~ `]");
            keyFlag = true;
        } else if (KeyEvent.VK_ESCAPE == e.getKeyCode() && !keyFlag) {
            System.out.println("press key: [ESC]");
            keyFlag = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (KeyEvent.VK_BACK_QUOTE == e.getKeyCode() && keyFlag) {
            System.out.println("release key: [~ `]");
            keyFlag = false;
        } else if (KeyEvent.VK_ESCAPE == e.getKeyCode() && keyFlag) {
            System.out.println("release key: [ESC]");
            keyFlag = false;
        }
    }
}
