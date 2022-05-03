package edu.hitsz.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.Config;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGame extends JPanel {
    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;
    protected final BloodPropFactory bloodPropFactory;
    protected final BulletPropFactory bulletPropFactory;
    protected final BombPropFactory bombPropFactory;
    protected final MobFactory mobFactory;
    protected final EliteFactory eliteFactory;
    protected final BossFactory bossFactory;
    protected final RecordDAOImpl recordDAOImpl;
    //Scheduled 线程池，用于定时任务调度
    protected ScheduledExecutorService executorService;
    protected MusicThread bulletHitThread = null;
    protected MusicThread bulletPropThread = null;
    protected MusicThread bombExplodeThread = null;
    protected MusicThread bloodPropThread = null;
    protected MusicThread bgmThread = null;
    protected MusicThread bgmBossThread = null;
    protected MusicThread gameOverThread = null;
    protected boolean enableAudio;
    protected double level = 0;
    protected int baseLevel = 0;
    protected int backGroundTop = 0;
    protected int bulletValidTimeCnt = 0;
    protected int enemyMaxNumber = 1;
    protected int enemyMaxNumberUpperBound = 10;
    protected int score = 0;
    protected int cycleDuration = 500;
    protected int cycleTime = 0;
    protected int mobCnt = 0;
    protected int mobCntMax = 5;
    protected int time = 0;
    protected int timeInterval = 40;
    protected boolean gameOverFlag = false;
    /**
     * boss机生成控制
     * 当scoreCnt == 0，并且score > 500时，产生boss敌机，bossFlag = true
     * 当boos机存在时，bossFlag = true，初始化scoreCnt = 500
     * 当boss机失效后，bossFlag = false, scoreCnt -= increment
     * 其中，increment是每次score变化的增量，通用控制语句如下
     * scoreCnt -= bossFlag ? 0 : increment;
     */
    protected int scoreCnt = 0;
    protected boolean bossFlag = false;
    protected boolean bombFlag = false;
    protected boolean bloodFlag = false;
    protected boolean bulletFlag = false;
    protected boolean bulletCrash = false;
    protected boolean crashFlag = false;

    protected AbstractGame(int gameLevel, boolean enableAudio) {
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<BaseBullet>();
        enemyBullets = new LinkedList<BaseBullet>();
        props = new LinkedList<>();
        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(2);
        bloodPropFactory = new BloodPropFactory();
        bulletPropFactory = new BulletPropFactory();
        bombPropFactory = new BombPropFactory();
        mobFactory = new MobFactory();
        eliteFactory = new EliteFactory();
        bossFactory = new BossFactory();
//        heroContext = new Context(new StraightShoot());
//        enemyContext = new Context(new StraightShoot());
        recordDAOImpl = new RecordDAOImpl(gameLevel);
    }

    public final void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable gameTask = () -> {
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                // 按周期生成敌机
                generateEnemyAircrafts();
                // 飞机按固定周期射出子弹
                shootAction();
            }
            // playBGM
            playBGM();
            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            // 道具移动
            propMoveAction();
            // 撞击检测
            crashCheckAction();
            // 后处理
            postProcessAction();
            //每个时刻重绘界面
            repaint();
            // 游戏结束检查
            gameOverCheck();
        };
        Runnable timeCounter = () -> {
            time += timeInterval;
            bulletPropStageCount();
            if (gameOverFlag) {
                executorService.shutdown();
            }
        };
        executorService.scheduleWithFixedDelay(gameTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(timeCounter, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

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
                bulletValidTimeCnt = (int)(2000 / (5 + level));
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

//        System.out.print("Bullet State: " + heroAircraft.getBulletPropStage());
//        System.out.println("\tBullet Time Cnt: " + bulletValidTimeCnt);

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

    abstract public void generateEnemyAircrafts();

//    abstract public void gameOverCheck();

    abstract public void playBGM();

//    abstract public boolean timeCountAndNewCycleJudge();

    abstract public void shootAction();

//    abstract public void bulletsMoveAction();

//    abstract public void aircraftsMoveAction();

//    abstract public void propMoveAction();

    abstract public void crashCheckAction();

//    abstract public void postProcessAction();

    public void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = Main.WINDOW_HEIGHT - 70;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
        x = heroAircraft.getLocationX() - 50;
        y = heroAircraft.getLocationY() - 60;
        g.setColor(Color.GRAY);
        g.drawRect(x, y, 100, 10);
        g.fillRect(x, y, 100, 10);
        g.setColor(Color.RED);
        g.fill3DRect(x, y, (int) (100 * ((heroAircraft.getHp()) / (double) heroAircraft.getMaxHp())), 10, true);
        g.setColor(Color.BLACK);
        g.draw3DRect(x, y, 100, 10, true);
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

    public void paintEnemyLife(Graphics g) {
        for (AbstractEnemy enemy : enemyAircrafts) {
            if (enemy.getClass().equals(EliteEnemy.class)) {
                int x = enemy.getLocationX() - 50;
                int y = enemy.getLocationY() - 50;
                g.setColor(Color.GRAY);
                g.drawRect(x, y, 100, 10);
                g.fillRect(x, y, 100, 10);
                g.setColor(Color.RED);
                g.fill3DRect(x, y, (int) (100 * ((enemy.getHp()) / (double) enemy.getMaxHp())), 10, true);
                g.setColor(Color.BLACK);
                g.draw3DRect(x, y, 100, 10, true);
            } else if (enemy.getClass().equals(BossEnemy.class)) {
                int x = (int) Math.floor(0.1 * Main.WINDOW_WIDTH);
                int y = 15;
                int curHp = enemy.getHp();
                int maxHp = enemy.getMaxHp();
                g.setColor(Color.GRAY);
                g.drawRect(x, y, (int) (0.8 * Main.WINDOW_WIDTH), 10);
                g.fillRect(x, y, (int) (0.8 * Main.WINDOW_WIDTH), 10);
                g.setColor(Color.RED);
                g.fill3DRect(x, y, (int) (0.8 * Main.WINDOW_WIDTH * (curHp / (double) maxHp)), 10, true);
                g.setColor(Color.BLACK);
                g.draw3DRect(x, y, (int) (0.8 * Main.WINDOW_WIDTH), 10, true);

                x = (int) (0.55 * Main.WINDOW_WIDTH);
                y = y + 25;
                g.setColor(Color.RED);
                g.setFont(new Font("SansSerif", Font.BOLD, 15));
                String strDisplay = String.format("BOSS HP: %4d / %4d",curHp,maxHp);
                g.drawString(strDisplay, x, y);

            }
        }

    }
}
