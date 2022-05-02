package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

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
