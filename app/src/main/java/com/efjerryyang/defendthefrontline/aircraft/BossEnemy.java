package com.efjerryyang.defendthefrontline.aircraft;

import com.efjerryyang.defendthefrontline.subscriber.BombSubscriber;


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
