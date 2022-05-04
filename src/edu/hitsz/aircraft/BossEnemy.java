package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.subscriber.BombSubscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JerryYang
 */
public class BossEnemy extends AbstractEnemy implements BombSubscriber {
    private int shootNum = 1;
    private int power = 30;
    private int direction=1;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
        this.setShootNum(this.getShootNum() * 5);
        this.setPower(30);
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
//        int x = this.getLocationX();
//        int y = this.getLocationY() + this.getDirection() * 2;
//        int speedX = 1;
//        int speedY = this.getSpeedY() + this.getDirection() * 5;
//        BaseBullet baseBullet;
//        for (int i = 0; i < this.getShootNum(); i++) {
//            // 子弹发射位置相对飞机位置向前偏移
//            // 多个子弹横向分散
//            int center = i * 2 - this.getShootNum() + 1;
//            int xloc = x + center;
//            int yloc = y - center * center + 10;
//            baseBullet = new EnemyBullet(xloc, yloc, center != 0 ? speedX * center : 0, speedY, this.getPower());
//            res.add(baseBullet);
//        }
        return res;
    }
    @Override
    public void forward() {
        super.forward();

    }

    @Override
    public void bombExplode() {
        if (this.hp > 500) {
            this.decreaseHp(500);
        } else {
            this.setHp(1);
        }
    }

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

    @Override
    public int getDirection() {
        return direction;
    }

    @Override
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
