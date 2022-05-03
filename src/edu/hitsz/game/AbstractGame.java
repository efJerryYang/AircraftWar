package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.MusicThread;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
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
    protected int level = 0;
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

    abstract public void bulletPropStageCount();

    abstract public void generateEnemyAircrafts();

    abstract public void gameOverCheck();

    abstract public void playBGM();

    abstract public boolean timeCountAndNewCycleJudge();

    abstract public void shootAction();

    abstract public void bulletsMoveAction();

    abstract public void aircraftsMoveAction();

    abstract public void propMoveAction();

    abstract public void crashCheckAction();

    abstract public void postProcessAction();

    public void paintScoreAndLife(Graphics g){
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }
    public void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects){
        if (objects.size() == 0) {
            return;
        }
        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2, object.getLocationY() - image.getHeight() / 2, null);
        }
    }
}
