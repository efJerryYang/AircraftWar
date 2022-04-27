package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.BitSet;
import java.util.List;

/**
 * @author JerryYang
 */
public abstract class AbstractProp extends AbstractEnemy {


    public AbstractProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, 0, score, type);
    }

    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, List<BaseBullet> enemyBullets, int time) {

    }

//    public void activate(List<AbstractEnemy> abstractEnemyList) {
//
//    }
//
//    public void activate(List<BaseBullet> heroBullets) {
//
//    }

}
