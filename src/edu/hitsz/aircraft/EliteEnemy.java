package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.subscriber.BombSubscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JerryYang
 */
public class EliteEnemy extends AbstractEnemy implements BombSubscriber {


    private int shootNum = 1;
    private int power = 30;
    private int direction=1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
        this.setDirection(1);
    }

    @Override
    public int getDirection() {
        return direction;
    }

    @Override
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void forward() {
        super.forward();

    }

//    @Override
//    public List<BaseBullet> shoot() {
//        List<BaseBullet> res = new LinkedList<>();
//        int x = this.getLocationX();
//        int y = this.getLocationY() + direction * 2;
//        int speedX = 0;
//        int speedY = this.getSpeedY() + direction * 5;
//        BaseBullet baseBullet;
//        for (int i = 0; i < shootNum; i++) {
//            // 子弹发射位置相对飞机位置向前偏移
//            // 多个子弹横向分散
//            baseBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
//            res.add(baseBullet);
//        }
//        return res;
//    }

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
    public void bombExplode() {
        this.decreaseHp(this.getMaxHp());
    }
}
