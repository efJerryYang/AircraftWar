package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;
    private final HeroAircraft heroAircraft;
    private final List<AbstractEnemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;

    private final BloodPropFactory bloodPropFactory;
    private final BulletPropFactory bulletPropFactory;
    private final BombPropFactory bombPropFactory;

    private final MobFactory mobFactory;
    private final EliteFactory eliteFactory;
    private final BossFactory bossFactory;
    private int backGroundTop = 0;
    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;
    private int enemyMaxNumber = 3;
    private int enemyMaxNumberUpperBound = 10;

    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 普通敌机计数器
     * 控制每出现 mobCntMax 个普通敌机，至少产生一个精英机
     * 用于控制产生频率的底线，避免随机巧合导致一直没有精英机产生
     */
    private int mobCnt = 0;
    private int mobCntMax = 15;

    /**
     * boss机生成控制
     * 当scoreCnt == 0，并且score > 500时，产生boss敌机，bossFlag = true
     * 当boos机存在时，bossFlag = true，初始化scoreCnt = 500
     * 当boss机失效后，bossFlag = false, scoreCnt -= increment
     * 其中，increment是每次score变化的增量，通用控制语句如下
     * scoreCnt -= bossFlag ? 0 : increment;
     */
    private int scoreCnt = 0;
    private boolean bossFlag = false;
    private boolean bombFlag = false;
    private boolean bloodFlag = false;
    private boolean bulletFlag = false;
    private boolean crashFlag = false;

    public Game() {
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<BaseBullet>();
        enemyBullets = new LinkedList<BaseBullet>();
        props = new LinkedList<>();
        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(5);
        bloodPropFactory = new BloodPropFactory();
        bulletPropFactory = new BulletPropFactory();
        bombPropFactory = new BombPropFactory();
        mobFactory = new MobFactory();
        eliteFactory = new EliteFactory();
        bossFactory = new BossFactory();


        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

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

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task1 = () -> {
            time += timeInterval;
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                boolean moveRight = Math.random() * 2 < 1;
                if (enemyAircrafts.size() <= enemyMaxNumber && enemyMaxNumber <= enemyMaxNumberUpperBound) {
                    // 随机数控制产生精英敌机
                    boolean createElite = Math.random() * 5 < 1;
                    if (mobCnt < mobCntMax && !createElite) {
                        enemyAircrafts.add(mobFactory.createEnemy(heroAircraft.getShootNum()));
                        mobCnt++;
                    } else {
                        enemyAircrafts.add(eliteFactory.createEnemy(heroAircraft.getShootNum()));
                        mobCnt = 0;
                    }
                }
                // 控制生成boss敌机
                System.out.println("score: " + score + " scoreCnt: " + scoreCnt + " bossFlag: " + bossFlag);
                if (score > 500 && scoreCnt <= 0) {
                    enemyAircrafts.add(bossFactory.createEnemy(heroAircraft.getShootNum()));
                    scoreCnt = 500;
                    bossFlag = true;
                    if (enemyMaxNumber < enemyMaxNumberUpperBound) {
                        enemyMaxNumber++;
                    }
                }// 飞机射出子弹
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
        Runnable task2 = () -> {
            try {
                AudioPlayer audioPlayer = new AudioPlayer("/src/audio/bgm.mp3");
                audioPlayer.playAudio();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        };
        Runnable task3 = () -> {
            // 需要处理线程安全的问题
            if (bombFlag) {
                bombFlag = false;
                try {
                    AudioPlayer audioPlayer = new AudioPlayer("/src/audio/boom2.mp3");
                    audioPlayer.playAudio();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
            if (bloodFlag) {
                bloodFlag = false;
                try {
                    AudioPlayer audioPlayer = new AudioPlayer("/src/audio/healing.mp3");
                    audioPlayer.playAudio();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }

            }
            if (bulletFlag) {
                bulletFlag = false;
                try {
                    AudioPlayer audioPlayer = new AudioPlayer("/src/audio/add-bullet.mp3");
                    audioPlayer.playAudio();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }

            }

        };
//        Runnable task4 = () -> {
//            if (bossFlag) {
//                try {
//                    AudioPlayer audioPlayer = new AudioPlayer("/src/audio/vsboss.mp3");
//                    audioPlayer.playAudio();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (JavaLayerException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task1, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(task2, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(task3, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
//        executorService.execute(task4);
//        if (!bossFlag) {
//            executorService.submit(task4).cancel(!bossFlag);
//        }

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
        // [DONE] 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        // 子弹移动
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
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
                String type = enemyAircraft.getType();
                double subNum = enemyAircraft.getHp() / 2.0;
                subNum *= "mob".equals(type) ? 1 : "elite".equals(type) ? 1.5 : "boss".equals(type) ? 2.0 : 0;
                score -= subNum;
                scoreCnt += bossFlag ? 0 : subNum;
                score = Math.max(score, 0);
            }
        }
    }

    private void propMoveAction() {
        // 道具移动
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
                // my Todo: bug, 在有一定的可能性导致之后无敌机产生，一般是击败几个boss敌机之后
                if (enemyAircraft.notValid()) {
                    // 如果击毁的敌机是boss机，代表boss机在当前窗口消失
                    // 潜在的bug，如果boss机因为超过底线而消失，会不会导致之后boss机不出现？
                    if ("boss".equals(enemyAircraft.getType())) {
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
                        if ("boss".equals(enemyAircraft.getType())) {
                            bossFlag = false;
                        }
                        crashFlag = true;
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        if ("elite".equals(enemyAircraft.getType())) {
                            // [DONE] 获得分数，产生道具补给
                            // 以 2/3 概率生成道具
                            if (Math.random() >= 1.0 / 3) {
                                double randNum = Math.random();
                                // my Todo: 这里的设计有点冗余，type似乎变得多余，以后优化的时候修改
                                String type = randNum < 1.0 / 3 ? "blood"
                                        : randNum < 2.0 / 3 ? "bomb"
                                        : "bullet";
                                switch (type) {
                                    case "blood" -> props.add(bloodPropFactory.createProp(
                                            enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), type));
                                    case "bomb" -> props.add(bombPropFactory.createProp(
                                            enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), type));
                                    case "bullet" -> props.add(bulletPropFactory.createProp(
                                            enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), type));
                                    default -> {
                                    }
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
        // (DONE) 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (prop.crash(heroAircraft)) {
                prop.activate(heroAircraft, enemyAircrafts, heroBullets, time);
                if ("bomb".equals(prop.getType())) {
                    bombFlag = true;
                } else if ("bullet".equals(prop.getType())) {
                    bulletFlag = true;
                } else if ("blood".equals(prop.getType())) {
                    bloodFlag = true;
                }

//                heroAircraft.setBulletValid(true);
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

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
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
}
