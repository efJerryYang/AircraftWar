package edu.hitsz.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDAOImpl;
import edu.hitsz.strategy.StraightShoot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static edu.hitsz.aircraft.HeroAircraft.BOSS_APPEAR_SCORE;

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
    public double bombPropGeneration;
    public double bloodPropGeneration;
    public double bulletPropGeneration;
    public BufferedImage backgroundImage;
    //Scheduled 线程池，用于定时任务调度
    protected ScheduledExecutorService executorService;
    protected MusicThread bulletHitThread = null;
    protected MusicThread bulletPropThread = null;
    protected MusicThread bombExplodeThread = null;
    protected MusicThread bloodPropThread = null;
    protected MusicThread crashWithShieldThread = null;
    protected MusicThread bgmThread = null;
    protected MusicThread bgmBossThread = null;
    protected MusicThread gameOverThread = null;
    protected ShootContext heroShootContext;
    protected ShootContext enemyShootContext;
    protected boolean enableAudio;
    protected int baseLevel = 0;
    protected double level = 0;
    protected double levelScalar = 1.0;
    protected int backGroundTop = 0;
    protected int bulletValidTimeCnt = 0;
    protected int bloodValidTimeCnt = 0;
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
    protected int[] enemyAircraftGenerationCycle = {0, 400};
    protected int[] enemyShootCycle = {0, 300};
    protected int[] heroShootCycle = {0, 100};
    /**
     * boss机生成控制
     * 当scoreCnt == 0，并且score > 500时，产生boss敌机，bossFlag = true
     * 当boos机存在时，bossFlag = true，初始化scoreCnt = 500
     * 当boss机失效后，bossFlag = false, scoreCnt -= increment
     * 其中，increment是每次score变化的增量，通用控制语句如下
     * scoreCnt -= bossFlag ? 0 : increment;
     */
    protected int scoreCnt = 0;
    protected int bossCnt = 0;
    protected boolean bossFlag = false;
    protected boolean bombFlag = false;
    protected boolean bloodFlag = false;
    protected boolean bulletFlag = false;
    protected boolean bulletCrash = false;
    protected boolean crashFlag = false;

    protected AbstractGame(int gameLevel, boolean enableAudio) {
        this.baseLevel = gameLevel;
        this.level = gameLevel;
        this.enableAudio = enableAudio;
        this.enemyMaxNumber = gameLevel + 1;
        this.enemyMaxNumberUpperBound = gameLevel * 2;
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<BaseBullet>();
        enemyBullets = new LinkedList<BaseBullet>();
        props = new LinkedList<>();
        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(3);
        bloodPropFactory = new BloodPropFactory();
        bulletPropFactory = new BulletPropFactory();
        bombPropFactory = new BombPropFactory();
        mobFactory = new MobFactory();
        eliteFactory = new EliteFactory();
        bossFactory = new BossFactory();
        heroShootContext = new ShootContext(new StraightShoot());
        enemyShootContext = new ShootContext(new StraightShoot());
        recordDAOImpl = new RecordDAOImpl(gameLevel);
        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    public final void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable gameTask = () -> {
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge(enemyAircraftGenerationCycle)) {
                generateAllEnemy();
            }
            if (timeCountAndNewCycleJudge(enemyShootCycle)) {
                enemyShootAction();
            }
            if (timeCountAndNewCycleJudge(heroShootCycle)) {
                heroShootAction();
            }
            // playBGM
            playBGM();
            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction(1);
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
            // 难度控制
            level = Math.min(bossCnt + 0.9999 + baseLevel, baseLevel * ((double) time / 1e5 + levelScalar));
            bulletPropStageCount();
            bloodPropStageCount();
            if (gameOverFlag) {
                executorService.shutdown();
            }
        };
        executorService.scheduleWithFixedDelay(gameTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(timeCounter, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    public void bloodPropStageCount() {
        if (bloodValidTimeCnt <= 0) {
            this.bloodValidTimeCnt = 0;
        } else {
            heroAircraft.setHp(heroAircraft.getMaxHp());
            this.bloodValidTimeCnt -= 1;
        }
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
                bulletValidTimeCnt = (int) (2000 / (5 + level));
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
    }

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

    public boolean timeCountAndNewCycleJudge(int[] cycle) {
        cycle[0] += timeInterval;
        if (cycle[0] >= cycle[1] && cycle[1] - timeInterval < cycle[0]) {
            // 跨越到新的周期
            cycle[0] %= cycle[1];
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

    public void aircraftsMoveAction(double factor) {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            boolean outOfBound = enemyAircraft.notValid();
            enemyAircraft.forward();
            if (enemyAircraft.notValid() != outOfBound) {
                double subNum = enemyAircraft.getHp();
                var type = enemyAircraft.getClass();
                subNum *= MobEnemy.class.equals(type) ? 1.5 / 2.0 * factor : EliteEnemy.class.equals(type) ? 1.75 / 2.0 * factor : BossEnemy.class.equals(type) ? 1.0 * factor : 0;
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
            gameOverThread = new MusicThread("src/audios/game_over.wav");
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

    public void generateEnemyAircraft() {
        if (enemyAircrafts.size() <= enemyMaxNumber && enemyMaxNumber <= enemyMaxNumberUpperBound) {
            // 随机数控制产生精英敌机
            boolean createElite = Math.random() < 0.5;
            if (mobCnt < mobCntMax && !createElite) {
                enemyAircrafts.add(mobFactory.createEnemy(this.level));
                mobCnt++;
            } else {
                enemyAircrafts.add(eliteFactory.createEnemy(this.level));
                mobCnt = 0;
            }
        }

    }

    public void generateBoss() {
        // System.out.println("score: " + score + " scoreCnt: " + scoreCnt + " bossFlag: " + bossFlag);
        if (score > (BOSS_APPEAR_SCORE * level) && scoreCnt <= 0) {
            enemyAircrafts.add(bossFactory.createEnemy(this.level)); // 不改变boss血量
            scoreCnt = (int) (BOSS_APPEAR_SCORE * level);
            bossFlag = true;
            if (enemyMaxNumber < enemyMaxNumberUpperBound) {
                enemyMaxNumber++;
            }
        }
    }

    public void generateAllEnemy() {
        System.out.printf("Time: %7d    Level:%7.4f    MobSpeed:%4d    EliteHp:%4d    PropValidMaxTime:%4d\n", time, level, Math.min((int) (5 * Math.sqrt(level)), 15), (int) (60 * Math.sqrt(this.level)), (int) (2000 / (5 + level)));
        // 新敌机产生
        generateEnemyAircraft();
        // 控制生成boss敌机
        generateBoss();
    }

    public void generateProp(AbstractEnemy enemyAircraft) {
        double randNum = Math.random();
        double threshHoldBlood = bloodPropGeneration;
        double threshHoldBomb = threshHoldBlood + bombPropGeneration;
        double threshHoldBullet = threshHoldBomb + bulletPropGeneration;
        if (randNum < threshHoldBlood) {
            props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
        } else if (randNum < threshHoldBomb) {
            props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
        } else if (randNum < threshHoldBullet) {
            props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
        } // else do nothing
    }

    public void generateBossProp(BossEnemy enemyAircraft) {
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
    }

    public void enemyShootAction() {
        // [DONE] 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyShootContext.executeShootStrategy(enemyAircraft));
        }
    }

    public void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroShootContext.executeShootStrategy(heroAircraft));
    }


    public void enemyShootHero() {
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
    }

    public void aircraftCrashJudge(AbstractEnemy enemyAircraft) {
        if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
            // 护盾道具开启时
            crashWithShieldThread = new MusicThread("src/audios/crash.wav");
            crashWithShieldThread.start();
            if (bloodValidTimeCnt > 0) {
                if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                    bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                    enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                    if (enemyAircraft.notValid()) {
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        generateProp(enemyAircraft);
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

    public void heroShootEnemy() {
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 如果击毁的敌机是boss机，代表boss机在当前窗口消失
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
                            generateBossProp((BossEnemy) enemyAircraft);
                            bossFlag = false;
                            bossCnt += 1;
                            levelScalar += 1;
                        }
                        crashFlag = true;
                        int increment = enemyAircraft.getClass().equals(BossEnemy.class) ? 100 : enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        // [DONE] 获得分数，产生道具补给
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            generateProp(enemyAircraft);
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                // System.out.println("bloodValidTimeCntCrash: " + this.bloodValidTimeCnt);
                aircraftCrashJudge(enemyAircraft);
            }
        }
    }

    public void heroActivateProp() {
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

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    public void crashCheckAction() {
        // [DONE] 敌机子弹攻击英雄
        enemyShootHero();
        // 英雄子弹攻击敌机
        heroShootEnemy();
        // (DONE) 我方获得道具，道具生效
        heroActivateProp();
    }

    public void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = Main.WINDOW_HEIGHT - 70;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);

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
                String strDisplay = String.format("BOSS HP: %4d / %4d", curHp, maxHp);
                g.drawString(strDisplay, x, y);

            }
        }

    }

    public void paintHeroAttributes(Graphics g) {
        int x = heroAircraft.getLocationX() - 50;
        int y = heroAircraft.getLocationY() - 50;
        int currentPropValidMaxTime = (int) (2000 / (5 + level));
        // draw hp and blood prop
        int currentBloodPropStage = heroAircraft.getBloodPropStage();
//        System.out.println(currentBloodPropStage);
        int currentBloodValidTime = bloodValidTimeCnt;
        if (currentBloodValidTime <= 0) {
            g.setColor(Color.GRAY);
            g.drawRect(x, y, 100, 5);
            g.fillRect(x, y, 100, 5);
            g.setColor(Color.RED);
            g.fill3DRect(x, y, (int) (100 * ((heroAircraft.getHp()) / (double) heroAircraft.getMaxHp())), 5, true);
            g.setColor(Color.BLACK);
            g.draw3DRect(x, y, 100, 5, true);
        } else {
            g.setColor(Color.RED);
            g.drawRect(x, y, 100, 5);
            g.fillRect(x, y, 100, 5);
            g.setColor(Color.YELLOW);
            g.fill3DRect(x, y, (int) (100 * (currentBloodValidTime / (double) currentPropValidMaxTime)), 5, true);
            g.setColor(Color.BLACK);
            g.draw3DRect(x, y, 100, 5, true);
        }

        // draw bullet prop
        y = y - 8;
        int currentBulletPropStage = heroAircraft.getBulletPropStage();
        int currentBulletValidTime = bulletValidTimeCnt;
        if (currentBulletPropStage == 0 || currentBulletPropStage == 1) {
            g.setColor(Color.GRAY);
        } else if (currentBulletPropStage == 2) {
            g.setColor(Color.BLUE);
        } else if (currentBulletPropStage == 3) {
            g.setColor(Color.CYAN);
        }
        g.drawRect(x, y, 100, 5);
        g.fillRect(x, y, 100, 5);
        if (currentBulletPropStage == 0 || currentBulletPropStage == 1) {
            g.setColor(Color.BLUE);
        } else if (currentBulletPropStage == 2) {
            g.setColor(Color.CYAN);
        } else if (currentBulletPropStage == 3) {
            g.setColor(Color.MAGENTA);
        }
        g.fill3DRect(x, y, (int) (100 * (currentBulletValidTime / (double) currentPropValidMaxTime)), 5, true);
        g.setColor(Color.BLACK);
        g.draw3DRect(x, y, 100, 5, true);
    }

    public void paintBackground(Graphics g) {
        // 绘制背景,图片滚动
        g.drawImage(backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }
    }

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
        paintBackground(g);

        // 先绘制子弹，后绘制飞机
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

    abstract public void playBGM();
}
