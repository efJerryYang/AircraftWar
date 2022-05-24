package com.efjerryyang.defendthefrontline.aircraft;

import com.efjerryyang.defendthefrontline.subscriber.BombSubscriber;


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
