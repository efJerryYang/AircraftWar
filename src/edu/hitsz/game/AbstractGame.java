package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Context;
import edu.hitsz.application.MusicThread;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.record.RecordDAOImpl;
import edu.hitsz.strategy.StraightShoot;

import javax.swing.*;
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
    protected boolean enableAudio;
    protected int time = 0;
    protected int timeInterval = 40;
    protected boolean gameOverFlag = false;
    //Scheduled 线程池，用于定时任务调度
    protected ScheduledExecutorService executorService;

    protected MusicThread bulletHitThread = null;
    protected MusicThread bulletPropThread = null;
    protected MusicThread bombExplodeThread = null;
    protected MusicThread bloodPropThread = null;
    protected MusicThread bgmThread = null;
    protected MusicThread bgmBossThread = null;
    protected MusicThread gameOverThread = null;

    protected AbstractGame(int gameLevel, boolean enableAudio) {
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<BaseBullet>();
        enemyBullets = new LinkedList<BaseBullet>();
        props = new LinkedList<>();
//Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(4);
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

    //    public final void action() {
//        Runnable gameTask = () -> {
//            if (timeCountAndNewCycleJudge()) {
//                generateEnemyAircrafts();
//                // 飞机按固定周期射出子弹
//                shootAction();
//            }
//            // playBGM
//            playBGM();
//            // 子弹移动
//            bulletsMoveAction();
//            // 飞机移动
//            aircraftsMoveAction();
//            // 道具移动
//            propMoveAction();
//            // 撞击检测
//            crashCheckAction();
//            // 后处理
//            postProcessAction();
//            //每个时刻重绘界面
//            repaint();
//            // 游戏结束检查
//            gameOverCheck();
//        };
//        Runnable timeCounter = () -> {
//            time += timeInterval;
//            bulletPropStageCount();
//            if (gameOverFlag) {
//                executorService.shutdown();
//            }
//        };
//        executorService.scheduleWithFixedDelay(gameTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
//        executorService.scheduleWithFixedDelay(timeCounter, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
//
//    }
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
    public abstract void bulletPropStageCount();

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

//    abstract public void paint(Graphics g);
//    abstract public void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects);
//    abstract public void paintScoreAndLife(Graphics g);
}
