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
    private int shootNum = 1;     //子弹一次发射数量
    private int power = 30;       //子弹伤害
    private int direction = -1;  //子弹射击方向 (向上发射：-1，向下发射：1)
    private boolean bulletValid = false;
    private boolean shieldValid = false;
    private boolean bulletSpeedUp = false;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public static synchronized HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            heroAircraft = new HeroAircraft(
                    Main.WINDOW_WIDTH / 2,
                    Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                    0, 0, 300);
        }
        return heroAircraft;
    }

    public void initialize() {
        heroAircraft.setShootNum(1);
        heroAircraft.setMaxHp(300);
        heroAircraft.setHp(300);
        heroAircraft.setDirection(-1);
        heroAircraft.setPower(30);
//        heroAircraft.setLocation();
        heroAircraft.setBulletValid(false);
        heroAircraft.setShieldValid(false);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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
        int y = this.getLocationY() + direction * 2;
        int speedX = 1;
        int speedY = this.getSpeedY() + direction * 5;
        BaseBullet baseBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int center = i * 2 - shootNum + 1;
            int xloc = x + center;
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

//    @Override
//    public void decreaseHp(int decrease) {
//
//    }

    public int getShootNum() {
        return shootNum;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    public int getPower() {
        return power;
    }

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
}
