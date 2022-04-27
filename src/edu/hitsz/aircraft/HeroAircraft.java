package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    public static final int SCATTERING_SHOOTNUM = 4;
    public static final int BOSS_APPEAR_SCORE = 500;
    public static final int HERO_MAX_HP = 1000;
    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp        初始生命值
     */
    private static HeroAircraft heroAircraft = null;
    /**
     * 攻击方式
     */
    private int shootNum = 1;
    private int bulletPropStage = 0;
    /**
     * power        英雄机子弹的伤害
     */
    private int power = 30;
    /**
     * direction    飞行的方向，-1向上，1向下
     */
    private boolean bulletValid = false;
    private boolean shieldValid = false;
    private boolean bulletSpeedUp = false;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setDirection(-1);
    }

    public static synchronized HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            heroAircraft = new HeroAircraft(
                    Main.WINDOW_WIDTH / 2,
                    Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                    0, 0, HERO_MAX_HP);
        }
        return heroAircraft;
    }

    public static void setHeroAircraft(HeroAircraft heroAircraft) {
        HeroAircraft.heroAircraft = heroAircraft;
    }

    public void initialize() {
        heroAircraft.setShootNum(1);
        heroAircraft.setMaxHp(HERO_MAX_HP);
        heroAircraft.setHp(HERO_MAX_HP);
        heroAircraft.setBulletPropStage(0);
        heroAircraft.setDirection(-1);
        heroAircraft.setPower(30);
        heroAircraft.setBulletValid(false);
        heroAircraft.setShieldValid(false);
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 通过射击产生子弹
     *
     * @return 射击出的子弹List
     */
    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + this.getDirection() * 2;
        int speedX = 1;
        int speedY = this.getSpeedY() + this.getDirection() * 5;
        BaseBullet baseBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int center = i * 2 - shootNum + 1;
            int xloc = x + center * 6;
            int yloc = y + center * center;
            if (bulletSpeedUp) {
                baseBullet = new HeroBullet(xloc, yloc, center != 0 ? speedX * center : 0, speedY * 2, Math.max(power, shootNum * power / 2));
            } else {
                baseBullet = new HeroBullet(xloc + center * 10, yloc, 0, speedY, power);
            }
            res.add(baseBullet);
        }
        return res;
    }

    /* Todo: 这里可以使英雄机不掉血
       public void decreaseHp(int decrease) { }
     */


    @Override
    public int getShootNum() {
        return shootNum;
    }

    @Override
    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public void setPower(int power) {
        this.power = power;
    }

    public boolean isBulletValid() {
        return bulletValid;
    }

    public void setBulletValid(boolean bulletValid) {
        this.bulletValid = bulletValid;
    }

    public boolean isShieldValid() {
        return shieldValid;
    }

    public void setShieldValid(boolean shieldValid) {
        this.shieldValid = shieldValid;
    }

    public boolean isBulletSpeedUp() {
        return bulletSpeedUp;
    }

    public void setBulletSpeedUp(boolean bulletSpeedUp) {
        this.bulletSpeedUp = bulletSpeedUp;
    }

    public int getBulletPropStage() {
        return bulletPropStage;
    }

    public void setBulletPropStage(int bulletPropStage) {
        this.bulletPropStage = Math.max(bulletPropStage, 0);
    }
}
