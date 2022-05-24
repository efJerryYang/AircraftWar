package com.efjerryyang.defendthefrontline.prop;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
import com.efjerryyang.defendthefrontline.basic.AbstractFlyingObject;
import com.efjerryyang.defendthefrontline.bullet.BaseBullet;

import java.util.BitSet;
import java.util.List;

/**
 * @author JerryYang
 */
public abstract class AbstractProp extends AbstractFlyingObject {
    private int score;
    private String type;

    public AbstractProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX,locationY,speedX,speedY);
        this.score = score;
        this.type = type;
    }

    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, List<BaseBullet> enemyBullets, int time) {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
